package com.zwb.androidtools.test_appmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zwb.androidtools.R
import com.zwb.appmanager.kt.AppManager
import kotlinx.android.synthetic.main.activity_app_manager_a.*

class AppManagerBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_manager_b)
        btNext.setOnClickListener {
            startActivity(Intent(this, AppManagerCActivity::class.java))
        }
        btShowActivities.setOnClickListener {
            setText()
        }
        btFinish.setOnClickListener {
            AppManager.finishAll()
        }
    }

    private fun setText() {
        val text = StringBuffer()
        for (i in 0 until AppManager.activities.size) {
            text.append("界面：" + AppManager.activities[i].toString() + "\n")
        }
        text.append("栈顶：" + AppManager.getTopActivity())
        tvActivities.text = text.toString()
    }
}
