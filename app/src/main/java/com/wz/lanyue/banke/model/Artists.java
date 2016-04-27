package com.wz.lanyue.banke.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BANBEICHAS on 2016/4/22.
 */
public class Artists {

    /**
     * artistId :
     * artistName : 姓名
     * artistAvatar : 头像
     * area : 国家
     */
    private int artistId;
    private String artistName;
    private String artistAvatar;
    private String area;

    public int getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistAvatar() {
        return artistAvatar;
    }

    public String getArea() {
        return area;
    }

    public static  Artists getArtists(JSONObject jsonObject) {
        if (!"".equals(jsonObject)) {
            Artists artists = new Artists();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("artists");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                artists.artistAvatar = jsonObject1.optString("artistAvatar");
                artists.artistId = jsonObject1.optInt("artistId");
                artists.artistName = jsonObject1.optString("artistName");
                return artists;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
