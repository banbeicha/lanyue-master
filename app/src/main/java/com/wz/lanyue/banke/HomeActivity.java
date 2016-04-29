package com.wz.lanyue.banke;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.wz.lanyue.banke.Fragment.meiwen;
import com.wz.lanyue.banke.Fragment.shouye;
import com.wz.lanyue.banke.Fragment.huabang;
import com.wz.lanyue.banke.model.AccessTokenKeeper;
import com.wz.lanyue.banke.util.AuthListener;
import com.wz.lanyue.banke.util.Constants;
import com.wz.lanyue.banke.util.MyBroadcastReceive;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.widgetview.AlertDialog;
import com.wz.lanyue.banke.widgetview.CircleImageView;
import com.wz.lanyue.banke.widgetview.MyViewPager;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.util.UserAPIhelper;
import com.wz.lanyue.banke.data.HomeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {
    MyViewPager viewPager;
    List<Fragment> fragmentList;
    RadioButton shouye, tupian, meiwen;
    private UsersAPI usersAPI;
    private User user;
    ImageView ivusersex;
    CircleImageView ivuserheader;
    NavigationView navigationView;
    TextView tvusername, tvfansinfo, tvgerenmiaoshu;
    private MyBroadcastReceive myBroadcastReceive;
    private AuthInfo authInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
       myBroadcastReceive=new MyBroadcastReceive();

      registerReceiver(myBroadcastReceive,intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        PushAgent.getInstance(this).onAppStart();
        String device_token = UmengRegistrar.getRegistrationId(this);
        mPushAgent.setPushCheck(true);
        initView();
    }

    private void initView() {
        viewPager = (MyViewPager) findViewById(R.id.homeviewPager);
        View v = findViewById(R.id.homeinclude);
        meiwen = (RadioButton) v.findViewById(R.id.homeincludemeiwen);
        tupian = (RadioButton) v.findViewById(R.id.homeincludetupian);
        shouye = (RadioButton) v.findViewById(R.id.homeincludeshouye);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View view = navigationView.getHeaderView(0);
        Menu menu=navigationView.getMenu();
        menu.findItem(R.id.Exit).setOnMenuItemClickListener(this);
        ivuserheader = (CircleImageView) view.findViewById(R.id.ivuserheader);
        tvusername = (TextView) view.findViewById(R.id.tvusername);
        tvusername.setOnClickListener(this);
        tvfansinfo = (TextView) view.findViewById(R.id.tvfansinfo);
        tvgerenmiaoshu = (TextView) view.findViewById(R.id.tvgerenmiaoshu);
        ivusersex= (ImageView) view.findViewById(R.id.ivusersex);
        tupian.setOnClickListener(this);
        shouye.setOnClickListener(this);
        meiwen.setOnClickListener(this);
        fragmentList = new ArrayList<Fragment>();
        initFragmentData();
      initslideruserdata();
    }

    public void initslideruserdata() {
        if(MyApplication.isLogin){
         usersAPI = UserAPIhelper.getUsersAPI(this);
         long uid = Long.parseLong(MyApplication.getToken(this).getUid());
         usersAPI.show(uid, mlistener);}
        else{
            tvusername.setText("立即登录");
        }
    }


    private RequestListener mlistener = new RequestListener() {

        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)) {
                user = User.parse(s);
                MyApplication.user=user;
                PicassoHelper.setimg(HomeActivity.this,user.getAvatar_large(),ivuserheader);
                tvusername.setText(user.getScreen_name());
                if(user.getGender().equals("m")){
                   ivusersex.setImageResource(R.drawable.userinfo_icon_male);
                }
                else{
                    ivusersex.setImageResource(R.drawable.userinfo_icon_female);
                }
                tvfansinfo.setText("关注 "+user.getFollowers_count()+" | "+"粉丝 "+user.getFriends_count());
               if(!"".equals(user.getDescription())){

                   tvgerenmiaoshu.setText(user.getDescription());
               }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

    private void initFragmentData() {
        fragmentList.add(new shouye());
        fragmentList.add(new meiwen());
        fragmentList.add(new huabang());
        viewPager.setAdapter(new HomeAdapter(getSupportFragmentManager(), fragmentList));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeincludeshouye:
                viewPager.setCurrentItem(0);
                break;
            case R.id.homeincludemeiwen:
                shouye.setChecked(false);
                viewPager.setCurrentItem(1);
                break;
            case R.id.homeincludetupian:
                viewPager.setCurrentItem(2);
                break;
            case R.id.tvusername:
                authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE); // 创建授权认证信息
                mSsoHandler = new SsoHandler(this, authInfo);
                mSsoHandler.authorizeWeb(new AuthListener(this));
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceive);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Exit:
                if(MyApplication.isLogin){
                new AlertDialog(this).builder().setTitle("提示").setMsg("确定要注销账号并退出吗？").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();}
                else{
                    ToastHelper.show(HomeActivity.this,"你还未登录账号");
                }
               break;

        }
        return true;
    }

    private void exit() {
        mAccessToken= AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            new LogoutAPI(this,MyApplication.appKey,mAccessToken).logout(new LogoutRequest());
        }
        else {
            ToastHelper.show(HomeActivity.this,"你还未登录账号");
        }

    }
    private class LogoutRequest implements RequestListener{


        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)){
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String result=jsonObject.optString("result");
                    if ("true".equalsIgnoreCase(result)) {
                        AccessTokenKeeper.clear(HomeActivity.this);
                        mAccessToken = null;
                    }
                    else if(!"".equalsIgnoreCase(jsonObject.optString("error"))){
                        AccessTokenKeeper.clear(HomeActivity.this);
                        mAccessToken = null;
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastHelper.show(HomeActivity.this,"系统异常注销失败");
        }
    }
}
