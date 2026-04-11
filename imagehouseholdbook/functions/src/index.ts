// functions/src/index.ts

import { initializeApp } from "firebase-admin/app";
import { getFirestore, Timestamp, FieldValue } from "firebase-admin/firestore";
import { onObjectFinalized } from "firebase-functions/v2/storage";
import { logger } from "firebase-functions";
import { Storage } from "@google-cloud/storage";
import { VertexAI } from "@google-cloud/vertexai";

initializeApp();
const db = getFirestore();
const storage = new Storage();

const PROJECT_ID =
process.env.GCLOUD_PROJECT || process.env.GCP_PROJECT || "imagescan-bc533";

// A안: Vertex/함수 리전을 us-central1로 맞춰서 우선 동작 확인
const FUNCTION_REGION = "us-central1";
const VERTEX_LOCATION = "us-central1";

// 모델명은 프로젝트/지역/정책에 따라 변경 가능
const GEMINI_MODEL = "gemini-2.5-flash";

function parseReceiptPath(objectName: string): { uid: string; jobId: string } | null {
const re =
/^receipts\/([^/]+)\/\d{8}_\d{6}_([0-9a-fA-F-]{36})\.(jpg|jpeg|png)$/i;
const m = objectName.match(re);
if (!m) return null;
return { uid: m[1], jobId: m[2] };
}

function parseFromCustomMetadata(
meta?: Record<string, string>,
): { uid: string; jobId: string } | null {
if (!meta) return null;
const uid = (meta["uid"] || "").trim();
const jobId = (meta["jobId"] || "").trim();
if (!uid || !jobId) return null;
return { uid, jobId };
}

