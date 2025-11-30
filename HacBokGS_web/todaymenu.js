import { db } from "./firebase.js";
import {
  collection,
  getDocs,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

document.addEventListener("DOMContentLoaded", async function () {
  const tableBody = document.querySelector("#menu-body");
  tableBody.innerHTML = "";

  try {
    const querySnapshot = await getDocs(collection(db, "today's_menu"));
    const docs = querySnapshot.docs;

    if (docs.length === 0) {
      tableBody.innerHTML = `<tr><td colspan="2">📭 메뉴가 없습니다</td></tr>`;
      return;
    }

    // 첫 줄: 메뉴 + 데이터 셀(rowspan)
    const firstDoc = docs[0];
    const firstRow = document.createElement("tr");

    const menuCell = document.createElement("td");
    menuCell.textContent = firstDoc.data().name || firstDoc.id;
    menuCell.classList.add("menu");
    menuCell.addEventListener("click", () => showSelectedData(firstDoc.data()));

    const selectedDataCell = document.createElement("td");
    selectedDataCell.setAttribute("id", "selected-data-cell");
    selectedDataCell.setAttribute("rowspan", docs.length);
    selectedDataCell.textContent = "선택된 데이터를 여기에 표시합니다.";

    firstRow.appendChild(menuCell);
    firstRow.appendChild(selectedDataCell);
    tableBody.appendChild(firstRow);

    // 나머지 줄
    for (let i = 1; i < docs.length; i++) {
      const docSnap = docs[i];
      const row = document.createElement("tr");
      const cell = document.createElement("td");
      cell.textContent = docSnap.data().name || docSnap.id;
      cell.classList.add("menu");
      cell.addEventListener("click", () => showSelectedData(docSnap.data()));
      row.appendChild(cell);
      tableBody.appendChild(row);
    }
  } catch (err) {
    console.error("🔥 Firestore 데이터 로드 실패:", err);
  }
});

function showSelectedData(data) {
    const selectedCell = document.querySelector("#selected-data-cell");
  
    // 출력 우선순위 지정
    const fieldOrder = ["Mon", "Tue", "Wed", "Thu", "Fri"];
    let output = "";
  
    // 우선순위에 맞는 필드 먼저 출력
    fieldOrder.forEach((key) => {
      if (data.hasOwnProperty(key)) {
        output += `🔹 ${key}: ${data[key]}\n`;
      }
    });
  
    // 나머지 필드는 따로 출력
    Object.entries(data).forEach(([key, value]) => {
      if (!fieldOrder.includes(key)) {
        output += `🔸 ${key}: ${value}\n`;
      }
    });
  
    selectedCell.textContent = output.trim();
  }
  
