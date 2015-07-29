package com.akodiakson.udacity.portfolio.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackModel implements Parcelable {

    public int duration;
    public String name;
    public String previewUrl;
    public String albumName;
    public String albumImage;
    public String artistName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.duration);
        dest.writeString(this.name);
        dest.writeString(this.previewUrl);
        dest.writeString(this.albumName);
        dest.writeString(this.albumImage);
        dest.writeString(this.artistName);
    }

    public TrackModel() {
    }

    protected TrackModel(Parcel in) {
        this.duration = in.readInt();
        this.name = in.readString();
        this.previewUrl = in.readString();
        this.albumName = in.readString();
        this.albumImage = in.readString();
        this.artistName = in.readString();
    }

    public static final Parcelable.Creator<TrackModel> CREATOR = new Parcelable.Creator<TrackModel>() {
        public TrackModel createFromParcel(Parcel source) {
            return new TrackModel(source);
        }

        public TrackModel[] newArray(int size) {
            return new TrackModel[size];
        }
    };
}
