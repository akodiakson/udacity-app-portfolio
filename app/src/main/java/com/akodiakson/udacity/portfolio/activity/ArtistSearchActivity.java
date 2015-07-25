package com.akodiakson.udacity.portfolio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.fragment.ArtistSearchFragment;
import com.akodiakson.udacity.portfolio.fragment.TopTracksFragment;
import com.akodiakson.udacity.portfolio.model.SpotifyArtistModel;

public class ArtistSearchActivity extends AppCompatActivity implements ArtistSearchFragment.Callbacks  {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        System.out.println("asdf hasContainer?" + (findViewById(R.id.top_tracks_content_frame_container)));

        if (findViewById(R.id.top_tracks_content_frame_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
//            ((DemoItemListFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.demoitem_list))
//                    .setActivateOnItemClick(true);
        }

    }

    @Override
    public void onArtistSelected(SpotifyArtistModel spotifyArtistModel) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            TopTracksFragment fragment = new TopTracksFragment();
            arguments.putParcelable(TopTracksFragment.EXTRA_SPOTIFY_ARTIST, spotifyArtistModel);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_content_frame_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ArtistTopTracksActivity.class);
            Bundle extras = new Bundle();


            extras.putParcelable(TopTracksFragment.EXTRA_SPOTIFY_ARTIST, spotifyArtistModel);
            detailIntent.putExtras(extras);



            startActivity(detailIntent);
        }
    }
}
