// Firestore 및 Storage 가져오기
import { db, storage } from "./firebase.js";
import {
  collection,
  doc,
  getDocs,
  setDoc,
  Timestamp,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";
import {
  ref,
  uploadBytes,
  getDownloadURL,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-storage.js";

// 게시물 등록 기능
export async function registerPost() {
  console.log("🔹 버튼 클릭됨, 데이터 저장 시작...");

  const title = document.getElementById("title").value;
  const url = document.getElementById("url").value;
  const datetime = document.getElementById("datetime").value;
  const imageFile = document.getElementById("image").files[0];
  const bannerImageFile = document.getElementById("bannerImage").files[0]; // ✅ 배너 이미지

  if (!title || !url || !datetime) {
    alert("제목, URL과 종료 시간을 모두 입력하세요.");
    console.error("❌ 입력값이 부족합니다.");
    return;
  }

  try {
    const postsCollection = collection(db, "advertising", "ing", "posts");

    // 새 문서 ID 생성 (advertise_숫자 형식)
    const querySnapshot = await getDocs(postsCollection);
    let maxNumber = 0;
    querySnapshot.forEach((docSnap) => {
      const docId = docSnap.id;
      const match = docId.match(/^advertise_(\d+)$/);
      if (match) {
        const num = parseInt(match[1]);
        if (num > maxNumber) {
          maxNumber = num;
        }
      }
    });

    const newDocId =
      maxNumber > 0 ? `advertise_${maxNumber + 1}` : "advertise_1";
    console.log(`📌 새 문서 ID: ${newDocId}`);

    // 홍보 이미지 업로드
    let imageUrl = "";
    if (imageFile) {
      const storageRef = ref(
        storage,
        `advertising/ing/${newDocId}/${imageFile.name}`
      );
      const snapshot = await uploadBytes(storageRef, imageFile);
      imageUrl = await getDownloadURL(snapshot.ref);
      console.log("📸 홍보 이미지 업로드 완료:", imageUrl);
    }

    // ✅ 광고 배너 이미지 업로드
    let bannerImageUrl = "";
    if (bannerImageFile) {
      const bannerRef = ref(
        storage,
        `advertising/ing/${newDocId}/banner_${bannerImageFile.name}`
      );
      const bannerSnapshot = await uploadBytes(bannerRef, bannerImageFile);
      bannerImageUrl = await getDownloadURL(bannerSnapshot.ref);
      console.log("🖼️ 광고 배너 이미지 업로드 완료:", bannerImageUrl);
    }

    // Firestore에 문서 저장
    await setDoc(doc(db, "advertising", "ing", "posts", newDocId), {
      title,
      url,
      expiration: Timestamp.fromDate(new Date(datetime)),
      imageUrl,
      bannerImageUrl, // ✅ 추가 저장
    });

    alert("✅ 게시물이 등록되었습니다!");
    console.log("📌 Firestore에 데이터 저장 완료!");

    // 입력 초기화
    document.getElementById("title").value = "";
    document.getElementById("url").value = "";
    document.getElementById("datetime").value = "";
    document.getElementById("image").value = "";
    document.getElementById("bannerImage").value = "";
    document.getElementById("preview").src = "";
    document.getElementById("bannerPreview").src = "";

    // 부모 새로고침 및 창 닫기
    if (window.opener) {
      window.opener.location.reload();
    }
    window.close();
  } catch (error) {
    console.error("🔥 등록 중 오류 발생:", error);
    alert("등록 중 오류가 발생했습니다. 콘솔을 확인하세요.");
  }
}

// 모듈 환경에서 이벤트 등록
document.addEventListener("DOMContentLoaded", () => {
  const button = document.querySelector("button");
  button.addEventListener("click", registerPost);

  // 🔁 이미지 미리보기 (홍보 이미지)
  document.getElementById("image").addEventListener("change", function () {
    const file = this.files[0];
    const preview = document.getElementById("preview");
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => (preview.src = e.target.result);
      reader.readAsDataURL(file);
    } else {
      preview.src = "";
    }
  });

  // ✅ 광고 배너 이미지 미리보기
  document
    .getElementById("bannerImage")
    .addEventListener("change", function () {
      const file = this.files[0];
      const preview = document.getElementById("bannerPreview");
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => (preview.src = e.target.result);
        reader.readAsDataURL(file);
      } else {
        preview.src = "";
      }
    });
});
