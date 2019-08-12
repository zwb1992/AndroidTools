package com.zwb.androidtools

import android.app.Application
import com.zwb.appmanager.kt.AppManager

/**
 * @ author : zhouweibin
 * @ time: 2019/8/12 16:34.
 * @ desc:
 **/
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppManager.init(this)
    }
}