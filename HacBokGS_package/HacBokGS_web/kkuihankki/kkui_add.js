import { db } from "../firebase.js";
import {
  doc,
  setDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";
import {
  getStorage,
  ref,
  uploadBytes,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-storage.js";

const storage = getStorage();
let uploadedImagePath = "";

document
  .getElementById("image-upload")
  .addEventListener("change", async (e) => {
    const file = e.target.files[0];
    const id = document.getElementById("menu-id").value.trim();
    const category = document.getElementById("category-select").value;

    if (!file || !id) {
      alert("먼저 메뉴 ID를 입력하고 이미지를 선택하세요.");
      return;
    }

    const storagePath = `store/kkuihankki/${category}/${id}.png`;
    const storageRef = ref(storage, storagePath);

    try {
      await uploadBytes(storageRef, file);
      uploadedImagePath = storagePath;
      alert("✅ 이미지 업로드 완료!");
    } catch (err) {
      console.error("🔥 이미지 업로드 실패:", err);
      alert("❌ 이미지 업로드 중 오류 발생");
    }
  });

document.getElementById("add-button").addEventListener("click", async () => {
  const category = document.getElementById("category-select").value;
  const id = document.getElementById("menu-id").value.trim();
  const description = document.getElementById("description").value.trim();
  const ingredients = document
    .getElementById("ingredients")
    .value.split(",")
    .map((e) => e.trim())
    .filter((e) => e);
  const priceSingle = document.getElementById("price-single").value;
  const priceSet = document.getElementById("price-set").value;
  const salesStatus = document.getElementById("sales-status").value;

  if (
    !id ||
    !description ||
    ingredients.length === 0 ||
    !priceSingle ||
    !priceSet ||
    !uploadedImagePath
  ) {
    alert("⚠️ 모든 필드를 입력하고 이미지를 업로드하세요.");
    return;
  }

  const data = {
    id: id,
    description: description,
    ingredient: ingredients,
    SalesStatus: salesStatus,
    imagePath: uploadedImagePath,
    size: {
      single: { price: priceSingle },
      set: { price: priceSet },
    },
  };

  try {
    await setDoc(doc(db, `store/kkuihankki/${category}`, id), data);
    alert("✅ 메뉴가 성공적으로 추가되었습니다.");
    uploadedImagePath = "";
  } catch (err) {
    console.error("🔥 메뉴 추가 실패:", err);
    alert("❌ Firestore 저장 중 오류 발생");
  }
});
