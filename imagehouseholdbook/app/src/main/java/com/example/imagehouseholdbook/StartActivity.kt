package com.example.imagehouseholdbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class StartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val TAG = "StartActivity"

    // 구글 로그인 결과 처리 런처
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "googleSignInLauncher resultCode=${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "Google sign in success. id=${account.id}, email=${account.email}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
                Toast.makeText(this, "구글 로그인 실패: ${e.statusCode} / ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.w(TAG, "Google sign in canceled or failed. resultCode=${result.resultCode}")
            Toast.makeText(this, "구글 로그인 취소 또는 실패 (code=$result.resultCode)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)

        auth = FirebaseAuth.getInstance()

        Log.d(TAG, "onCreate: setting up GoogleSignInOptions")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        Log.d(TAG, "onCreate: googleSignInClient created")

        val btnGoogleLogin = findViewById<SignInButton>(R.id.btn_google_login)
        btnGoogleLogin.setSize(SignInButton.SIZE_WIDE)

        // [수정됨] 버튼 클릭 시 권한을 먼저 초기화하고 로그인을 진행합니다.
        btnGoogleLogin.setOnClickListener {
            Log.d(TAG, "Google login button clicked - Revoking access first for testing")

            // 1. 기존 권한 강제 철회 (초기화)
            googleSignInClient.revokeAccess().addOnCompleteListener(this) {
                // 2. 초기화가 끝나면(성공하든 실패하든) 로그인 창 띄우기
                Toast.makeText(this, "권한 초기화됨. 다시 로그인하세요.", Toast.LENGTH_SHORT).show()
                signIn()
            }
        }
    }

    // 로그인 창 띄우기
    private fun signIn() {
        try {
            // 초기화 후 실행되므로, 여기서 계정 선택 및 권한 동의 창이 다시 뜹니다.
            val signInIntent = googleSignInClient.signInIntent
            Log.d(TAG, "Launching Google sign-in intent")
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error launching Google sign-in", e)
            Toast.makeText(this, "구글 로그인 시작 오류: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Firebase 인증 처리
    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "firebaseAuthWithGoogle called. idToken length=${idToken.length}")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val isNewUser = task.result.additionalUserInfo?.isNewUser ?: false
                    Log.d(
                        TAG,
                        "signInWithCredential:success, uid=${user?.uid}, email=${user?.email}, isNewUser=$isNewUser"
                    )

                    if (isNewUser) {
                        moveToLogin()
                    } else {
                        moveToMain()
                    }
                } else {
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this,
                        "파이어베이스 인증 실패: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun moveToLogin() {
        Log.d(TAG, "moveToLogin()")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToMain() {
        Log.d(TAG, "moveToMain()")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}