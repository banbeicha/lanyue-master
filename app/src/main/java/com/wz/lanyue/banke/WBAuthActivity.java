package com.wz.lanyue.banke;


import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro2;
import com.wz.lanyue.banke.Fragment.LoginYingDaoYe;
import com.wz.lanyue.banke.Fragment.Yindaoye0;
import com.wz.lanyue.banke.Fragment.Yindaoye1;
import com.wz.lanyue.banke.Fragment.Yindaoye2;

public class WBAuthActivity extends AppIntro2 {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addSlide(new Yindaoye0());
        addSlide(new Yindaoye1());
        addSlide(new Yindaoye2());
        addSlide(new LoginYingDaoYe());
        setProgressButtonEnabled(false);
        setZoomAnimation();
    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

   @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
