package kr.ac.nsu.hakbokgs.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;

public class GoogleLogin extends AppCompatActivity {
    private static final String TAG = "GoogleLogin";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SignInButton btnGoogleLogin;

    // 🔹 Progress UI 요소
    private ProgressBar progressBar;
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progress_bar);
        loadingText = findViewById(R.id.loading_text);
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);

        // Google 로그인 옵션 설정
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btnGoogleLogin = findViewById(R.id.btn_google_sign_in);
        btnGoogleLogin.setOnClickListener(view -> signOut());

        // 🔹 자동 로그인 처리
        GoogleSignInAccount alreadySignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadySignedInAccount != null) {
            showLoading();
            firebaseAuthWithGoogle(alreadySignedInAccount);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount gsa = task.getResult(ApiException.class);
                if (gsa != null) {
                    showLoading();
                    firebaseAuthWithGoogle(gsa);
                }
            } catch (ApiException e) {
                Log.e(TAG, "Google Sign-In failed: " + e.getStatusCode() + " - " + e.getMessage());
                Toast.makeText(GoogleLogin.this, "로그인 실패: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: 인증 시작");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserInFirestore(user);
                        }
                    } else {
                        hideLoading();
                        Log.e(TAG, "Firebase 로그인 실패: " + task.getException());
                        Toast.makeText(GoogleLogin.this, "Firebase 로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserInFirestore(FirebaseUser user) {
        String userEmail = user.getEmail();

        db.collection("users").document(userEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    hideLoading();
                    if (documentSnapshot.exists()) {
                        navigateToMain();
                    } else {
                        navigateToUserInformation();
                    }
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    Log.e(TAG, "Firestore 조회 실패: " + e.getMessage());
                    Toast.makeText(GoogleLogin.this, "사용자 정보 확인 실패", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(GoogleLogin.this, UserInformation.class);
            intent.putExtra("userEmail", user.getEmail());
            startActivity(intent);
            finish();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(GoogleLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
    }
}
