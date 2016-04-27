package com.wz.lanyue.banke.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wz.lanyue.banke.HomeActivity;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.AccessTokenKeeper;


/**
 * Created by BANBEICHA on 2016/3/29.
 */
public class Yindaoye0 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(AccessTokenKeeper.readAccessToken(getContext()).isSessionValid()){
            startActivity(new Intent(getActivity(), HomeActivity.class));
            MyApplication.isLogin=true;
        }
           boolean isfist=    getActivity().getSharedPreferences("see", Context.MODE_PRIVATE).getBoolean("isFist",false);
           if(isfist){
               startActivity(new Intent(getActivity(), HomeActivity.class));
               MyApplication.isLogin=false;
           }

        return inflater.inflate(R.layout.yingdaoye0,container,false);
    }
}
