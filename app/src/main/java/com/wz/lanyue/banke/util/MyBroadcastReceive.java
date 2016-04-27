package com.wz.lanyue.banke.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BANBEICHAS on 2016/3/30.
 */
public class MyBroadcastReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       if(!NetworkState.getNetworkState(context)){
           ToastHelper.show(context,"网络不可用，请检查网络设置！");
       }
    }
}
