package kr.ac.nsu.hakbokgs.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;

public class ChatBoardHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_board_home);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        Button btnQna = findViewById(R.id.btn_chat_qna);
        Button btnLunch = findViewById(R.id.btn_chat_lunch);

        btnQna.setOnClickListener(v -> moveToBoard("chat_qna"));
        btnLunch.setOnClickListener(v -> moveToBoard("chat_lunch"));

        // 홈 버튼 클릭 시 MainActivity로 이동
        LinearLayout btnHome = findViewById(R.id.main_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChatBoardHomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 주문내역 버튼 클릭
        LinearLayout btnList = findViewById(R.id.main_list);
        btnList.setOnClickListener(v -> {
            Intent intent = new Intent(ChatBoardHomeActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        // 마이페이지 버튼 클릭 → MypageActivity로 이동
        LinearLayout btnMypage = findViewById(R.id.main_mypage);
        btnMypage.setOnClickListener(v -> {
            Intent intent = new Intent(ChatBoardHomeActivity.this, MypageActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void moveToBoard(String category) {
        Intent intent = new Intent(this, ChatListActivity.class); // 게시판 목록 Activity
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
