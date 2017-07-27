package com.droiddevgeeks.untroddenlabsdemo.search;

import com.droiddevgeeks.untroddenlabsdemo.fetchdata.DownloadParseResponse;
import com.droiddevgeeks.untroddenlabsdemo.models.SongsVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2017-07-27.
 */

public class SearchResponse extends DownloadParseResponse
{
    private List<SongsVO> songsVOList;

    @Override
    public boolean parseJson(JSONObject jsonObject)
    {
        JSONArray jsonArray = null;
        try
        {
            songsVOList = new ArrayList<>();
            jsonArray = jsonObject.getJSONArray("results");
            int len = jsonArray.length();
            if (len == 0)
            {
                return false;
            }
            for (int i = 0; i < len; i++)
            {
                JSONObject songJsonObject = jsonArray.getJSONObject(i);
                SongsVO songsVO = new SongsVO();
                songsVO.setWrapperType(songJsonObject.optString("wrapperType"));
                songsVO.setKind(songJsonObject.optString("kind"));
                songsVO.setArtistId(songJsonObject.optString("artistId"));
                songsVO.setArtistName(songJsonObject.optString("artistName"));
                songsVO.setCollectionName(songJsonObject.optString("collectionName"));
                songsVO.setTrackName(songJsonObject.optString("trackName"));
                songsVO.setCollectionCensoredName(songJsonObject.optString("collectionCensoredName"));
                songsVO.setTrackCensoredName(songJsonObject.optString("trackCensoredName"));
                songsVO.setCollectionArtistName(songJsonObject.optString("artistName"));
                songsVO.setPrice(songJsonObject.optString("trackPrice"));
                songsVO.setartworkUrl100(songJsonObject.optString("artworkUrl100"));
                songsVO.setReleaseDate(songJsonObject.optString("releaseDate"));
                songsVO.setTrackTimeMillis(songJsonObject.optString("trackTimeMillis", "0"));
                songsVO.setPrimaryGenreName(songJsonObject.optString("primaryGenreName"));
                songsVO.setCollectionPrice(songJsonObject.optString("collectionPrice"));
                songsVO.setTrackCount(songJsonObject.optString("trackCount"));
                songsVO.setCountry(songJsonObject.optString("country"));
                songsVO.setPreviewUrl(songJsonObject.optString("previewUrl"));
                songsVO.setIsStreamable(songJsonObject.optBoolean("isStreamable"));
                songsVO.setTrackId(songJsonObject.optString("trackId"));

                songsVOList.add(songsVO);
            }
            return true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return  false;
        }
    }


    public List<SongsVO> getSongsVOList()
    {
        return songsVOList;
    }
}
