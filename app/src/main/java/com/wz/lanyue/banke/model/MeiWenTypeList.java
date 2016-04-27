package com.wz.lanyue.banke.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BANBEICHAS on 2016/4/15.
 */
public class MeiWenTypeList {

    private static ArrayList<MeiWenType> meiWenTypeArrayList;


    public static ArrayList<MeiWenType> getnewsList(JSONObject jsonObject) {
        try {
            if(jsonObject.getInt("code")==200){
                JSONArray jsonArray= jsonObject.getJSONArray("catalogs");
                if(jsonArray!=null&&jsonArray.length()>0){
                    int length=jsonArray.length();
                    MeiWenTypeList.meiWenTypeArrayList =new ArrayList<MeiWenType>(length);
                    for (int i=0;i<length;i++){
                     JSONObject jsonObject1=   jsonArray.getJSONObject(i);
                        if(jsonObject1.optInt("which")==1){
                        meiWenTypeArrayList.add(MeiWenType.parse(jsonObject1));}
                    }
                }
                return meiWenTypeArrayList;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
