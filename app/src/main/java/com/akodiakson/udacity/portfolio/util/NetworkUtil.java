package com.akodiakson.udacity.portfolio.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

public class NetworkUtil {
    // Source: http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineConnection
    public static boolean isNetworkAvailable(WeakReference<Context> contextWeakReference){
        ConnectivityManager cm =
                (ConnectivityManager)contextWeakReference.get().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
