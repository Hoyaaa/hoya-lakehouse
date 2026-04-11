package kr.ac.nsu.hakbokgs.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.main.advertising.AdActivity;
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity;
import kr.ac.nsu.hakbokgs.main.map.MapActivity;
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity;
import kr.ac.nsu.hakbokgs.main.store.RestaurantListActivity;
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity;
import kr.ac.nsu.hakbokgs.main.store.popup.CookingStateRecovery;
import kr.ac.nsu.hakbokgs.main.todaymenu.TodayMenuActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<String> adImageUrls = new ArrayList<>();
    private int currentAdIndex = 0;
    private Handler adHandler = new Handler(Looper.getMainLooper());
    private Runnable adRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        db = FirebaseFirestore.getInstance();

        // 날짜 표시
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 E요일", Locale.KOREAN);
        String todayDate = dateFormat.format(new Date());
        TextView todayTextView = findViewById(R.id.today_date);
        todayTextView.setText("오늘은\n" + todayDate);

        // 버튼 이동
        findViewById(R.id.main_menu).setOnClickListener(v -> startActivity(new Intent(this, TodayMenuActivity.class)));
        findViewById(R.id.main_order).setOnClickListener(v -> startActivity(new Intent(this, RestaurantListActivity.class)));
        findViewById(R.id.main_Advert).setOnClickListener(v -> startActivity(new Intent(this, AdActivity.class)));
        findViewById(R.id.main_chat).setOnClickListener(v -> startActivity(new Intent(this, ChatBoardHomeActivity.class)));
        findViewById(R.id.main_list).setOnClickListener(v -> startActivity(new Intent(this, OrderHistoryActivity.class)));
        findViewById(R.id.main_mypage).setOnClickListener(v -> startActivity(new Intent(this, MypageActivity.class)));
        findViewById(R.id.main_map).setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));

        loadTodayBestMenus();
        loadAdvertImages();
        listenCongestionLevel(); // 🔥 혼잡도
    }

    private void loadAdvertImages() {
        db.collection("advertising")
                .document("ing")
                .collection("posts")
                .get()
                .addOnSuccessListener(snapshot -> {
                    adImageUrls.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        String url = doc.getString("bannerImageUrl");
                        if (url != null && !url.isEmpty()) {
                            adImageUrls.add(url);
                        }
                    }
                    startAdSlideshow();
                })
                .addOnFailureListener(e -> Log.e("MainActivity", "⚠️ 광고 이미지 로드 실패", e));
    }

    private void startAdSlideshow() {
        ImageView adView = findViewById(R.id.main_Advert);
        if (adImageUrls.isEmpty()) return;

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.1);

        adRunnable = new Runnable() {
            @Override
            public void run() {
                if (adImageUrls.isEmpty()) return;

                String imageUrl = adImageUrls.get(currentAdIndex);

                Glide.with(MainActivity.this)
                        .load(imageUrl)
                        .override(width, height)
                        .centerCrop()
                        .into(adView);

                currentAdIndex = (currentAdIndex + 1) % adImageUrls.size();
                adHandler.postDelayed(this, 4000);
            }
        };
        adHandler.post(adRunnable);
    }

    private void loadTodayBestMenus() {
        GridLayout orderTable = findViewById(R.id.order_table);
        String todayKey = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(new Date());
        List<MenuEntry> allMenus = new ArrayList<>();

        db.collection("best_menu")
                .get()
                .addOnSuccessListener(snapshot -> {
                    final int[] storeCount = {0};
                    final int totalStores = snapshot.size();

                    for (DocumentSnapshot storeDoc : snapshot.getDocuments()) {
                        String storeName = storeDoc.getId();

                        db.collection("best_menu")
                                .document(storeName)
                                .collection(todayKey)
                                .document("menu")
                                .get()
                                .addOnSuccessListener(menuDoc -> {
                                    Map<String, Object> menuData = menuDoc.getData();
                                    if (menuData != null) {
                                        for (Map.Entry<String, Object> entry : menuData.entrySet()) {
                                            long count = ((Number) entry.getValue()).longValue();
                                            allMenus.add(new MenuEntry(storeName, entry.getKey(), count));
                                        }
                                    }

                                    storeCount[0]++;
                                    if (storeCount[0] == totalStores) {
                                        showTopMenus(orderTable, allMenus);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("MainActivity", "🔥 메뉴 문서 접근 실패: " + storeName, e);
                                    storeCount[0]++;
                                    if (storeCount[0] == totalStores) {
                                        showTopMenus(orderTable, allMenus);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Log.e("MainActivity", "🔥 best_menu 불러오기 실패", e));
    }

    private void showTopMenus(GridLayout orderTable, List<MenuEntry> allMenus) {
        allMenus.sort(Comparator.comparingLong(MenuEntry::getCount).reversed());
        List<MenuEntry> top5 = allMenus.subList(0, Math.min(5, allMenus.size()));

        for (int i = 0; i < top5.size(); i++) {
            MenuEntry entry = top5.get(i);
            int baseIndex = 5 + (i * 5);

            ((TextView) orderTable.getChildAt(baseIndex)).setText(String.valueOf(i + 1));
            ((TextView) orderTable.getChildAt(baseIndex + 1)).setText(entry.store);
            ((TextView) orderTable.getChildAt(baseIndex + 2)).setText(entry.menu);
            ((TextView) orderTable.getChildAt(baseIndex + 3)).setText(entry.count + "건");

            int finalIndex = baseIndex + 4;
            db.collection("order_management").document(entry.store)
                    .get()
                    .addOnSuccessListener(docSnap -> {
                        long waiting = docSnap.contains("count") ? ((Number) docSnap.get("count")).longValue() : 0;
                        ((TextView) orderTable.getChildAt(finalIndex)).setText(waiting + "팀");
                    })
                    .addOnFailureListener(e -> ((TextView) orderTable.getChildAt(finalIndex)).setText("-"));
        }
    }

    private void listenCongestionLevel() {
        String todayKey = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(new Date());
        db.collection("store_order")
                .document("congestion")
                .collection(todayKey)
                .document("eating")
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("MainActivity", "[혼잡도 실시간 감지 실패]", error);
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        long count = documentSnapshot.contains("count") ? ((Number) documentSnapshot.get("count")).longValue() : 0;
                        applyCongestionLevel((int) count);
                    } else {
                        Log.d("MainActivity", "[혼잡도 문서 없음 - 실시간 감지 중]");
                    }
                });
    }

    private void applyCongestionLevel(int count) {
        int maxCount = 250;
        float ratio = Math.min(count / (float) maxCount, 1.0f);

        String colorHex, label;
        if (count >= 201) {
            colorHex = "#FF9AA2"; label = "만석";
        } else if (count >= 150) {
            colorHex = "#FFB347"; label = "매우 혼잡";
        } else if (count >= 120) {
            colorHex = "#FFD56B"; label = "혼잡";
        } else if (count >= 90) {
            colorHex = "#FFFFB5"; label = "약간 혼잡";
        } else if (count >= 60) {
            colorHex = "#B5EAD7"; label = "보통";
        } else if (count >= 30) {
            colorHex = "#C7CEEA"; label = "여유";
        } else {
            colorHex = "#AEC6CF"; label = "매우 여유";
        }

        View congestionBar = findViewById(R.id.congestion_bar);
        View progressView = findViewById(R.id.congestion_progress);
        TextView textView = findViewById(R.id.congestion_text);

        congestionBar.post(() -> {
            int fullWidth = congestionBar.getWidth();
            int targetWidth = (int) (fullWidth * ratio);

            ValueAnimator animator = ValueAnimator.ofInt(progressView.getWidth(), targetWidth);
            animator.setDuration(500);
            animator.addUpdateListener(animation -> {
                int animatedWidth = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = progressView.getLayoutParams();
                params.width = animatedWidth;
                progressView.setLayoutParams(params);
            });
            animator.start();

            textView.setText("혼잡도: " + label + " (" + count + "팀)");

            // 💡 배경 색상 적용: LayerDrawable 내부 접근
            Drawable bg = progressView.getBackground();
            if (bg instanceof LayerDrawable) {
                Drawable inner = ((LayerDrawable) bg).getDrawable(0);  // 첫 번째 레이어 가져오기
                if (inner instanceof GradientDrawable) {
                    ((GradientDrawable) inner).setColor(Color.parseColor(colorHex));
                } else if (inner instanceof ShapeDrawable) {
                    ((ShapeDrawable) inner).getPaint().setColor(Color.parseColor(colorHex));
                }
            }
        });
    }





    private static class MenuEntry {
        String store, menu;
        long count;

        public MenuEntry(String store, String menu, long count) {
            this.store = store;
            this.menu = menu;
            this.count = count;
        }

        public long getCount() {
            return count;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String userEmail = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getEmail()
                    : null;

            if (userEmail != null) {
                Log.d("MainActivity", "🔥 감지 복구 시도 중 → 사용자: " + userEmail);
                CookingStateRecovery.INSTANCE.recoverWatching(this);
            } else {
                Log.d("MainActivity", "🚫 로그인된 사용자 없음 → 감지 생략");
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adHandler != null && adRunnable != null) {
            adHandler.removeCallbacks(adRunnable);
        }
    }
}
