package com.akodiakson.udacity.portfolio.activity;

import com.akodiakson.udacity.portfolio.model.TrackModel;

import java.util.ArrayList;

public interface IRestoreNowPlaying {
    void onArtistTrackSelectedForPlayback(TrackModel mode, ArrayList<TrackModel> tracks);
}
