package com.akodiakson.udacity.portfolio.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
}
