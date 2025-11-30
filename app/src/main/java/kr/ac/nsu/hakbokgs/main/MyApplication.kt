package kr.ac.nsu.hakbokgs.main

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kr.ac.nsu.hakbokgs.main.common.CurrentActivityProvider
import kr.ac.nsu.hakbokgs.main.store.popup.MultiStoreCookingWatcher

class MyApplication : Application() {

    private val handler = Handler(Looper.getMainLooper())
    private val checkIntervalMillis = 1000L // 1초마다 UI 체크
    private var hasStartedWatcher = false

    override fun onCreate() {
        super.onCreate()

        // 현재 액티비티 추적 콜백 등록
        registerActivityLifecycleCallbacks(CurrentActivityProvider)

        // 주기적으로 UI 상태 확인하여 감지 재개
        startCookingWatcherRecoveryLoop()
    }

    private fun startCookingWatcherRecoveryLoop() {
        handler.post(object : Runnable {
            override fun run() {
                val currentActivity = CurrentActivityProvider.getCurrentActivity()
                val userId = FirebaseAuth.getInstance().currentUser?.email

                if (currentActivity != null && userId != null) {
                    if (!hasStartedWatcher) {
                        hasStartedWatcher = true
                        Log.d("MyApplication", "🔥 감지 복구 시도 중 → 사용자: $userId")
                        MultiStoreCookingWatcher.startWatchingAllStores(userId)
                    } else {
                        // UI가 준비되었으니 보류 팝업도 표시 시도
                        MultiStoreCookingWatcher.showPendingPopups(currentActivity)
                    }
                } else {
                    hasStartedWatcher = false
                }

                handler.postDelayed(this, checkIntervalMillis)
            }
        })
    }
}
