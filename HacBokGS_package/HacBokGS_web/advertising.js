import { db } from "./firebase.js";
import {
  collection,
  getDocs,
  doc,
  getDoc,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

document.addEventListener("DOMContentLoaded", function () {
  const advertisingIcon = document.getElementById("advertising");
  if (advertisingIcon) {
    advertisingIcon.addEventListener("click", function () {
      const contentContainer = document.querySelector(".content-container");
      if (contentContainer) {
        contentContainer.innerHTML =
          '<iframe src="advertising.html" style="width: 100%; height: 100%; border: none;"></iframe>';
      }
    });
  }

  if (document.querySelector("tbody")) {
    console.log("✅ advertising.html 감지됨 - 광고 데이터 로드 시작");
    loadAdvertisingData();
  }
});

async function loadAdvertisingData() {
  const tableBody = document.querySelector("tbody");
  tableBody.innerHTML = "";

  try {
    const querySnapshot = await getDocs(collection(db, "advertising"));
    let firstRow = true;
    let postListCell, detailsCell;

    querySnapshot.forEach((docSnapshot) => {
      const adId = docSnapshot.id;

      const newRow = document.createElement("tr");

      const statusCell = document.createElement("td");
      statusCell.textContent = adId;
      statusCell.style.cursor = "pointer";
      statusCell.addEventListener("click", function () {
        resetData();
        loadPostsData(adId, postListCell, detailsCell);
        showAddAdButton(adId);
      });
      newRow.appendChild(statusCell);

      if (firstRow) {
        firstRow = false;

        postListCell = document.createElement("td");
        postListCell.innerHTML =
          "<div id='post-list'>진행 상황을 클릭하세요</div>";
        postListCell.setAttribute("rowspan", querySnapshot.size);
        postListCell.style.textAlign = "center";
        postListCell.style.verticalAlign = "middle";
        postListCell.style.whiteSpace = "pre-line";
        newRow.appendChild(postListCell);

        detailsCell = document.createElement("td");
        detailsCell.innerHTML = "<div id='details'></div>";
        detailsCell.setAttribute("rowspan", querySnapshot.size);
        detailsCell.style.textAlign = "left";
        newRow.appendChild(detailsCell);
      }

      tableBody.appendChild(newRow);
    });

    console.log("✅ 광고 데이터 로드 완료");
  } catch (error) {
    console.error("🔥 광고 데이터 로드 오류:", error);
  }
}

async function loadPostsData(adId, targetCell, detailsCell) {
  if (!targetCell || !detailsCell) return;
  targetCell.innerHTML = "<div id='post-list'>불러오는 중...</div>";

  try {
    const postsCollection = collection(db, `advertising/${adId}/posts`);
    const querySnapshot = await getDocs(postsCollection);

    if (querySnapshot.empty) {
      targetCell.innerHTML = "<div id='post-list'>게시된 광고 없음</div>";
      detailsCell.innerHTML = "<div id='details'></div>";
      return;
    }

    let postListHTML =
      "<div id='post-list' style='display: flex; flex-direction: column; align-items: center;'>";

    querySnapshot.forEach((postDoc) => {
      postListHTML += `<div style='border-bottom: 1px solid #ddd; padding: 5px 0; width: 100%; text-align: center; cursor: pointer; color: #007BFF; text-decoration: underline;' onclick="loadPostDetails('${adId}', '${postDoc.id}')">${postDoc.id}</div>`;
    });

    postListHTML += "</div>";
    targetCell.innerHTML = postListHTML;
  } catch (error) {
    console.error(`🔥 ${adId}의 posts 데이터 로드 오류:`, error);
    targetCell.innerHTML =
      "<div id='post-list'>데이터를 불러올 수 없습니다.</div>";
  }
}

window.loadPostDetails = async function (adId, postId) {
  const detailsCell = document.querySelector("#details");
  if (!detailsCell) return;

  detailsCell.innerHTML = "<div>불러오는 중...</div>";

  try {
    const postDocRef = doc(db, `advertising/${adId}/posts`, postId);
    const postDocSnap = await getDoc(postDocRef);

    if (!postDocSnap.exists()) {
      console.error(`❌ ${postId} 문서가 존재하지 않습니다.`);
      detailsCell.innerHTML = "<div>문서를 찾을 수 없습니다.</div>";
      return;
    }

    const postData = postDocSnap.data();
    let detailsText = `<b>📌 ${postId}</b><br>`;

    if (postData.expiration) {
      const expirationDate = postData.expiration.toDate();
      detailsText += `만료일: ${expirationDate.toLocaleString()}<br>`;
    }
    if (postData.title) {
      detailsText += `광고 제목: ${postData.title}<br>`;
    }
    if (postData.url) {
      const fullUrl = postData.url.startsWith("http")
        ? postData.url
        : `https://${postData.url}`;

      detailsText += `URL: <a href="${fullUrl}" target="_blank">${fullUrl}</a><br>`;
    }

    if (postData.imageUrl) {
      detailsText += `이미지: <a href="#" onclick="window.open('${postData.imageUrl}', '미리보기', 'width=600,height=600'); return false;">이미지 보기</a><br>`;
    }

    detailsCell.innerHTML = detailsText;
  } catch (error) {
    console.error(`🔥 ${postId} 데이터 로드 오류:`, error);
    detailsCell.innerHTML = "<div>데이터를 불러올 수 없습니다.</div>";
  }
};

function resetData() {
  const postListCell = document.querySelector("#post-list");
  const detailsCell = document.querySelector("#details");

  if (postListCell) {
    postListCell.innerHTML = "<div id='post-list'>진행 상황을 클릭하세요</div>";
  }
  if (detailsCell) {
    detailsCell.innerHTML = "<div id='details'></div>";
  }
}

function showAddAdButton(adId) {
  const btnContainer = document.getElementById("add-ad-btn-container");
  btnContainer.innerHTML = "";

  const addAdBtn = document.createElement("button");
  addAdBtn.id = adId === "end" ? "advertising_del" : "advertising_add";
  addAdBtn.textContent = adId === "end" ? "광고 삭제" : "광고 추가";
  addAdBtn.style.marginLeft = "20px";
  addAdBtn.style.backgroundColor = adId === "end" ? "red" : "blue";
  addAdBtn.style.color = "white";
  addAdBtn.style.padding = "8px 12px";
  addAdBtn.style.fontSize = "16px";
  addAdBtn.style.border = "none";
  addAdBtn.style.borderRadius = "5px";
  addAdBtn.style.cursor = "pointer";

  addAdBtn.addEventListener("click", function () {
    const width = 800;
    const height = 600;
    const left = window.innerWidth / 2 - width / 2;
    const top = window.innerHeight / 2 - height / 2;

    const popupWindow = window.open(
      adId === "end" ? "advertising_del.html" : "advertising_add.html",
      adId === "end" ? "광고 삭제" : "광고 추가",
      `width=${width},height=${height},top=${top},left=${left}`
    );
  });

  btnContainer.appendChild(addAdBtn);
}
