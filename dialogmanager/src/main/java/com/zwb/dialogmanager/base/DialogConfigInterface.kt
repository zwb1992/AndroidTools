package com.zwb.dialogmanager.base

import android.view.Gravity
import android.view.View

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 19:42.
 * @ desc:
 **/
interface DialogConfigInterface {

    /**
     * 布局id
     */
    fun tellMeLayout(): Int

    /**
     * 生成的view
     */
    fun onCreateView(view: View)

    /**
     * 是否可以点击外部消失
     */
    fun isCanceledOnTouchOutside() = true

    /**
     * 是否可以按返回键消失
     */
    fun isCanceledOnBackPressed() = true

    /**
     * 自定义宽度
     */
    fun getCustomWidth() = 0

    /**
     * 自定义布局显示位置
     */
    fun getGravity() = Gravity.CENTER
}