package com.example.android.myapplication.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ace8810 on 6/29/2015.
 */
public class ArtistSearchResultAdapter extends RecyclerView.Adapter<ArtistSearchResultAdapter.ArtistViewHolder> {

    private List<ArtistSearchResultViewModel> artistSearchResultList;

    public ArtistSearchResultAdapter(List<ArtistSearchResultViewModel> artistSearchResultList) {
        this.artistSearchResultList = artistSearchResultList;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_search_result, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
//        Picasso.load("http://i.imgur.com/DvpvklR.png").into(imageView);
//        holder.artistImage.setImageDrawable(artistSearchResultList.get(position).getArtistImage());
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
        }


    }
}
