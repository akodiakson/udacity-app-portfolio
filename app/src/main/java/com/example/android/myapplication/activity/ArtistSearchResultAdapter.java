package com.example.android.myapplication.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

public class ArtistSearchResultAdapter extends RecyclerView.Adapter<ArtistSearchResultAdapter.ArtistViewHolder> {

    private Context context;
    private List<Artist> artistSearchResultList;

    public ArtistSearchResultAdapter(Context context, List<Artist> artistSearchResultList) {
        this.context = context;
        this.artistSearchResultList = artistSearchResultList;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_search_result, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, int position) {
        Artist artist = artistSearchResultList.get(position);
        holder.artistName.setText(artist.name);

        List<Image> images = artist.images;
            if(hasImages(images)){
            Image firstImage = images.get(0);
            Picasso.with(context).load(firstImage.url).resize(firstImage.height, firstImage.height).centerCrop().into(holder.artistImage);
        }

        //TODO -- Case for no artist images, show the default image


        holder.artistImage.setBackgroundColor(context.getResources().getColor(R.color.primary_material_dark));
    }

    private boolean hasImages(List<Image> images){
        return images != null && images.size() > 0;
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
            artistImage.setOutlineProvider(new CircularOutlineProvider());
        }
    }
}
