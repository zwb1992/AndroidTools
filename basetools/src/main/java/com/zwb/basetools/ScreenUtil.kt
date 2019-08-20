package com.zwb.basetools

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

/**
 * @ author : zhouweibin
 * @ time: 2019/8/20 17:43.
 * @ desc: 屏幕相关的工具
 **/
object ScreenUtil {
    private lateinit var app: Application
    private const val STANDER_SCREEN_WIDTH_IN_DP = 360      // 屏幕适配的基准宽度

    private var mScreenWidth = 0                            // 屏幕宽度
    private var mScreenHeight = 0                           // 屏幕高度
    private var mAdapterWidth = STANDER_SCREEN_WIDTH_IN_DP  // 屏幕适配的基准宽度

    /**
     * 初始化
     */
    fun initApp(app: Application) {
        this.app = app
        initScreenSize()
    }

    /**
     * 初始化
     */
    fun initApp(app: Application, standerWidth: Int) {
        this.app = app
        if (standerWidth > 0) {
            mAdapterWidth = standerWidth
        }
        initScreenSize()
    }

    private fun initScreenSize() {
        var d: Display? = null
        try {
            val w = app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            d = w.defaultDisplay
            val metrics = DisplayMetrics()
            d!!.getMetrics(metrics)
            mScreenWidth = metrics.widthPixels
            mScreenHeight = metrics.heightPixels
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT >= 19) {
            try {
                val realSize = Point()
                Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
                mScreenWidth = realSize.x
                mScreenHeight = realSize.y
            } catch (ignored: Throwable) {
            }
        }
    }


    private var sNonCompatDensity: Float = 0f
    private var sNonCompatScaleDensity: Float = 0f


    private var oldDensity: Float = 0f
    private var oldScaledDensity: Float = 0f
    private var oldDensityDpi: Int = 0


    private var newDensity: Float = 0f
    private var newScaledDensity: Float = 0f
    private var newDensityDpi: Int = 0

    /**
     * 屏幕适配-> 头条的方案
     */
    fun screenAdapter(activity: Activity) {
        try {
            val appDisplayMetrics = app.resources.displayMetrics

            if (sNonCompatDensity == 0f) {

                oldDensity = appDisplayMetrics.density
                oldScaledDensity = appDisplayMetrics.scaledDensity
                oldDensityDpi = appDisplayMetrics.densityDpi

                sNonCompatDensity = appDisplayMetrics.density
                sNonCompatScaleDensity = appDisplayMetrics.scaledDensity
                app.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onConfigurationChanged(newConfig: Configuration) {
                        if (newConfig.fontScale > 0) {
                            sNonCompatScaleDensity = app.resources.displayMetrics.scaledDensity
                        }
                    }

                    override fun onLowMemory() = Unit
                })
            }
            var width = getScreenWidth()
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                width = activity.resources.displayMetrics.widthPixels
            }

            val targetDensity = width.toFloat() / mAdapterWidth
            val targetScaleDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity)
            val targetDensityDpi = (160 * targetDensity).toInt()

            appDisplayMetrics.density = targetDensity
            appDisplayMetrics.scaledDensity = targetScaleDensity
            appDisplayMetrics.densityDpi = targetDensityDpi
            val activityDisplayMetrics = activity.resources.displayMetrics
            activityDisplayMetrics.density = targetDensity
            activityDisplayMetrics.scaledDensity = targetScaleDensity
            activityDisplayMetrics.densityDpi = targetDensityDpi
            if (newDensity == 0f) {
                newDensity = targetDensity
                newScaledDensity = targetScaleDensity
                newDensityDpi = targetDensityDpi
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth() = mScreenWidth

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight() = mScreenHeight

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp==dip
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @params fontScale
     * （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }


    fun getScaledDensity(context: Context): Float {
        return context.resources.displayMetrics.scaledDensity
    }
}