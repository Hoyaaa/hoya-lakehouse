import { db } from "../firebase.js";
import {
  collection,
  getDocs,
  doc,
  getDoc,
  updateDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

let selectedDocInfo = null;

document.addEventListener("DOMContentLoaded", function () {
  if (document.querySelector("tbody")) {
    loadStoreData();
    showSaveButton();
  }
});

async function loadStoreData() {
  const tableBody = document.querySelector("tbody");
  tableBody.innerHTML = "";

  try {
    const storeId = "hamburger"; // 🔸 버거운버거 ID 고정
    const storeRef = doc(db, "store", storeId);
    const storeSnap = await getDoc(storeRef);

    if (!storeSnap.exists()) {
      console.error("❌ hamburger 문서를 찾을 수 없습니다.");
      tableBody.innerHTML =
        "<tr><td colspan='4'>버거운버거 가게 데이터를 찾을 수 없습니다.</td></tr>";
      return;
    }

    const newRow = document.createElement("tr");

    const storeCell = document.createElement("td");
    storeCell.textContent = storeId;
    storeCell.style.cursor = "pointer";
    newRow.appendChild(storeCell);

    const menuListCell = document.createElement("td");
    menuListCell.innerHTML = "<div id='menu-list'>식당을 클릭하세요</div>";
    menuListCell.setAttribute("rowspan", 1);
    newRow.appendChild(menuListCell);

    const foodListCell = document.createElement("td");
    foodListCell.innerHTML = "<div id='food-list'></div>";
    foodListCell.setAttribute("rowspan", 1);
    newRow.appendChild(foodListCell);

    const detailsCell = document.createElement("td");
    detailsCell.id = "details-cell";
    detailsCell.innerHTML = `<div id="details"></div>`;
    detailsCell.setAttribute("rowspan", 1);
    detailsCell.style.textAlign = "left";
    detailsCell.style.verticalAlign = "top";
    detailsCell.style.border = "1px solid #aaa";
    newRow.appendChild(detailsCell);

    storeCell.addEventListener("click", function () {
      resetData();
      loadFoodList(storeId, menuListCell, foodListCell, detailsCell);
    });

    tableBody.appendChild(newRow);
  } catch (error) {
    console.error("🔥 Firestore 데이터 로드 중 오류 발생:", error);
  }
}

async function loadFoodList(storeId, menuCell, foodCell, detailsCell) {
  if (!menuCell || !foodCell || !detailsCell) return;

  try {
    const storeRef = doc(db, "store", storeId);
    const storeSnap = await getDoc(storeRef);

    if (!storeSnap.exists()) {
      console.error("해당 스토어 문서를 찾을 수 없습니다.");
      return;
    }

    const storeData = storeSnap.data();
    const subCollectionNames = storeData.categories || [];

    if (subCollectionNames.length === 0) {
      menuCell.innerHTML = "<div>카테고리가 없습니다.</div>";
      return;
    }

    menuCell.innerHTML = `
      <div id='menu-list' style='display: flex; flex-direction: column; align-items: center;'>
        ${subCollectionNames
          .map(
            (name) =>
              `<div class="menu-item" data-category="${name}">🍴 ${name}</div>`
          )
          .join("")}
      </div>`;

    document.querySelectorAll(".menu-item").forEach((item) => {
      item.addEventListener("click", async function () {
        const category = item.dataset.category;
        await loadCategoryFoodList(storeId, category, foodCell);

        const details = document.querySelector("#details");
        if (details) {
          details.innerHTML = "<div>선택된 데이터가 없습니다.</div>";
        }
      });
    });

    foodCell.innerHTML = "<div id='post-list'>카테고리를 클릭하세요</div>";
  } catch (error) {
    console.error("🔥 카테고리 로딩 실패:", error);
    menuCell.innerHTML = "<div>카테고리를 불러올 수 없습니다.</div>";
  }
}

async function loadCategoryFoodList(storeId, category, foodCell) {
  const ths = document.querySelectorAll("thead th");
  if (ths.length >= 3) {
    ths[2].textContent = category;
  }

  foodCell.innerHTML = "<div id='post-list'>불러오는 중...</div>";

  try {
    const foodColRef = collection(db, `store/${storeId}/${category}`);
    const querySnapshot = await getDocs(foodColRef);

    if (querySnapshot.empty) {
      foodCell.innerHTML = "<div id='post-list'>음식 목록이 없습니다.</div>";
      return;
    }

    let html = `<div id='post-list' style='display: flex; flex-direction: column; align-items: center;'>`;
    querySnapshot.forEach((doc) => {
      const data = doc.data();
      const displayId = data.id || doc.id;
      html += `<div class="food-item" onclick="loadFoodDetail('${storeId}', '${category}', '${doc.id}')">${displayId}</div>`;
    });
    html += "</div>";

    foodCell.innerHTML = html;
  } catch (error) {
    console.error(`🔥 ${category} 음식 로딩 실패:`, error);
    foodCell.innerHTML =
      "<div id='post-list'>데이터를 불러올 수 없습니다.</div>";
  }
}

window.loadFoodDetail = async function (storeId, category, foodId) {
  const detailsCell = document.querySelector("#details");
  if (!detailsCell) return;

  detailsCell.innerHTML = "<div>불러오는 중...</div>";

  try {
    const docRef = doc(db, `store/${storeId}/${category}`, foodId);
    const docSnap = await getDoc(docRef);

    if (!docSnap.exists()) {
      detailsCell.innerHTML = "<div>문서를 찾을 수 없습니다.</div>";
      return;
    }

    const data = docSnap.data();
    const displayId = data.id || foodId;

    let html = `<div><b>📌 ${displayId}</b><br><br>`;

    html += `SalesStatus: 
      <select id="status-select">
        <option value="sell" ${
          data.SalesStatus === "sell" ? "selected" : ""
        }>sell</option>
        <option value="soldout" ${
          data.SalesStatus === "soldout" ? "selected" : ""
        }>soldout</option>
      </select><br>`;

    if ("size" in data && typeof data.size === "object") {
      html += `size:<br>`;
      for (const [sizeType, sizeObj] of Object.entries(data.size)) {
        html += `&nbsp;&nbsp;- ${sizeType}:<br>`;
        if (typeof sizeObj === "object") {
          for (const [key, value] of Object.entries(sizeObj)) {
            const inputId = `price-${sizeType}-${key}`;
            html += `&nbsp;&nbsp;&nbsp;&nbsp;${key}: 
              <input type="number" id="${inputId}" value="${value}" 
              style="width: 80px; font-size: 16px; padding: 2px 5px; margin: 2px 0;"><br>`;
          }
        }
      }
    }

    html += `</div>`;
    detailsCell.innerHTML = html;

    selectedDocInfo = { storeId, category, foodId };
  } catch (error) {
    console.error("🔥 음식 상세 정보 로딩 실패:", error);
    detailsCell.innerHTML = "<div>데이터를 불러올 수 없습니다.</div>";
  }
};

function resetData() {
  const postListCell = document.querySelector("#post-list");
  const detailsCell = document.querySelector("#details");

  if (postListCell) {
    postListCell.innerHTML = "<div id='post-list'>식당을 클릭하세요</div>";
  }

  if (detailsCell) {
    detailsCell.innerHTML = "<div id='details'></div>";
  }

  const ths = document.querySelectorAll("thead th");
  if (ths.length >= 3) {
    ths[2].textContent = "음식 목록";
  }

  selectedDocInfo = null;
}

function showSaveButton() {
  const container = document.getElementById("add-ad-btn-container");
  if (!container) return;

  container.innerHTML = "";

  // ✅ 메뉴 추가 버튼 생성
  const addBtn = document.createElement("button");
  addBtn.textContent = "메뉴 추가";
  addBtn.id = "menu-add-button";
  addBtn.style.marginLeft = "10px";
  addBtn.style.backgroundColor = "#4E71FF";
  addBtn.style.color = "white";
  addBtn.style.padding = "6px 14px";
  addBtn.style.fontSize = "15px";
  addBtn.style.border = "none";
  addBtn.style.borderRadius = "5px";
  addBtn.style.cursor = "pointer";

  // ✅ 메뉴 삭제 버튼 생성
  const delBtn = document.createElement("button");
  delBtn.textContent = "메뉴 삭제";
  delBtn.id = "menu-del-button";
  delBtn.style.marginLeft = "10px";
  delBtn.style.backgroundColor = "#C62828";
  delBtn.style.color = "white";
  delBtn.style.padding = "6px 14px";
  delBtn.style.fontSize = "15px";
  delBtn.style.border = "none";
  delBtn.style.borderRadius = "5px";
  delBtn.style.cursor = "pointer";

  // ✅ 저장 버튼 생성
  const saveBtn = document.createElement("button");
  saveBtn.textContent = "저장";
  saveBtn.id = "save-button";
  saveBtn.style.marginLeft = "10px";
  saveBtn.style.backgroundColor = "#0D0D0D";
  saveBtn.style.color = "white";
  saveBtn.style.padding = "6px 14px";
  saveBtn.style.fontSize = "15px";
  saveBtn.style.border = "none";
  saveBtn.style.borderRadius = "5px";
  saveBtn.style.cursor = "pointer";

  // 버튼들을 컨테이너에 추가
  container.appendChild(addBtn);
  container.appendChild(delBtn);
  container.appendChild(saveBtn);

  // ✅ 저장 버튼 기능
  saveBtn.addEventListener("click", async () => {
    if (!selectedDocInfo) {
      alert("선택된 데이터가 없습니다.");
      return;
    }

    const { storeId, category, foodId } = selectedDocInfo;
    const newStatus = document.getElementById("status-select")?.value;

    const sizeInputs = document.querySelectorAll("input[id^='price-']");
    const newSize = {};

    sizeInputs.forEach((input) => {
      const [_, sizeType, key] = input.id.split("-");
      const price = parseFloat(input.value) || 0;

      if (!newSize[sizeType]) newSize[sizeType] = {};
      newSize[sizeType][key] = price;
    });

    try {
      await updateDoc(doc(db, `store/${storeId}/${category}`, foodId), {
        SalesStatus: newStatus,
        size: newSize,
      });
      alert("✅ 저장 완료!");
    } catch (err) {
      console.error("🔥 저장 실패:", err);
      alert("업데이트 실패. 다시 시도해주세요.");
    }
  });

  // ✅ 메뉴 추가 팝업 열기
  addBtn.addEventListener("click", () => {
    window.open("hamburger_add.html", "메뉴 추가", "width=1200,height=800");
  });

  // ✅ 메뉴 삭제 팝업 열기
  delBtn.addEventListener("click", () => {
    window.open("hamburger_del.html", "메뉴 삭제", "width=800,height=600");
  });
}
