// Firebase Firestore 관련 함수들 가져오기
import { db } from "./firebase.js";
import {
  collection,
  doc,
  getDocs,
  deleteDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

// 광고 삭제 기능
async function loadAdvertisingData() {
  const adListContainerIng = document.getElementById("ad-list-ing");
  const adListContainerEnd = document.getElementById("ad-list-end");

  adListContainerIng.innerHTML = ""; // 기존 목록 초기화
  adListContainerEnd.innerHTML = ""; // 기존 목록 초기화

  try {
    // Firestore에서 "advertising" 컬렉션의 "end"와 "ing" 문서의 "posts" 컬렉션 가져오기
    const postsCollectionEnd = collection(db, "advertising", "end", "posts");
    const postsCollectionIng = collection(db, "advertising", "ing", "posts");

    const querySnapshotEnd = await getDocs(postsCollectionEnd);
    const querySnapshotIng = await getDocs(postsCollectionIng);

    // 광고 목록 표시 - "end" 컬렉션
    querySnapshotEnd.forEach((docSnapshot) => {
      const adId = docSnapshot.id; // Document ID를 광고 제목으로 사용
      const { title, url } = docSnapshot.data(); // title과 url 필드를 가져옵니다.
      const fieldsHTML = generateFieldsHTML(docSnapshot);
      const newItem = document.createElement("tr");
      newItem.innerHTML = `
        <td>${adId}</td> <!-- Document ID를 광고 제목으로 표시 -->
        <td>
          <p><strong>Title:</strong> ${title}</p> <!-- title 필드 -->
          <p><strong>URL:</strong> <a href="${url}" target="_blank">${url}</a></p> <!-- url 필드 -->
          ${fieldsHTML}
          <button onclick="deletePost('${docSnapshot.id}', 'end')">삭제</button>
        </td>
      `;
      adListContainerEnd.appendChild(newItem);
    });

    // 광고 목록 표시 - "ing" 컬렉션
    querySnapshotIng.forEach((docSnapshot) => {
      const adId = docSnapshot.id; // Document ID를 광고 제목으로 사용
      const { title, url } = docSnapshot.data(); // title과 url 필드를 가져옵니다.
      const fieldsHTML = generateFieldsHTML(docSnapshot);
      const newItem = document.createElement("tr");
      newItem.innerHTML = `
        <td>${adId}</td> <!-- Document ID를 광고 제목으로 표시 -->
        <td>
          <p><strong>Title:</strong> ${title}</p> <!-- title 필드 -->
          <p><strong>URL:</strong> <a href="${url}" target="_blank">${url}</a></p> <!-- url 필드 -->
          ${fieldsHTML}
          <button onclick="deletePost('${docSnapshot.id}', 'ing')">삭제</button>
        </td>
      `;
      adListContainerIng.appendChild(newItem);
    });
  } catch (error) {
    console.error("🔥 광고 데이터 로드 중 오류 발생:", error);
  }
}

// 광고 삭제 기능
async function deletePost(adId, status) {
  try {
    const postRef = doc(db, "advertising", status, "posts", adId);
    await deleteDoc(postRef);
    alert(`광고 ID: ${adId} 삭제 완료!`);
    loadAdvertisingData(); // 목록 새로고침
  } catch (error) {
    console.error(`🔥 ${adId} 삭제 중 오류 발생:`, error);
    alert(`삭제 중 오류가 발생했습니다. ${adId}를 확인해 주세요.`);
  }
}

// 필드 이름과 값을 HTML로 생성
function generateFieldsHTML(docSnapshot) {
  let fieldsHTML = "";
  Object.entries(docSnapshot.data()).forEach(([fieldName, fieldValue]) => {
    if (
      fieldName !== "status" &&
      fieldName !== "title" &&
      fieldName !== "url" &&
      fieldName !== "expiration" // expiration 필드는 제외
    ) {
      fieldsHTML += `<p><strong>${fieldName}:</strong> ${fieldValue}</p>`;
    }
  });
  return fieldsHTML;
}

// 페이지 로드 시 광고 데이터 로드
window.onload = loadAdvertisingData;

// Make deletePost function globally accessible
window.deletePost = deletePost;
