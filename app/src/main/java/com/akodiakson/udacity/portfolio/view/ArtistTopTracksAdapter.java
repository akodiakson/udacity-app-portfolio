package com.akodiakson.udacity.portfolio.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(final TopTrackViewHolder holder, int position) {
        final Track track = topTracks.get(position);
        holder.trackName.setText(track.name);
        holder.albumName.setText(track.album.name);

        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) holder.artThumbnail.getDrawable()).getBitmap(); // Ew!
                Palette palette = Palette.from(bitmap).generate();
                holder.trackName.setTextColor(palette.getDarkVibrantColor(R.color.colorPrimaryText));
                holder.albumName.setTextColor(palette.getDarkMutedColor(R.color.colorPrimaryText));

            }

            @Override
            public void onError() {

            }
        };
        Picasso.with(holder.itemView.getContext())
                .load(track.album.images.get(0).url)
                .into(holder.artThumbnail, callback);

    }

    @Override
    public TopTrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_tracks_track, parent, false);

        return new TopTrackViewHolder(view);

    }

    public static class TopTrackViewHolder extends RecyclerView.ViewHolder {

        public ImageView artThumbnail;
        public TextView trackName;
        public TextView albumName;

        public TopTrackViewHolder(View trackView){
            super(trackView);
            artThumbnail = (ImageView) trackView.findViewById(R.id.trackImage);
            trackName = (TextView) trackView.findViewById(R.id.trackName);
            albumName = (TextView) trackView.findViewById(R.id.trackAlbum);
        }
    }
}