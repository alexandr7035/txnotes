package com.alexandr7035.txnotes.dialogs

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alexandr7035.txnotes.R
import com.alexandr7035.txnotes.dialogs.core.SingleActionDialog

class InfoDialog(private val title: String, private val description: String): SingleActionDialog() {
    override lateinit var btnAction: TextView

    private lateinit var dialogTitle: TextView
    private lateinit var dialogDescription: TextView

    val FM_TAG = "DIALOG_INFO"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dialogView: View = inflater.inflate(R.layout.dialog_info, container, false)

        dialog?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.background_dialog))
        isCancelable = false

        dialogTitle = dialogView.findViewById(R.id.dialogTitleView)
        dialogDescription = dialogView.findViewById(R.id.dialogDescriptionView)

        dialogTitle.text = this.title
        dialogDescription.text = this.description

        btnAction = dialogView.findViewById(R.id.btnAction)
        btnAction.setOnClickListener(this)

        return dialogView
    }

}