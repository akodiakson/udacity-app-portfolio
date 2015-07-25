package com.akodiakson.udacity.portfolio.activity;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akodiakson.udacity.portfolio.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaybackFragment extends Fragment {

    public PlaybackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playback, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAlbumArt();
        setupAlbumDetails();
        setupPlaybackControls();
    }

    private void setupAlbumArt(){
        ImageView albumArt = (ImageView) getView().findViewById(R.id.playback_album_cover);
        //TODO -- load image from picasso

    }

    private void setupAlbumDetails(){

        View view = getView();
        TextView songName = (TextView) view.findViewById(R.id.playback_song_name);
        TextView artistName = (TextView) view.findViewById(R.id.playback_artist_name);
        TextView albumName = (TextView) view.findViewById(R.id.playback_track_album_name);

        songName.setText("Atlas");
        artistName.setText("Battles");
        albumName.setText("Mirrored");

    }

    private void setupPlaybackControls(){
        View controlsContainer = getView().findViewById(R.id.playback_controls_container);
        ImageView previous = (ImageView) controlsContainer.findViewById(R.id.player_previous);
        ImageView playPause = (ImageView) controlsContainer.findViewById(R.id.player_play_pause);
        ImageView next = (ImageView) controlsContainer.findViewById(R.id.player_next);

        previous.setOnClickListener(onPreviousTapped());
        playPause.setOnClickListener(onPlayPauseTapped());
        next.setOnClickListener(onNextTapped());
    }

    private View.OnClickListener onPlayPauseTapped() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO -- switch the visible state from|to play|pause
                //TODO -- start|pause playback
            }
        };
    }

    private View.OnClickListener onPreviousTapped() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private View.OnClickListener onNextTapped() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
