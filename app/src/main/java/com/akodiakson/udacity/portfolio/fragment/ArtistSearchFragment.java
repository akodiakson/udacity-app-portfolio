package com.akodiakson.udacity.portfolio.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.akodiakson.udacity.portfolio.network.ArtistSearchTask;
import com.akodiakson.udacity.portfolio.util.KeyboardUtil;
import com.akodiakson.udacity.portfolio.view.ArtistSearchResultAdapter;
import com.example.android.myapplication.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistSearchFragment extends Fragment implements ArtistSearchTaskResultListener {

    public static final int GRIDVIEW_SPAN_COUNT = 2;
    private RecyclerView.Adapter adapter;
    private List<Artist> artists = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private View fragmentView;

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_artist_search, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.artist_search_recycler_view);

        layoutManager = new GridLayoutManager(getActivity(), GRIDVIEW_SPAN_COUNT);
        adapter = new ArtistSearchResultAdapter(artists);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSearchView();
    }

    private void setupSearchView() {
        EditText searchInput = (EditText) fragmentView.findViewById(R.id.artistSearchEditText);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView artistSearchEditText, int actionId, KeyEvent event) {
                //when the "go" action is pressed on the keyboard...
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    //clear any current search results
                    artists.clear();
                    adapter.notifyDataSetChanged();

                    //ask Spotify for the answer
                    searchForArtistByInput(artistSearchEditText.getText());

                    //TODO -- show on-screen visual to indicate search progress

                    //...hide the keyboard
                    KeyboardUtil.hideKeyboard(new WeakReference<Context>(getActivity()), artistSearchEditText);

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
        Pager<Artist> pager =  results.artists;
        List<Artist> artistsResult = pager.items;
        if(artistsResult == null || artistsResult.isEmpty()){
            Snackbar
                    .make(fragmentView, getString(R.string.error_no_artists_found), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action_try_again), new RetryTapListener())
                    .show();
        } else {
            artists.addAll(artistsResult);
            adapter.notifyDataSetChanged();
        }
    }

    private class RetryTapListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            TextView searchTextEdit = (TextView) fragmentView.findViewById(R.id.artistSearchEditText);
            KeyboardUtil.showKeyboard(new WeakReference<Context>(getActivity()), searchTextEdit);
            searchTextEdit.setText("");
        }
    }
}
