package com.droiddevgeeks.untroddenlabsdemo.search;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.droiddevgeeks.untroddenlabsdemo.R;
import com.droiddevgeeks.untroddenlabsdemo.models.SongsVO;
import com.droiddevgeeks.untroddenlabsdemo.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Vampire on 2017-07-27.
 */

public class SongsSearchAdapter extends RecyclerView.Adapter<SongSearchViewHolder>
{
    private List<SongsVO> songsVOList;
    private WeakReference<IItemClick> listener;

    public SongsSearchAdapter(IItemClick listener, List<SongsVO> songsVOs)
    {
        songsVOList = songsVOs;
        this.listener = new WeakReference<>(listener);
    }

    @Override
    public SongSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_layout, parent, false);
        return new SongSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongSearchViewHolder holder, int position)
    {
        final SongsVO songsVO = songsVOList.get(position);
        holder.trackName.setText(songsVO.getTrackName());
        holder.artistName.setText(songsVO.getArtistName());
        holder.collectionName.setText(songsVO.getCollectionName());
        holder.duration.setText("" + String.format("%.2f", Float.valueOf(songsVO.getTrackTimeMillis()) / 60000) + " min");
        holder.genre.setText(songsVO.getPrimaryGenreName());
        String price = songsVO.getPrice() + " " + holder.itemView.getContext().getResources().getString(R.string.rupee_symbol);
        holder.priceValue.setText(price);

        if (Utilities.isNetworkConnected(holder.itemView.getContext()))
        {
            if (!songsVO.getartworkUrl100().equalsIgnoreCase(""))
            {
                Glide.with(holder.itemView.getContext()).load(songsVO.getartworkUrl100()).into(holder.thumb);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.get().onItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return songsVOList.size();
    }
}
