package com.zwb.dialogmanager

import android.app.Activity

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 15:08.
 * @ desc: 所有Dialog都必须实现这个接口
 **/
interface DialogManagerInterface {

    /**
     * 获取弹框的优先级
     */
    fun getPriority() = 0

    /**
     * 获取上下文
     */
    fun getActivity(): Activity?

    /**
     * 显示Dialog
     */
    fun show()

    /**
     * 隐藏Dialog
     */
    fun dismiss()

    /**
     * 添加弹框显示的监听
     */
    fun addOnDismissListener(onDismissListener: DialogInterface.OnDismissListener)

    /**
     * 销毁资源
     */
    fun destroy()

}