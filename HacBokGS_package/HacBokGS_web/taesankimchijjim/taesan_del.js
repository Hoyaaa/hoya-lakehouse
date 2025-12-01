import { db } from "../firebase.js";
import {
  collection,
  getDocs,
  doc,
  getDoc,
  deleteDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";
import {
  getStorage,
  ref,
  deleteObject,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-storage.js";

const categorySelect = document.getElementById("category-select");
const menuSelect = document.getElementById("menu-select");
const deleteButton = document.getElementById("delete-button");

const storage = getStorage();

// 🔹 카테고리 선택 → 메뉴 목록 불러오기
categorySelect.addEventListener("change", async () => {
  const category = categorySelect.value;
  menuSelect.innerHTML = "";

  if (!category) {
    menuSelect.innerHTML =
      "<option value=''>카테고리를 먼저 선택하세요</option>";
    menuSelect.disabled = true;
    return;
  }

  try {
    const menuColRef = collection(db, `store/taesankimchijjim/${category}`);
    const snapshot = await getDocs(menuColRef);

    if (snapshot.empty) {
      menuSelect.innerHTML =
        "<option value=''>해당 카테고리에 메뉴가 없습니다</option>";
      menuSelect.disabled = true;
      return;
    }

    menuSelect.disabled = false;
    menuSelect.innerHTML = `<option value="">-- 삭제할 메뉴 선택 --</option>`;

    snapshot.forEach((docSnap) => {
      const data = docSnap.data();
      const label = data.id || docSnap.id;
      menuSelect.innerHTML += `<option value="${docSnap.id}">${label}</option>`;
    });
  } catch (err) {
    console.error("🔥 메뉴 목록 로딩 실패:", err);
    alert("❌ 메뉴 불러오기 오류");
  }
});

// 🔹 삭제 버튼 → Firestore + Storage 삭제
deleteButton.addEventListener("click", async () => {
  const category = categorySelect.value;
  const menuId = menuSelect.value;

  if (!category || !menuId) {
    alert("⚠️ 카테고리와 메뉴를 모두 선택하세요.");
    return;
  }

  const confirmDelete = confirm(`정말로 메뉴 '${menuId}'를 삭제하시겠습니까?`);
  if (!confirmDelete) return;

  try {
    const docRef = doc(db, `store/taesankimchijjim/${category}`, menuId);
    const docSnap = await getDoc(docRef);
    let imagePath = null;

    if (docSnap.exists()) {
      imagePath = docSnap.data().imagePath;
    }

    await deleteDoc(docRef);
    console.log(`📄 Firestore 문서 '${menuId}' 삭제 완료`);

    if (imagePath) {
      const imageRef = ref(storage, imagePath);
      await deleteObject(imageRef);
      console.log(`🖼 Storage 이미지 '${imagePath}' 삭제 완료`);
    }

    alert(`✅ 메뉴 '${menuId}' 삭제 완료`);
    menuSelect.querySelector(`option[value="${menuId}"]`).remove();
    menuSelect.selectedIndex = 0;
  } catch (err) {
    console.error("🔥 삭제 실패:", err);
    alert("❌ 삭제 중 오류 발생");
  }
});
