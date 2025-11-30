package kr.ac.nsu.hakbokgs.main.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

object CurrentActivityProvider : Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    fun getCurrentActivity(): Activity? = currentActivity

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        Log.d("CurrentActivityProvider", "🎬 onActivityResumed: ${activity.localClassName}")
    }

    override fun onActivityPaused(activity: Activity) {
        // Do not clear immediately
    }

    override fun onActivityStopped(activity: Activity) {
        if (currentActivity === activity && activity.isFinishing) {
            currentActivity = null
            Log.d("CurrentActivityProvider", "🛑 onActivityStopped: Cleared (isFinishing)")
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity === activity) {
            currentActivity = null
            Log.d("CurrentActivityProvider", "🗑️ onActivityDestroyed: Cleared")
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}
