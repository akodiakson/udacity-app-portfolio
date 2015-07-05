package com.akodiakson.udacity.portfolio.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.network.ArtistSearchTask;
import com.akodiakson.udacity.portfolio.util.KeyboardUtil;
import com.akodiakson.udacity.portfolio.util.NetworkUtil;
import com.akodiakson.udacity.portfolio.util.StringUtil;
import com.akodiakson.udacity.portfolio.view.ArtistSearchResultAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistSearchFragment extends Fragment implements ArtistSearchTaskResultListener {

    private static final int GRID_VIEW_SPAN_COUNT = 2;
    private RecyclerView.Adapter adapter;
    private List<Artist> artists = new ArrayList<>();

    private View fragmentView;

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        Toolbar toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.artist_search_recycler_view);

        adapter = new ArtistSearchResultAdapter(artists);

        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), GRID_VIEW_SPAN_COUNT);
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

                    String searchTerm = artistSearchEditText.getText().toString();
                    if (StringUtil.isNotEmpty(searchTerm)) {
                        // Ask Spotify for the answer
                        searchForArtistByInput(searchTerm);
                        //...hide the keyboard
                        KeyboardUtil.hideKeyboard(new WeakReference<Context>(getActivity()), artistSearchEditText);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void handleArtistSearchTaskResults(ArtistsPager results) {
        Pager<Artist> pager =  results.artists;
        List<Artist> artistsResult = pager.items;
        if(artistsResult == null || artistsResult.isEmpty()){
            Snackbar
                    .make(fragmentView, getString(R.string.error_no_artists_found), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action_try_again), new RetryEntryTapListener())
                    .show();
        } else {
            artists.addAll(artistsResult);
            adapter.notifyDataSetChanged();
        }
    }

    private void searchForArtistByInput(@NonNull CharSequence text) {
        if(NetworkUtil.isNetworkAvailable(new WeakReference<Context>(getActivity()))){
            new ArtistSearchTask(this).execute(text.toString());
        } else {
            Snackbar
                    .make(fragmentView.findViewById(R.id.toolbar), getString(R.string.error_no_connectivity), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action_try_again), new RetrySearchForConnectivityIssueListener())
                    .show();
        }
    }

    //For cases where a network connection was unavailble, keep the search term, just try to search again
    private class RetrySearchForConnectivityIssueListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            searchForArtistByInput(((EditText) fragmentView.findViewById(R.id.artistSearchEditText)).getText().toString());
        }
    }

    //For cases where there was invalid input or no search results for that input.
    private class RetryEntryTapListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            TextView searchTextEdit = (TextView) fragmentView.findViewById(R.id.artistSearchEditText);
            KeyboardUtil.showKeyboard(new WeakReference<Context>(getActivity()), searchTextEdit);
            searchTextEdit.setText(null);
        }
    }
}
