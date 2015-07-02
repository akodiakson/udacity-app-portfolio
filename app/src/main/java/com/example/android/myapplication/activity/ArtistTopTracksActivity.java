package com.example.android.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class ArtistTopTracksActivity extends AppCompatActivity implements OnTopTracksResultListener {

    private RecyclerView.Adapter adapter;

    private List<Track> topTracks = new ArrayList<>();

    public static final String EXTRA_ARTIST_ID = "EXTRA_ARTIST_ID";
    public static final String EXTRA_ARTIST_NAME = "EXTRA_ARTIST_NAME";
    public static final String EXTRA_ARTIST_IMAGE_URL = "EXTRA_ARTIST_IMAGE_URL";
    public static final String EXTRA_ARTIST_IMAGE_RESIZE_WIDTH = "EXTRA_ARTIST_IMAGE_RESIZE_WIDTH";
    public static final String EXTRA_ARTIST_IMAGE_RESIZE_HEIGHT = "EXTRA_ARTIST_IMAGE_RESIZE_HEIGHT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_top_tracks);

        Intent intent = getIntent();
        final String artistId = intent.getStringExtra(EXTRA_ARTIST_ID);
        final String artistName = intent.getStringExtra(EXTRA_ARTIST_NAME);
        final String artistImageURL = intent.getStringExtra(EXTRA_ARTIST_IMAGE_URL);
        final int artistImageResizeWidth = intent.getIntExtra(EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, 0);
        final int artistImageResizeHeight = intent.getIntExtra(EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, 0);

        TextView artistNameView = (TextView) findViewById(R.id.artistNameTopTracks);
        artistNameView.setText(artistName);

        showArtistImage(artistImageURL, artistImageResizeWidth, artistImageResizeHeight);

        RecyclerView topTracksView = (RecyclerView)findViewById(R.id.artist_top_tracks_recycler_view);
        topTracksView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ArtistTopTracksAdapter(topTracks);
        topTracksView.setAdapter(adapter);

        searchArtistTopTracks(artistId);
    }

    private void searchArtistTopTracks(String artistId) {
        new TopTracksTask(this).execute(artistId);
    }

    private void showArtistImage(String artistImageURL, int artistImageResizeWidth, int intartistImageResizeHeight ) {
        final ImageView imageView = (ImageView) findViewById(R.id.artistImageTopTracks);
        imageView.setClipToOutline(true);
        imageView.setBackgroundColor(getResources().getColor(R.color.black));
        imageView.setOutlineProvider(new CircularOutlineProvider(true));
        Picasso.with(this)
                .load(artistImageURL)
                .centerCrop()
                .resize(artistImageResizeWidth, intartistImageResizeHeight)
                .into(imageView);
    }

    @Override
    public void onTracksObtained(Tracks tracks) {
        Toast.makeText(this, "tracks obtained", Toast.LENGTH_SHORT).show();
    }
}
