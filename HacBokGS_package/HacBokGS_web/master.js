console.log("📌 master.js가 정상적으로 로드되었습니다.");

import {
  getFirestore,
  collection,
  getDocs,
  doc,
  getDoc,
  setDoc,
  deleteDoc,
  Timestamp,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

import {
  getStorage,
  ref,
  getDownloadURL,
  uploadBytes,
  deleteObject,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-storage.js";

const db = getFirestore();
const storage = getStorage();
const storeList = ["꾸이한끼", "버거운버거", "쑝쑝돈가스", "태산김치찜"];

function getTodayKey() {
  const now = new Date();
  const koreaTime = new Date(now.getTime() + 9 * 60 * 60 * 1000); // UTC+9 보정
  return koreaTime.toISOString().slice(0, 10).replace(/-/g, "");
}

// ✅ 광고 만료 처리
async function moveExpiredAds() {
  console.log("📌 moveExpiredAds 실행됨");
  try {
    const postsCollection = collection(db, "advertising", "ing", "posts");
    const expiredPosts = [];
    const snapshot = await getDocs(postsCollection);
    const now = Timestamp.now();

    snapshot.forEach((docSnap) => {
      const data = docSnap.data();
      if (data.expiration && data.expiration.toMillis() <= now.toMillis()) {
        expiredPosts.push({ id: docSnap.id, data });
      }
    });

    if (expiredPosts.length === 0) {
      console.log("✅ 만료된 광고 없음");
      return;
    }

    const endPostsCollection = collection(db, "advertising", "end", "posts");
    const endSnapshot = await getDocs(endPostsCollection);
    let existingIds = endSnapshot.docs
      .map((docSnap) => {
        const match = docSnap.id.match(/^advertise_(\d+)$/);
        return match ? parseInt(match[1]) : null;
      })
      .filter(Boolean)
      .sort((a, b) => b - a);

    let newId = existingIds.length > 0 ? existingIds[0] - 1 : 30;
    while (existingIds.includes(newId)) newId--;

    for (let post of expiredPosts) {
      const newDocId = `advertise_${newId}`;
      const originalData = post.data;
      const originalImageUrl = originalData.imageUrl || null;

      if (originalImageUrl) {
        try {
          const fileName = decodeURIComponent(
            originalImageUrl.split("/").pop().split("?")[0]
          );
          const sourceRef = ref(
            storage,
            `advertising/ing/${post.id}/${fileName}`
          );
          const destRef = ref(
            storage,
            `advertising/end/${newDocId}/${fileName}`
          );
          const blob = await fetch(originalImageUrl).then((res) => res.blob());
          await uploadBytes(destRef, blob);
          await deleteObject(sourceRef);
          originalData.imageUrl = await getDownloadURL(destRef);
        } catch (e) {
          console.warn("⚠️ 이미지 이동 중 오류:", e);
        }
      }

      await setDoc(
        doc(db, "advertising", "end", "posts", newDocId),
        originalData
      );
      await deleteDoc(doc(db, "advertising", "ing", "posts", post.id));
      console.log(`📌 광고 이동 완료: ${post.id} → ${newDocId}`);
      newId--;
    }

    console.log("✅ 광고 이동 완료!");
  } catch (error) {
    console.error("🔥 광고 이동 오류:", error);
  }
}

// ✅ 주문 수 카운트
async function updateOrderCountForStore(storeId) {
  try {
    const ref = doc(db, "store_order", storeId);
    const snap = await getDoc(ref);
    if (!snap.exists()) return;

    const orderCount = Object.keys(snap.data()).length;
    await setDoc(
      doc(db, "order_management", storeId),
      { count: orderCount },
      { merge: true }
    );
    console.log(`✅ [${storeId}] 대기 주문 수: ${orderCount}`);
  } catch (error) {
    console.error(`🔥 [${storeId}] 대기 수 계산 실패:`, error.message);
  }
}

async function updateOrderCountAllStores() {
  for (const storeId of storeList) {
    await updateOrderCountForStore(storeId);
  }
}

// ✅ best_menu 오늘 데이터 저장 (/best_menu/{storeId}/{yyyyMMdd}/menu)
async function updateBestMenuForStore(storeId) {
  try {
    const todayKey = getTodayKey();
    const menuCount = {};

    const orderDocs = await getDocs(
      collection(db, "store_order", storeId, todayKey)
    );

    for (const docSnap of orderDocs.docs) {
      const items = docSnap.data().data || [];
      items.forEach((item) => {
        const menu = item.menu;
        const amount = item.amount ?? 1;
        if (!menu) return;
        if (!menuCount[menu]) menuCount[menu] = 0;
        menuCount[menu] += amount;
      });
    }

    if (Object.keys(menuCount).length === 0) {
      console.log(`⚠️ [${storeId}] ${todayKey} 베스트 메뉴 없음`);
      return;
    }

    await setDoc(doc(db, "best_menu", storeId, todayKey, "menu"), menuCount, {
      merge: true,
    });

    console.log(`✅ [${storeId}] ${todayKey} 베스트 메뉴 저장 완료`);
  } catch (error) {
    console.error(`🔥 [${storeId}] 베스트 메뉴 저장 실패:`, error.message);
  }
}

async function updateBestMenuAllStores() {
  for (const storeId of storeList) {
    await updateBestMenuForStore(storeId);
  }
}

// 🔁 주기 실행
setInterval(() => {
  console.log("⏳ 광고 만료 체크 실행");
  moveExpiredAds();
}, 30 * 1000);

setInterval(() => {
  console.log("⏳ 대기 주문 수 체크 실행");
  updateOrderCountAllStores();
}, 10 * 1000);

setInterval(() => {
  console.log("⏳ 금일 베스트 메뉴 집계 실행");
  updateBestMenuAllStores();
}, 10 * 1000);

// ▶ 최초 1회 실행
document.addEventListener("DOMContentLoaded", function () {
  console.log("📌 페이지 로드 완료, 초기 실행");
  moveExpiredAds();
  updateOrderCountAllStores();
  updateBestMenuAllStores();

  const menuMap = {
    advertising: "advertising.html",
    bullentin_board: "bullentin_board.html",
    today_menu: "todaymenu.html",
    store: "store.html",
    icon_order: "jumun.html",
  };

  Object.entries(menuMap).forEach(([id, page]) => {
    const icon = document.getElementById(id);
    if (icon) {
      icon.addEventListener("click", function () {
        const container = document.querySelector(".content-container");
        if (container) {
          container.innerHTML = `<iframe src="${page}" style="width: 100%; height: 100%; border: none;"></iframe>`;
        }
      });
    }
  });
});
