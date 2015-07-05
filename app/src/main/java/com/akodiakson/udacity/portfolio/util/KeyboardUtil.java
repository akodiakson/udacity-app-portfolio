package com.akodiakson.udacity.portfolio.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

public class KeyboardUtil {
    public static void hideKeyboard(WeakReference<Context> context, View viewWithWindowToken){
        InputMethodManager imm = (InputMethodManager) context.get().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewWithWindowToken.getWindowToken(), 0);
    }

    public static void showKeyboard(WeakReference<Context> context, View viewWithWindowToken){
        InputMethodManager imm = (InputMethodManager) context.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewWithWindowToken, InputMethodManager.SHOW_IMPLICIT);
    }
}
