package com.akodiakson.udacity.portfolio.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.activity.IRestoreNowPlaying;
import com.akodiakson.udacity.portfolio.activity.TopTracksActivity;
import com.akodiakson.udacity.portfolio.application.BusProvider;
import com.akodiakson.udacity.portfolio.model.SpotifyArtistModel;
import com.akodiakson.udacity.portfolio.model.TrackModel;
import com.akodiakson.udacity.portfolio.network.TopTracksTask;
import com.akodiakson.udacity.portfolio.service.SpotifyPlayerService;
import com.akodiakson.udacity.portfolio.util.DimensUtil;
import com.akodiakson.udacity.portfolio.util.NetworkUtil;
import com.akodiakson.udacity.portfolio.util.ServiceStatusUtil;
import com.akodiakson.udacity.portfolio.util.StringUtil;
import com.akodiakson.udacity.portfolio.view.ArtistTopTracksAdapter;
import com.akodiakson.udacity.portfolio.view.CircularOutlineProvider;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class TopTracksFragment extends Fragment implements OnTopTracksResultListener {

    public static final String EXTRA_SPOTIFY_ARTIST = "EXTRA_SPOTIFY_ARTIST";
    private RecyclerView.Adapter adapter;

    private List<Track> topTracks = new ArrayList<>();

    private Toolbar toolbar;

    private SpotifyArtistModel spotifyArtistModel;

    //TODO -- this should pass song-related info
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onArtistTrackSelectedForPlayback(TrackModel spotifyArtistModel, ArrayList<TrackModel> topTracks);
    }

    public TopTracksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO -- Show empty state view on tablet?
        if(savedInstanceState != null && savedInstanceState.containsKey(EXTRA_SPOTIFY_ARTIST)){
            spotifyArtistModel = savedInstanceState.getParcelable(EXTRA_SPOTIFY_ARTIST);
        }

        return inflater.inflate(R.layout.fragment_top_tracks, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.action_now_playing) == null)
        inflater.inflate(R.menu.menu_playback, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ServiceStatusUtil.isMyServiceRunning(new WeakReference<Context>(getActivity()))){
            Intent intent = new Intent(getActivity(), SpotifyPlayerService.class);
            intent.setAction(SpotifyPlayerService.ACTION_RESTORE_NOW_PLAYING);
            getActivity().startService(intent);

        } else {
            Snackbar
                    .make(getView().findViewById(R.id.artist_top_tracks_recycler_view), "Nothing playing yet", Snackbar.LENGTH_LONG)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

        Bundle arguments = getActivity().getIntent().getExtras();
        if(spotifyArtistModel == null){
            if (arguments != null) {
                //you came from a single-pane view, via activity
                spotifyArtistModel = arguments.getParcelable(EXTRA_SPOTIFY_ARTIST);
            } else {
                //you came from a two-pane view, via fragment transaction
                spotifyArtistModel = getArguments().getParcelable(EXTRA_SPOTIFY_ARTIST);
            }
        }
        setupToolbar();
        setupTopTracksList();
        showArtistImage();
        if (topTracks.isEmpty()) {
            searchArtistTopTracks();
        }

    }





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_SPOTIFY_ARTIST, spotifyArtistModel);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        android.app.ActionBar supportActionBar = getActivity().getActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(spotifyArtistModel.artistName);
        }
    }

    private void setupTopTracksList() {
        RecyclerView topTracksView = (RecyclerView) getView().findViewById(R.id.artist_top_tracks_recycler_view);
        topTracksView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ArtistTopTracksAdapter((TopTracksFragment.Callbacks)getActivity(), topTracks);
        topTracksView.setAdapter(adapter);
    }

    private void searchArtistTopTracks() {
        if (NetworkUtil.isNetworkAvailable(new WeakReference<Context>(getActivity()))) {
            new TopTracksTask(this).execute(spotifyArtistModel.artistId);
        } else {
            Snackbar
                    .make(getView().findViewById(R.id.toolbar), getString(R.string.error_no_connectivity), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void showArtistImage() {
        int imageDimension = spotifyArtistModel.imageDimension;
        if (StringUtil.isEmpty(spotifyArtistModel.image)
                || DimensUtil.isInvalidImageDimensPair(imageDimension, imageDimension)) {
            // Hide the artist image if there aren't any images.
            getView().findViewById(R.id.artistImageTopTracks).setVisibility(View.GONE);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryText));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setTitle(spotifyArtistModel.artistName);
            return;
        }

        final ImageView imageView = (ImageView) getView().findViewById(R.id.artistImageTopTracks);
        imageView.setClipToOutline(true);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorDividerColor));
        imageView.setOutlineProvider(new CircularOutlineProvider(true));
        Callback callback = new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                //Source: http://jakewharton.com/coercing-picasso-to-play-with-palette/
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap(); // Ew!
                Palette palette = Palette.from(bitmap).generate();
                toolbar.setBackgroundColor(palette.getVibrantColor(R.color.colorPrimaryText));
                toolbar.setTitleTextColor(palette.getLightVibrantColor(R.color.white));
                toolbar.setTitle(spotifyArtistModel.artistName);
            }
        };
        Picasso.with(getActivity())
                .load(spotifyArtistModel.image)
                .placeholder(R.drawable.ic_music_note_white_24dp)
                .centerCrop()
                .resize(imageDimension, imageDimension)
                .into(imageView, callback);

    }

    @Override
    public void onTracksObtained(Tracks tracks) {
        List<Track> tracksList = tracks.tracks;
        if (tracksList == null || tracksList.isEmpty()) {
            Snackbar
                    .make(getView().findViewById(R.id.toolbar), getString(R.string.error_no_top_tracks), Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
        topTracks.addAll(tracksList);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onRestorePlayerEvent(SpotifyPlayerService.RestorePlayingViewEvent event){
        ((IRestoreNowPlaying)getActivity()).onArtistTrackSelectedForPlayback(event.getmTrack(), (ArrayList) event.getmTopTracks());
    }

}
