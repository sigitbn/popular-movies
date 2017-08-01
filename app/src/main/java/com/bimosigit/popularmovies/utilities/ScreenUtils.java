package com.bimosigit.popularmovies.utilities;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by sigitbn on 8/2/17.
 */

public class ScreenUtils {
    public static int numberOfColumns(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
