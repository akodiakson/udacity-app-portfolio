package com.example.android.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.squareup.picasso.Picasso;

public class ArtistTopTracksActivity extends AppCompatActivity {

    public static final String EXTRA_ARTIST_ID = "EXTRA_ARTIST_ID";
    public static final String EXTRA_ARTIST_NAME = "EXTRA_ARTIST_NAME";
    public static final String EXTRA_ARTIST_IMAGE_URL = "EXTRA_ARTIST_IMAGE_URL";
    public static final String EXTRA_ARTIST_IMAGE_RESIZE_WIDTH = "EXTRA_ARTIST_IMAGE_RESIZE_WIDTH";
    public static final String EXTRA_ARTIST_IMAGE_RESIZE_HEIGHT = "EXTRA_ARTIST_IMAGE_RESIZE_HEIGHT";


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


        TextView artistNameView = (TextView) findViewById(R.id.artistNameTopTracks);
        artistNameView.setText(artistName);

        showArtistImage(artistImageURL, artistImageResizeWidth, artistImageResizeHeight);
    }

    private void showArtistImage(String artistImageURL, int artistImageResizeWidth, int intartistImageResizeHeight ) {
        final ImageView imageView = (ImageView) findViewById(R.id.artistImageTopTracks);
        imageView.setClipToOutline(true);
        imageView.setBackgroundColor(getResources().getColor(R.color.black));
        imageView.setOutlineProvider(new CircularOutlineProvider(true));
        Picasso.with(this)
                .load(artistImageURL)
                .centerCrop()
                .resize(artistImageResizeWidth, intartistImageResizeHeight)
                .into(imageView);

    }
}
