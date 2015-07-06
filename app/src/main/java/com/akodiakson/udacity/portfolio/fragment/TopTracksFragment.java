package com.akodiakson.udacity.portfolio.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.network.TopTracksTask;
import com.akodiakson.udacity.portfolio.util.DimensUtil;
import com.akodiakson.udacity.portfolio.util.NetworkUtil;
import com.akodiakson.udacity.portfolio.util.StringUtil;
import com.akodiakson.udacity.portfolio.view.ArtistTopTracksAdapter;
import com.akodiakson.udacity.portfolio.view.CircularOutlineProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class TopTracksFragment extends Fragment implements OnTopTracksResultListener {

    private RecyclerView.Adapter adapter;

    private List<Track> topTracks = new ArrayList<>();

    public static final String EXTRA_ARTIST_ID = "EXTRA_ARTIST_ID";
    public static final String EXTRA_ARTIST_NAME = "EXTRA_ARTIST_NAME";
    public static final String EXTRA_ARTIST_IMAGE_URL = "EXTRA_ARTIST_IMAGE_URL";
    public static final String EXTRA_ARTIST_IMAGE_RESIZE_WIDTH = "EXTRA_ARTIST_IMAGE_RESIZE_WIDTH";

    private String artistId;
    private String artistName;
    private String artistImageURL;
    private int artistImageResizeWidth;
    private int artistImageResizeHeight;

    private Toolbar toolbar;

    private View fragmentView;

    public TopTracksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar supportActionBar = activity.getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(artistName);
        }

        RecyclerView topTracksView = (RecyclerView)fragmentView.findViewById(R.id.artist_top_tracks_recycler_view);
        topTracksView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ArtistTopTracksAdapter(topTracks);
        topTracksView.setAdapter(adapter);
        showArtistImage(artistImageURL, artistImageResizeWidth, artistImageResizeHeight);

        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Intent intent = getActivity().getIntent();
        artistId = intent.getStringExtra(EXTRA_ARTIST_ID);
        artistName = intent.getStringExtra(EXTRA_ARTIST_NAME);
        artistImageURL = intent.getStringExtra(EXTRA_ARTIST_IMAGE_URL);
        artistImageResizeWidth = intent.getIntExtra(EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, 0);
        artistImageResizeHeight = intent.getIntExtra(EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, 0);
    }

    private void searchArtistTopTracks(String artistId) {
        if(NetworkUtil.isNetworkAvailable(new WeakReference<Context>(getActivity()))){
            new TopTracksTask(this).execute(artistId);
        } else {
            Snackbar
                    .make(fragmentView.findViewById(R.id.toolbar), getString(R.string.error_no_connectivity), Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(topTracks.isEmpty()){
            searchArtistTopTracks(artistId);
        }
    }

    private void showArtistImage(String artistImageURL, int artistImageResizeWidth, int intartistImageResizeHeight ) {

        if(StringUtil.isEmpty(artistImageURL)
                || DimensUtil.isInvalidImageDimensPair(artistImageResizeWidth, intartistImageResizeHeight)){
            // Hide the artist image if there aren't any images.
            fragmentView.findViewById(R.id.artistImageTopTracks).setVisibility(View.GONE);
            return;
        }

        final ImageView imageView = (ImageView) fragmentView.findViewById(R.id.artistImageTopTracks);
        imageView.setClipToOutline(true);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorDividerColor));
        imageView.setOutlineProvider(new CircularOutlineProvider(true));
        Callback callback = new Callback.EmptyCallback(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                //Source: http://jakewharton.com/coercing-picasso-to-play-with-palette/
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap(); // Ew!
                Palette palette = Palette.from(bitmap).generate();
                toolbar.setBackgroundColor(palette.getVibrantColor(R.color.colorPrimaryText));
                toolbar.setTitleTextColor(palette.getLightVibrantColor(R.color.white));
            }
        };
        Picasso.with(getActivity())
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
                    .make(fragmentView.findViewById(R.id.toolbar), getString(R.string.error_no_top_tracks), Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
        topTracks.addAll(tracksList);
        adapter.notifyDataSetChanged();
    }

}
