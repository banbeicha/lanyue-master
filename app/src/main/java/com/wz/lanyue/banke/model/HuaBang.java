package com.wz.lanyue.banke.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by BANBEICHAS on 2016/4/19.
 */
public class HuaBang implements Serializable{


    /**
     * id : 歌曲id
     * title : 标题
     * description : 简介
     * artists : [{"artistId":15775,"artistName":"马上又"}]
     * artistName : 作者
     * posterPic : 封面图
     * thumbnailPic : 封面图
     * albumImg :封面大图
     * regdate : 时间
     * videoSourceTypeName :视频类型
     * totalViews : 总播放数
     * totalPcViews : pc播放数
     * totalMobileViews : 手机播放数
     * totalComments : 评论数
     * url : 标清
     * hdUrl : 高清
     * uhdUrl : 超清
     * videoSize : 标清大小
     * hdVideoSize : 高清大小
     * uhdVideoSize : 超清大小
     * shdVideoSize : 0
     * duration : 时长
     * status : 状态
     * linkId : 0
     * promoTitle : 深刻隐含了剧中人物的命运
     * playListPic : http://img2.yytcdn.com/video/mv/160419/2546986/-M-5acb87ffad287a128f0c7db3be39e27b_240x135.jpg
     */

    private int id;
    private String title;
    private String description;
    private String artistName;
    private String regdate;
    private int totalViews;
    private int totalPcViews;
    private int totalMobileViews;
    private String url;
    private String hdUrl;
    private String uhdUrl;
    private int videoSize;
    private int hdVideoSize;
    private int uhdVideoSize;
    private String promoTitle;
    private String thumbnailPic;
 private String playListPic;
    private String posterPic;
    private String albumImg;

    public String getAlbumImg() {
        return albumImg;
    }

    public String getPosterPic() {
        return posterPic;
    }

    public String getThumbnailPic() {
        return thumbnailPic;
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public String getArtistName() {
        return artistName;
    }


    public String getRegdate() {
        return regdate;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public int getTotalPcViews() {
        return totalPcViews;
    }


    public int getTotalMobileViews() {
        return totalMobileViews;
    }

    public String getUrl() {
        return url;
    }


    public String getHdUrl() {
        return hdUrl;
    }


    public String getUhdUrl() {
        return uhdUrl;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public int getHdVideoSize() {
        return hdVideoSize;
    }

    public int getUhdVideoSize() {
        return uhdVideoSize;
    }

    public String getPromoTitle() {
        return promoTitle;
    }

    public String getPlayListPic() {
        return playListPic;
    }

    public static HuaBang getHuaBang(JSONObject jsonObject) {
        if (jsonObject != null) {
            HuaBang huaBang = new HuaBang();
            huaBang.id = jsonObject.optInt("id");
            huaBang.totalViews = jsonObject.optInt("totalViews");
            huaBang.totalPcViews = jsonObject.optInt("totalPcViews");
            huaBang.totalMobileViews = jsonObject.optInt("totalMobileViews");
            huaBang.videoSize = jsonObject.optInt("videoSize");
            huaBang.hdVideoSize = jsonObject.optInt("hdVideoSize");
            huaBang.uhdVideoSize = jsonObject.optInt("uhdVideoSize");
            huaBang.artistName = jsonObject.optString("artistName");
            huaBang.title = jsonObject.optString("title");
            huaBang.description = jsonObject.optString("description");
            huaBang.regdate = jsonObject.optString("regdate");
            huaBang.url = jsonObject.optString("url");
            huaBang.hdUrl = jsonObject.optString("hdUrl");
            huaBang.uhdUrl = jsonObject.optString("uhdUrl");
            huaBang.promoTitle = jsonObject.optString("promoTitle");
            huaBang.playListPic = jsonObject.optString("playListPic");
            huaBang.thumbnailPic = jsonObject.optString("thumbnailPic");
            huaBang.albumImg = jsonObject.optString("albumImg");
            huaBang.posterPic = jsonObject.optString("posterPic");
            return huaBang;
        }
        return null;


    }
}
