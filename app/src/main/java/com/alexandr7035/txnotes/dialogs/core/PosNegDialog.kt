package com.alexandr7035.txnotes.dialogs.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alexandr7035.txnotes.R

abstract class PosNegDialog: CustomDialog(), View.OnClickListener {

    private lateinit var actionHandler: DialogActionHandler

    abstract var btnNegative: TextView
    abstract var btnPositive: TextView

    override fun onClick(v: View) {
        if (v.id == btnNegative.id) {
            actionHandler.onNegativeClick()
        }
        else if (v.id == btnPositive.id) {
            actionHandler.onPositiveClick()
        }
    }

    fun setActionHandler(actionHandler: DialogActionHandler) {
        this.actionHandler = actionHandler
    }

    interface DialogActionHandler {
        fun onPositiveClick()
        fun onNegativeClick()
    }
}