package com.akodiakson.udacity.portfolio.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.activity.ArtistSearchActivity;
import com.akodiakson.udacity.portfolio.application.BusProvider;
import com.akodiakson.udacity.portfolio.model.SpotifyArtistModel;
import com.akodiakson.udacity.portfolio.network.ArtistSearchTask;
import com.akodiakson.udacity.portfolio.service.SpotifyPlayerService;
import com.akodiakson.udacity.portfolio.util.KeyboardUtil;
import com.akodiakson.udacity.portfolio.util.NetworkUtil;
import com.akodiakson.udacity.portfolio.util.ServiceStatusUtil;
import com.akodiakson.udacity.portfolio.util.StringUtil;
import com.akodiakson.udacity.portfolio.view.ArtistSearchResultAdapter;
import com.squareup.otto.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistSearchFragment extends Fragment implements ArtistSearchTaskResultListener {

    private RecyclerView.Adapter adapter;
    private List<Artist> artists = new ArrayList<>();

    private View fragmentView;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onArtistSelected(SpotifyArtistModel spotifyArtistModel);
    }

    public ArtistSearchFragment() {
    }

    @Override
    public void onDestroy() {
        System.out.println("asdf --> ASF DESTROY");
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.artist_search_recycler_view);

        adapter = new ArtistSearchResultAdapter((ArtistSearchActivity) getActivity(), artists);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ServiceStatusUtil.isMyServiceRunning(new WeakReference<Context>(getActivity()))){
            Intent intent = new Intent(getActivity(), SpotifyPlayerService.class);
            intent.setAction(SpotifyPlayerService.ACTION_RESTORE_NOW_PLAYING);
            getActivity().startService(intent);
            return true;
        } else {
            Snackbar
                    .make(getView().findViewById(R.id.artist_search_recycler_view), "Nothing playing yet", Snackbar.LENGTH_LONG)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onRestorePlayingViewEvent(SpotifyPlayerService.RestorePlayingViewEvent event){
        ((ArtistSearchActivity)getActivity()).onArtistTrackSelectedForPlayback(event.getmTrack(), (ArrayList)event.getmTopTracks());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_playback, menu);
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
        Pager<Artist> pager = results.artists;
        List<Artist> artistsResult = pager.items;
        if (artistsResult == null || artistsResult.isEmpty()) {
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
        if (NetworkUtil.isNetworkAvailable(new WeakReference<Context>(getActivity()))) {
            new ArtistSearchTask(this).execute(text.toString());
        } else {
            Snackbar
                    .make(fragmentView.findViewById(R.id.toolbar), getString(R.string.error_no_connectivity), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action_try_again), new RetrySearchForConnectivityIssueListener())
                    .show();
        }
    }

    //For cases where a network connection was unavailble, keep the search term, just try to search again
    private class RetrySearchForConnectivityIssueListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            searchForArtistByInput(((EditText) fragmentView.findViewById(R.id.artistSearchEditText)).getText().toString());
        }
    }

    //For cases where there was invalid input or no search results for that input.
    private class RetryEntryTapListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView searchTextEdit = (TextView) fragmentView.findViewById(R.id.artistSearchEditText);
            KeyboardUtil.showKeyboard(new WeakReference<Context>(getActivity()), searchTextEdit);
            searchTextEdit.setText(null);
        }
    }

    @Subscribe
    public void onIsATrackPlayingRightNowResult(SpotifyPlayerService.IsPlayingStatusEvent event){
        Toast.makeText(getActivity(), "isPlaying?" + event.isPlaying(), Toast.LENGTH_SHORT).show();

        if(event.isPlaying()){
            getActivity().supportInvalidateOptionsMenu();
            Toast.makeText(getActivity(), "isPlaying?" + event.isPlaying(), Toast.LENGTH_SHORT).show();
        }
    }
}
