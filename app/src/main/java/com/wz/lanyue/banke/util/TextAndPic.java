package com.wz.lanyue.banke.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;

/**
 * Created by BANBEICHAS on 2016/4/12.
 */
public class TextAndPic {
    private static CharSequence content;

    public static CharSequence getTextPic(String text, Activity context) {

        try {
          content = Html.fromHtml(ContentExtractor.getNewsByHtml(text).getContentElement().toString(), getImageGetter(context,0), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;

    }
    public static CharSequence getContentPic(String contents, Activity context) {

        try {
            content = Html.fromHtml(contents, getImageGetter(context,1), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;

    }
    private static Html.ImageGetter getImageGetter(final Activity context, final int sizetype) {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Bitmap bitmap;

            @Override
            public Drawable getDrawable(String source) {
                try {
                    InputStream inputStream = (InputStream) new URL(source).getContent();
                    Drawable drawable = Drawable.createFromStream(inputStream, "src");
                    DisplayMetrics dm = new DisplayMetrics();
                    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
                  int h=  drawable.getIntrinsicHeight() * (dm.widthPixels / drawable.getIntrinsicWidth());
                    if (sizetype==1){

                            drawable.setBounds(0, 0, dm.widthPixels,720 );
                    }
                    else{
                        drawable.setBounds(10, 10, dm.widthPixels, h);
                    }
                    inputStream.close();
                    return drawable;
                } catch (IOException e) {
                    return null;
                }
            }
        };
        return imageGetter;
    }

}
