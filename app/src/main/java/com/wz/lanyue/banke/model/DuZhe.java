package com.wz.lanyue.banke.model;

import java.io.Serializable;

/**
 * Created by BANBEICHAS on 2016/5/25.
 */
public class DuZhe implements Serializable{


    /**
     * id : id
     * img : 图片路径
     * img_num : 图片总数
     * title : 标题
     * date : 日期
     * authour : 作者
     * url : 文章链接
     * c_title : 标题分类
     * type : 文章类型
     */

    private String id;
    private String img;
    private String title;
    private String date;
    private String authour;
    private String url;
    private String c_title;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthour() {
        return authour;
    }

    public void setAuthour(String authour) {
        this.authour = authour;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
