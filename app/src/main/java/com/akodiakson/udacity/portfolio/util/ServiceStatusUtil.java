package com.akodiakson.udacity.portfolio.util;

import android.app.ActivityManager;
import android.content.Context;

import com.akodiakson.udacity.portfolio.service.SpotifyPlayerService;

import java.lang.ref.WeakReference;

public class ServiceStatusUtil {
    //Source : http://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
    public static boolean isMyServiceRunning(WeakReference<Context> context) {
        ActivityManager manager = (ActivityManager) context.get().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SpotifyPlayerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
