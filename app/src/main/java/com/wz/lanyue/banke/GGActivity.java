package com.wz.lanyue.banke;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.qq.e.ads.cfg.MultiProcessFlag;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;


public class GGActivity extends AppCompatActivity implements SplashADListener {
    private FrameLayout frameLayout;
    private SplashAD splashAD;
    public static String APPID = "1105373885";
    private String SplashPosID = "7020116197858844";
    boolean isshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gg);
        MultiProcessFlag.setMultiProcess(true);
        frameLayout = (FrameLayout) findViewById(R.id.ll_ad);
        ShowAD();
    }

    private void ShowAD() {
        splashAD = new SplashAD(GGActivity.this, frameLayout, APPID, SplashPosID, GGActivity.this);
    }

    private void GoInHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onADDismissed() {
        GoInHome();
    }

    @Override
    public void onNoAD(int i) {
        System.out.println("广告加载失败" + i);
        GoInHome();
    }

    @Override
    public void onADPresent() {

    }

    @Override
    public void onADClicked() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

