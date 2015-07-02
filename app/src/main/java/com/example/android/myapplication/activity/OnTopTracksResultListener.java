package com.example.android.myapplication.activity;

import kaaes.spotify.webapi.android.models.Tracks;

public interface OnTopTracksResultListener {
    void onTracksObtained(Tracks tracks);
}
