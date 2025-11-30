package kr.ac.nsu.hakbokgs.main.chat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;

public class ChatWriteActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText editTitle, editContent, editVoteTitle;
    private CheckBox checkboxVoteEnable;
    private LinearLayout voteOptionContainer, voteDeadlineContainer;
    private Button btnAddOption, btnSetDeadline, btnSubmit;
    private TextView voteDeadlineText;
    private Date voteDeadline;
    private String category = "chat_qna";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_write);

        db = FirebaseFirestore.getInstance();
        category = getIntent().getStringExtra("category");
        if (category == null) category = "chat_qna";

        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        editVoteTitle = findViewById(R.id.edit_vote_title);
        checkboxVoteEnable = findViewById(R.id.checkbox_vote_enable);
        voteOptionContainer = findViewById(R.id.vote_option_container);
        voteDeadlineContainer = findViewById(R.id.vote_deadline_container);
        voteDeadlineText = findViewById(R.id.text_vote_deadline);
        btnAddOption = findViewById(R.id.btn_add_option);
        btnSetDeadline = findViewById(R.id.btn_set_deadline);
        btnSubmit = findViewById(R.id.btn_submit);

        checkboxVoteEnable.setOnCheckedChangeListener((btn, isChecked) -> {
            int v = isChecked ? View.VISIBLE : View.GONE;
            editVoteTitle.setVisibility(v);
            voteOptionContainer.setVisibility(v);
            voteDeadlineContainer.setVisibility(v);
            btnAddOption.setVisibility(v);
        });

        btnAddOption.setOnClickListener(v -> {
            EditText optionInput = new EditText(this);
            optionInput.setHint("옵션 입력");
            optionInput.setBackgroundColor(getResources().getColor(android.R.color.white));
            optionInput.setPadding(16, 8, 16, 8);
            voteOptionContainer.addView(optionInput);
        });

        btnSetDeadline.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                cal.set(year, month, day);
                voteDeadline = cal.getTime();
                voteDeadlineText.setText("기한: " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(voteDeadline));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSubmit.setOnClickListener(v -> submitPost());

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());


        // 홈 버튼 클릭 시 MainActivity로 이동
        LinearLayout btnHome = findViewById(R.id.main_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChatWriteActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 게시판 진입 버튼 (게시글 기능)
        LinearLayout btnChat = findViewById(R.id.main_chat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(ChatWriteActivity.this, ChatBoardHomeActivity.class);
            startActivity(intent);
        });

        // 주문내역 버튼 클릭
        LinearLayout btnList = findViewById(R.id.main_list);
        btnList.setOnClickListener(v -> {
            Intent intent = new Intent(ChatWriteActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        // 마이페이지 버튼 클릭 → MypageActivity로 이동
        LinearLayout btnMypage = findViewById(R.id.main_mypage);
        btnMypage.setOnClickListener(v -> {
            Intent intent = new Intent(ChatWriteActivity.this, MypageActivity.class);
            startActivity(intent);
        });
    }

    private void submitPost() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Date createdAt = new Date();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. 마지막 board_n 조회
        db.collection("bulletin_board")
                .document(category)
                .collection("board")
                .orderBy("board_n", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    long nextBoardNumber = 1;
                    if (!querySnapshot.isEmpty()) {
                        Long last = querySnapshot.getDocuments().get(0).getLong("board_n");
                        if (last != null) nextBoardNumber = last + 1;
                    }

                    // 2. 데이터 구성
                    Map<String, Object> data = new HashMap<>();
                    data.put("board_n", nextBoardNumber);
                    data.put("title", title);
                    data.put("content", content);
                    data.put("user", user);
                    data.put("registration", createdAt);

                    if (checkboxVoteEnable.isChecked()) {
                        String voteTitle = editVoteTitle.getText().toString().trim();
                        if (voteTitle.isEmpty() || voteDeadline == null) {
                            Toast.makeText(this, "투표 제목과 기한을 설정하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Map<String, Object>> voteOptions = new ArrayList<>();
                        for (int i = 0; i < voteOptionContainer.getChildCount(); i++) {
                            View child = voteOptionContainer.getChildAt(i);
                            if (child instanceof EditText) {
                                String option = ((EditText) child).getText().toString().trim();
                                if (!option.isEmpty()) {
                                    Map<String, Object> opt = new HashMap<>();
                                    opt.put("option", option);
                                    opt.put("count", 0L);
                                    voteOptions.add(opt);
                                }
                            }
                        }

                        if (voteOptions.size() < 2) {
                            Toast.makeText(this, "최소 2개 이상의 옵션을 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        data.put("isVote", true);
                        data.put("voteTitle", voteTitle);
                        data.put("voteDeadline", voteDeadline);
                        data.put("voteOptions", voteOptions);
                        data.put("voteUserMap", new HashMap<>());
                    } else {
                        data.put("isVote", false);
                    }

                    // 3. 지정된 ID로 저장 (ex: board_3)
                    String docId = "board_" + nextBoardNumber;
                    db.collection("bulletin_board")
                            .document(category)
                            .collection("board")
                            .document(docId)
                            .set(data)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "게시글 등록 완료", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "등록 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "게시글 번호 조회 실패", Toast.LENGTH_SHORT).show();
                });
    }

}
