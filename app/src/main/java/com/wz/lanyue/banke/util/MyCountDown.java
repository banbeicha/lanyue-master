package com.wz.lanyue.banke.util;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.concurrent.CountDownLatch;

/**
 * Created by BANBEICHAS on 2016/4/21.
 */
public class MyCountDown extends CountDownTimer {
    LinearLayout lltitle, lltcontrol;
    ImageView imageView;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public MyCountDown(long millisInFuture, long countDownInterval, LinearLayout lltitle,LinearLayout lltcontrol,ImageView imageView) {
        super(millisInFuture, countDownInterval);
        this.lltcontrol=lltcontrol;
        this.lltitle=lltitle;
        this.imageView=imageView;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        this.lltcontrol.setVisibility(View.GONE);
        this.lltitle.setVisibility(View.GONE);
        this.imageView.setVisibility(View.GONE);

    }
}
