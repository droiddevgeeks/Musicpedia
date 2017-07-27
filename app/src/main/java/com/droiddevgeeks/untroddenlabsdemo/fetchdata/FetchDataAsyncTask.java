package com.droiddevgeeks.untroddenlabsdemo.fetchdata;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vampire on 2017-07-27.
 */

public class FetchDataAsyncTask extends AsyncTask<String, Integer, String>
{
    private DownloadParseResponse downloadParseResponse;
    private WeakReference<IDownloadListener> iDownloadListener;

    public FetchDataAsyncTask(DownloadParseResponse downloadParseResponse, IDownloadListener listener)
    {
        this.downloadParseResponse = downloadParseResponse;
        iDownloadListener = new WeakReference<>(listener);
    }

    @Override
    protected String doInBackground(String... params)
    {
        URL url = null;
        try
        {
            url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String res = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((res = bufferedReader.readLine()) != null)
            {
                if(isCancelled())
                {
                    Log.v("Cancel", "cancel");
                    stringBuilder = null;
                    httpURLConnection.disconnect();
                    inputStream.close();
                    bufferedReader.close();
                    break;
                }
                stringBuilder = stringBuilder.append(res);
            }
            res = stringBuilder.toString();
            httpURLConnection.disconnect();
            inputStream.close();
            return res;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if (s != null)
        {
            try
            {
                if (downloadParseResponse != null)
                {
                    if (downloadParseResponse.parseJson(new JSONObject(s)))
                    {
                        iDownloadListener.get().onDownloadSuccess(downloadParseResponse);
                    }
                    else
                    {
                           iDownloadListener.get().onDownloadFailed("No Result found");
                    }
                }
                downloadParseResponse = null;
                iDownloadListener = null;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}

