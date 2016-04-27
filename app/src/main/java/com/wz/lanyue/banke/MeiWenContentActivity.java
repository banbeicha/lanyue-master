package com.wz.lanyue.banke;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.wz.lanyue.banke.util.ActionBarSetting;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.TextAndPic;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MeiWenContentActivity extends AppCompatActivity {
Intent intent;
    int aid;
    int which;
    TextView tvmeiwentitle,tvmeiwenauthor,tvmeiwenliulanshu,tvmeiwencontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mei_wen_content);
        initviewdata();
    }

    private void initviewdata() {
        intent=getIntent();
        aid=intent.getIntExtra("aid",0);
        which=intent.getIntExtra("which",0);
        tvmeiwentitle= (TextView) findViewById(R.id.tvmeiwentitle);
        tvmeiwenauthor= (TextView) findViewById(R.id.tvmeiwenauthor);
        tvmeiwenliulanshu= (TextView) findViewById(R.id.tvmeiwenliulanshu);
        tvmeiwencontent= (TextView) findViewById(R.id.tvmeiwencontent);
        ActionBarSetting.setTitle(getSupportActionBar(),"美文内容");
        initdata();
    }

    private void initdata() {
        RequestParams requestParams=new RequestParams(MyApplication.meiwencontenturl);
        requestParams.setCacheMaxAge(1000*60*60*24*7);
        requestParams.addBodyParameter("uid",0+"");
        requestParams.addBodyParameter("which",which+"");
        requestParams.addBodyParameter("aid",aid+"");
       x.http().post(requestParams, new Callback.CacheCallback<JSONObject>() {
           @Override
           public void onSuccess(JSONObject jsonObject) {
               MeiWenContentActivity.this.initdata(jsonObject);

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
               MeiWenContentActivity.this.initdata(jsonObject);

               return !NetworkState.getNetworkState(MeiWenContentActivity.this);
           }
       });

    }

    private void initdata(JSONObject jsonObject) {
        if ("success".equals(jsonObject.optString("flag"))){
            try {
                JSONObject article= jsonObject.getJSONObject("result").getJSONObject("article");
                tvmeiwentitle.setText(article.optString("title"));
              String author=  article.optString("author");
                ActionBarSetting.setTitle(getSupportActionBar(),author);
                tvmeiwenauthor.setText(author+"    "+ DateUtil.getZHdate(article.optString("date")));
                tvmeiwenliulanshu.setText("浏览"+" "+article.optString("count"));
               setContentTextView(article.optString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setContentTextView(final String content) {
        AsyncTask<CharSequence,CharSequence,CharSequence> asyncTask=new AsyncTask<CharSequence, CharSequence, CharSequence>() {
            @Override
            protected CharSequence doInBackground(CharSequence... params) {
                return TextAndPic.getContentPic(content,MeiWenContentActivity.this);
            }

            @Override
            protected void onPostExecute(CharSequence charSequence) {
                tvmeiwencontent.setText(charSequence);
                super.onPostExecute(charSequence);
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
