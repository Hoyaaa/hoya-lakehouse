package kr.ac.nsu.hakbokgs.main.mypage;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;

public class MypageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    private TextView tvUserName;
    private ImageView imgProfile;
    private Uri imageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        tvUserName = findViewById(R.id.tv_user_name);
        imgProfile = findViewById(R.id.img_profile);

        loadUserData();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imgProfile.setImageURI(imageUri);
                        uploadImageToFirebase();
                    }
                });

        // 기능 카드 클릭 이벤트
        findViewById(R.id.layout_change_photo).setOnClickListener(v -> openImageChooser());
        imgProfile.setOnClickListener(v -> openImageChooser());
        findViewById(R.id.layout_change_name).setOnClickListener(v -> showNameChangeDialog());
        findViewById(R.id.layout_account_check).setOnClickListener(v -> showAccountInfoDialog());

        // 외부 링크 연결
        findViewById(R.id.btn_instagram).setOnClickListener(v ->
                openWebLink("https://www.instagram.com/namseoul_univ/?hl=ko"));

        findViewById(R.id.btn_school_homepage).setOnClickListener(v ->
                openWebLink("https://nsu.ac.kr/"));

        // 하단 네비게이션 및 뒤로가기
        findViewById(R.id.mypage_back).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        findViewById(R.id.main_home).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        // ✅ 게시판 진입 버튼 (게시글 기능)
        LinearLayout btnChat = findViewById(R.id.main_chat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, ChatBoardHomeActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.main_list).setOnClickListener(v ->
                startActivity(new Intent(this, OrderHistoryActivity.class)));
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void uploadImageToFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && imageUri != null) {
            String userEmail = user.getEmail();
            StorageReference fileRef = storageRef.child(userEmail + ".jpg");

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                saveProfileUrlToFirestore(userEmail, downloadUrl);
                            }))
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "사진 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void saveProfileUrlToFirestore(String email, String url) {
        db.collection("users").document(email)
                .update("profileUrl", url)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "프로필 사진이 저장되었습니다.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Firestore 저장 실패", Toast.LENGTH_SHORT).show());
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();

            db.collection("users").document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String profileUrl = documentSnapshot.getString("profileUrl");

                            if (name != null && !name.isEmpty()) {
                                tvUserName.setText(name);
                            } else {
                                tvUserName.setText("이름 없음");
                            }

                            if (profileUrl != null && !profileUrl.isEmpty()) {
                                Glide.with(this)
                                        .load(profileUrl)
                                        .placeholder(R.drawable.ico_user)
                                        .circleCrop()  //
                                        .into(imgProfile);
                            }

                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "사용자 정보 로드 실패", Toast.LENGTH_SHORT).show());
        }
    }

    private void showNameChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이름 변경");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
        final EditText input = viewInflated.findViewById(R.id.et_new_name);
        builder.setView(viewInflated);

        builder.setPositiveButton("저장", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                updateUserName(newName);
            } else {
                Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateUserName(String newName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();

            Map<String, Object> updates = new HashMap<>();
            updates.put("name", newName);

            db.collection("users").document(email)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        tvUserName.setText(newName);
                        Toast.makeText(this, "이름이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "이름 변경 실패", Toast.LENGTH_SHORT).show());
        }
    }

    private void showAccountInfoDialog() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            new AlertDialog.Builder(this)
                    .setTitle("연동된 계정 정보")
                    .setMessage("현재 로그인된 계정:\n\n" + email)
                    .setPositiveButton("확인", null)
                    .show();
        } else {
            Toast.makeText(this, "로그인된 계정이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}