function sanitizeDocIdPart(input: string): string {
return input.replace(/[\/\\?#\[\]]/g, "_").trim();
}

type ParsedReceiptItem = {
productName: string;
unitPrice: number;
receivedQty: number;
totalAmount: number;
};

type GeminiReceiptResult = {
storeName: string;
purchaseDate: string; // YYYY-MM-DD
items: ParsedReceiptItem[];
};

function extractFirstJsonObject(text: string): string {
const start = text.indexOf("{");
if (start < 0) throw new Error("No JSON object start found");
let depth = 0;
for (let i = start; i < text.length; i++) {
const ch = text[i];
if (ch === "{") depth++;
if (ch === "}") {
depth--;
if (depth === 0) return text.slice(start, i + 1);
}
}
throw new Error("Unclosed JSON object");
}

// @google-cloud/vertexai는 response.text() 헬퍼가 TS 타입에 없을 수 있어
// REST 스펙 구조(candidates[].content.parts[].text)에 맞춰 직접 텍스트를 합침. [web:453]
function getTextFromGenerateContentResponse(result: unknown): string {
const r: any = result as any;
const response = r?.response ?? r;

const candidates = Array.isArray(response?.candidates) ? response.candidates : [];
const parts = candidates?.[0]?.content?.parts;

if (!Array.isArray(parts)) return "";
return parts.map((p: any) => (typeof p?.text === "string" ? p.text : "")).join("").trim();
}

function normalizeGeminiResult(raw: any): GeminiReceiptResult {
const storeName = String(raw?.storeName ?? "").trim();
const purchaseDate = String(raw?.purchaseDate ?? "").trim();
const itemsRaw = Array.isArray(raw?.items) ? raw.items : [];

const items: ParsedReceiptItem[] = itemsRaw.map((it: any) => {
const productName = String(it?.productName ?? "").trim();
const unitPrice = Number(it?.unitPrice ?? it?.price ?? 0);
const receivedQty = Number(it?.receivedQty ?? it?.quantity ?? 1);
const totalAmount = Number(it?.totalAmount ?? 0) || unitPrice * receivedQty;

return { productName, unitPrice, receivedQty, totalAmount };
});

return { storeName, purchaseDate, items };
}

async function analyzeReceiptWithGemini(params: {
bucket: string;
objectName: string;
contentType?: string;
}): Promise<GeminiReceiptResult> {
const { bucket, objectName } = params;

const mimeType =
params.contentType && params.contentType.startsWith("image/")
? params.contentType
: "image/jpeg";

// 1) GCS에서 이미지 다운로드
const [bytes] = await storage.bucket(bucket).file(objectName).download();
const base64 = bytes.toString("base64");

// 2) Vertex AI Gemini 호출
const vertexAI = new VertexAI({ project: PROJECT_ID, location: VERTEX_LOCATION });
const model = vertexAI.getGenerativeModel({
model: GEMINI_MODEL,
generationConfig: {
temperature: 0.1,
maxOutputTokens: 2048,
},
});

const prompt = `
당신은 영수증 분석기입니다.
아래 이미지에서 구매 영수증의 정보를 추출하여 **반드시 JSON만** 출력하세요(설명/마크다운/코드블록 금지).

JSON 스키마:
{
"storeName": "string",
"purchaseDate": "YYYY-MM-DD",
"items": [
{ "productName": "string", "unitPrice": 0, "receivedQty": 0, "totalAmount": 0 }
]
}

규칙:
- items에는 실제 상품 행만 포함(합계/총계/결제/할인/부가세/카드정보 라인은 제외).
- 숫자는 원 단위 정수로.
- receivedQty가 없으면 1.
- totalAmount가 없으면 unitPrice*receivedQty.
- 값이 불확실하면 가장 가능성 높은 값으로 추정하되, 완전히 못 찾으면 빈 문자열/0 대신 그 항목 자체를 items에서 제외.
`.trim();

const result = await model.generateContent({
contents: [
{
role: "user",
parts: [
{ text: prompt },
{
inlineData: {
mimeType,
data: base64,
},
},
],
},
],
});

const text = getTextFromGenerateContentResponse(result);
if (!text) throw new Error("Gemini empty response");

// 3) JSON 파싱(모델이 실수로 텍스트 섞어도 1개 JSON 오브젝트만 뽑아서 파싱)
let jsonStr = text.trim();
if (!jsonStr.startsWith("{")) jsonStr = extractFirstJsonObject(jsonStr);

const parsed = JSON.parse(jsonStr);
const normalized = normalizeGeminiResult(parsed);

// 최소 검증
if (!normalized.purchaseDate) throw new Error("purchaseDate missing");
if (!Array.isArray(normalized.items) || normalized.items.length === 0) {
throw new Error("items missing");
}

return normalized;
}

export const onReceiptUploaded = onObjectFinalized(
{
region: FUNCTION_REGION,
memory: "1GiB",
timeoutSeconds: 300,
},
async (event) => {
const obj = event.data;
const objectName = obj.name;
const bucket = obj.bucket;

if (!objectName) {
logger.warn("No object name in event. Skip.");
return;
}

if (!objectName.startsWith("receipts/")) {
logger.info(`Skip non-receipt object: ${objectName}`);
return;
}

const fromPath = parseReceiptPath(objectName);
const fromMeta = parseFromCustomMetadata(
obj.metadata as Record<string, string> | undefined,
);

const identity = fromPath ?? fromMeta;
const identitySource = fromPath ? "path" : fromMeta ? "metadata" : "none";

if (!identity) {
logger.warn(`Path/metadata missing. Skip object=${objectName}`);
return;
}

const { uid, jobId } = identity;

logger.info(
`Receipt finalize event. source=${identitySource}, uid=${uid}, jobId=${jobId}, object=${objectName}`,
);

const jobRef = db.collection("receiptJobs").doc(jobId);

const canProcess = await db.runTransaction(async (tx) => {
const snap = await tx.get(jobRef);

if (!snap.exists) {
tx.set(
jobRef,
{
jobId,
uid,
storagePath: objectName,
status: "processing",
createdAt: FieldValue.serverTimestamp(),
updatedAt: FieldValue.serverTimestamp(),
},
{ merge: true },
);
return true;
}

const status = (snap.get("status") as string | undefined) ?? "pending";
if (status === "done" || status === "processing") return false;

tx.set(
jobRef,
{
status: "processing",
storagePath: objectName,
updatedAt: FieldValue.serverTimestamp(),
},
{ merge: true },
);

return true;
});

if (!canProcess) {
logger.info(`Job already processing/done. jobId=${jobId} -> return`);
return;
}

try {
const result = await analyzeReceiptWithGemini({
bucket,
objectName,
contentType: obj.contentType,
});

const storeName = (result.storeName || "").trim() || "가맹점(상호미상)";
const purchaseDate = (result.purchaseDate || "").trim();
const items = Array.isArray(result.items) ? result.items : [];

// uid -> email 유지 요구사항
// eslint-disable-next-line @typescript-eslint/no-var-requires
const adminAuth = require("firebase-admin/auth");
const userRecord = await adminAuth.getAuth().getUser(uid);
const email = userRecord.email;

if (!email) {
throw new Error(`User email is null for uid=${uid}. Cannot write to user/{email}/...`);
}

const cleanDate = purchaseDate.replace(/-/g, "");
const ts = Timestamp.fromDate(new Date(purchaseDate));

const batch = db.batch();
const savedDocIds: string[] = [];

items.forEach((item, index) => {
const productName = (item.productName || "").trim();
const unitPrice = Number(item.unitPrice) || 0;
const receivedQty = Number(item.receivedQty) || 1;
const totalAmount = Number(item.totalAmount) || unitPrice * receivedQty;

if (!productName || unitPrice <= 0 || receivedQty <= 0 || totalAmount <= 0) return;

const safeName = sanitizeDocIdPart(productName);
const docId = `${cleanDate}_${index}_${safeName}`;
savedDocIds.push(docId);

const docRef = db
.collection("user")
.doc(email)
.collection("imagehouseholdbook")
.doc(docId);

batch.set(docRef, {
storeName,
purchaseDate: ts,
productName,
unitPrice,
receivedQty,
totalAmount,
jobId,
sourceImagePath: objectName,
createdAt: FieldValue.serverTimestamp(),
});
});

if (savedDocIds.length === 0) {
throw new Error("No valid items after normalization");
}

await batch.commit();

await jobRef.set(
{
status: "done",
storeName,
purchaseDate,
docIdList: savedDocIds,
updatedAt: FieldValue.serverTimestamp(),
},
{ merge: true },
);

logger.info(`Job done. jobId=${jobId}, items=${savedDocIds.length}`);
} catch (err: unknown) {
const msg = err instanceof Error ? err.message : "unknown";
logger.error(`Job failed. jobId=${jobId}`, err);

await jobRef.set(
{
status: "failed",
errorMessage: msg,
updatedAt: FieldValue.serverTimestamp(),
},
{ merge: true },
);
}
},
);