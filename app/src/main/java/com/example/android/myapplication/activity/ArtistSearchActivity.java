package com.example.android.myapplication.activity;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

public class ArtistSearchActivity extends AppCompatActivity implements ArtistSearchTaskResultListener {

    private RecyclerView.Adapter adapter;
    private List<Artist> artists = new ArrayList<>();
    private RecyclerView recyclerView;

    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        searchInput = (EditText) findViewById(R.id.artistSearchEditText);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){

                    //clear any current search results
                    artists.clear();
                    adapter.notifyDataSetChanged();

                    //TODO -- show on-screen visual to indicate search progress
                    searchForArtistByInput(v.getText());

                    //hide the keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.artist_search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtistSearchResultAdapter(this, artists);
        recyclerView.setAdapter(adapter);
    }

    private void searchForArtistByInput(@NonNull CharSequence text) {
        new ArtistSearchTask(this).execute(text.toString());
    }

    private class ArtistSearchTask extends AsyncTask<String, Void, ArtistsPager>{

        private ArtistSearchTaskResultListener artistSearchTaskResultListener;

        public ArtistSearchTask(ArtistSearchTaskResultListener artistSearchTaskResultListener){
            this.artistSearchTaskResultListener = artistSearchTaskResultListener;
        }

        @Override
        protected ArtistsPager doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results;
        }

        @Override
        protected void onPostExecute(ArtistsPager results) {
            artistSearchTaskResultListener.handleArtistSearchTaskResults(results);
        }
    }

    @Override
    public void handleArtistSearchTaskResults(ArtistsPager results) {
        Toast.makeText(this, results.artists.total + " results", Toast.LENGTH_SHORT).show();
        Pager<Artist> pager =  results.artists;
        List<Artist> artistsResult = pager.items;
        artists.addAll(artistsResult);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
