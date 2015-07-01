package com.example.android.myapplication.activity;

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
}
