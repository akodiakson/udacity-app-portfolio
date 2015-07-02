package com.example.android.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.myapplication.R;

public class ArtistTopTracksActivity extends AppCompatActivity {

    public static final String EXTRA_ARTIST_ID = "EXTRA_ARTIST_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_top_tracks);
    }
}
