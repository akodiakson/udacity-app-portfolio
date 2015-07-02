package com.example.android.myapplication.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class ArtistTopTracksAdapter extends RecyclerView.Adapter<ArtistTopTracksAdapter.TopTrackViewHolder> {

    private List<Track> topTracks;
    public ArtistTopTracksAdapter(@NonNull List<Track> topTracks) {
        this.topTracks = topTracks;
    }

    @Override
    public int getItemCount() {
        return topTracks.size();
    }

    @Override
    public void onBindViewHolder(TopTrackViewHolder holder, int position) {

    }

    @Override
    public TopTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public static class TopTrackViewHolder extends RecyclerView.ViewHolder {

        public ImageView artThumbnail;
        public TextView trackName;
        public TextView albumName;

        public TopTrackViewHolder(View trackView){
            super(trackView);
        }
    }
}
