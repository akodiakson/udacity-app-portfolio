package com.akodiakson.udacity.portfolio.activity;

import kaaes.spotify.webapi.android.models.ArtistsPager;

public interface ArtistSearchTaskResultListener{
    void handleArtistSearchTaskResults(ArtistsPager results);
}