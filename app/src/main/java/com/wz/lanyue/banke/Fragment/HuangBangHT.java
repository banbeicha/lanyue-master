package com.wz.lanyue.banke.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.data.InItViewhuaBangData;


/**
 * Created by BANBEICHAS on 2016/4/19.
 */
public class HuangBangHT extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.huabangs,container,false);
        new InItViewhuaBangData().initview(getActivity(),"HT",view);
        return view;

    }
}
