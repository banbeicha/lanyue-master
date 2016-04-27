package com.wz.lanyue.banke.util;

import android.content.Context;
import android.content.Intent;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.UserInfoActivity;

/**
 * Created by BANBEICHA on 2016/3/29.
 */
public class UserAPIhelper {
    private static Oauth2AccessToken oauth2AccessToken;
    private static UsersAPI usersAPI;

    public static UsersAPI getUsersAPI(Context context) {
        oauth2AccessToken= MyApplication.getToken(context);
        if (usersAPI==null){
            usersAPI=new UsersAPI(context,MyApplication.appKey,oauth2AccessToken);
        }
        return usersAPI;
    }
    public static void startUserInfoactivity(Context context,String name){
        context.startActivity(new Intent(context, UserInfoActivity.class).putExtra("name",name));
    }
}
