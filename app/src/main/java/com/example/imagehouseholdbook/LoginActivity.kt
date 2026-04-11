package com.example.imagehouseholdbook

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // 권한 요청 코드 (임의의 숫자)
    private val RC_SHEET_PERMISSION = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // login.xml 연결

        // [추가] 화면이 켜지자마자 엑셀(구글 시트) 권한이 있는지 확인하고, 없으면 요청합니다.
        checkGoogleSheetPermissions()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // 1. 레이아웃 뷰 연결
        val etId = findViewById<EditText>(R.id.et_id)
        val etName = findViewById<EditText>(R.id.et_name)
        val btnRegister = findViewById<AppCompatButton>(R.id.btn_register_start)

        // 2. 구글 로그인 정보 가져와서 ID(이메일) 칸에 채우기
        val currentUser = auth.currentUser
        val userEmail = currentUser?.email ?: ""

        etId.setText(userEmail)
        etId.isEnabled = false // ID는 수정 불가능하게 설정 (사용자가 변경 못함)

        // 3. 시작하기 버튼 클릭 리스너
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()

            // 이름 입력 확인
            if (name.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase에 저장 및 화면 이동
            saveUserDataAndMoveMain(userEmail, name)
        }
    }

    // [추가] 구글 시트 권한 확인 및 요청 함수
    private fun checkGoogleSheetPermissions() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        // 구글 시트 권한 범위 URL
        val sheetScope = Scope("https://www.googleapis.com/auth/spreadsheets")

        if (account != null && !GoogleSignIn.hasPermissions(account, sheetScope)) {
            // 권한이 없으면 요청 창 띄우기
            GoogleSignIn.requestPermissions(
                this,
                RC_SHEET_PERMISSION,
                account,
                sheetScope
            )
        }
    }

    private fun saveUserDataAndMoveMain(email: String, name: String) {
        // 한국 시간 기준 날짜 생성
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        val currentDate = sdf.format(Date())

        // 저장할 데이터 객체 (HashMap)
        val userMap = hashMapOf(
            "id" to email,
            "name" to name,
            "date" to currentDate
        )

        // Firestore 저장 경로: user 컬렉션 -> [이메일] 문서
        db.collection("user").document(email)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "등록이 완료되었습니다!", Toast.LENGTH_SHORT).show()

                // [수정됨] 저장이 성공하면 MainActivity로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // 뒤로가기 눌렀을 때 다시 로그인 화면으로 오지 않도록 종료
            }
            .addOnFailureListener { e ->
                // 저장 실패 시 에러 메시지 출력
                Toast.makeText(this, "저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}