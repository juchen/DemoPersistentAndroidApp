package com.experiments.demopersistentandroidapp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.*

private const val keyProcessUUID = "The process UUID"
private val processUUID = UUID.randomUUID().toString()

val TAG = "Persistent.kt"

class ClearOnRestore : Application.ActivityLifecycleCallbacks {
    interface NoClearActivity

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        outState?.putString(keyProcessUUID, processUUID)
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity == null) return
        if (activity is VolatileActivity) return
        if (activity is NoClearActivity) return
        if (savedInstanceState == null) return
        if (savedInstanceState.getString(keyProcessUUID) == processUUID) return

        activity.restart()
    }

}

open class VolatileActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) return
        if (savedInstanceState.getString(keyProcessUUID)
                == processUUID) return
        restart()
    }
}

private fun Activity.restart(root: Class<out Activity>? = null) {
    Log.v(TAG, "Cleaning activities.")
    val it = if (root == null) {
        packageManager.getLaunchIntentForPackage(packageName)
    } else {
        Intent(this, root)
    }
    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(it)
}


