// Firebase SDK에서 필요한 기능을 가져오기
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-analytics.js";
import { getFirestore } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";
import { getStorage } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-storage.js"; // ✅ 추가

// 🔹 Firebase 설정
const firebaseConfig = {
  apiKey: "AIzaSyDnMGZoWixVP60C6_z8pwA0kFqTMozVeJk",
  authDomain: "nsualarmy.firebaseapp.com",
  projectId: "nsualarmy",
  storageBucket: "nsualarmy.firebasestorage.app", // <- 도메인 아님!
  messagingSenderId: "260568281022",
  appId: "1:260568281022:web:dd558b72a7e8d577f813ef",
  measurementId: "G-QY68L8LP0S",
};

// 🔹 Firebase 초기화
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

// Firestore & Storage 연결
export const db = getFirestore(app);
export const storage = getStorage(app); // ✅ 추가

// 🔹 Firebase 연결 확인 로그
console.log("✅ Firebase가 정상적으로 초기화되었습니다.");
console.log("🔹 프로젝트 ID:", firebaseConfig.projectId);
console.log("🔹 Firestore 연결 테스트:", db);
console.log("🔹 Storage 연결 테스트:", storage);
