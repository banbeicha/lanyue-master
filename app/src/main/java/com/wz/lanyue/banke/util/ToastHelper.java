package com.wz.lanyue.banke.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by BANBEICHA on 2016/3/25.
 */
public class ToastHelper {
    private static Toast toast;
    Context context;


    public static void show(Context context,String message){
     if(toast==null){
         toast=Toast.makeText(context,message,Toast.LENGTH_SHORT);
     }
    else{
         toast.setText(message);

     }
        toast.show();

    }


}
