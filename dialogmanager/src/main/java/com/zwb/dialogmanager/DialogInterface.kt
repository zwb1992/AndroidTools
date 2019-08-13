package com.zwb.dialogmanager

import android.view.KeyEvent
import com.zwb.dialogmanager.base.BaseDialog

/**
 * @ author : zhouweibin
 * @ time: 2019/8/13 15:14.
 * @ desc:
 **/
interface DialogInterface {

    interface OnDismissListener {
        fun onDismiss(dialogManagerInterface: DialogManagerInterface)
    }

    interface OnKeyListener {
        fun onKey(dialog: BaseDialog, code: Int, event: KeyEvent): Boolean
    }

}