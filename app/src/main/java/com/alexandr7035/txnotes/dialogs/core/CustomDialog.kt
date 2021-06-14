package com.alexandr7035.txnotes.dialogs.core

import android.app.Dialog
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.alexandr7035.txnotes.R

abstract class CustomDialog: DialogFragment() {

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

}
