package com.droiddevgeeks.untroddenlabsdemo.fetchdata;

import org.json.JSONObject;

/**
 * Created by Vampire on 2017-05-03.
 */

public abstract class DownloadParseResponse
{
    public abstract boolean parseJson(JSONObject jsonObject);
}