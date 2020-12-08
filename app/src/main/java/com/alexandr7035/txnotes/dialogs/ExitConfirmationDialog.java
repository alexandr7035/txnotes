package com.alexandr7035.txnotes.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alexandr7035.txnotes.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ExitConfirmationDialog extends BottomSheetDialog implements View.OnClickListener {

    private Context context;
    private TextView positiveBtn;
    private TextView negativeBtn;
    private final String LOG_TAG = "DEBUG_TXNOTES";
    private ExitConfirmationDialogClickListener clickListener;

    public ExitConfirmationDialog(@NonNull Context context) {
        super(context);

        setCancelable(false);

        setContentView(R.layout.dialog_exit_confirmation);
        View contentView = findViewById(android.R.id.content);

        negativeBtn = contentView.findViewById(R.id.negativeBtn);
        negativeBtn.setOnClickListener(this);
        positiveBtn = contentView.findViewById(R.id.positiveBtn);
        positiveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == positiveBtn.getId()) {

            if (clickListener != null) {
                clickListener.exitConfirmationDialogPositiveClick();
            }

        }
        else if (v.getId() == negativeBtn.getId()) {

            if (clickListener != null) {
                clickListener.exitConfirmationDialogNegativeClick();
            }

        }
    }


    public void setOnButtonClickListener(ExitConfirmationDialogClickListener listener) {
        this.clickListener = listener;
    }

    public interface ExitConfirmationDialogClickListener {
        void exitConfirmationDialogPositiveClick();

        void exitConfirmationDialogNegativeClick();
    }
}
