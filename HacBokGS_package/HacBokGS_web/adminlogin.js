import { db } from "./firebase.js";
import {
  doc,
  setDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("login-form");
  const spinner = document.getElementById("loading-spinner");
  const statusText = document.querySelector("#loading-spinner p");

  loginForm.addEventListener("submit", function (event) {
    event.preventDefault();

    const adminPassword = document.getElementById("admin-password").value;
    const correctPassword = "1234";

    // 1. 관리자 번호 확인 시작
    spinner.style.display = "block";
    statusText.textContent = "🔐 관리자 번호 확인 중입니다...";

    setTimeout(async () => {
      if (adminPassword === correctPassword) {
        // ✅ 관리자 인증 완료
        statusText.textContent = "✅ 관리자 번호 확인 완료!";

        // ✅ store_order/orderNum 문서 number 필드 초기화
        try {
          await setDoc(
            doc(db, "store_order", "orderNum"),
            { number: 0 },
            { merge: true }
          );
          console.log("✅ orderNum 초기화 완료");
        } catch (e) {
          console.error("🔥 orderNum 초기화 실패:", e);
        }

        // 3. 메뉴 불러오기 시작
        setTimeout(() => {
          statusText.textContent = "📦 메뉴 정보를 불러오는 중입니다...";

          // 4. Flask fetch 요청
          fetch("http://localhost:5001/run-todaymenu")
            .then((res) => res.text())
            .then(async (msg) => {
              console.log("📦 크롤링 응답:", msg);
              statusText.textContent = "✅ 메뉴 정보를 불러왔습니다!";

              setTimeout(() => {
                window.location.href = "master_main.html";
              }, 1000);
            })
            .catch((err) => {
              console.error("🔥 크롤링 요청 실패:", err);
              alert("크롤링 실패!");
              spinner.style.display = "none";
            });
        }, 1000); // 메뉴 불러오기 메시지 전환 시간
      } else {
        alert("비밀번호가 틀렸습니다.");
        spinner.style.display = "none";
      }
    }, 800); // 관리자 번호 확인 완료 시간
  });
});
