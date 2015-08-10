package com.akodiakson.udacity.portfolio;

import android.app.Application;

import com.akodiakson.udacity.portfolio.model.TrackModel;

//import com.squareup.leakcanary.LeakCanary;

public class PortfolioApplication extends Application {

    private TrackModel currentlyPlayingTrack;
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
    }

    public TrackModel getCurrentlyPlayingTrack() {
        return currentlyPlayingTrack;
    }

    public void setCurrentlyPlayingTrack(TrackModel currentlyPlayingTrack) {
        this.currentlyPlayingTrack = currentlyPlayingTrack;
    }
}
