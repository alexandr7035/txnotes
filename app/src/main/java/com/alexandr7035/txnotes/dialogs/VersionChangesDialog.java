package com.alexandr7035.txnotes.dialogs;

import android.app.Dialog;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.alexandr7035.txnotes.BuildConfig;
import com.alexandr7035.txnotes.R;

public class VersionChangesDialog extends DialogFragment implements View.OnClickListener {

    private TextView closeDialogBtn;
    private TextView dialogTitle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.DialogTheme);
        return super.onCreateDialog(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_version_change, container, false);

        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.dialog_change_log_title, BuildConfig.VERSION_NAME));

        closeDialogBtn = dialogView.findViewById(R.id.closeDialogBtn);
        closeDialogBtn.setOnClickListener(this);

        setCancelable(false);

        getDialog().getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.background_dialog));

        return dialogView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set dialog with ~ to 85% of screen width
        Window window = getDialog().getWindow();
        window.setLayout((int) (getScreenWidth(getActivity()) * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == closeDialogBtn.getId()) {
            dismiss();
        }
    }


    private int getScreenWidth(FragmentActivity activity) {

        int width;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());

            width = windowMetrics.getBounds().width() - insets.left - insets.right;
        }

        else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
        }

        return width;
    }
}
