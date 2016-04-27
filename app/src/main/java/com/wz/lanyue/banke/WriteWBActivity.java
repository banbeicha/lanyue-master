package com.wz.lanyue.banke;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.sina.weibo.sdk.api.share.ui.EditBlogView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.squareup.picasso.Picasso;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.widgetview.AlertDialog;

import java.io.File;

public class WriteWBActivity extends AppCompatActivity implements OnClickListener, ActionSheet.ActionSheetListener {
    ImageView ivfaweibopic, ivdelfaweibopic;
    TextView tvaddpic;

    EditBlogView etfaweibocontent;
    private Intent intent;
    boolean hadpic;
    private Bitmap bitmap;
    private StatusesAPI statusesAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_wb);
        initview();
    }

    private void initview() {
        ivfaweibopic = (ImageView) findViewById(R.id.ivfaweibopic);
        ivdelfaweibopic = (ImageView) findViewById(R.id.ivdelfaweibopic);
        tvaddpic = (TextView) findViewById(R.id.tvaddpic);
        etfaweibocontent = (EditBlogView) findViewById(R.id.etfaweibocontent);
        ivfaweibopic.setOnClickListener(this);
        ivdelfaweibopic.setOnClickListener(this);
        ivfaweibopic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ivfaweibopic.setAnimation(AnimationUtils.loadAnimation(WriteWBActivity.this,R.anim.scale));
                ivdelfaweibopic.setVisibility(View.VISIBLE);
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{100, 360}, -1);
                return true;
            }
        });
        ivfaweibopic.setLongClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvcancelfabiao:
                finish();
                break;
            case R.id.tvfabiao:
                statusesAPI = new StatusesAPI(this, MyApplication.appKey, MyApplication.getToken(this));
                WeiBoUpload();
                break;
            case R.id.ivfaweibopic:
                showActionSheet();
                break;
            case R.id.ivdelfaweibopic:
                ShowDialog();
                break;
        }
    }

    private void ShowDialog() {
        new AlertDialog(this).builder().setTitle("").setMsg("放弃上传这张照片吗").setNegativeButton("否", new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setPositiveButton("是", new OnClickListener() {
            @Override
            public void onClick(View v) {
                ivfaweibopic.setImageResource(R.drawable.addpic);
                tvaddpic.setText("添加照片");
                ivfaweibopic.setLongClickable(false);
                ivdelfaweibopic.setVisibility(View.GONE);
                hadpic = false;
            }
        }).show();
    }

    private void WeiBoUpload() {
        RequestListener requestListener = new RequestListener() {
            @Override
            public void onComplete(String s) {
                ToastHelper.show(WriteWBActivity.this, "发表成功");
                finish();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastHelper.show(WriteWBActivity.this, "发表失败");
            }
        };
        String content = etfaweibocontent.getText().toString();
        if (!"".equals(content)) {
            if (hadpic) {

                statusesAPI.upload(content, bitmap, "0.0", "0.0", requestListener);
            } else {
                statusesAPI.update(content, "0.0", "0.0", requestListener);
            }
        } else {
            ToastHelper.show(this, "发表内容不能为空");
        }
    }

    private void showActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager()).
                setCancelButtonTitle("取消").
                setOtherButtonTitles("拍照", "从相册中选择").
                setCancelableOnTouchOutside(true).
                setListener(this).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 0:
                    bitmap = (Bitmap) data.getParcelableExtra("data");
                    if (bitmap != null) {
                        ivfaweibopic.setImageBitmap(bitmap);
                        ivfaweibopic.setLongClickable(true);
                        tvaddpic.setText("");
                        hadpic = true;
                    }
                    break;
                case 1:
                    Uri uri = data.getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), uri, new String[]{MediaStore.Images.Media.DATA}, null, null);
                        // 获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头
                        cursor.moveToFirst();
                        String path = cursor.getString(column_index);
                        if (!"".equals(path) && path != null) {
                            Picasso.with(this).load(new File(path)).into(ivfaweibopic);
                            ivfaweibopic.setLongClickable(true);
                            tvaddpic.setText("");
                            hadpic = true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        switch (index) {
            case 0:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, 0);
                break;
            case 1:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;

        }
    }
}
