import { db } from "./firebase.js";
import {
  getDoc,
  doc,
  setDoc,
  increment, // ✅ 누락된 부분 추가
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

document.addEventListener("DOMContentLoaded", function () {
  const congestionIcon = document.getElementById("congestion");
  if (congestionIcon) {
    congestionIcon.addEventListener("click", function () {
      const contentContainer = document.querySelector(".content-container");
      if (contentContainer) {
        contentContainer.innerHTML =
          '<iframe src="congestion.html" style="width: 100%; height: 100%; border: none;"></iframe>';
      }
    });
  }

  function getCongestionStatus(score) {
    if (score > 200) {
      return { label: "혼잡", desc: "매우 혼잡함", color: "#C62828" };
    } else if (score > 150) {
      return { label: "혼잡우려", desc: "혼잡 예상됨", color: "#FFA726" };
    } else if (score > 100) {
      return { label: "보통", desc: "중간 수준", color: "#FFF176" };
    } else if (score > 50) {
      return { label: "여유", desc: "여유 있음", color: "#81C784" };
    } else {
      return { label: "여유", desc: "거의 없음", color: "#A5D6A7" };
    }
  }

  function getTodayString() {
    const now = new Date();
    const yyyy = now.getFullYear();
    const mm = String(now.getMonth() + 1).padStart(2, "0");
    const dd = String(now.getDate()).padStart(2, "0");
    return `${yyyy}${mm}${dd}`;
  }

  async function renderCongestionStatus() {
    const today = getTodayString();
    const storeIds = ["꾸이한끼", "버거운버거", "쑝쑝돈가스", "태산김치찜"];
    const now = new Date();

    let totalScore = 0;
    let totalEatingCount = 0;

    for (const storeId of storeIds) {
      const waitDoc = await getDoc(doc(db, "order_management", storeId));
      if (waitDoc.exists()) {
        const waitCount = waitDoc.data().count || 0;
        totalScore += waitCount * 0.5;
      }
    }

    const congestionDoc = await getDoc(
      doc(db, "store_order", "congestion", today, "completedAt")
    );

    if (congestionDoc.exists()) {
      const completedMap = congestionDoc.data();
      for (const [timeKey, count] of Object.entries(completedMap)) {
        const [hh, mm] = timeKey.split(":").map(Number);
        const completedAt = new Date(now);
        completedAt.setHours(hh, mm, 0, 0);

        const minutesAgo = (now - completedAt) / 1000 / 60;

        if (minutesAgo < 10) totalScore += count * 1.0;
        else if (minutesAgo < 20) totalScore += count * 0.8;
        else if (minutesAgo < 30) totalScore += count * 0.5;
        else if (minutesAgo < 40) totalScore += count * 0.2;

        if (minutesAgo >= 0 && minutesAgo <= 30) {
          totalEatingCount += count;
        }
      }
    }

    // ✅ 식사 인원 증감 처리
    const eatingRef = doc(db, "store_order", "congestion", today, "eating");
    const eatingSnap = await getDoc(eatingRef);
    const prevCount = eatingSnap.exists() ? eatingSnap.data().count || 0 : 0;
    const diff = totalEatingCount - prevCount;

    console.log("🍽️ 식사 인원 업데이트");
    console.log(" - 이전 추정 인원:", prevCount);
    console.log(" - 현재 추정 인원:", totalEatingCount);
    console.log(" - 증감 값:", diff);

    if (diff !== 0) {
      await setDoc(eatingRef, { count: increment(diff) }, { merge: true });
      console.log("✅ Firestore 식사 인원 count 증감 적용 완료");
    } else {
      console.log("⚠️ 변경 없음 → Firestore 업데이트 생략");
    }

    // ✅ 혼잡도 시각화
    const percent = Math.min(totalScore / 350, 1) * 100;
    const { label, desc, color } = getCongestionStatus(totalScore);

    const barFill = document.getElementById("congestionBar");
    if (barFill) {
      barFill.style.width = `${percent}%`;
      barFill.style.backgroundColor = color;
    }

    const infoArea = document.getElementById("eating-info");
    if (infoArea) {
      infoArea.innerHTML = `
        <div style="margin-top: 10px; font-size: 18px; font-weight: bold;">
          혼잡도 등급: ${label} (${desc})<br>
          현재 식사 중으로 추정되는 인원: ${totalEatingCount}팀
        </div>
      `;
    }
  }

  renderCongestionStatus();
  setInterval(renderCongestionStatus, 60000);
});
