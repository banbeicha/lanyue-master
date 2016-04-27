package com.wz.lanyue.banke.model;

import org.json.JSONObject;

/**
 * Created by BANBEICHAS on 2016/4/15.
 */
public class MeiWen {


    /**
     * id : id
     * aid : 文章id
     * title : 标题
     * date : 日期
     * author : 作者
     * image : 图片链接
     * type : 类型
     * count :  浏览总数
     * which : 0
     */

        private int id;
        private int aid;
        private String title;
        private String date;
        private String author;
        private String image;
        private String type;
        private int fav;
        private int count;
        private int which;

        public int getId() {
            return id;
        }

        public int getAid() {
            return aid;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }


        public String getAuthor() {
            return author;
        }

        public String getImage() {
            return image;
        }

        public String getType() {
            return type;
        }

        public int getFav() {
            return fav;
        }

        public int getCount() {
            return count;
        }


        public int getWhich() {
            return which;
        }

     public static MeiWen parse(JSONObject jsonObject){
         if(jsonObject==null){
             return  null;
         }
         MeiWen meiWen=new MeiWen();
         meiWen.aid= jsonObject.optInt("aid");
         meiWen.id=jsonObject.optInt("id");
         meiWen.fav= jsonObject.optInt("fav");
         meiWen.count=jsonObject.optInt("count");
         meiWen.which= jsonObject.optInt("which");
         meiWen.title=jsonObject.optString("title");
         meiWen.date=jsonObject.optString("date");
         meiWen.author=jsonObject.optString("author");
         meiWen.image=jsonObject.optString("image");
         meiWen.type=jsonObject.optString("type");
          return meiWen;

     }

}
