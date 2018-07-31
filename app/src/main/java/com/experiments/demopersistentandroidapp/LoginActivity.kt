package com.experiments.demopersistentandroidapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : VolatileActivity() {
    private fun getPersistent() = thePersistent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.button).setOnClickListener {
            getPersistent().login(this,
                    findViewById<EditText>(R.id.name).text.toString(),
                    findViewById<EditText>(R.id.id).text.toString())
        }
    }
}

class VerifyActivity : VolatileActivity() {
    private fun getPersistent() = thePersistent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        val name = getPersistent().name
        val id = getPersistent().id
        findViewById<TextView>(R.id.name).text = "Is your name $name?"
        findViewById<TextView>(R.id.id).text = "Is your id $id?"
        findViewById<Button>(R.id.button).setOnClickListener {
            getPersistent().verify(this)
        }
    }
}

class WelcomeActivity : VolatileActivity() {
    private fun getPersistent() = thePersistent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val name = getPersistent().name
        val id = getPersistent().id

        findViewById<TextView>(R.id.name).text = "Hello, $name"
        findViewById<TextView>(R.id.id).text = "Hi, $id"
    }
}

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ClearOnRestore())
    }

}


class RootActivity: AppCompatActivity(), ClearOnRestore.NoClearActivity {
    private fun getPersistent() = thePersistent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
    }

    override fun onResume() {
        super.onResume()
        if (isLaunched) {
            finish()
        } else {
            getPersistent().startFirstActivity(this)
            isLaunched = true
        }
    }

    companion object {
        var isLaunched = false
    }
}

val thePersistent = MyPersistent()

class MyPersistent {
    var name = "Not set yet"
    var id = "N/A"

    fun startFirstActivity(context: Context) {
        val it = Intent(context, LoginActivity::class.java)
        context.startActivity(it)
    }
    fun login(context: Context, name: String, id: String) {
        this.name = name
        this.id = id
        val it = Intent(context, VerifyActivity::class.java)
        context.startActivity(it)
    }

    fun verify(context: Context) {
        val it = Intent(context, WelcomeActivity::class.java)
        context.startActivity(it)
    }
}

