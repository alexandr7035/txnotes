package com.alexandr7035.txnotes.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandr7035.txnotes.R;

public class NavigationMenuItem extends LinearLayout {

    private TextView textView;

    public NavigationMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_navigation_menu_item, this);

        // Child views
        textView = findViewById(R.id.text);

        // Attribute set
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.NavigationMenuItem);

        String item_text = arr.getString(R.styleable.NavigationMenuItem_item_text);

        if (item_text != null) {
            textView.setText(item_text);
        }

        arr.recycle();
    }

}
