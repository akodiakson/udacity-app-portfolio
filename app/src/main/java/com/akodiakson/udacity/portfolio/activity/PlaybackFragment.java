package com.akodiakson.udacity.portfolio.activity;



import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.model.TrackModel;
import com.akodiakson.udacity.portfolio.service.SpotifyPlayerService;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaybackFragment extends Fragment {

    public static final String EXTRA_SELECTED_SONG = "EXTRA_SELECTED_SONG";
    private TrackModel mTrack;

    private MediaPlayer mMediaPlayer;

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
        mMediaPlayer = new MediaPlayer();
        populateTrackModel();
        setupAlbumArt();
        setupAlbumDetails();
        setupPlaybackControls();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO -- see if this is the best place to release
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    private void populateTrackModel(){
        Bundle arguments = getActivity().getIntent().getExtras();
        TrackModel track = (TrackModel) arguments.getParcelable(EXTRA_SELECTED_SONG);
        this.mTrack = track;
    }

    private void setupAlbumArt(){
        ImageView albumArt = (ImageView) getView().findViewById(R.id.playback_album_cover);
        //TODO -- load image from picasso
        Picasso.with(getActivity())
                .load(mTrack.albumImage)
                .into(albumArt);
    }

    private void setupAlbumDetails(){

        View view = getView();
        TextView songName = (TextView) view.findViewById(R.id.playback_song_name);
        TextView artistName = (TextView) view.findViewById(R.id.playback_artist_name);
        TextView albumName = (TextView) view.findViewById(R.id.playback_track_album_name);

        songName.setText(mTrack.name);
        artistName.setText(mTrack.artistName);
        albumName.setText(mTrack.albumName);

    }

    private void setupPlaybackControls(){
        SeekBar seekBar = (SeekBar) getView().findViewById(R.id.playback_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO -- Here, the've let go, so advance the track x ms
                int millis_to_advance = seekBar.getProgress();
                Intent intent = new Intent(getActivity(), SpotifyPlayerService.class);
                getActivity().startService(intent);
            }
        });

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
                playSelectedTrack();
            }
        };
    }

    private void playSelectedTrack() {
        //TODO -- switch the visible state from|to play|pause

        String url = mTrack.previewUrl; // your URL here
        //TODO -- consider passing all 10 top tracks urls for continuous playback
        Intent intent = new Intent(getActivity(), SpotifyPlayerService.class);
        intent.putExtra(SpotifyPlayerService.EXTRA_TRACK_URL, url);
        getActivity().startService(intent);


//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//        try {
//            mMediaPlayer.setDataSource(url); //this can take awhile and/or throw an exception
//            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.start(); //when you are prepared, then start the stream
//                }
//            });
//            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
//            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//
//                }
//            });
//        } catch (IOException e) {
//            Snackbar
//                    .make(getView().findViewById(R.id.toolbar), ":( no audio", Snackbar.LENGTH_LONG)
//                    .show();
//        }
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
