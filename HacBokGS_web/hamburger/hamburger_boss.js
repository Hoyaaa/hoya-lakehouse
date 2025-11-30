import { db } from "../firebase.js";
import {
  collectionGroup,
  writeBatch,
  doc,
  setDoc,
  getDoc,
  onSnapshot,
  deleteField,
  increment,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

const orderListElement = document.getElementById("order-list");
const dateInput = document.getElementById("dateInput");
const loadBtn = document.getElementById("load-btn");

const STORE_ID = "버거운버거";
let unsubscribe = null;

// 초기 날짜 세팅
const now = new Date();
const kst = new Date(now.getTime() + 9 * 60 * 60 * 1000);
const today = kst.toISOString().slice(0, 10);
dateInput.value = today;

function getSelectedDate() {
  return dateInput.value.replace(/-/g, "");
}

function formatDate(timestamp) {
  if (!timestamp?.toDate) return "";
  const utc = timestamp.toDate();
  const kst = new Date(utc.getTime() + 9 * 60 * 60 * 1000);
  return kst.toISOString().slice(0, 10).replace(/-/g, "");
}

function loadAllOrdersByStoreKey(dateStr) {
  if (unsubscribe) unsubscribe();

  orderListElement.innerHTML =
    "<tr><td colspan='6'>🔄 불러오는 중...</td></tr>";

  const query = collectionGroup(db, "orders");

  unsubscribe = onSnapshot(query, (snapshot) => {
    const matched = snapshot.docs
      .map((docSnap) => {
        const data = docSnap.data();
        if (!data[STORE_ID] || formatDate(data.timestamp) !== dateStr)
          return null;
        return { docSnap, storeData: data[STORE_ID], orderData: data };
      })
      .filter(Boolean);

    if (matched.length === 0) {
      orderListElement.innerHTML =
        "<tr><td colspan='6'>📭 주문이 없습니다.</td></tr>";
      return;
    }

    matched.sort((a, b) => {
      const aData = a.storeData;
      const bData = b.storeData;
      return aData.complete === bData.complete
        ? (a.orderData.orderNumber ?? a.docSnap.id) -
            (b.orderData.orderNumber ?? b.docSnap.id)
        : aData.complete
        ? 1
        : -1;
    });

    orderListElement.innerHTML = "";
    matched.forEach(({ docSnap, storeData, orderData }) =>
      renderOrder(docSnap, storeData, orderData)
    );
  });
}

function renderOrder(docSnap, storeData, orderData) {
  const row = document.createElement("tr");

  const orderNumberCell = document.createElement("td");
  orderNumberCell.textContent = orderData.orderNumber ?? docSnap.id;
  row.appendChild(orderNumberCell);

  const timeCell = document.createElement("td");
  const timestamp = orderData.timestamp?.toDate?.();
  timeCell.textContent = timestamp
    ? timestamp.toLocaleTimeString("ko-KR", {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
      })
    : "⏱️ 없음";
  row.appendChild(timeCell);

  const foodNames =
    storeData.menuList
      ?.map((item) => `${item.menu} (${item.amount})`)
      .join(", ") ?? "❓ 없음";
  const menuCell = document.createElement("td");
  menuCell.textContent = foodNames;
  row.appendChild(menuCell);

  const options =
    storeData.menuList
      ?.map((item) =>
        Array.isArray(item.optionList)
          ? item.optionList
              .map((opt) => `- ${opt.optionName} ×${opt.quantity ?? 1}`)
              .join("\n")
          : ""
      )
      .join("\n") ?? "";
  const optionCell = document.createElement("td");
  optionCell.textContent = options;
  row.appendChild(optionCell);

  const isComplete = storeData.complete === true;
  const stateCell = document.createElement("td");
  stateCell.innerHTML = `<span class="status ${
    isComplete ? "status-end" : "status-ing"
  }">${isComplete ? "완료" : "조리중"}</span>`;
  row.appendChild(stateCell);

  const actionCell = document.createElement("td");
  const btn = document.createElement("button");
  btn.textContent = "조리 완료";
  btn.classList.add("done-btn");
  btn.disabled = isComplete;

  btn.addEventListener("click", async () => {
    const userOrderDocId = docSnap.id;
    const userPath = docSnap.ref.path;
    const orderNumKey = orderData.orderNumber?.toString() ?? userOrderDocId;

    const flatOrderDocRef = doc(db, "store_order", STORE_ID);
    const datedBackupRef = doc(
      db,
      "store_order",
      STORE_ID,
      getSelectedDate(),
      orderNumKey
    );

    const batch = writeBatch(db);

    // ✅ 사용자 주문 complete 처리
    batch.update(docSnap.ref, {
      [`${STORE_ID}.complete`]: true,
    });

    try {
      // ✅ 기존 주문 내용 가져오기
      const flatDocSnap = await getDoc(flatOrderDocRef);
      const flatData = flatDocSnap.data();
      const fieldData = flatData?.[orderNumKey];

      if (!fieldData) {
        alert("삭제할 주문 데이터를 찾을 수 없습니다.");
        return;
      }

      // ✅ 현재 한국 시간 객체 생성
      const kstNow = new Date(
        new Date().toLocaleString("en-US", { timeZone: "Asia/Seoul" })
      );

      const dateKey = new Intl.DateTimeFormat("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        timeZone: "Asia/Seoul",
      })
        .format(kstNow)
        .replace(/\./g, "")
        .replace(/\s/g, "");

      const timeKey = new Intl.DateTimeFormat("ko-KR", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
        timeZone: "Asia/Seoul",
      })
        .format(kstNow)
        .replace("시", "")
        .replace("분", "")
        .replace(/\s/g, ":");

      // ✅ 1. 백업 저장
      await setDoc(datedBackupRef, {
        data: fieldData,
        completedAt: kstNow,
      });

      // ✅ 2. 혼잡도 기록 누적
      const congestionRef = doc(
        db,
        "store_order",
        "congestion",
        dateKey,
        "completedAt"
      );
      await setDoc(congestionRef, { [timeKey]: increment(1) }, { merge: true });

      const eatingRef = doc(db, "store_order", "congestion", dateKey, "eating");
      await setDoc(eatingRef, { count: increment(1) }, { merge: true });

      // ✅ 3. flat 문서에서 삭제
      batch.update(flatOrderDocRef, {
        [orderNumKey]: deleteField(),
      });

      // ✅ 4. 커밋
      await batch.commit();

      console.log(
        `✅ ${orderNumKey} 조리완료 → 백업 저장 + 혼잡도 기록 + 식사인원 증가 + 삭제 완료`
      );
    } catch (error) {
      console.error("🔥 처리 실패:", error.message);
      alert("처리 중 오류 발생: 콘솔 확인");
    }
  });

  actionCell.appendChild(btn);
  row.appendChild(actionCell);
  orderListElement.appendChild(row);
}

loadBtn.addEventListener("click", () => {
  if (!dateInput.value) {
    alert("날짜를 선택하세요!");
    return;
  }
  loadAllOrdersByStoreKey(getSelectedDate());
});

document.addEventListener("DOMContentLoaded", () => {
  const storeBtn = document.getElementById("store");
  if (storeBtn) {
    storeBtn.addEventListener("click", () => {
      window.open(
        "hamburger_food.html",
        "식당관리",
        "width=1800,height=800,resizable=yes,scrollbars=yes"
      );
    });
  }
});
