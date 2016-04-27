package com.wz.lanyue.banke.model;

import org.json.JSONObject;

/**
 * Created by BANBEICHAS on 2016/4/15.
 */
public class MeiWenType {


    /**
     * id : id
     * count : 0
     * which : 1
     * title : 优美散文
     * image : http://www.umeitime.com/img/wenzi1.jpg
     * type : umeitime
     * pubdate : 2015-12-13 13:36:35.0
     */

    private int id;
    private int count;
    private int which;
    private String title;
    private String image;
    private String type;
    private String pubdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWhich() {
        return which;
    }

    public void setWhich(int which) {
        this.which = which;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }
    public static MeiWenType parse(JSONObject jsonObject){
        if(jsonObject==null){
            return  null;
        }
        MeiWenType meiWenType =new MeiWenType();
        meiWenType.id=jsonObject.optInt("id");
        meiWenType.title=jsonObject.optString("title");
        meiWenType.type=jsonObject.optString("type");
        meiWenType.which=jsonObject.optInt("which");
        meiWenType.pubdate=jsonObject.optString("pubdate");
        meiWenType.image=jsonObject.optString("image");
        meiWenType.count=jsonObject.optInt("count");
        return meiWenType;




    }
}
