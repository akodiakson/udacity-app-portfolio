package com.example.android.myapplication.activity;

import kaaes.spotify.webapi.android.models.ArtistsPager;

public interface ArtistSearchTaskResultListener{
    void handleArtistSearchTaskResults(ArtistsPager results);
}