package com.zwb.dialogmanager.base

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import com.zwb.dialogmanager.DialogInterface
import com.zwb.dialogmanager.DialogManager
import com.zwb.dialogmanager.DialogManagerInterface
import com.zwb.dialogmanager.R

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 19:18.
 * @ desc:
 **/
abstract class BaseDialog(var mActivity: Activity?) : AppCompatDialog(mActivity, R.style.baseDialog),
    DialogManagerInterface, DialogConfigInterface {

    private val listeners = mutableListOf<DialogInterface.OnDismissListener>()
    private var mOnKeyListener: DialogInterface.OnKeyListener? = null
    private var canceledOnBackPressed = true

    init {
        val view = layoutInflater.inflate(this.tellMeLayout(), null)
        this.setContentView(view)
        this.onCreateView(view)
        setConfig()
        setOnDismissListener {
            listeners.forEach {
                it.onDismiss(this)
            }
        }
    }

    private fun setConfig() {
        val width = getCustomWidth()
        if (width > 0) {
            window?.attributes?.width = width
        }
        setCanceledOnTouchOutside(isCanceledOnTouchOutside())
        setOnKeyListener { _, code, keyEvent ->
            if (mOnKeyListener != null) {
                return@setOnKeyListener mOnKeyListener!!.onKey(this, code, keyEvent)
            }
            if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK) {
                return@setOnKeyListener !isCanceledOnBackPressed()
            }
            return@setOnKeyListener false
        }
        window?.attributes?.gravity = getGravity()
    }

    fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener) {
        this.mOnKeyListener = onKeyListener
    }

    override fun addOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        listeners.add(onDismissListener)
    }

    override fun getActivity() = mActivity

    override fun destroy() {
        mActivity = null
        listeners.clear()
    }

    /**
     * 加入管理
     */
    fun showDialogManager() {
        DialogManager.showDialogManager(this)
    }

    override fun getCustomWidth(): Int {
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        return (metrics.widthPixels * 0.71).toInt()
    }

    override fun isCanceledOnBackPressed() = canceledOnBackPressed

    /**
     * 动态设置位置
     */
    fun setGravity(gravity: Int) {
        window?.attributes?.gravity = gravity
    }

    fun setCanceledOnBackPressed(cancelable: Boolean) {
        this.canceledOnBackPressed = cancelable
    }
}