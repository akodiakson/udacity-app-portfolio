package com.akodiakson.udacity.portfolio.fragment;

import kaaes.spotify.webapi.android.models.ArtistsPager;

public interface ArtistSearchTaskResultListener{
    void handleArtistSearchTaskResults(ArtistsPager results);
}