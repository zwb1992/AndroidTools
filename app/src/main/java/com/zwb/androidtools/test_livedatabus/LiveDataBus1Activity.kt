package com.zwb.androidtools.test_livedatabus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.zwb.androidtools.R
import com.zwb.livedatabus.bus1.LiveDataBus
import kotlinx.android.synthetic.main.activity_live_data_bus1.*

class LiveDataBus1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_bus1)
        btNext.setOnClickListener {
            startActivity(Intent(this, LiveDataBus2Activity::class.java))
        }

        LiveDataBus.getChannel(EventKey.KEY_BEAN, TestBean::class.java)
            .observe(this, Observer<TestBean> {
                Log.e("zwb", "LiveDataBus1Activity ===TestBean=== ${it.value}")
            })

        LiveDataBus.getChannel(EventKey.KEY_STR, String::class.java)
            .observe(this, Observer<String> {
                Log.e("zwb", "LiveDataBus1Activity ===String=== $it")
            })
    }
}
