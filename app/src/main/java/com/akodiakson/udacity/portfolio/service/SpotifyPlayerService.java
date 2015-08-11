package com.akodiakson.udacity.portfolio.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

import com.akodiakson.udacity.portfolio.PortfolioApplication;
import com.akodiakson.udacity.portfolio.fragment.PlaybackFragment;
import com.akodiakson.udacity.portfolio.application.BusProvider;
import com.akodiakson.udacity.portfolio.model.TrackModel;

import java.io.IOException;
import java.util.List;

public class SpotifyPlayerService extends Service {

    //TODO -- create actions here for play/pause, scrub, next/previous
    public static final String EXTRA_TRACK_URL = "EXTRA_TRACK_URL";
    public static final String ACTION_SEEK = "ACTION_SEEK";
    private static final String ACTION_CHECK_IF_PLAYING = "ACTION_CHECK_IF_PLAYING";

    public static final String EXTRA_MILLIS_TO_SEEK = "EXTRA_MILLIS_TO_SEEK";
    public static final String ACTION_RESTORE_NOW_PLAYING = "ACTION_RESTORE_NOW_PLAYING";
    public static final String EXTRA_DIRECTIVE = "EXTRA_DIRECTIVE";

    private MediaPlayer mMediaPlayer;
    private boolean mIsCurrentlyPlaying = false;
    private boolean mIsPaused = false;
    private String mCurrentlyPlayingUrl;

    private Handler handler = new Handler();

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                BusProvider.getInstance().post(new AdvanceSeekBarEvent(mMediaPlayer.getCurrentPosition()));
            }
            handler.postDelayed(run, 100);
        }
    };

    public SpotifyPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return super.onStartCommand(null, flags, startId);
        }
        final String url = intent.getStringExtra(EXTRA_TRACK_URL);

        mMediaPlayer = getMediaPlayer();

        if(ACTION_SEEK.equals(intent.getAction())){
            int millisToSeek = intent.getIntExtra(SpotifyPlayerService.EXTRA_MILLIS_TO_SEEK, 0);
            System.out.println("millisToSeek->" + millisToSeek);
            mMediaPlayer.seekTo(millisToSeek);
        }
        else if(ACTION_CHECK_IF_PLAYING.equals(intent.getAction())){
            BusProvider.getInstance().post(new IsPlayingStatusEvent(mMediaPlayer != null && mMediaPlayer.isPlaying()));
            return super.onStartCommand(intent, flags, startId);
        } else if(ACTION_RESTORE_NOW_PLAYING.equals(intent.getAction())){
            PortfolioApplication app = (PortfolioApplication) getApplication();
            BusProvider.getInstance().post(new RestorePlayingViewEvent(app.getCurrentlyPlayingTrack(), app.getTopTracks()));
            return super.onStartCommand(intent, flags, startId);
        }
        else {
            TrackModel track = intent.getExtras().getParcelable(PlaybackFragment.EXTRA_SELECTED_SONG);
            List<TrackModel> topTracks = intent.getExtras().getParcelableArrayList(PlaybackFragment.EXTRA_TOP_TRACKS);

            TrackModel mTrack = track;
            List<TrackModel> mTopTracks = topTracks;

            if (mCurrentlyPlayingUrl == null || url.equals(mCurrentlyPlayingUrl)) {
                if (!mIsCurrentlyPlaying && !mIsPaused) {
                    //Start fresh
                    startPlayback(url);
                } else if (mIsPaused) {
                    //Resume
                    mMediaPlayer.start();
                    markAsPlaying();
                } else {
                    String directive = intent.getStringExtra(EXTRA_DIRECTIVE);
                    if("pause".equals(directive)){
                        pausePlayback();
                    }
                }
            } else {
                stopPlayback();
                startPlayback(url);
            }
        }

        run.run();

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        System.out.println("SpotifyPlayerService->onDestroy");
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onDestroy();
    }

    private void pausePlayback() {
        mMediaPlayer.pause();
        markAsPaused();
    }

    private void stopPlayback() {
        mMediaPlayer.pause();
        mMediaPlayer.release();
        mMediaPlayer = null;
        mMediaPlayer = getMediaPlayer();
    }

    private void startPlayback(final String url) {
        try {

            mMediaPlayer.setDataSource(url); //this can take awhile and/or throw an exception
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    BusProvider.getInstance().post(new SongCompletedEvent());
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start(); //when you are prepared, then start the stream
                    markAsPlaying();
                    mCurrentlyPlayingUrl = url;
                }
            });
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        } catch (IOException | IllegalStateException e) {
            //TODO
        }
    }

    private MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //TODO -- if next track, then play it
                    stopSelf();
                }
            });

        }
        return mMediaPlayer;
    }

    private void markAsPlaying() {
        BusProvider.getInstance().post(new PlayerPlayingEvent());
        mIsPaused = false;
        mIsCurrentlyPlaying = true;
    }

    private void markAsPaused() {
        BusProvider.getInstance().post(new PlayerPausedEvent());
        mIsCurrentlyPlaying = false;
        mIsPaused = true;
    }

    public static final class PlayerPlayingEvent{
        public PlayerPlayingEvent(){}
    }

    public static final class PlayerPausedEvent{
        public PlayerPausedEvent(){}
    }

    public static final class SongCompletedEvent{
        public SongCompletedEvent(){}
    }

    public static final class AdvanceSeekBarEvent{
        private int millisToAdvance;
        public AdvanceSeekBarEvent(int millisToAdvance){
            this.millisToAdvance = millisToAdvance;
        }

        public int getMillisToAdvance() {
            return millisToAdvance;
        }
    }

    public static final class IsPlayingStatusEvent {
        private boolean isPlaying;
        public IsPlayingStatusEvent(boolean isPlaying) {
            this.isPlaying = isPlaying;
        }

        public boolean isPlaying() {
            return isPlaying;
        }
    }

    public static final class RestorePlayingViewEvent{
        private TrackModel mTrack;
        private List<TrackModel> mTopTracks;

        public RestorePlayingViewEvent(TrackModel track, List<TrackModel> topTracks){
            this.mTrack = track;
            this.mTopTracks = topTracks;
        }

        public TrackModel getmTrack() {
            return mTrack;
        }

        public List<TrackModel> getmTopTracks() {
            return mTopTracks;
        }
    }

    //TODO -- An event for next song advanced?
}
