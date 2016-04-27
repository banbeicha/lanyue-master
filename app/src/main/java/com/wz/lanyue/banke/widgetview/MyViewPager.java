package com.wz.lanyue.banke.widgetview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by BANBEICHA on 2016/3/28.
 */
public class MyViewPager extends ViewPager {



    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {

        return false;

    }


}
