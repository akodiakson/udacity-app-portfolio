package com.akodiakson.udacity.portfolio.network;

import android.os.AsyncTask;

import com.akodiakson.udacity.portfolio.activity.OnTopTracksResultListener;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;

public class TopTracksTask extends AsyncTask<String, Void, Tracks>{

    private static final String KEY_SEARCH_COUNTRY = "country";
    private static final String SEARCH_COUNTRY = "US";

    private final OnTopTracksResultListener onTopTracksResultListener;

    public TopTracksTask(OnTopTracksResultListener onTopTracksResultListener){
        this.onTopTracksResultListener = onTopTracksResultListener;
    }

    @Override
    protected Tracks doInBackground(String... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map<String, Object> options = new HashMap<>();
        options.put(KEY_SEARCH_COUNTRY, SEARCH_COUNTRY);
        return spotify.getArtistTopTrack(params[0], options);
    }

    @Override
    protected void onPostExecute(Tracks tracks) {
        onTopTracksResultListener.onTracksObtained(tracks);
    }
}
