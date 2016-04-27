package com.wz.lanyue.banke.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.wz.lanyue.banke.HomeActivity;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.AccessTokenKeeper;
import com.wz.lanyue.banke.util.AuthListener;
import com.wz.lanyue.banke.util.Constants;

/**
 * Created by BANBEICHA on 2016/3/25.
 */
public class LoginYingDaoYe extends Fragment implements View.OnClickListener{
    private TextView tvlogin,tvsee;
    private AuthInfo authInfo;
    private SsoHandler mSsoHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     View view= inflater.inflate(R.layout.yindaoyeimg,container,false);
        tvlogin= (TextView) view.findViewById(R.id.tvlogin);
        tvsee= (TextView) view.findViewById(R.id.tvsee);
        authInfo = new AuthInfo(getContext(), Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE); // 创建授权认证信息
        mSsoHandler = new SsoHandler(getActivity(), authInfo);
        tvlogin.setOnClickListener(this);
        tvsee.setOnClickListener(this);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.tvlogin:
        mSsoHandler.authorizeWeb(new AuthListener(getContext()));
            getActivity().finish();

            break;
            case R.id.tvsee:
                startActivity(new Intent(getActivity(), HomeActivity.class));
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("see", Context.MODE_PRIVATE);
               sharedPreferences.edit().putBoolean("isFist",true).commit();
                MyApplication.isLogin=false;
                break;
        }
    }
}
