package kr.ac.nsu.hakbokgs.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;

public class UserInformation extends AppCompatActivity {

    private static final String TAG = "UserInformation";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText etName, etBirthdate, etPhoneNumber;
    private Button btnSave;
    private TextView tvUserEmail, tvUserName;
    private CheckBox cbAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user); // user.xml 연결

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI 요소 초기화
        etName = findViewById(R.id.et_name);
        etBirthdate = findViewById(R.id.et_birthdate);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnSave = findViewById(R.id.btn_save);
        tvUserEmail = findViewById(R.id.tv_user_email);
        cbAgree = findViewById(R.id.cb_agree);

        // 로그인한 사용자의 정보 가져오기
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvUserEmail.setText(user.getEmail());
            loadUserName(user.getEmail()); // Firestore에서 이름 가져오기
        }

        // 저장 버튼 클릭 시
        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserName(String userEmail) {
        db.collection("users").document(userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        String name = document.getString("name");
                        if (name != null && !name.isEmpty()) {
                            tvUserName.setText(name); // TextView에 사용자 이름 설정
                        }
                    } else {
                        Log.d(TAG, "사용자 이름을 불러오지 못함");
                    }
                });
    }

    private void saveUserData() {
        String name = etName.getText().toString().trim();
        String birthdate = etBirthdate.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (name.isEmpty() || birthdate.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (birthdate.length() != 8 || !birthdate.matches("\\d{8}")) {
            Toast.makeText(this, "생년월일은 8자리 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
            Toast.makeText(this, "전화번호는 11자리 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbAgree.isChecked()) {
            Toast.makeText(this, "개인정보 수집 및 이용에 동의해야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            saveDataToFirestore(user.getEmail(), name, birthdate, phoneNumber);
        }
    }

    private void saveDataToFirestore(String userEmail, String name, String birthdate, String phoneNumber) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("birthdate", birthdate);
        userData.put("phoneNumber", phoneNumber);

        db.collection("users").document(userEmail)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    tvUserName.setText(name); // 저장 후 즉시 UI 업데이트
                    Intent intent = new Intent(UserInformation.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "정보 저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
