package kr.ac.nsu.hakbokgs.main.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.firebase.Timestamp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.R;

public class AdActivity extends AppCompatActivity {

    private static final String TAG = "AdActivity";
    private AdAdapter adapter;
    private List<Advertisement> posts;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);  // ad.xml 레이아웃 파일

        db = FirebaseFirestore.getInstance(); // Firestore 초기화

        // RecyclerView 설정
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 어댑터 설정
        posts = new ArrayList<>();
        adapter = new AdAdapter(posts, this);
        recyclerView.setAdapter(adapter);

        // 뒤로 가기 버튼 설정
        ImageView adBackButton = findViewById(R.id.ad_back);
        adBackButton.setOnClickListener(v -> finish());

        // ✅ 게시판 진입 버튼 (게시글 기능)
        LinearLayout btnChat = findViewById(R.id.main_chat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(AdActivity.this, ChatBoardHomeActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭 시 이동
        LinearLayout btnMypage = findViewById(R.id.main_mypage);
        btnMypage.setOnClickListener(v -> {
            Intent intent = new Intent(AdActivity.this, MypageActivity.class);
            startActivity(intent);
        });

        // 홈 버튼 클릭 시 이동
        LinearLayout btnHome = findViewById(R.id.main_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AdActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 🚀 앱이 실행되면 자동으로 진행중 데이터 로드
        fetchAdvertisements("ing");
    }

    // 버튼 클릭 시 Firestore에서 데이터 가져오기
    public void onButtonClicked(View view) {
        if (view.getId() == R.id.button_ongoing) {
            fetchAdvertisements("ing"); // 진행중 데이터 가져오기
        } else if (view.getId() == R.id.button_completed) {
            fetchAdvertisements("end"); // 종료된 데이터 가져오기
        }
    }

    // Firestore에서 데이터를 가져오는 함수
    private void fetchAdvertisements(String status) {
        posts.clear();
        adapter.updateList(posts);

        CollectionReference postsCollection = db.collection("advertising")
                .document(status)
                .collection("posts");

        postsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String websiteUrl = document.getString("url");
                    Timestamp expiration = document.getTimestamp("expiration");
                    String imageUrl = document.getString("imageUrl");

                    if (title != null && websiteUrl != null && expiration != null && imageUrl != null) {
                        posts.add(new Advertisement(title, websiteUrl, expiration, imageUrl));
                        Log.d(TAG, "Loaded: " + title + " | Expiration: " + expiration);
                    } else {
                        Log.w(TAG, "Document missing fields: " + document.getId());
                    }
                }

                adapter.updateList(posts);
            } else {
                Log.e(TAG, "Firestore Error: ", task.getException());
            }
        });
    }

}
