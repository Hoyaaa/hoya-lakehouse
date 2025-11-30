import { db } from "./firebase.js";
import {
  doc,
  getDoc,
  setDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

// 🏪 추적할 가게 목록
const STORE_LIST = ["꾸이한끼", "버거운버거", "쑝쑝돈가스", "태산김치찜"];

/**
 * 특정 가게의 store_order 필드 수를 count → order_management에 저장
 */
async function updateOrderCountForStore(storeId) {
  try {
    const storeOrderRef = doc(db, "store_order", storeId);
    const storeOrderSnap = await getDoc(storeOrderRef);

    if (!storeOrderSnap.exists()) {
      console.warn(`📭 [${storeId}] 주문 문서 없음`);
      return;
    }

    const orderData = storeOrderSnap.data();

    // 필드 수 = 대기 주문 수
    const orderCount = Object.keys(orderData).length;

    const mgmtRef = doc(db, "order_management", storeId);
    await setDoc(mgmtRef, { count: orderCount }, { merge: true });

    console.log(`✅ [${storeId}] 대기 주문 수 ${orderCount}건 반영`);
  } catch (error) {
    console.error(`🔥 [${storeId}] 처리 실패:`, error.message);
  }
}

/**
 * 전체 가게 순회 후 갱신
 */
async function updateAllStoresOrderCount() {
  for (const storeId of STORE_LIST) {
    await updateOrderCountForStore(storeId);
  }
}

// ⏱️ 일정 주기로 자동 실행 (예: 30초마다)
setInterval(updateAllStoresOrderCount, 30000); // 30,000ms = 30초

// 또는 수동 실행도 가능
updateAllStoresOrderCount();
