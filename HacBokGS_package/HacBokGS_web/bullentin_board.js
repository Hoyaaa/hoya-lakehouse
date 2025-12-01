import { db } from "./firebase.js";
import {
  collection,
  getDocs,
  doc,
  getDoc,
  Timestamp,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

document.addEventListener("DOMContentLoaded", async function () {
  console.log("✅ DOM 로드 완료 - Firebase에서 게시판 데이터 가져오는 중...");

  if (document.querySelector("tbody")) {
    const tableBody = document.querySelector("tbody");
    tableBody.innerHTML = "";

    try {
      const querySnapshot = await getDocs(collection(db, "bulletin_board"));

      let firstRow = true;
      let boardListCell, detailsCell, commentsCell, commentInfoCell;

      querySnapshot.forEach((docSnapshot) => {
        const boardId = docSnapshot.id;

        const newRow = document.createElement("tr");

        const typeCell = document.createElement("td");
        typeCell.textContent = boardId;
        typeCell.style.cursor = "pointer";
        typeCell.addEventListener("click", function () {
          resetData();
          loadBoardData(boardId, boardListCell, detailsCell);
        });
        newRow.appendChild(typeCell);

        if (firstRow) {
          firstRow = false;

          boardListCell = document.createElement("td");
          boardListCell.innerHTML =
            "<div id='board-list'>게시판을 클릭하세요</div>";
          boardListCell.setAttribute("rowspan", querySnapshot.size);
          boardListCell.style.textAlign = "center";
          boardListCell.style.verticalAlign = "middle";
          newRow.appendChild(boardListCell);

          detailsCell = document.createElement("td");
          detailsCell.innerHTML = "<div id='details'></div>";
          detailsCell.setAttribute("rowspan", querySnapshot.size);
          detailsCell.style.textAlign = "left";
          newRow.appendChild(detailsCell);

          commentsCell = document.createElement("td");
          commentsCell.innerHTML = "<div id='comments'></div>";
          commentsCell.setAttribute("rowspan", querySnapshot.size);
          newRow.appendChild(commentsCell);

          commentInfoCell = document.createElement("td");
          commentInfoCell.innerHTML = "<div id='comment-info'></div>";
          commentInfoCell.setAttribute("rowspan", querySnapshot.size);
          newRow.appendChild(commentInfoCell);
        }

        tableBody.appendChild(newRow);
      });

      console.log("✅ Firestore에서 게시판 데이터를 성공적으로 가져왔습니다.");
    } catch (error) {
      console.error("🔥 Firestore 데이터 로드 중 오류 발생:", error);
    }
  }
});

async function loadBoardData(boardId, targetCell, detailsCell) {
  if (!targetCell || !detailsCell) {
    console.error("❌ targetCell 또는 detailsCell이 존재하지 않습니다.");
    return;
  }

  targetCell.innerHTML = "<div id='board-list'>불러오는 중...</div>";

  try {
    const boardCollection = collection(db, `bulletin_board/${boardId}/board`);
    const querySnapshot = await getDocs(boardCollection);

    if (querySnapshot.empty) {
      targetCell.innerHTML = "<div id='board-list'>게시된 게시판 없음</div>";
      detailsCell.innerHTML = "<div id='details'></div>";
      return;
    }

    let boardListHTML =
      "<div id='board-list' style='display: flex; flex-direction: column; align-items: center;'>";

    querySnapshot.forEach((boardDoc) => {
      boardListHTML += `<div style='border-bottom: 1px solid #ddd; padding: 5px 0; width: 100%; text-align: center; cursor: pointer; color: #007BFF; text-decoration: underline;' onclick="loadBoardDetails('${boardId}', '${boardDoc.id}')">${boardDoc.id}</div>`;
    });

    boardListHTML += "</div>";
    targetCell.innerHTML = boardListHTML;
  } catch (error) {
    console.error(`🔥 ${boardId}의 board 데이터 로드 중 오류 발생:`, error);
    targetCell.innerHTML =
      "<div id='board-list'>데이터를 불러올 수 없습니다.</div>";
  }
}

window.loadBoardDetails = async function (boardId, boardDocId) {
  const detailsCell = document.querySelector("#details");
  const commentsCell = document.querySelector("#comments");
  const commentInfoCell = document.querySelector("#comment-info");

  if (!detailsCell || !commentsCell || !commentInfoCell) return;

  detailsCell.innerHTML = "<div>불러오는 중...</div>";
  commentsCell.innerHTML = "";
  commentInfoCell.innerHTML = "";

  try {
    const boardDocRef = doc(db, `bulletin_board/${boardId}/board`, boardDocId);
    const boardDocSnap = await getDoc(boardDocRef);

    if (!boardDocSnap.exists()) {
      console.error(`❌ ${boardDocId} 문서가 존재하지 않습니다.`);
      detailsCell.innerHTML = "<div>문서를 찾을 수 없습니다.</div>";
      return;
    }

    const boardData = boardDocSnap.data();
    let detailsText = `<b>📌 ${boardDocId}</b><br>`;

    Object.entries(boardData).forEach(([key, value]) => {
      const v =
        value instanceof Timestamp ? value.toDate().toLocaleString() : value;
      detailsText += `<b>${key}:</b> ${v}<br>`;
    });

    detailsCell.innerHTML = detailsText;

    // 🔸 댓글 목록 불러오기
    commentsCell.innerHTML = "<div>댓글 불러오는 중...</div>";
    try {
      const commentsCol = collection(
        db,
        `bulletin_board/${boardId}/board/${boardDocId}/comments`
      );
      const commentsSnap = await getDocs(commentsCol);

      if (commentsSnap.empty) {
        commentsCell.innerHTML = "<div>댓글이 없습니다.</div>";
      } else {
        let commentHTML = "<ul style='list-style: none; padding-left: 0;'>";
        commentsSnap.forEach((commentDoc) => {
          commentHTML += `
            <li style='padding: 5px 0; border-bottom: 1px solid #ccc; cursor: pointer; color: #007BFF; text-decoration: underline;'
                onclick="loadCommentDetails('${boardId}', '${boardDocId}', '${commentDoc.id}')">
              ${commentDoc.id}
            </li>`;
        });
        commentHTML += "</ul>";
        commentsCell.innerHTML = commentHTML;
      }
    } catch (error) {
      console.error("🔥 댓글 목록 로드 실패:", error);
      commentsCell.innerHTML = "<div>댓글을 불러올 수 없습니다.</div>";
    }
  } catch (error) {
    console.error(`🔥 ${boardDocId} 데이터 로드 중 오류 발생:`, error);
    detailsCell.innerHTML = "<div>데이터를 불러올 수 없습니다.</div>";
  }
};

window.loadCommentDetails = async function (boardId, boardDocId, commentId) {
  const commentInfoCell = document.querySelector("#comment-info");
  if (!commentInfoCell) return;

  commentInfoCell.innerHTML = "<div>댓글 정보 불러오는 중...</div>";
  try {
    const commentRef = doc(db, `bulletin_board/${boardId}/board/${boardDocId}/comments`, commentId);
    const commentSnap = await getDoc(commentRef);

    if (!commentSnap.exists()) {
      commentInfoCell.innerHTML = "<div>댓글을 찾을 수 없습니다.</div>";
      return;
    }

    const data = commentSnap.data();
    let text = `<b>📝 ${commentId}</b><br>`;
    for (const [key, value] of Object.entries(data)) {
      const v = value instanceof Timestamp ? value.toDate().toLocaleString() : value;
      text += `<b>${key}:</b> ${v}<br>`;
    }

    // 왼쪽 정렬된 div로 출력
    commentInfoCell.innerHTML = `<div style="text-align: left;">${text}</div>`;
  } catch (err) {
    console.error("🔥 댓글 정보 로드 실패:", err);
    commentInfoCell.innerHTML = "<div>댓글 정보를 불러올 수 없습니다.</div>";
  }
};


function resetData() {
  const boardListCell = document.querySelector("#board-list");
  const detailsCell = document.querySelector("#details");
  const commentsCell = document.querySelector("#comments");
  const commentInfoCell = document.querySelector("#comment-info");

  if (boardListCell) {
    boardListCell.innerHTML = "<div id='board-list'>게시판을 클릭하세요</div>";
  }
  if (detailsCell) {
    detailsCell.innerHTML = "<div id='details'></div>";
  }
  if (commentsCell) {
    commentsCell.innerHTML = "<div id='comments'></div>";
  }
  if (commentInfoCell) {
    commentInfoCell.innerHTML = "<div id='comment-info'></div>";
  }
}
