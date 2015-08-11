package com.akodiakson.udacity.portfolio.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

public class SpotifyArtistModel implements Parcelable {
    public String artistName;
    public String artistId;
    public String image;
    public int imageDimension;

    public SpotifyArtistModel(Artist artist){
        this.artistId = artist.id;
        this.artistName = artist.name;
        List<Image> images = artist.images;
        if(images.size() > 0){
            this.image = images.get(0).url;
            this.imageDimension = images.get(0).height;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artistName);
        dest.writeString(this.artistId);
        dest.writeString(this.image);
        dest.writeInt(this.imageDimension);
    }

    protected SpotifyArtistModel(Parcel in) {
        this.artistName = in.readString();
        this.artistId = in.readString();
        this.image = in.readString();
        this.imageDimension = in.readInt();
    }

    public static final Parcelable.Creator<SpotifyArtistModel> CREATOR = new Parcelable.Creator<SpotifyArtistModel>() {
        public SpotifyArtistModel createFromParcel(Parcel source) {
            return new SpotifyArtistModel(source);
        }

        public SpotifyArtistModel[] newArray(int size) {
            return new SpotifyArtistModel[size];
        }
    };
}
