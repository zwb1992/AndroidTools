package com.zwb.androidtools.test_livedatabus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.zwb.androidtools.R
import com.zwb.livedatabus.bus1.LiveDataBus
import kotlinx.android.synthetic.main.activity_live_data_bus1.*

class LiveDataBus3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_bus3)
        btNext.setOnClickListener {
            finish()
        }

        LiveDataBus.getChannel(EventKey.KEY_BEAN, TestBean::class.java)
            .observe(this, Observer<TestBean> {
                Log.e("zwb", "LiveDataBus3Activity ===TestBean=== ${it.value}")
            })

        LiveDataBus.getChannel(EventKey.KEY_STR, String::class.java)
            .observe(this, Observer<String> {
                Log.e("zwb", "LiveDataBus3Activity ===String=== $it")
            })
    }
}
