package com.alexandr7035.txnotes.dialogs.core

import android.view.View
import android.widget.TextView

abstract class SingleActionDialog: CustomDialog(), View.OnClickListener {

    private lateinit var actionHandler: DialogActionHandler

    abstract var btnAction: TextView

    override fun onClick(v: View) {
        if (v.id == btnAction.id) {
            actionHandler.onActionClick()
        }
    }

    fun setActionHandler(actionHandler: DialogActionHandler) {
        this.actionHandler = actionHandler
    }

    interface DialogActionHandler {
        fun onActionClick()
    }
}