import { db } from "./firebase.js";
import {
  collection,
  getDocs,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

// Firestore에서 users 컬렉션 데이터 가져와 user.html 테이블에 추가
document.addEventListener("DOMContentLoaded", async function () {
  console.log(
    "✅ user.js 로드 완료 - Firebase에서 사용자 데이터 가져오는 중..."
  );

  if (document.querySelector("tbody")) {
    const tableBody = document.querySelector("tbody");
    tableBody.innerHTML = ""; // 기존 데이터 초기화

    try {
      const querySnapshot = await getDocs(collection(db, "users"));

      querySnapshot.forEach((doc) => {
        const userId = doc.id; // 🔹 Document ID를 계정 이름으로 출력
        const userData = doc.data();

        // 🔹 모든 필드 데이터 가져오기 (줄바꿈 추가)
        let userInfo = Object.entries(userData)
          .map(([key, value]) => `${key}: ${value}`)
          .join("<br>"); // 🔹 필드 데이터를 '<br>'로 줄바꿈

        // 새로운 행 추가
        const newRow = document.createElement("tr");

        // 계정 이름 (Document ID 출력)
        const nameCell = document.createElement("td");
        nameCell.textContent = userId;
        nameCell.contentEditable = true;
        nameCell.style.fontSize = "20px";
        newRow.appendChild(nameCell);

        // 계정 정보 (해당 document의 모든 필드 출력, 줄바꿈 적용)
        const infoCell = document.createElement("td");
        infoCell.innerHTML = userInfo; // 🔹 innerHTML 사용하여 <br> 태그 적용
        infoCell.contentEditable = true;
        infoCell.style.fontSize = "20px";
        infoCell.style.textAlign = "left"; // 🔹 왼쪽 정렬 적용
        newRow.appendChild(infoCell);

        tableBody.appendChild(newRow);
      });

      console.log("✅ Firestore에서 사용자 데이터를 성공적으로 가져왔습니다.");
    } catch (error) {
      console.error("🔥 Firestore 데이터 로드 중 오류 발생:", error);
    }
  }
});

// 🔹 아이콘 클릭 시 user.html이 content-container에 로드되도록 설정 (master.html에서 실행)
document.addEventListener("DOMContentLoaded", function () {
  const userIcon = document.getElementById("icon-user");
  if (userIcon) {
    userIcon.addEventListener("click", function () {
      console.log("👥 사용자 관리 아이콘 클릭됨 - user.html 로드 시도");
      const contentContainer = document.querySelector(".content-container");
      if (contentContainer) {
        contentContainer.innerHTML =
          '<iframe src="user.html" style="width: 100%; height: 100%; border: none;"></iframe>';
        console.log("✅ user.html이 content-container에 로드됨.");
      } else {
        console.error(
          "❌ .content-container를 찾을 수 없음! master.html에 존재하는지 확인하세요."
        );
      }
    });
  }
});
