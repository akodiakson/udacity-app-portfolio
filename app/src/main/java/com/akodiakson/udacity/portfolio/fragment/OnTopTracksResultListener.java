package com.akodiakson.udacity.portfolio.fragment;

import kaaes.spotify.webapi.android.models.Tracks;

public interface OnTopTracksResultListener {
    void onTracksObtained(Tracks tracks);
}
