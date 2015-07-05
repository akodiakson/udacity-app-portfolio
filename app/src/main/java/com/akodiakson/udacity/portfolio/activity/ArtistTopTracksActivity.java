package com.akodiakson.udacity.portfolio.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.util.DimensUtil;
import com.akodiakson.udacity.portfolio.util.NetworkUtil;
import com.akodiakson.udacity.portfolio.util.StringUtil;

import com.akodiakson.udacity.portfolio.view.ArtistTopTracksAdapter;
import com.akodiakson.udacity.portfolio.view.CircularOutlineProvider;
import com.akodiakson.udacity.portfolio.network.TopTracksTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
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

    private Toolbar toolbar;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(artistName);

        showArtistImage(artistImageURL, artistImageResizeWidth, artistImageResizeHeight);

        RecyclerView topTracksView = (RecyclerView)findViewById(R.id.artist_top_tracks_recycler_view);
        topTracksView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtistTopTracksAdapter(topTracks);
        topTracksView.setAdapter(adapter);

        searchArtistTopTracks(artistId);
    }

    private void searchArtistTopTracks(String artistId) {
        if(NetworkUtil.isNetworkAvailable(new WeakReference<Context>(this))){
            new TopTracksTask(this).execute(artistId);
        } else {
            Snackbar
                    .make(findViewById(R.id.toolbar), getString(R.string.error_no_connectivity), Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    private void showArtistImage(String artistImageURL, int artistImageResizeWidth, int intartistImageResizeHeight ) {

        if(StringUtil.isEmpty(artistImageURL)
                || DimensUtil.isInvalidImageDimensPair(artistImageResizeWidth, intartistImageResizeHeight)){
            // Hide the artist image if there aren't any images.
            findViewById(R.id.artistImageTopTracks).setVisibility(View.GONE);
            return;
        }

        final ImageView imageView = (ImageView) findViewById(R.id.artistImageTopTracks);
        imageView.setClipToOutline(true);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorDividerColor));
        imageView.setOutlineProvider(new CircularOutlineProvider(true));
        Callback callback = new Callback.EmptyCallback(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap(); // Ew!
                Palette palette = Palette.from(bitmap).generate();
                toolbar.setBackgroundColor(palette.getVibrantColor(R.color.colorPrimaryText));
                toolbar.setTitleTextColor(palette.getLightVibrantColor(R.color.white));
            }
        };
        Picasso.with(this)
                .load(artistImageURL)
                .placeholder(R.drawable.ic_music_note_white_24dp)
                .centerCrop()
                .resize(artistImageResizeWidth, intartistImageResizeHeight)
                .into(imageView, callback);

    }

    @Override
    public void onTracksObtained(Tracks tracks) {
        List<Track> tracksList = tracks.tracks;
        if(tracksList == null || tracksList.isEmpty()){
            Snackbar
                    .make(findViewById(R.id.toolbar), getString(R.string.error_no_top_tracks), Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
        topTracks.addAll(tracksList);
        adapter.notifyDataSetChanged();
    }
}
