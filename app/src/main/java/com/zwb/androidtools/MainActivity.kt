package com.zwb.androidtools

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zwb.androidtools.test_appmanager.AppManagerAActivity
import com.zwb.androidtools.test_dialogmanager.DialogManagerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btTestAppManager.setOnClickListener {
            startActivity(Intent(this, AppManagerAActivity::class.java))
        }
        btTestDialogManager.setOnClickListener {
            startActivity(Intent(this, DialogManagerActivity::class.java))
        }
    }
}
