package com.wz.lanyue.banke.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.NewsContentActivity;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.News;
import com.wz.lanyue.banke.model.NewsList;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;


import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;



/**
 * Created by BANBEICHAS on 2016/4/13.
 */
public class InitViewNewsData {
    private  XRecyclerView xRecyclerView;
   private  Context context;
    private   ArrayList<News> newsArrayList,moreNewsArrayList;
    private  Myapapter  myApapter;
    private RequestParams requestParams;
    private int page=2;
    public  void initview(View v, Context context,String url) {
        this.context = context;
        xRecyclerView = (XRecyclerView) v.findViewById(R.id.xrlvshehui);
        xRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayout.HORIZONTAL, 1, Color.parseColor("#A3A3A3")));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        initdata(url);
        shuaxindata(url);
    }

    private  void shuaxindata(final String url) {
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {


            @Override
            public void onRefresh() {
                initdata(url);
            }

            @Override
            public void onLoadMore() {
                jiazaigengduodata(url);
            }
        });

    }

    private  void jiazaigengduodata(String url) {
        requestParams=new RequestParams(url);
        requestParams.addHeader("apikey", MyApplication.apikey);
        requestParams.addBodyParameter("page",page+"");
        requestParams.addBodyParameter("num",30+"");
        requestParams.setCacheMaxAge(1000*60*60*24*7);
        x.http().get(requestParams, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LoadMore(jsonObject);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                xRecyclerView.loadMoreComplete();
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(JSONObject jsonObject) {
                LoadMore(jsonObject);
                return !NetworkState.getNetworkState(context);
            }
        });
    }

    private void LoadMore(JSONObject jsonObject) {
        moreNewsArrayList= NewsList.getnewsList(jsonObject);
        if (moreNewsArrayList!=null){
            for (int i=0;i<moreNewsArrayList.size();i++){
                newsArrayList.add(moreNewsArrayList.get(i));
                myApapter.notifyItemInserted(newsArrayList.size());
            }
            page++;
            xRecyclerView.loadMoreComplete();

        }
        else{
            xRecyclerView.loadMoreComplete();
        }
    }

    private  void initdata(String url) {
        requestParams=new RequestParams(url);
        requestParams.addHeader("apikey", MyApplication.apikey);
        requestParams.addBodyParameter("page",1+"");
        requestParams.addBodyParameter("num",30+"");
        requestParams.setCacheMaxAge(1000*60*60*24*7);
        x.http().get(requestParams, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                InitViewNewsData.this.initdata(jsonObject);

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(JSONObject jsonObject) {

                InitViewNewsData.this.initdata(jsonObject);

                return !NetworkState.getNetworkState(context);
            }
        });

    }

    private void initdata(JSONObject jsonObject) {
        newsArrayList= NewsList.getnewsList(jsonObject);
        if(newsArrayList!=null){
            myApapter=new Myapapter();
            xRecyclerView.setAdapter(myApapter);
            xRecyclerView.refreshComplete();
        }
        else{
            xRecyclerView.refreshComplete();
        }
    }

    private  class Myapapter extends XRecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.news_list, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final News news = newsArrayList.get(position);
            if(!"".equals( news.getPicUrl())){
            PicassoHelper.noyuanjiaosetimg(context, news.getPicUrl(), holder.ivnewspic);
                holder.ivnewspic.setVisibility(View.VISIBLE);
            }
            else {
                holder.ivnewspic.setVisibility(View.GONE);
            }
            holder.tvnewstitle.setText(news.getTitle());
            holder.tvnewsdate.setText(news.getDescription() + "  " + DateUtil.getZHdate(news.getCtime()));
            holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 context.startActivity(new Intent(context, NewsContentActivity.class).putExtra("news",news));
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsArrayList.size();
        }
    }

    private  class MyViewHolder extends XRecyclerView.ViewHolder {
        ImageView ivnewspic;
        TextView tvnewstitle, tvnewsdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivnewspic = (ImageView) itemView.findViewById(R.id.ivnewspic);
            tvnewstitle = (TextView) itemView.findViewById(R.id.tvnewstitle);
            tvnewsdate = (TextView) itemView.findViewById(R.id.tvnewsdate);
        }
    }
}
