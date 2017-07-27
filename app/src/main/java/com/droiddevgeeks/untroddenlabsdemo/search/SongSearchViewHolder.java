package com.droiddevgeeks.untroddenlabsdemo.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droiddevgeeks.untroddenlabsdemo.R;

/**
 * Created by Vampire on 2017-07-27.
 */

public class SongSearchViewHolder extends RecyclerView.ViewHolder
{
    public ImageView thumb;
    public TextView trackName;
    public TextView artistName;
    public TextView genre;
    public TextView duration;
    public TextView priceValue;
    public TextView collectionName;


    public SongSearchViewHolder(View itemView)
    {
        super(itemView);
        trackName = (TextView) itemView.findViewById(R.id.trackName);
        artistName = (TextView) itemView.findViewById(R.id.artistName);
        genre = (TextView) itemView.findViewById(R.id.genre);
        duration = (TextView) itemView.findViewById(R.id.duration);
        priceValue = (TextView) itemView.findViewById(R.id.priceValue);
        collectionName = (TextView) itemView.findViewById(R.id.collectionName);
        thumb = (ImageView) itemView.findViewById(R.id.imgThumb);

    }
}
