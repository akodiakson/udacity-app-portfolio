package com.example.android.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistSearchActivity extends AppCompatActivity implements ArtistSearchTaskResultListener {

    private RecyclerView.Adapter adapter;
    private List<Artist> artists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        setupSearchView();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.artist_search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtistSearchResultAdapter(artists);
        recyclerView.setAdapter(adapter);
    }

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
    public void handleArtistSearchTaskResults(ArtistsPager results) {
        //TODO -- handle the negative case
        Pager<Artist> pager =  results.artists;
        List<Artist> artistsResult = pager.items;
        artists.addAll(artistsResult);
        adapter.notifyDataSetChanged();
    }
}
