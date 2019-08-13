package com.zwb.androidtools.test_dialogmanager

import android.app.Activity
import android.view.View
import com.zwb.androidtools.R
import com.zwb.dialogmanager.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_common.*

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 15:41.
 * @ desc:
 **/
class CommonDialog : BaseDialog {
    constructor(context: Activity?) : super(context)

    override fun tellMeLayout() = R.layout.dialog_common

    override fun onCreateView(view: View) {
        btCancel.setOnClickListener {
            dismiss()
        }
        btConfirm.setOnClickListener {
            mActivity?.finish()
        }
    }

    private var mPriority = 0

    fun setPriority(priority: Int) {
        mPriority = priority
    }

    fun setText(content: String) {
        tvContent.text = content
    }

    override fun getPriority() = mPriority

    override fun toString(): String {
        return "mPriority=$mPriority"
    }
}