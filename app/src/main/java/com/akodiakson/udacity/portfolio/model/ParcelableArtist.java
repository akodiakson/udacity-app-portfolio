package com.akodiakson.udacity.portfolio.model;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;

public class ParcelableArtist extends Artist implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
