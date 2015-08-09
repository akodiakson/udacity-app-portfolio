package com.akodiakson.udacity.portfolio.activity;


import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.application.BusProvider;
import com.akodiakson.udacity.portfolio.model.TrackModel;
import com.akodiakson.udacity.portfolio.service.SpotifyPlayerService;
import com.akodiakson.udacity.portfolio.util.BlurTransformation;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlaybackFragment extends DialogFragment {

    public static final String EXTRA_SELECTED_SONG = "EXTRA_SELECTED_SONG";
    public static final String EXTRA_TOP_TRACKS = "EXTRA_TOP_TRACKS";

    private TrackModel mTrack;
    private List<TrackModel> mTopTracks;

    public PlaybackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playback, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getActionBar().hide();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        populateTrackModel();
        setupAlbumArt();
        setupAlbumDetails();
        setupPlaybackControls();

        playSelectedTrack();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onPlayerPlaying(SpotifyPlayerService.PlayerPlayingEvent event) {
        ImageView playPause = (ImageView) getView().findViewById(R.id.player_play_pause);
        playPause.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause, null));
    }

    @Subscribe
    public void onPlayerPaused(SpotifyPlayerService.PlayerPausedEvent event) {
        ImageView playPause = (ImageView) getView().findViewById(R.id.player_play_pause);
        playPause.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play, null));
    }

    @Subscribe
    public void onSeekBarAdvance(SpotifyPlayerService.AdvanceSeekBarEvent event) {
        int millisToAdvance = event.getMillisToAdvance();
        SeekBar seekBar = (SeekBar) getView().findViewById(R.id.playback_seek_bar);
        seekBar.setProgress(millisToAdvance / 1000);
    }

    @Subscribe
    public void onSongCompleted(SpotifyPlayerService.SongCompletedEvent event) {
        advanceToNextTrack();
    }

    private void populateTrackModel() {
        Bundle arguments = getActivity().getIntent().getExtras();
        TrackModel track = arguments.getParcelable(EXTRA_SELECTED_SONG);
        List<TrackModel> topTracks = arguments.getParcelableArrayList(EXTRA_TOP_TRACKS);

        this.mTrack = track;
        this.mTopTracks = topTracks;
    }

    private void advanceToNextTrack() {
        int currentTrackPosition = getCurrentTrackPosition();
        int nextPosition = (currentTrackPosition == mTopTracks.size() - 1) ? 0 : currentTrackPosition + 1;
        TrackModel nextTrack = mTopTracks.get(nextPosition);
        mTrack = nextTrack;
        //Update the UI for the next track
        setupAlbumArt();
        setupAlbumDetails();

        //start playing the next track
        playSelectedTrack();
    }

    private void goBackToPreviousTrack() {
        int currentTrackPosition = getCurrentTrackPosition();
        int nextPosition = (currentTrackPosition == 0) ? mTopTracks.size() - 1 : currentTrackPosition - 1;
        TrackModel nextTrack = mTopTracks.get(nextPosition);
        mTrack = nextTrack;
        //Update the UI for the next track
        setupAlbumArt();
        setupAlbumDetails();

        //start playing the next track
        playSelectedTrack();
    }

    private int getCurrentTrackPosition() {
        for (int i = 0; i < mTopTracks.size(); i++) {
            if (mTrack.previewUrl.equals(mTopTracks.get(i).previewUrl)) {
                return i;
            }
        }
        return -1;
    }

    private void setupAlbumArt() {
        final ImageView albumArt = (ImageView) getView().findViewById(R.id.playback_album_cover);
        //TODO -- load image from picasso
        Picasso.with(getActivity())
                .load(mTrack.albumImage)
                .into(albumArt, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        //Source : http://jakewharton.com/coercing-picasso-to-play-with-palette/
                        Bitmap bitmap = ((BitmapDrawable) albumArt.getDrawable()).getBitmap(); // Ew!
                        Palette palette = Palette.from(bitmap).generate();
                        final int vibrantColor = palette.getVibrantColor(R.color.colorAccent);
                        final SeekBar seekBar = (SeekBar) getView().findViewById(R.id.playback_seek_bar);
                        seekBar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                seekBar.getThumb().setColorFilter(vibrantColor, PorterDuff.Mode.MULTIPLY);
                            }
                        }, 200);
                        // TODO apply palette to text views, backgrounds, etc.
                    }
                });

        ImageView background = (ImageView) getView().findViewById(R.id.playback_background);
        Picasso.with(getActivity())
                .load(mTrack.albumImage)
                .transform(new BlurTransformation(getActivity()))
                .fit()
                .into(background);
    }

    private void setupAlbumDetails() {

        View view = getView();
        TextView songName = (TextView) view.findViewById(R.id.playback_song_name);
        TextView artistName = (TextView) view.findViewById(R.id.playback_artist_name);
        TextView albumName = (TextView) view.findViewById(R.id.playback_track_album_name);
        TextView duration = (TextView) view.findViewById(R.id.playback_track_duration);
        DateFormat df = new SimpleDateFormat("m:ss");
        String formatted = df.format(mTrack.duration);

        songName.setText(mTrack.name);
        artistName.setText(mTrack.artistName);
        albumName.setText(mTrack.albumName);
        duration.setText(formatted);

    }

    private void setupPlaybackControls() {
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
                //TODO -- Here, they've let go, so advance the track x ms
                int millis_to_advance = seekBar.getProgress() * 1000;
                Intent intent = new Intent(getActivity(), SpotifyPlayerService.class);
                intent.setAction(SpotifyPlayerService.ACTION_SEEK);
                intent.putExtra(SpotifyPlayerService.EXTRA_MILLIS_TO_SEEK, millis_to_advance);
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
        String url = mTrack.previewUrl; // your URL here
        Intent intent = new Intent(getActivity(), SpotifyPlayerService.class);
        intent.putExtra(SpotifyPlayerService.EXTRA_TRACK_URL, url);
        getActivity().startService(intent);
    }

    private View.OnClickListener onPreviousTapped() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToPreviousTrack();
            }
        };
    }

    private View.OnClickListener onNextTapped() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceToNextTrack();
            }
        };
    }
}
