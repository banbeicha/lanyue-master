package com.wz.lanyue.banke.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BANBEICHAS on 2016/4/19.
 */
public class HuaBangList {
    private static ArrayList<HuaBang> huaBangArrayList;


    public static ArrayList<HuaBang> getnewsList(JSONObject jsonObject) {
        try {
            if(!"".equals(jsonObject.getJSONArray("videos"))){
                JSONArray jsonArray= jsonObject.getJSONArray("videos");
                if(jsonArray!=null&&jsonArray.length()>0){
                    int length=jsonArray.length();
                    HuaBangList.huaBangArrayList=new ArrayList<HuaBang>(length);
                    for (int i=0;i<length;i++){
                        huaBangArrayList.add(HuaBang.getHuaBang(jsonArray.getJSONObject(i)));
                    }
                }
                return  huaBangArrayList;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
