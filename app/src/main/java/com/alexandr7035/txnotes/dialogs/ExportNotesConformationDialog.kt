package com.alexandr7035.txnotes.dialogs

import android.app.Dialog
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.alexandr7035.txnotes.R

class ExportNotesConformationDialog: DialogFragment(), View.OnClickListener {

    private val LOG_TAG = "DEBUG_TXNOTES"
    private lateinit var actionHandler: DialogActionHandler

    private lateinit var btnCancel: TextView
    private lateinit var btnContinue: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // According to the documentation, calling this after
        // the fragment's Dialog is created will have no effect.
        // So call it here
        setStyle(STYLE_NORMAL, R.style.DialogTheme)

        return super.onCreateDialog(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dialogView: View = inflater.inflate(R.layout.dialog_export_notes, container, false)

        dialog?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.background_dialog))
        isCancelable = false

        btnCancel = dialogView.findViewById(R.id.btnNegative)
        btnContinue = dialogView.findViewById(R.id.btnPositive)
        btnCancel.setOnClickListener(this)
        btnContinue.setOnClickListener(this)

        return dialogView
    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window

        if (window != null) {
            window.setLayout((getScreenWidth(activity) * 0.85).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER)
        }
    }


    private fun getScreenWidth(activity: FragmentActivity?): Int {

        if (activity == null ) {
            return 0
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

            return windowMetrics.bounds.width() - insets.left - insets.right

        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }

    override fun onClick(v: View) {
        if (v.id == btnCancel.id) {
            actionHandler.onNegativeClick()
        }
        else {
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