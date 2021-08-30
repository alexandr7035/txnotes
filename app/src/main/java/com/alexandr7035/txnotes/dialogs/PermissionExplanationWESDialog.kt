package com.alexandr7035.txnotes.dialogs

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alexandr7035.txnotes.R
import com.alexandr7035.txnotes.dialogs.core.PosNegDialog

class PermissionExplanationWESDialog: PosNegDialog() {
    override lateinit var btnNegative: TextView
    override lateinit var btnPositive: TextView

    val FM_TAG = "DIALOG_PERMISSION_EXPLANATION_WES_TAG"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dialogView: View = inflater.inflate(R.layout.dialog_persmissions_write_ext, container, false)

        dialog?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.background_dialog))
        isCancelable = false

        btnNegative = dialogView.findViewById(R.id.btnNegative)
        btnPositive = dialogView.findViewById(R.id.btnPositive)
        btnPositive.setOnClickListener(this)
        btnNegative.setOnClickListener(this)

        return dialogView
    }
}