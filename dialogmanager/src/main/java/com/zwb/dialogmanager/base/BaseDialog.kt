package com.zwb.dialogmanager.base

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat.getSystemService
import com.zwb.dialogmanager.DialogInterface
import com.zwb.dialogmanager.DialogManager
import com.zwb.dialogmanager.DialogManagerInterface
import com.zwb.dialogmanager.R

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 19:18.
 * @ desc:
 **/
abstract class BaseDialog : AppCompatDialog, DialogManagerInterface, DialogConfigInterface {

    private val listeners = mutableListOf<DialogInterface.OnDismissListener>()
    protected var mActivity: Activity? = null
    private var mOnKeyListener: DialogInterface.OnKeyListener? = null

    constructor(context: Activity?) : super(context, R.style.baseDialog) {
        mActivity = context
    }

    init {
        val view = layoutInflater.inflate(tellMeLayout(), null)
        setContentView(view)
        onCreateView(view)
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

    override fun showDialog() {
        show()
    }

    override fun dismissDialog() {
        dismiss()
    }

    override fun destroy() {
        mActivity = null
        listeners.clear()
    }

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
}