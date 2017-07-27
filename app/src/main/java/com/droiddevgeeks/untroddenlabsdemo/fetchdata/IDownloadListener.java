package com.droiddevgeeks.untroddenlabsdemo.fetchdata;

/**
 * Created by Vampire on 2017-05-03.
 */

public interface IDownloadListener
{
    void onDownloadSuccess(DownloadParseResponse downloadParseResponse);
    void onDownloadFailed(String message);
}
