package com.example.android.myapplication.activity;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.example.android.myapplication.R;

/*
Based on this example: https://github.com/googlesamples/android-topeka/blob/master/app/src/main/java/com/google/samples/apps/topeka/widget/outlineprovider/RoundOutlineProvider.java#L9
 */
public class CircularOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        final int size = view.getResources().getDimensionPixelSize(R.dimen.artist_search_result_image_avatar_dimen);
        outline.setOval(0, 0, size, size);

    }
}
