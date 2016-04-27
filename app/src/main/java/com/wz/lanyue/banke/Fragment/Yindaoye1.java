package com.wz.lanyue.banke.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wz.lanyue.banke.R;


/**
 * Created by BANBEICHA on 2016/3/29.
 */
public class Yindaoye1 extends Fragment {
    LinearLayout bg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=     inflater.inflate(R.layout.yingdaoye0,container,false);
        bg= (LinearLayout) v.findViewById(R.id.bg);
        bg.setBackgroundResource(R.mipmap.ic_guide_02);
        return v;
    }
}
