package com.wz.lanyue.banke.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.data.InitViewNewsData;


/**
 * Created by BANBEICHAS on 2016/4/13.
 */
public class health extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shehui, container, false);
        new InitViewNewsData().initview(v,getContext(), MyApplication.healthurl);
        return v;
    }
}
