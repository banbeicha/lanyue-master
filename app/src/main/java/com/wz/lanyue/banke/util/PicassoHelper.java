package com.wz.lanyue.banke.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wz.lanyue.banke.R;

/**
 * Created by BANBEICHAS on 2016/3/30.
 */
public class PicassoHelper {

    public  static void setimg(Context context,String url, ImageView view){

        Picasso.with(context).load(url).into(view);
    }
    public  static void noyuanjiaosetimg(Context context,String url, ImageView view){
        Picasso.with(context).load(url).error(R.mipmap.zhanweitu).config(Bitmap.Config.RGB_565).into(view);
    }
}
