package com.wz.lanyue.banke.util;

import android.support.v7.app.ActionBar;

/**
 * Created by BANBEICHAS on 2016/4/8.
 */
public class ActionBarSetting {

    public static void setTitle(ActionBar actionBar,String title){
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(title);

    }

}
