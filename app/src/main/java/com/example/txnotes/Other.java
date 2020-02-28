package com.example.txnotes;

import android.content.Context;

public class Other {

    public static Integer dpToPixels(Context context, Integer dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return(pixels);
    }

}
