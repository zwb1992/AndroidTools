package com.zwb.dialogmanager

import android.app.Activity
import android.content.Context

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 10:48.
 * @ desc: 弹框统一管理
 **/
object DialogManager {

    /**
     * 所有计划展示的Dialog
     */
    private val dialogs = mutableMapOf<Activity, MutableList<DialogManagerInterface>>()

    /**
     * 正在显示的Dialog
     */
    private val showingDialogs = mutableMapOf<Activity, DialogManagerInterface>()


    /**
     * 需要加入管理的Dialog
     */
    fun showDialogManager(dialog: DialogManagerInterface) {
        val context = dialog.getActivity()
        context?.let {
            if (!dialogs.containsKey(context)) {
                dialogs[context] = mutableListOf()
            }
            val contextDialogs = dialogs[context]
            contextDialogs?.add(dialog)
            // 进行排序
            contextDialogs?.sortBy { it.getPriority() }
            dialog.addOnDismissListener(object : DialogInterface.OnDismissListener {
                override fun onDismiss(dialogManagerInterface: DialogManagerInterface) {
                    val context = dialogManagerInterface.getActivity()
                    removeShowingDialog(context)
                    showNextDialog(context)
                }
            })
            if (!showingDialogs.containsKey(context)) {
                showNextDialog(context)
            }
        }
    }

    /**
     * 显示下一个Dialog
     * @param context 传入activity
     */
    fun showNextDialog(context: Activity?) {
        context?.let {
            if (dialogs.containsKey(context)) {
                val contextDialogs = dialogs[context]
                if (contextDialogs.isNullOrEmpty()) {
                    dialogs.remove(context)
                } else {
                    val targetDialog = contextDialogs.last()
                    contextDialogs.remove(targetDialog)
                    showingDialogs[context] = targetDialog
                    targetDialog.showDialog()
                }
            }
        }
    }

    /**
     * 移除当前正在显示的dialog
     */
    private fun removeShowingDialog(context: Context?) {
        context?.let {
            showingDialogs.remove(context)
        }
    }

    /**
     * 页面销毁的时候调用一下
     */
    fun destroy(context: Activity?) {
        context?.let {
            dialogs[context]?.forEach {
                it.destroy()
            }
            dialogs.remove(context)
            showingDialogs[context]?.destroy()
            showingDialogs[context]?.dismissDialog()
        }
    }
}