package com.wz.lanyue.banke.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BANBEICHAS on 2016/4/14.
 */
public class WeChatSelectionList {
    private static ArrayList<WeChatSelection> weChatSelectionArrayList;

    public static ArrayList<WeChatSelection> getWechatSelectionlist(JSONObject jsonObject) {
        if (jsonObject.optString("reason").equals("success")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("list");
                if (jsonArray != null && jsonArray.length() > 0) {
                    int length = jsonArray.length();
                    weChatSelectionArrayList = new ArrayList<WeChatSelection>(length);
                    for (int i = 0; i < length; i++) {
                        weChatSelectionArrayList.add(WeChatSelection.parse(jsonArray.getJSONObject(i)));
                    }
                    return weChatSelectionArrayList;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return weChatSelectionArrayList;
    }


}
