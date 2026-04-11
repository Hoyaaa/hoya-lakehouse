package kr.ac.nsu.hakbokgs.main.store.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.order.data.OrderViewModel;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String tag = "OrderHistoryActivity";

    private OrderViewModel orderViewModel;
    private RecyclerView mainRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // ✅ 로그인한 사용자 이메일 기반 ID 사용
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : null;
        if (userId == null) {
            return;
        }

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class); // ViewModel 생성

        // 외부 recyclerView 연결
        mainRecyclerView = findViewById(R.id.rv_each_date);
        mainRecyclerView.setHasFixedSize(true);

        // recyclerView 형태
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mainRecyclerView.addItemDecoration(decoration);

        orderHistoryAdapter = new OrderHistoryAdapter(userId, this, orderViewModel, this);
        mainRecyclerView.setAdapter(orderHistoryAdapter);


        orderViewModel.listenAllOrderData(userId); // 주문 내역 데이터 가져오기
        Log.d(tag, "가져온 데이터(액티비티):" + orderViewModel.getOrderDataMap().getValue());


        // 뒤로 가기 버튼 설정
        ImageView adBackButton = findViewById(R.id.btn_back);
        adBackButton.setOnClickListener(v -> finish());
        
        
        // 홈화면
        LinearLayout btnHome = findViewById(R.id.main_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // ✅ 게시판 진입 버튼 (게시글 기능)
        LinearLayout btnChat = findViewById(R.id.main_chat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, ChatBoardHomeActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭 → MypageActivity로 이동
        LinearLayout btnMypage = findViewById(R.id.main_mypage);
        btnMypage.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, MypageActivity.class);
            startActivity(intent);
        });
        

    }
}
