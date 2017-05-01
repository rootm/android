package com.example.muvindu.recyclerdemo.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Muvindu on 1/6/2017.
 */

public class utils {
    public static float getscreenSize(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return dpWidth;
        }

    public static int getscreenHeigth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dpHeight = (int)(displayMetrics.heightPixels / displayMetrics.density);
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return dpHeight;
    }
}
