package kr.ac.nsu.hakbokgs.main.store;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.MainActivity;
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.cart.BasketActivity;
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.BurgerMainActivity;
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.KkuiMainActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;
import kr.ac.nsu.hakbokgs.main.store.syongsyong.main.SsyongMainActivity;
import kr.ac.nsu.hakbokgs.main.store.taesan.main.TaesanMainActivity;

public class RestaurantListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        // 꾸이한끼
        ImageView burgerButton = findViewById(R.id.kkui_store_image);
        burgerButton.setOnClickListener(v -> showKkuihankkiPopup());

        // 버거운버거
        ImageView hamburgerButton = findViewById(R.id.burger_store_image);
        hamburgerButton.setOnClickListener(v -> showBurgerPopup());

        // 숑숑돈가스
        ImageView ssyongButton = findViewById(R.id.ssyong_store_image);
        ssyongButton.setOnClickListener(v -> showSsyongPopup());

        // 태산김치찜
        ImageView taesanButton = findViewById(R.id.taesan_store_image);
        taesanButton.setOnClickListener(v -> showTaesanPopup());

        // 장바구니
        ImageView cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> startActivity(new Intent(this, BasketActivity.class)));

        // 하단바
        findViewById(R.id.main_home).setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.main_list).setOnClickListener(v -> startActivity(new Intent(this, OrderHistoryActivity.class)));
        findViewById(R.id.main_chat).setOnClickListener(v -> startActivity(new Intent(this, ChatBoardHomeActivity.class)));
        findViewById(R.id.main_mypage).setOnClickListener(v -> startActivity(new Intent(this, MypageActivity.class)));
    }

    private void showBurgerPopup() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_burger);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // 대기 수 표시
        TextView waitText = dialog.findViewById(R.id.wait_burger);
        FirebaseFirestore.getInstance()
                .collection("order_management").document("버거운버거")
                .get()
                .addOnSuccessListener(snapshot -> {
                    Long count = snapshot.getLong("count");
                    waitText.setText("대기팀 : " + (count != null ? count : 0) + " 팀");
                })
                .addOnFailureListener(e -> waitText.setText("대기 정보 오류"));

        dialog.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnEnter_burger).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, BurgerMainActivity.class));
        });

        dialog.show();
    }

    private void showKkuihankkiPopup() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_kkui);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView waitText = dialog.findViewById(R.id.wait_kkui);
        FirebaseFirestore.getInstance()
                .collection("order_management").document("꾸이한끼")
                .get()
                .addOnSuccessListener(snapshot -> {
                    Long count = snapshot.getLong("count");
                    waitText.setText("대기팀 : " + (count != null ? count : 0) + " 팀");
                })
                .addOnFailureListener(e -> waitText.setText("대기 정보 오류"));

        dialog.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnEnter_kkui).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, KkuiMainActivity.class));
        });

        dialog.show();
    }

    private void showSsyongPopup() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_ssyong);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView waitText = dialog.findViewById(R.id.wait_ssyong);
        FirebaseFirestore.getInstance()
                .collection("order_management").document("쑝쑝돈가스")
                .get()
                .addOnSuccessListener(snapshot -> {
                    Long count = snapshot.getLong("count");
                    waitText.setText("대기팀 : " + (count != null ? count : 0) + " 팀");
                })
                .addOnFailureListener(e -> waitText.setText("대기 정보 오류"));

        dialog.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnEnter_ssyong).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, SsyongMainActivity.class));
        });

        dialog.show();
    }

    private void showTaesanPopup() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_taesan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView waitText = dialog.findViewById(R.id.wait_taesan);
        FirebaseFirestore.getInstance()
                .collection("order_management").document("태산김치찜")
                .get()
                .addOnSuccessListener(snapshot -> {
                    Long count = snapshot.getLong("count");
                    waitText.setText("대기팀 : " + (count != null ? count : 0) + " 팀");
                })
                .addOnFailureListener(e -> waitText.setText("대기 정보 오류"));

        dialog.findViewById(R.id.btn_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnEnter_taesan).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, TaesanMainActivity.class));
        });

        dialog.show();
    }
}
