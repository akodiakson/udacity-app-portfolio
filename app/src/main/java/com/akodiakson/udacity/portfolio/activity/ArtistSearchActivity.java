package com.akodiakson.udacity.portfolio.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.akodiakson.udacity.portfolio.view.ArtistSearchResultAdapter;
import com.akodiakson.udacity.portfolio.network.ArtistSearchTask;
import com.akodiakson.udacity.portfolio.util.KeyboardUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistSearchActivity extends AppCompatActivity implements ArtistSearchTaskResultListener {

    private static final String KEY_ARTIST_SEARCH_RESULTS = "KEY_ARTIST_SEARCH_RESULTS";
    private RecyclerView.Adapter adapter;
    private List<Artist> artists = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Parcelable listState;

    private static final String KEY_ARTIST_SEARCH_TERM = "KEY_ARTIST_SEARCH_TERM";
    private String artistSearchTerm;
    private EditText artistSearchTermView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        setupSearchView();

        adapter = new ArtistSearchResultAdapter(artists);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.artist_search_recycler_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        artistSearchTermView = (EditText) findViewById(R.id.artistSearchEditText);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(listState != null){
//            layoutManager.onRestoreInstanceState(listState);
//        }
//        if(artistSearchTerm != null){
//            artistSearchTermView.setText(artistSearchTerm);
//        }
//    }

    private void setupSearchView() {
        EditText searchInput = (EditText) findViewById(R.id.artistSearchEditText);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //when the "go" action is pressed on the keyboard...
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    //clear any current search results
                    artists.clear();
                    adapter.notifyDataSetChanged();

                    //ask Spotify for the answer
                    searchForArtistByInput(v.getText());

                    //TODO -- show on-screen visual to indicate search progress

                    //...hide the keyboard
                    KeyboardUtil.hideKeyboard(new WeakReference<Context>(ArtistSearchActivity.this), v);

                    return true;
                }
                return false;
            }
        });
    }

    private void searchForArtistByInput(@NonNull CharSequence text) {
        new ArtistSearchTask(this).execute(text.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        artistSearchTerm = savedInstanceState.get(KEY_ARTIST_SEARCH_TERM).toString();
        listState = savedInstanceState.getParcelable(KEY_ARTIST_SEARCH_RESULTS);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        listState = layoutManager.onSaveInstanceState();
        artistSearchTerm = artistSearchTermView.getText().toString();
        outState.putString(KEY_ARTIST_SEARCH_TERM, artistSearchTerm);
        outState.putParcelable(KEY_ARTIST_SEARCH_RESULTS, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void handleArtistSearchTaskResults(ArtistsPager results) {
        Pager<Artist> pager =  results.artists;
        List<Artist> artistsResult = pager.items;
        if(artistsResult == null || artistsResult.isEmpty()){
            Snackbar
                    .make(artistSearchTermView, getString(R.string.error_no_artists_found), Snackbar.LENGTH_LONG)
                    .setAction("Try again", new RetryTapListener())
                    .show(); // Don�t forget to show!
        } else {
            artists.addAll(artistsResult);
            adapter.notifyDataSetChanged();
        }
    }

    private class RetryTapListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            KeyboardUtil.showKeyboard(new WeakReference<Context>(ArtistSearchActivity.this), artistSearchTermView);
            artistSearchTermView.setText("");
        }
    }
}
