package com.akodiakson.udacity.portfolio;

import android.app.Application;

import com.akodiakson.udacity.portfolio.model.TrackModel;

import java.util.List;

public class PortfolioApplication extends Application {

    private TrackModel currentlyPlayingTrack;
    private List<TrackModel> mTopTracks;

    public TrackModel getCurrentlyPlayingTrack() {
        return currentlyPlayingTrack;
    }

    public void setCurrentlyPlayingTrack(TrackModel currentlyPlayingTrack) {
        this.currentlyPlayingTrack = currentlyPlayingTrack;
    }

    public List<TrackModel> getTopTracks() {
        return mTopTracks;
    }

    public void setTopTracks(List<TrackModel> topTracks) {
        this.mTopTracks = topTracks;
    }
}
