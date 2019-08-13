package com.zwb.androidtools.test_dialogmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zwb.androidtools.R
import com.zwb.dialogmanager.DialogManager
import kotlinx.android.synthetic.main.activity_dialog_manager.*

class DialogManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_manager)
        btNext.setOnClickListener {
            startActivity(Intent(this, DialogManagerActivity::class.java))
        }

        btShowDialog.setOnClickListener {
            Toast.makeText(this, "即将启动弹框", Toast.LENGTH_SHORT).show()
            showDialog()
        }
    }

    private fun showDialog() {
        btShowDialog.postDelayed({
            for (i in 1 until 4) {
                val dialog = CommonDialog(this)
                dialog.setPriority(i)
                dialog.setText("这是级别为 $i 的弹框")
                dialog.showDialogManager()
            }
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        DialogManager.destroy(this)
    }


}
