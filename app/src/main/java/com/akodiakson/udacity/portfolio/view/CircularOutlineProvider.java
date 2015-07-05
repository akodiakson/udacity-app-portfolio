package com.akodiakson.udacity.portfolio.view;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.akodiakson.udacity.portfolio.R;


/*
Based on this example: https://github.com/googlesamples/android-topeka/blob/master/app/src/main/java/com/google/samples/apps/topeka/widget/outlineprovider/RoundOutlineProvider.java#L9
 */
public class CircularOutlineProvider extends ViewOutlineProvider {

    private boolean isTracksImage;

    public CircularOutlineProvider(boolean isTracksImage){
        this.isTracksImage = isTracksImage;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        final int size;
        if(isTracksImage){
            size = view.getResources().getDimensionPixelSize(R.dimen.artist_top_tracks_image_dimen);
        }
        else {
            size = view.getResources().getDimensionPixelSize(R.dimen.artist_search_result_image_dimen);
        }
        outline.setOval(0, 0, size, size);
    }
}
