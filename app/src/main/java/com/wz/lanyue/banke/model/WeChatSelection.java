package com.wz.lanyue.banke.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by BANBEICHAS on 2016/4/14.
 */
public class WeChatSelection implements Serializable{


    /**
     * firstImg : 图片
     * id : id
     * source : 来源
     * title : 标题
     * url : 内容url
     * mark :
     */

    private String firstImg;
    private String id;
    private String source;
    private String title;
    private String url;

    public String getFirstImg() {
        return firstImg;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
    public static WeChatSelection parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            WeChatSelection weChatSelection = new WeChatSelection();
            weChatSelection.firstImg = jsonObject.optString("firstImg");
            weChatSelection.id = jsonObject.optString("id");
            weChatSelection.source = jsonObject.optString("source");
            weChatSelection.title = jsonObject.optString("title");
            weChatSelection.url = jsonObject.optString("url");
            return weChatSelection;
        }

        return null;
    }

}
