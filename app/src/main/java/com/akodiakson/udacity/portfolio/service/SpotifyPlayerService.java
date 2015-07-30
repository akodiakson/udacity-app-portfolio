package com.akodiakson.udacity.portfolio.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class SpotifyPlayerService extends Service {

    //TODO -- create actions here for play/pause, scrub, next/previous
    public static final String EXTRA_TRACK_URL = "EXTRA_TRACK_URL";

    private MediaPlayer mMediaPlayer;
    private boolean mIsCurrentlyPlaying = false;
    private boolean mIsPaused = false;
    private String mCurrentlyPlayingUrl;

    public SpotifyPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String url = intent.getStringExtra(EXTRA_TRACK_URL);
        System.out.println("SpotifyPlayerService->onStartCommand" + " " + url);

        mMediaPlayer = getMediaPlayer();

        if(mCurrentlyPlayingUrl == null || url.equals(mCurrentlyPlayingUrl)){
            if (!mIsCurrentlyPlaying && !mIsPaused) {
                startPlayback(url);
            }
            else if (mIsPaused) {
                mMediaPlayer.start();
                markAsPlaying();
            } else {
                pausePlayback();
            }
        } else {
            stopPlayback();
            startPlayback(url);
        }

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
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start(); //when you are prepared, then start the stream
                    markAsPlaying();
                    mCurrentlyPlayingUrl = url;
                }
            });
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        } catch (IOException e) {
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
        mIsPaused = false;
        mIsCurrentlyPlaying = true;
    }

    private void markAsPaused() {
        mIsCurrentlyPlaying = false;
        mIsPaused = true;
    }
}
