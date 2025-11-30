package kr.ac.nsu.hakbokgs.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;

public class ChatListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout listContainer;
    private String category = "chat_qna";
    private TextView listTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        db = FirebaseFirestore.getInstance();
        listContainer = findViewById(R.id.list_container);
        listTitle = findViewById(R.id.list_title);

        category = getIntent().getStringExtra("category");
        if (category == null) category = "chat_qna";

        // 상단 제목 텍스트 설정
        if ("chat_lunch".equals(category)) {
            listTitle.setText("🍱 점메추 게시판");
        } else {
            listTitle.setText("📢 건의사항 게시판");
        }

        // 뒤로가기 버튼
        findViewById(R.id.map_back).setOnClickListener(v -> finish());

        // 글쓰기 버튼
        Button btnWrite = findViewById(R.id.btn_write);
        btnWrite.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatWriteActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });

        loadPostList();


        // 홈 버튼 클릭 시 MainActivity로 이동
        LinearLayout btnHome = findViewById(R.id.main_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // ✅ 게시판 진입 버튼 (게시글 기능)
        LinearLayout btnChat = findViewById(R.id.main_chat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, ChatBoardHomeActivity.class);
            startActivity(intent);
        });

        // 주문내역 버튼 클릭
        LinearLayout btnList = findViewById(R.id.main_list);
        btnList.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        // 마이페이지 버튼 클릭 → MypageActivity로 이동
        LinearLayout btnMypage = findViewById(R.id.main_mypage);
        btnMypage.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, MypageActivity.class);
            startActivity(intent);
        });
    }

    private void loadPostList() {
        listContainer.removeAllViews();

        db.collection("bulletin_board")
                .document(category)
                .collection("board")
                .orderBy("registration", Query.Direction.DESCENDING)
                .addSnapshotListener((query, e) -> {
                    if (e != null || query == null) return;

                    listContainer.removeAllViews();

                    for (QueryDocumentSnapshot doc : query) {
                        String title = doc.getString("title");
                        String documentId = doc.getId();
                        Date timestamp = doc.getDate("registration");

                        View item = getLayoutInflater().inflate(R.layout.item_chat, listContainer, false);
                        ((TextView) item.findViewById(R.id.item_title)).setText(title);
                        ((TextView) item.findViewById(R.id.item_date)).setText(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        .format(timestamp != null ? timestamp : new Date())
                        );

                        item.setOnClickListener(v -> {
                            Intent intent = new Intent(this, ChatDetailActivity.class);
                            intent.putExtra("documentId", documentId);
                            intent.putExtra("category", category);
                            startActivity(intent);
                        });

                        listContainer.addView(item);
                    }
                });
    }
}