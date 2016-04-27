package com.wz.lanyue.banke.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by BANBEICHAS on 2016/4/11.
 */
public class NewsList {
    private static ArrayList<News> newsArrayList;


    public static ArrayList<News> getnewsList(JSONObject jsonObject) {
        try {
            if(jsonObject.getInt("code")==200){
              JSONArray jsonArray= jsonObject.getJSONArray("newslist");
                if(jsonArray!=null&&jsonArray.length()>0){
                    int length=jsonArray.length();
                    NewsList.newsArrayList=new ArrayList<News>(length);
                    for (int i=0;i<length;i++){
                        newsArrayList.add(News.parse(jsonArray.getJSONObject(i)));
                    }
                }
               return  newsArrayList;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
