package com.wz.lanyue.banke;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wz.lanyue.banke.model.Artists;
import com.wz.lanyue.banke.model.HuaBang;
import com.wz.lanyue.banke.util.MyCountDown;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.widgetview.AlertDialog;
import com.wz.lanyue.banke.widgetview.CircleImageView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.loader.FileLoader;
import org.xutils.http.request.LocalFileRequest;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;


public class HuaBangContentActivity extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    VideoView videoView;
    FrameLayout frameLayout;
    TextView textView, tvhuabangtime;
    LinearLayout lltitle, llControl;
    ImageView ivtitleback, ivHuangBangPlay, ivHuaBangfull;
    String counttime = "00:00", times = "00:00";
    AppCompatSeekBar seekbarHuabang;
    Long duration;
    Thread thread;
    Long progress = 0L;
    SimpleDateFormat simpleDateFormat;
    boolean update = true;
    int id;
    TextView tvHuaBangYiRen, tvHuaBangTime, tvHuaBangMiaoshu;
    TextView tvvideoload;
    CircleImageView circleImageView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            times = simpleDateFormat.format(new Date((Long) msg.obj));
            tvhuabangtime.setText(times + "/" + counttime);
            seekbarHuabang.setProgress(Integer.parseInt(msg.obj.toString()));
        }
    };
    private HuaBang huaBang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_hua_bang_content);
        initViewData();
        showControlAndTitle();
    }

    private void showControlAndTitle() {
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llControl.setVisibility(View.VISIBLE);
                lltitle.setVisibility(View.VISIBLE);
                ivHuangBangPlay.setVisibility(View.VISIBLE);
                new MyCountDown(8 * 1000, 1000, lltitle, llControl, ivHuangBangPlay).start();
            }
        });
    }

    private void initViewData() {
        videoView = (VideoView) findViewById(R.id.vvhuabang);
        frameLayout = (FrameLayout) findViewById(R.id.FlHuaBang);
        textView = (TextView) findViewById(R.id.tvHuabBangContentTitle);
        lltitle = (LinearLayout) findViewById(R.id.lltitle);
        llControl = (LinearLayout) findViewById(R.id.llControl);
        ivtitleback = (ImageView) findViewById(R.id.ivtitleback);
        ivtitleback.setOnClickListener(this);
        ivHuangBangPlay = (ImageView) findViewById(R.id.ivHuangBangPlay);
        ivHuangBangPlay.setOnClickListener(this);
        tvhuabangtime = (TextView) findViewById(R.id.tvhuabangtime);
        seekbarHuabang = (AppCompatSeekBar) findViewById(R.id.seekbarHuabang);
        simpleDateFormat = new SimpleDateFormat("mm:ss");
        ivHuaBangfull = (ImageView) findViewById(R.id.ivHuaBangfull);
        ivHuaBangfull.setOnClickListener(this);
        tvHuaBangYiRen = (TextView) findViewById(R.id.tvHuaBangYiRen);
        tvHuaBangTime = (TextView) findViewById(R.id.tvHuaBangTime);
        tvHuaBangMiaoshu = (TextView) findViewById(R.id.tvHuaBangMiaoshu);
        circleImageView = (CircleImageView) findViewById(R.id.ccivHuaBangpic);
        tvvideoload= (TextView) findViewById(R.id.tvvideoload);

        initdata();

    }

    private void initdata() {
        intent = getIntent();
        huaBang = (HuaBang) intent.getSerializableExtra("huabang");
        id = huaBang.getId();
        textView.setText(huaBang.getTitle());
        tvHuaBangTime.setText("更新时间：" + huaBang.getRegdate());
        tvHuaBangYiRen.setText(huaBang.getArtistName());
        tvHuaBangMiaoshu.setText(huaBang.getDescription());
        setViewUrl(huaBang);
        videoView.setBufferSize(2*6*1024);
        ViedoCaoZuo();
        controlProgress();
        upadteProgress();
        requestData();
    }

    private void requestData() {
    String url=    MyApplication.getHuaBanghendpicUrl(id);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setCacheMaxAge(1000 * 60 * 60 * 24 * 7);
        x.http().get(requestParams, new Callback.CacheCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                requestdata(jsonObject);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(JSONObject jsonObject) {
                return false;
            }
        });

    }

    private void requestdata(JSONObject jsonObject) {
        if (Artists.getArtists(jsonObject) != null) {
            Artists artists = Artists.getArtists(jsonObject);
            String picurl = artists.getArtistAvatar();
            if (!"".equals(picurl)) {
                PicassoHelper.setimg(HuaBangContentActivity.this, picurl, circleImageView);
            }

        }
    }

    private void upadteProgress() {
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (update) {
                    try {
                        Thread.sleep(1000);
                        Long time = videoView.getCurrentPosition();
                        handler.obtainMessage(0, time).sendToTarget();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    private void controlProgress() {
        seekbarHuabang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    times = new SimpleDateFormat("mm:ss").format(new Date(progress));
                    tvhuabangtime.setText(times + "/" + counttime);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setViewUrl(HuaBang huaBang) {
        Uri uri = null;
        String uhdurl = huaBang.getUhdUrl();
        String hdurl = huaBang.getHdUrl();
        final String url = huaBang.getUrl();
        if (NetworkState.isWifi(this)) {
            if (!"".equals(uhdurl)) {
                uri = Uri.parse(uhdurl);
            } else if (!"".equals(hdurl)) {
                uri = Uri.parse(hdurl);
            } else if (!"".equals(url)) {
                uri = Uri.parse(url);
            } else {
                ToastHelper.show(HuaBangContentActivity.this, "获取视频资源失败");
            }
            videoView.setVideoURI(uri);
        } else {
            new AlertDialog(this).builder().setTitle("").setMsg("当前为移动网络，会消耗大量流量，确定要播放吗").setNegativeButton("否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).setPositiveButton("是", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"".equals(url)) {
                        videoView.setVideoURI(Uri.parse(url));
                    } else {
                        ToastHelper.show(HuaBangContentActivity.this, "获取视频资源失败");
                    }
                }
            }).show();

        }


    }

    private void ViedoCaoZuo() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp != null) {
                    new MyCountDown(8 * 1000, 1000, lltitle, llControl, ivHuangBangPlay).start();
                    duration = videoView.getDuration();
                    seekbarHuabang.setMax(Integer.parseInt(duration.toString()));
                    counttime = new SimpleDateFormat("mm:ss").format(new Date(duration));
                    tvhuabangtime.setText(times + "/" + counttime);
                    videoView.seekTo(progress);
                    ivHuangBangPlay.setImageResource(R.mipmap.pause_video);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FIT_PARENT, 0);
                    }
                    if (!thread.isAlive()) {
                        thread.start();
                    }
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp != null) {
                    ivHuangBangPlay.setImageResource(R.mipmap.play_video);
                }
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                    tvvideoload.setVisibility(View.GONE);
                    ivHuangBangPlay.setVisibility(View.VISIBLE);
                    videoView.start();
                }
                else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                    tvvideoload.setText("正在加载...");
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivtitleback:
                finish();
                break;
            case R.id.ivHuangBangPlay:
                playControl();
                break;
            case R.id.ivHuaBangfull:
                screenControl();
                break;
        }
    }

    private void screenControl() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ivHuaBangfull.setImageResource(R.mipmap.play_controller_narrow);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        } else {
            ivHuaBangfull.setImageResource(R.mipmap.play_controller_full);
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FIT_PARENT, 3);
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void playControl() {
        if (videoView.isPlaying()) {
            videoView.pause();
            ivHuangBangPlay.setImageResource(R.mipmap.play_video);
        } else {
            videoView.start();
            ivHuangBangPlay.setImageResource(R.mipmap.pause_video);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        update = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        progress = videoView.getCurrentPosition();
    }

}
