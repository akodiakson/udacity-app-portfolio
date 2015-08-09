package com.akodiakson.udacity.portfolio.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akodiakson.udacity.portfolio.R;

import com.akodiakson.udacity.portfolio.fragment.ArtistSearchFragment;
import com.akodiakson.udacity.portfolio.model.SpotifyArtistModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

public class ArtistSearchResultAdapter extends RecyclerView.Adapter<ArtistSearchResultAdapter.ArtistViewHolder> {

    private List<Artist> artistSearchResultList;
    private ArtistSearchFragment.Callbacks callbacks;

    private int currentlySelectedPosition = -1;

    public ArtistSearchResultAdapter(ArtistSearchFragment.Callbacks callbacks, List<Artist> artistSearchResultList) {
        this.callbacks = callbacks;
        this.artistSearchResultList = artistSearchResultList;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_search_result, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, final int position) {
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
        }

        holder.artistImage.setBackgroundColor(context.getResources().getColor(R.color.colorLightPrimary));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setTag("selected");
                SpotifyArtistModel parcelable = new SpotifyArtistModel(artist);
                callbacks.onArtistSelected(parcelable);
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
        public View currentlySelectedIndicator;

        public ArtistViewHolder(View artistView) {
            super(artistView);
            artistImage = (ImageView) artistView.findViewById(R.id.artistImage);
            artistName = (TextView) artistView.findViewById(R.id.artistName);
            artistImage.setClipToOutline(true);
            artistImage.setOutlineProvider(new CircularOutlineProvider(false));
            currentlySelectedIndicator = artistView.findViewById(R.id.currently_selected_artist_indicator);
        }
    }
}
