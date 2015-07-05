package com.akodiakson.udacity.portfolio;

import android.app.Application;

//import com.squareup.leakcanary.LeakCanary;

public class PortfolioApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
    }
}
