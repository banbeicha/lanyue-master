package com.wz.lanyue.banke.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BANBEICHAS on 2016/3/31.
 */
public class StringUtil {


    public static SpannableString getWeiboContent(final Context context, final TextView tv, String source) {
        String regexAt = "@[\u4e00-\u9fa5\\w]+";
        String regexTopic = "#[\u4e00-\u9fa5.\\w]+#";
        String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";


        String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")";

        SpannableString spannableString = new SpannableString(source);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);

        if(matcher.find()) {
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            matcher.reset();
        }

        while(matcher.find()) {
            final String atStr = matcher.group(1);
            final String topicStr = matcher.group(2);
            String emojiStr = matcher.group(3);

            if(atStr != null) {
                int start = matcher.start(1);

                BoreClickableSpan clickableSpan = new BoreClickableSpan(context) {

                    @Override
                    public void onClick(View widget) {
                     UserAPIhelper.startUserInfoactivity(context,atStr.substring(1));
                    }
                };
                spannableString.setSpan(clickableSpan, start, start + atStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(topicStr != null) {
                int start = matcher.start(2);

                BoreClickableSpan clickableSpan = new BoreClickableSpan(context) {

                    @Override
                    public void onClick(View widget) {
                        ToastHelper.show(context, "话题: " + topicStr);
                    }
                };

                spannableString.setSpan(clickableSpan, start, start + topicStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(emojiStr != null) {
                int start = matcher.start(3);

                int imgRes = BiaoqingUtil.getImgByName(emojiStr);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);

                if(bitmap != null) {
                    int size = (int) tv.getTextSize();
                    bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);

                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }


        }


        return spannableString;
    }

    static class BoreClickableSpan extends ClickableSpan {

        private Context context;

        public BoreClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
            //

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#578BBE"));
            ds.setUnderlineText(false);
        }


    }
}
