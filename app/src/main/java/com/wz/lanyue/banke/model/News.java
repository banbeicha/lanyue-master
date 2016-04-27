package com.wz.lanyue.banke.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by BANBEICHAS on 2016/4/11.
 */
public class News implements Serializable{


    /**
     * ctime : 时间
     * title : 标题
     * description : 来源
     * picUrl : 图片路径
     * url : 新闻路径
     */

    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;

    public String getCtime() {
        return ctime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public String getPicUrl() {
        return picUrl;
    }

    public String getUrl() {
        return url;
    }
    public static News parse(JSONObject jsonObject){
        if(jsonObject==null){
            return  null;
        }
        News news=new News();
        try {
            news.picUrl=jsonObject.getString("picUrl");
            news.title=jsonObject.getString("title");
            news.ctime=jsonObject.getString("ctime");
            news.description=jsonObject.getString("description");
            news.url=jsonObject.getString("url");
            return news;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
