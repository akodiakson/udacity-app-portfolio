package com.akodiakson.udacity.portfolio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.akodiakson.udacity.portfolio.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAppLaunchListeners();
    }

    private void setupAppLaunchListeners() {
        View.OnClickListener appButtonClickListener = getAppButtonClickListener();
        findViewById(R.id.spotifyStreamer).setOnClickListener(appButtonClickListener);
        findViewById(R.id.scoresApp).setOnClickListener(appButtonClickListener);
        findViewById(R.id.libraryApp).setOnClickListener(appButtonClickListener);
        findViewById(R.id.builtItBigger).setOnClickListener(appButtonClickListener);
        findViewById(R.id.xyz_reader).setOnClickListener(appButtonClickListener);
        findViewById(R.id.capstone).setOnClickListener(appButtonClickListener);
    }

    private View.OnClickListener getAppButtonClickListener() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent appLaunchIntent = getIntent(v.getId());
                if(appLaunchIntent != null){
                    //if not null, then launch the implemented app
                    startActivity(appLaunchIntent);
                } else{
                    //otherwise, just show a Toast
                    String appName = getAppName(v.getId());
                    showToastForApp(appName);
                }
            }
        };
    }

    private Intent getIntent(int id){
        if(id == R.id.spotifyStreamer){
            return new Intent(this, ArtistSearchActivity.class);
        }
        return null;
    }

    /*
    Build and show the Toast message.
     */
    private void showToastForApp(String appName){
        final String EXCLAMATION = "!";
        Toast.makeText(this, getString(R.string.button_selected_message_prefix) + appName + EXCLAMATION, Toast.LENGTH_SHORT).show();
    }

    /*
    Derive app name from the button
     */
    private String getAppName(int buttonId) {
        String appName = null;
        switch(buttonId){
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
