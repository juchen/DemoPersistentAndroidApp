package com.experiments.demopersistentandroidapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.util.*

private const val keyProcessUUID = "The process UUID"
private val processUUID = UUID.randomUUID().toString()

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

        activity.finish()
    }

}

open class VolatileActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) return
        if (savedInstanceState.getString(keyProcessUUID)
                == processUUID) return
        finish()
    }
}

