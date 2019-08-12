package com.zwb.appmanager.kt

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

/**
 * @ author : zhouweibin
 * @ time: 2019/8/12 15:45.
 * @ desc: app管理器
 **/
object AppManager {

    private var app: Application? = null

    private var appCount = 0

    /**
     * 是否处于前台
     */
    var isForeground = false

    var activities: LinkedList<Activity> = LinkedList()

    /**
     * 获取Application
     */
    fun getApplication(): Application {
        if (app != null) {
            return app!!
        }
        try {
            return Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(
                null,
                null
            ) as Application
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            return Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(
                null,
                null
            ) as Application
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return app!!
    }

    /**
     * 初始化
     */
    fun init(app: Application) {
        AppManager.app = app
        app.registerActivityLifecycleCallbacks(ActivityLifecycleDelegate())
    }

    class ActivityLifecycleDelegate : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStarted(activity: Activity) {
            appCount++
            if (appCount > 0) {
                isForeground = true
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            activities.remove(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
            appCount--
            if (appCount == 0) {
                isForeground = false
            }
        }

        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            activities.addLast(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            sortActivities(activity)
        }
    }

    /**
     * 重新给activity列表排序
     */
    private fun sortActivities(activity: Activity) {
        if (activities.contains(activity) && activities.last != activity) {
            activities.remove(activity)
            activities.addLast(activity)
        }
    }

    /**
     * 获取栈顶的activity
     */
    fun getTopActivity(): Activity? {
        if (activities.isEmpty()) {
            return null
        }
        return activities.last
    }

    fun finishAll() {
        for (i in 0 until activities.size) {
            activities[i].overridePendingTransition(0, 0)
            activities[i].finish()
        }
        activities.clear()
    }
}