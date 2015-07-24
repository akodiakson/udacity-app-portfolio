package com.akodiakson.udacity.portfolio.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.fragment.TopTracksFragment;

public class ArtistTopTracksActivity extends AppCompatActivity{

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
//                arguments.putString(TopTracksFragment.EXTRA_ARTIST_NAME, receivedIntent.getStringExtra(TopTracksFragment.EXTRA_ARTIST_NAME));
//                arguments.putString(TopTracksFragment.EXTRA_ARTIST_IMAGE_URL, receivedIntent.getStringExtra(TopTracksFragment.EXTRA_ARTIST_IMAGE_URL));
//                arguments.putInt(TopTracksFragment.EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, receivedIntent.getIntExtra(TopTracksFragment.EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, 0));
            }

            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_content_frame_container, fragment)
                    .commit();
        }
    }
}
