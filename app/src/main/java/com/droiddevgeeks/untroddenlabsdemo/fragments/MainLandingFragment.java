package com.droiddevgeeks.untroddenlabsdemo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droiddevgeeks.untroddenlabsdemo.R;
import com.droiddevgeeks.untroddenlabsdemo.api.ApplicationUrls;
import com.droiddevgeeks.untroddenlabsdemo.fetchdata.DownloadParseResponse;
import com.droiddevgeeks.untroddenlabsdemo.fetchdata.FetchDataAsyncTask;
import com.droiddevgeeks.untroddenlabsdemo.fetchdata.IDownloadListener;
import com.droiddevgeeks.untroddenlabsdemo.models.SongsVO;
import com.droiddevgeeks.untroddenlabsdemo.search.IItemClick;
import com.droiddevgeeks.untroddenlabsdemo.search.SearchResponse;
import com.droiddevgeeks.untroddenlabsdemo.search.SongsSearchAdapter;
import com.droiddevgeeks.untroddenlabsdemo.utils.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vampire on 2017-07-27.
 */

public class MainLandingFragment extends Fragment implements IDownloadListener, IItemClick
{
    private TextView txtSongSearch;
    private SearchView searchView;
    private List<SongsVO> songsVOList;
    private SongsSearchAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private FetchDataAsyncTask fetchDataAsyncTask;

    public static final String CURRENT_FRAGMENT_TAG = "MainLandingFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.landing_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        txtSongSearch = (TextView) view.findViewById(R.id.txtSongSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.songsList);

        setAdapter();

        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (Utilities.isNetworkConnected(getContext()))
                {
                    displayProgressBar();
                    fetchSongsData(query);
                    searchView.clearFocus();
                }
                else
                {
                    txtSongSearch.setVisibility(View.VISIBLE);
                    Utilities.showToastMessage(getContext(), getString(R.string.internet_error));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }

        });

        searchView.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                txtSongSearch.setVisibility(View.INVISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                txtSongSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }


    private void setAdapter()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        songsVOList = new ArrayList<>();
        adapter = new SongsSearchAdapter(this, songsVOList);
        recyclerView.setAdapter(adapter);
    }

    private void displayProgressBar()
    {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getContext().getResources().getString(R.string.progress_message));
        dialog.show();
    }


    private void fetchSongsData(String query)
    {
        try
        {
            String key = URLEncoder.encode(query, "utf-8");
            String url = ApplicationUrls.SEARCH_URL + key;
            fetchDataAsyncTask = new FetchDataAsyncTask(new SearchResponse(), this);
            fetchDataAsyncTask.execute(url);

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        if (dialog != null)
        {
            dialog.dismiss();
        }

        if (downloadParseResponse instanceof SearchResponse)
        {
            SearchResponse response = (SearchResponse) downloadParseResponse;
            if (songsVOList != null)
            {
                songsVOList.clear();
                songsVOList.addAll(response.getSongsVOList());
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.smoothScrollToPosition(0);
                txtSongSearch.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDownloadFailed(String message)
    {
        if (dialog != null)
        {
            dialog.dismiss();
        }
        txtSongSearch.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Utilities.showToastMessage(getContext(), message);

    }

    @Override
    public void onItemClick(int position)
    {
        // to hide keyboard
        searchView.clearFocus();

        SongsVO songsVO = songsVOList.get(position);
        SongsDetailFragment songsDetailFragment = new SongsDetailFragment();
        getFragmentManager().beginTransaction().add(R.id.container, songsDetailFragment, CURRENT_FRAGMENT_TAG).addToBackStack(null).commit();
        songsDetailFragment.setSongsData(songsVO);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (fetchDataAsyncTask != null)
        {
            Log.v("Cancel", "onDestroy ");
            fetchDataAsyncTask.cancel(true);
            fetchDataAsyncTask = null;
        }
        recyclerView = null;
        songsVOList = null;
        adapter = null;
        dialog = null;
    }


}
