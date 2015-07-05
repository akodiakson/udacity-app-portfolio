package com.akodiakson.udacity.portfolio.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akodiakson.udacity.portfolio.R;
import com.akodiakson.udacity.portfolio.activity.ArtistTopTracksActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

public class ArtistSearchResultAdapter extends RecyclerView.Adapter<ArtistSearchResultAdapter.ArtistViewHolder> {

    private List<Artist> artistSearchResultList;

    public ArtistSearchResultAdapter(List<Artist> artistSearchResultList) {
        this.artistSearchResultList = artistSearchResultList;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_search_result, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, int position) {
        final Artist artist = artistSearchResultList.get(position);
        holder.artistName.setText(artist.name);

        List<Image> images = artist.images;
        Context context = holder.itemView.getContext();

        final Image image;

        if(!images.isEmpty()){
            Image firstImage = images.get(0);
            image = firstImage;
            Picasso.with(context)
                    .load(firstImage.url)
                    .resize(firstImage.height, firstImage.height)
                    .placeholder(R.drawable.ic_music_note_white_24dp)
                    .centerCrop()
                    .into(holder.artistImage);
        } else {
            holder.artistImage.setColorFilter(0);
            holder.artistImage.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_music_note_white_24dp, null));
            image = new Image();
        }

        holder.artistImage.setBackgroundColor(context.getResources().getColor(R.color.colorLightPrimary));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent artistTopTracksIntent = new Intent(context, ArtistTopTracksActivity.class);
                artistTopTracksIntent.putExtra(ArtistTopTracksActivity.EXTRA_ARTIST_ID, artist.id);
                artistTopTracksIntent.putExtra(ArtistTopTracksActivity.EXTRA_ARTIST_NAME, artist.name);
                artistTopTracksIntent.putExtra(ArtistTopTracksActivity.EXTRA_ARTIST_IMAGE_URL, image.url);
                artistTopTracksIntent.putExtra(ArtistTopTracksActivity.EXTRA_ARTIST_IMAGE_RESIZE_WIDTH, image.height);
                context.startActivity(artistTopTracksIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistSearchResultList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {

        public ImageView artistImage;
        public TextView artistName;

        public ArtistViewHolder(View artistView) {
            super(artistView);
            artistImage = (ImageView) artistView.findViewById(R.id.artistImage);
            artistName = (TextView) artistView.findViewById(R.id.artistName);
            artistImage.setClipToOutline(true);
            artistImage.setOutlineProvider(new CircularOutlineProvider(false));
        }
    }
}
