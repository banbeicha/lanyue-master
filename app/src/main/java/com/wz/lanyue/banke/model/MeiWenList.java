package com.wz.lanyue.banke.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BANBEICHAS on 2016/4/16.
 */
public class MeiWenList {
    private static ArrayList<MeiWen> meiWenArrayList;


    public static ArrayList<MeiWen> getnewsList(JSONObject jsonObject) {
        try {
            if("success".equals(jsonObject.getString("flag"))){
                JSONArray jsonArray= jsonObject.getJSONArray("result");
                if(jsonArray!=null&&jsonArray.length()>0){
                    int length=jsonArray.length();
                    MeiWenList.meiWenArrayList=new ArrayList<MeiWen>(length);
                    for (int i=0;i<length;i++){
                        meiWenArrayList.add(MeiWen.parse(jsonArray.getJSONObject(i)));
                    }
                }
                return  meiWenArrayList;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
