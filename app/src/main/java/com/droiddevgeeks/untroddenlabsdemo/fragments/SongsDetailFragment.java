package com.droiddevgeeks.untroddenlabsdemo.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.droiddevgeeks.untroddenlabsdemo.R;
import com.droiddevgeeks.untroddenlabsdemo.models.SongsVO;

import java.io.File;
import java.io.IOException;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Vampire on 2017-07-27.
 */

public class SongsDetailFragment extends Fragment implements View.OnClickListener
{

    private TextView play;
    private TextView genre;
    private SongsVO songsVO;
    private TextView country;
    private TextView duration;
    private TextView download;
    private TextView trackName;
    private TextView artistName;
    private TextView priceValue;
    private TextView trackCount;
    private TextView isStreamable;
    private TextView collectionPrice;
    private ImageView backIcon;
    private MediaPlayer mPlayer;
    private ProgressDialog dialog;
    private DownloadManager manager = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.songs_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        genre = (TextView) view.findViewById(R.id.genre);
        country = (TextView) view.findViewById(R.id.country);
        duration = (TextView) view.findViewById(R.id.duration);
        trackName = (TextView) view.findViewById(R.id.trackName);
        artistName = (TextView) view.findViewById(R.id.artistName);
        priceValue = (TextView) view.findViewById(R.id.priceValue);
        trackCount = (TextView) view.findViewById(R.id.trackCount);
        isStreamable = (TextView) view.findViewById(R.id.isStreamable);
        collectionPrice = (TextView) view.findViewById(R.id.collectionPrice);

        download = (TextView) view.findViewById(R.id.songDownload);
        download.setOnClickListener(this);

        play = (TextView) view.findViewById(R.id.songPlay);
        play.setOnClickListener(this);

        backIcon = (ImageView) getActivity().findViewById(R.id.backIcon);
        backIcon.setVisibility(View.VISIBLE);
        backIcon.setOnClickListener(this);

        manager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
    }

    public void setSongsData(SongsVO songsVO)
    {
        this.songsVO = songsVO;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if (songsVO != null)
        {
            country.setText(songsVO.getCountry());
            priceValue.setText(songsVO.getPrice());
            trackName.setText(songsVO.getTrackName());
            trackCount.setText(songsVO.getTrackCount());
            artistName.setText(songsVO.getArtistName());
            genre.setText(songsVO.getPrimaryGenreName());
            isStreamable.setText("" + songsVO.getIsStreamable());
            collectionPrice.setText(songsVO.getCollectionPrice());
            duration.setText("" + String.format("%.2f", Float.valueOf(songsVO.getTrackTimeMillis()) / 60000) + " min");
            File song = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + songsVO.getTrackId());
            if (song.exists())
            {
                download.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.backIcon:
                backIcon.setVisibility(View.INVISIBLE);
                getFragmentManager().popBackStack();
                break;
            case R.id.songDownload:
                displayProgressBar();
                songDownload();
                break;
            case R.id.songPlay:
                if (mPlayer != null)
                {
                    if (mPlayer.isPlaying())
                    {
                        mPlayer.pause();
                        play.setText("Pause");

                    }
                    else
                    {
                        mPlayer.start();
                        play.setText("Play");
                    }
                }
                else
                {
                    playSong();
                }
                break;

        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if (mPlayer != null)
        {
            mPlayer.stop();
            mPlayer.release();
        }
        getActivity().unregisterReceiver(onComplete);
    }


    private void songDownload()
    {
        DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(songsVO.getPreviewUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(songsVO.getArtistName());
        request.setDescription("Downloading");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songsVO.getTrackId());
        downloadmanager.enqueue(request);

    }

    BroadcastReceiver onComplete = new BroadcastReceiver()
    {
        public void onReceive(Context ctxt, Intent intent)
        {
            if (dialog != null)
            {
                dialog.dismiss();
            }
            play.setVisibility(View.VISIBLE);
            download.setVisibility(View.GONE);
        }
    };

    private void displayProgressBar()
    {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getContext().getResources().getString(R.string.downloading));
        dialog.show();
    }

    private void playSong()
    {
        try
        {
            mPlayer = new MediaPlayer();
            Uri myUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + songsVO.getTrackId());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(getActivity(), myUri);
            // mPlayer.prepare();
            // mPlayer.start();
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer)
                {
                    mPlayer.start();
                }
            });

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        manager = null;
        mPlayer = null;
        dialog = null;
    }


}
