package com.akodiakson.udacity.portfolio.network;

import android.os.AsyncTask;

import com.akodiakson.udacity.portfolio.fragment.ArtistSearchTaskResultListener;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class ArtistSearchTask extends AsyncTask<String, Void, ArtistsPager> {

    private static final String KEY_SEARCH_RESULT_LIMIT = "limit";
    private static final int SEARCH_RESULT_LIMIT = 10;

    private static final String KEY_TYPE = "type";
    private static final String ARTIST = "artist";

    private ArtistSearchTaskResultListener artistSearchTaskResultListener;

    public ArtistSearchTask(ArtistSearchTaskResultListener artistSearchTaskResultListener){
        this.artistSearchTaskResultListener = artistSearchTaskResultListener;
    }

    @Override
    protected ArtistsPager doInBackground(String... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map<String, Object> options = new HashMap<>();
        options.put(KEY_SEARCH_RESULT_LIMIT, SEARCH_RESULT_LIMIT);
        options.put(KEY_TYPE, ARTIST);
        return spotify.searchArtists(params[0], options);
    }

    @Override
    protected void onPostExecute(ArtistsPager results) {
        artistSearchTaskResultListener.handleArtistSearchTaskResults(results);
    }
}
