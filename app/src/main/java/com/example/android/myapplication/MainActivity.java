package com.example.android.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View.OnClickListener appButtonClickListener = getAppButtonClickListener();
        findViewById(R.id.spotifyStreamer).setOnClickListener(appButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private View.OnClickListener getAppButtonClickListener() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String appName = getAppName(v.getId());
                showToastForApp(appName);
            }
        };
    }

    /*
    Build and show the Toast message.
     */
    private void showToastForApp(String appName){
        final String EXCLAMATION = "!";
        StringBuilder message = new StringBuilder();
        message.append(getString(R.string.button_selected_message_prefix));
        message.append(appName);
        message.append(EXCLAMATION);
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
    }

    /*
    Derive app name from the button
     */
    private String getAppName(int buttonId) {
        String appName = null;
        switch(buttonId){
            case R.id.spotifyStreamer:
                appName = getString(R.string.spotify_streamer);
                break;
            case R.id.scoresApp:
                appName = getString(R.string.scores_app);
                break;
            case R.id.libraryApp:
                appName = getString(R.string.library_app);
                break;
            case R.id.builtItBigger:
                appName = getString(R.string.build_it_bigger);
                break;
            case R.id.xyz_reader:
                appName = getString(R.string.xyz_reader);
                break;
            case R.id.capstone:
                appName = getString(R.string.capstone);
                break;
        }
        return appName;
    }
}
