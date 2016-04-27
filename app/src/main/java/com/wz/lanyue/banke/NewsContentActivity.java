package com.wz.lanyue.banke;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wz.lanyue.banke.model.MyPlatform;
import com.wz.lanyue.banke.model.News;
import com.wz.lanyue.banke.util.ActionBarSetting;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.TextAndPic;

import org.jsoup.Jsoup;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;

public class NewsContentActivity extends AppCompatActivity {
    Intent intent;
    String title, url, date, description, id;
    TextView tvnewstitle, tvnewsdate, tvnewscontent;
    QuickCommentBar qbr;
    private OnekeyShare oks;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        initdata();
    }

    private void Sharedsdk() {
        ShareSDK.initSDK(this);
        ShareSDK.registerService(Socialization.class);
        initOnekeyShare();
        initqbr();
    }

    private void initqbr() {
        qbr = (QuickCommentBar) findViewById(R.id.qbr);
        qbr.getBackButton().setVisibility(View.GONE);
        qbr.setOnekeyShare(oks);
        if (title.length() > 18) {
            qbr.setTopic(id, title.substring(0, 16) + "...", date, description);
        } else {
            qbr.setTopic(id, title, date, description);
        }

        qbr.setAuthedAccountChangeable(false);
        Socialization service = ShareSDK.getService(Socialization.class);
        service.setCustomPlatform(new MyPlatform(this));
    }

    private void initOnekeyShare() {
        oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setTitleUrl(url);
        oks.setUrl(url);
        oks.setSite("览阅");
        oks.setVenueName("览阅");
        oks.disableSSOWhenAuthorize();
    }

    private void initdata() {
        intent = getIntent();
        news = (News) intent.getSerializableExtra("news");
        title = news.getTitle();
        date = DateUtil.getZHdate(news.getCtime());
        url = news.getUrl();
        id = news.getCtime() + title;
        description = news.getDescription();
        ActionBarSetting.setTitle(getSupportActionBar(), description);
        tvnewstitle = (TextView) findViewById(R.id.tvnewstitle);
        tvnewsdate = (TextView) findViewById(R.id.tvnewsdate);
        tvnewscontent = (TextView) findViewById(R.id.tvnewscontent);
        tvnewstitle.setText(title);
        tvnewsdate.setText(description + "  " + date);
        Sharedsdk();
        asyncTask.execute();
    }

    private AsyncTask<CharSequence, CharSequence, CharSequence> asyncTask = new AsyncTask<CharSequence, CharSequence, CharSequence>() {
        @Override
        protected void onPostExecute(CharSequence s) {
            tvnewscontent.setText(s);
        }
        @Override
        protected CharSequence doInBackground(CharSequence... params) {
            try {
                String txt = Jsoup.connect(url).get().toString();
                return TextAndPic.getTextPic(txt, NewsContentActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
