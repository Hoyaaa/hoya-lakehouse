import { db } from "./firebase.js";
import {
  collection,
  doc,
  getDocs,
  getDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

const tbody = document.querySelector("tbody");

function getTodayKey() {
  const now = new Date();
  const koreaTime = new Date(now.getTime() + 9 * 60 * 60 * 1000); // UTC +9
  return koreaTime.toISOString().slice(0, 10).replace(/-/g, "");
}

async function loadOrderStatus() {
  const orderManagementRef = collection(db, "order_management");
  const todayKey = getTodayKey();

  try {
    const snapshot = await getDocs(orderManagementRef);

    snapshot.forEach(async (docSnap) => {
      const storeName = docSnap.id;
      const storeData = docSnap.data();
      const waitingCount = storeData.count ?? 0;

      const tr = document.createElement("tr");

      const td1 = document.createElement("td");
      td1.textContent = storeName;

      const td2 = document.createElement("td");
      td2.textContent = `${waitingCount}건`;

      const td3 = document.createElement("td");
      td3.textContent = "로딩 중...";

      tr.appendChild(td1);
      tr.appendChild(td2);
      tr.appendChild(td3);
      tbody.appendChild(tr);

      // ✅ best_menu Top 5 (금일) 조회
      try {
        const bestMenuRef = doc(db, "best_menu", storeName, todayKey, "menu");
        const bestMenuSnap = await getDoc(bestMenuRef);

        if (!bestMenuSnap.exists()) {
          td3.textContent = "-";
          return;
        }

        const data = bestMenuSnap.data();
        const menuEntries = Object.entries(data)
          .filter(
            ([key, value]) => key !== "updatedAt" && typeof value === "number"
          )
          .sort((a, b) => b[1] - a[1])
          .slice(0, 5);

        if (menuEntries.length === 0) {
          td3.textContent = "-";
        } else {
          td3.innerHTML = `
  <div style="display: inline-block; text-align: left;">
    ${menuEntries
      .map((entry, index) => `${index + 1}. ${entry[0]} (${entry[1]})`)
      .join("<br>")}
  </div>`;
        }
      } catch (err) {
        console.error(`🔥 [${storeName}] best_menu 조회 실패:`, err.message);
        td3.textContent = "-";
      }
    });
  } catch (error) {
    console.error("🔥 주문 현황을 불러오는 데 실패했습니다:", error);
  }
}

loadOrderStatus();
