package kr.ac.nsu.hakbokgs.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;

public class ChatDetailActivity extends AppCompatActivity {

    private TextView titleView, contentView, dateView, voteTitleView, voteDeadlineView;
    private LinearLayout voteArea, voteOptionContainer;
    private EditText commentInput;
    private ImageButton btnSendComment;
    private LinearLayout commentContainer;

    private FirebaseFirestore db;
    private String documentId = "";
    private String category = "chat_qna"; // 기본값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        db = FirebaseFirestore.getInstance();

        titleView = findViewById(R.id.text_title);
        contentView = findViewById(R.id.text_content);
        dateView = findViewById(R.id.text_date);

        voteArea = findViewById(R.id.vote_area);
        voteTitleView = findViewById(R.id.vote_title);
        voteDeadlineView = findViewById(R.id.vote_deadline);
        voteOptionContainer = findViewById(R.id.vote_option_container);

        commentInput = findViewById(R.id.edit_comment);
        btnSendComment = findViewById(R.id.btn_send_comment);

        commentContainer = findViewById(R.id.comment_container);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        documentId = getIntent().getStringExtra("documentId");
        category = getIntent().getStringExtra("category");
        if (category == null) category = "chat_qna";

        if (documentId != null) {
            loadPost();
            loadComments();
        }

        btnSendComment.setOnClickListener(v -> submitComment());

        // 홈 버튼 클릭 시 MainActivity로 이동
        LinearLayout btnHome = findViewById(R.id.main_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // ✅ 게시판 진입 버튼 (게시글 기능)
        LinearLayout btnChat = findViewById(R.id.main_chat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, ChatBoardHomeActivity.class);
            startActivity(intent);
        });

        // 주문내역 버튼 클릭
        LinearLayout btnList = findViewById(R.id.main_list);
        btnList.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        // 마이페이지 버튼 클릭 → MypageActivity로 이동
        LinearLayout btnMypage = findViewById(R.id.main_mypage);
        btnMypage.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MypageActivity.class);
            startActivity(intent);
        });



    }

    private void loadComments() {
        commentContainer.removeAllViews();
        db.collection("bulletin_board")
                .document(category)
                .collection("board").document(documentId)
                .collection("comments")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null || querySnapshot == null) return;
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String text = doc.getString("text");
                        Timestamp ts = doc.getTimestamp("timestamp");
                        String time = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(ts != null ? ts.toDate() : new Date());

                        TextView commentView = new TextView(this);
                        commentView.setText(text + "  (" + time + ")");
                        commentView.setPadding(12, 8, 12, 8);
                        commentView.setTextSize(14f);

                        commentContainer.addView(commentView);
                    }
                });
    }

    private void loadPost() {
        db.collection("bulletin_board").document(category)
                .collection("board").document(documentId)
                .addSnapshotListener((snap, error) -> {
                    if (snap == null || !snap.exists()) return;

                    titleView.setText(snap.getString("title"));
                    contentView.setText(snap.getString("content"));

                    Timestamp ts = snap.getTimestamp("registration");
                    if (ts != null) {
                        String formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(ts.toDate());
                        dateView.setText(formatted);
                    }

                    boolean isVote = Boolean.TRUE.equals(snap.getBoolean("isVote"));
                    if (isVote) {
                        voteArea.setVisibility(View.VISIBLE);
                        voteTitleView.setText(snap.getString("voteTitle"));

                        Timestamp deadline = snap.getTimestamp("voteDeadline");
                        if (deadline != null) {
                            voteDeadlineView.setText("마감일: " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(deadline.toDate()));
                        }

                        List<Map<String, Object>> options = (List<Map<String, Object>>) snap.get("voteOptions");
                        Map<String, Object> voteUserMap = (Map<String, Object>) snap.get("voteUserMap");
                        String myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        Number myIndex = voteUserMap != null && myEmail != null ? (Number) voteUserMap.get(myEmail) : null;

                        voteOptionContainer.removeAllViews();
                        long totalVotes = 0;
                        for (Map<String, Object> opt : options) {
                            Object countObj = opt.get("count");
                            if (countObj instanceof Number) totalVotes += ((Number) countObj).longValue();
                        }

                        for (int i = 0; i < options.size(); i++) {
                            final int index = i;
                            Map<String, Object> opt = options.get(i);
                            String optText = String.valueOf(opt.get("option"));
                            long count = ((Number) opt.get("count")).longValue();

                            View item = LayoutInflater.from(this).inflate(R.layout.item_vote_result, voteOptionContainer, false);
                            TextView txt = item.findViewById(R.id.vote_option_text);
                            TextView percent = item.findViewById(R.id.vote_percent_text);
                            ProgressBar bar = item.findViewById(R.id.vote_progress_bar);
                            ImageView check = item.findViewById(R.id.vote_selected_icon);

                            txt.setText(optText);
                            int pct = totalVotes > 0 ? (int)((count * 100.0f) / totalVotes) : 0;
                            percent.setText(pct + "% (" + count + "명)");
                            bar.setProgress(pct);

                            if (myIndex != null && myIndex.intValue() == i) {
                                check.setVisibility(View.VISIBLE);
                                txt.setTextColor(getResources().getColor(R.color.teal_700));
                            } else {
                                check.setVisibility(View.GONE);
                            }

                            item.setOnClickListener(v -> castVote(index));
                            voteOptionContainer.addView(item);
                        }
                    } else {
                        voteArea.setVisibility(View.GONE);
                    }
                });
    }

    private void castVote(int index) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = db.collection("bulletin_board").document(category)
                .collection("board").document(documentId);

        db.runTransaction(tx -> {
            DocumentSnapshot snap = tx.get(docRef);
            List<Map<String, Object>> options = (List<Map<String, Object>>) snap.get("voteOptions");
            Map<String, Object> voteUserMap = (Map<String, Object>) snap.get("voteUserMap");
            if (voteUserMap == null) voteUserMap = new HashMap<>();

            if (!voteUserMap.containsKey(email)) {
                voteUserMap.put(email, index);
                long count = ((Number) options.get(index).get("count")).longValue();
                options.get(index).put("count", count + 1);
                tx.update(docRef, "voteOptions", options);
                tx.update(docRef, "voteUserMap", voteUserMap);
            }
            return null;
        });
    }

    private void submitComment() {
        String text = commentInput.getText().toString().trim();
        if (text.isEmpty()) return;

        Map<String, Object> comment = new HashMap<>();
        comment.put("text", text);
        comment.put("timestamp", Timestamp.now());

        db.collection("bulletin_board").document(category)
                .collection("board").document(documentId)
                .collection("comments")
                .add(comment)
                .addOnSuccessListener(doc -> commentInput.setText(""));
    }
}
