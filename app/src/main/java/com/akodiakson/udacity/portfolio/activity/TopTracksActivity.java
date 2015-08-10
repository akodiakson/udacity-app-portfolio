package com.akodiakson.udacity.portfolio.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.fragment.TopTracksFragment;
import com.akodiakson.udacity.portfolio.model.SpotifyArtistModel;
import com.akodiakson.udacity.portfolio.model.TrackModel;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class TopTracksActivity extends AppCompatActivity implements TopTracksFragment.Callbacks{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_top_tracks);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            Intent receivedIntent = getIntent();
            if(receivedIntent != null && receivedIntent.getExtras() != null){
                arguments.putParcelable(TopTracksFragment.EXTRA_SPOTIFY_ARTIST, receivedIntent.getParcelableExtra(TopTracksFragment.EXTRA_SPOTIFY_ARTIST));
            }

            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_content_frame_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onArtistTrackSelectedForPlayback(TrackModel track, ArrayList<TrackModel> tracks) {
        //TODO -- implement, param will be new

        playForSmallLayout(track, tracks);
    }

    private void playForSmallLayout(TrackModel track, ArrayList<TrackModel> tracks) {
        Intent intent = new Intent(this, PlaybackActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(PlaybackFragment.EXTRA_SELECTED_SONG, track);
        extras.putParcelableArrayList(PlaybackFragment.EXTRA_TOP_TRACKS, tracks);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
