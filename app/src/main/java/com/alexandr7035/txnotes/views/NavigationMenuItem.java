package com.alexandr7035.txnotes.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandr7035.txnotes.R;

public class NavigationMenuItem extends LinearLayout {

    private TextView textView;
    private ImageView imageView;

    public NavigationMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_navigation_menu_item, this);

        setClickable(true);

        // Child views
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.image);

        // Attribute set
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.NavigationMenuItem);

        // Text
        String item_text = arr.getString(R.styleable.NavigationMenuItem_item_text);

        if (item_text != null) {
            textView.setText(item_text);
        }

        // Image
        Drawable item_image = arr.getDrawable(R.styleable.NavigationMenuItem_item_icon);

        if (item_image != null) {
            imageView.setImageDrawable(item_image);
        }

        arr.recycle();

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG_TX", "CLICKED");
            }
        });

    }


}
