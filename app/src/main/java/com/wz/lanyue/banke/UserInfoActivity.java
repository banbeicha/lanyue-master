package com.wz.lanyue.banke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.User;
import com.wz.lanyue.banke.util.ActionBarSetting;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.util.UserAPIhelper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInfoActivity extends AppCompatActivity {
Intent intent;
    String name;
    View vname,vsex,vlocal,vintro,vifline,vrenzheng,vregistertime;
    TextView tvname,tvsex,tvlocal,tvintro,tvifline,tvrenzheng,tvregistertime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActionBarSetting.setTitle(getSupportActionBar(),"基本信息");
        initviewdata();
    }

    private void initviewdata() {
        intent=getIntent();
        name=intent.getStringExtra("name");
        vname=findViewById(R.id.ilname);
        vsex=findViewById(R.id.ilsex);
        vlocal=findViewById(R.id.illocal);
        vintro=findViewById(R.id.ilintro);
        vifline=findViewById(R.id.ilifline);
        vrenzheng=findViewById(R.id.ilrenzheng);

        vregistertime=findViewById(R.id.ilregistertime);
        ((TextView) vname.findViewById(R.id.tvtag)).setText("昵称");
        tvname= (TextView) vname.findViewById(R.id.tvusercontent);
        ((TextView) vsex.findViewById(R.id.tvtag)).setText("性别");
        tvsex= (TextView) vsex.findViewById(R.id.tvusercontent);
        ((TextView) vlocal.findViewById(R.id.tvtag)).setText("所在地");
        tvlocal= (TextView) vlocal.findViewById(R.id.tvusercontent);
        ((TextView) vintro.findViewById(R.id.tvtag)).setText("简介");
        tvintro= (TextView) vintro.findViewById(R.id.tvusercontent);
        ((TextView) vifline.findViewById(R.id.tvtag)).setText("状态");
        tvifline= (TextView) vifline.findViewById(R.id.tvusercontent);
        ((TextView) vrenzheng.findViewById(R.id.tvtag)).setText("认证");
        tvrenzheng= (TextView) vrenzheng.findViewById(R.id.tvusercontent);
        ((TextView) vregistertime.findViewById(R.id.tvtag)).setText("注册时间");
        tvregistertime= (TextView) vregistertime.findViewById(R.id.tvusercontent);
        initinfodata();
    }

    private void initinfodata() {
        RequestListener requstuserinfo=new RequestListener() {
            @Override
            public void onComplete(String s) {
              User user=  User.parse(s);
                if(user!=null){
                    tvname.setText(user.getScreen_name());
                    String sex=user.getGender();
                    if("m".equals(sex)){
                    tvsex.setText("男");}
                    else if("f".equals(sex)){
                        tvsex.setText("女");
                    }
                    else{
                        tvsex.setText("未知");
                    }
                    tvlocal.setText(user.getLocation());
                    tvintro.setText(user.getDescription());
                    int ifline=user.getOnline_status();
                    if(ifline==0){
                    tvifline.setText("不在线");
                    }
                    else{
                        tvifline.setText("在线");
                    }
                    boolean ifrenzheng=user.isVerified();
                    if(ifrenzheng){
                        tvrenzheng.setText("已认证");
                    }
                    else{
                        tvrenzheng.setText("未认证");
                    }
                   SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
                    try {
                        Date date=simpleDateFormat.parse(user.getCreated_at());
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-hh", Locale.CHINA);
                        tvregistertime.setText(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastHelper.show(UserInfoActivity.this,"服务器错误");
            }
        };
        UserAPIhelper.getUsersAPI(this).show(name,requstuserinfo);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
