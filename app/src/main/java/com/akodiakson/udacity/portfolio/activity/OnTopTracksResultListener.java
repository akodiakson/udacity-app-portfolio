package com.akodiakson.udacity.portfolio.activity;

import kaaes.spotify.webapi.android.models.Tracks;

public interface OnTopTracksResultListener {
    void onTracksObtained(Tracks tracks);
}
