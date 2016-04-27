package com.wz.lanyue.banke.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.wz.lanyue.banke.HomeActivity;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.model.AccessTokenKeeper;

/**
 * Created by BANBEICHA on 2016/3/24.
 */
public class AuthListener implements WeiboAuthListener {
    Context context;
    private Oauth2AccessToken mAccessToken;


    public AuthListener(Context context) {
        this.context = context;
    }

    @Override
    public void onComplete(Bundle bundle) {
        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle); // 从 Bundle 中解析 Token
        if (mAccessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(context, mAccessToken); //保存Token
            context.startActivity(new Intent(context, HomeActivity.class));
            MyApplication.isLogin=true;
        } else {
            // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
            String code = bundle.getString("code", "");

        }

    }

    @Override
    public void onWeiboException(WeiboException e) {

    }

    @Override
    public void onCancel() {
        ToastHelper.show(context, "你已取消授权");
    }
}
