package com.wz.lanyue.banke.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by BANBEICHAS on 2016/3/30.
 */
public class NetworkState {


    public static boolean getNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {

            return networkInfo.isConnected();

        }
        return false;

    }

    public static boolean isWifi(Context context) {
        if (getNetworkState(context)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
            return false;
        }

        return false;
    }
}
