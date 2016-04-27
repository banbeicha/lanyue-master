package com.wz.lanyue.banke.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import com.thefinestartist.finestwebview.FinestWebView;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.WeChatSelection;
import com.wz.lanyue.banke.model.WeChatSelectionList;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by BANBEICHA on 2016/3/28.
 */
public class weixinjingxuan extends Fragment {
    XRecyclerView xRecyclerView;
    private Context context;
    ArrayList<WeChatSelection> weChatSelectionList, loadMorelist;
    Myapapter myapapter;
    int page = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shehui, container, false);
        initviewdata(view);
        return view;
    }

    private void initviewdata(View view) {
        context = getContext();
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.xrlvshehui);
        xRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayout.HORIZONTAL, 1, Color.parseColor("#A3A3A3")));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        initdata(1);
        xRecyclerView.setLoadingListener(loadingListener);

    }

    XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            initdata(1);
        }

        @Override
        public void onLoadMore() {
            initdata(2);
        }
    };

    private void initdata(final int type) {
        RequestParams params = new RequestParams(MyApplication.weixinurl);
        params.addBodyParameter("key", "ba489b27077289fc1094b8df52572d11");
        params.setCacheMaxAge(1000*60*60*24*7);
        if (type == 1) {
            params.addBodyParameter("pno", 1 + "");
        } else {
            params.addBodyParameter("pno", page + "");
        }
        x.http().get(params, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                weixinjingxuan.this.initdata(jsonObject, type);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

                if (type == 1) {
                    xRecyclerView.refreshComplete();
                }
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
                weixinjingxuan.this.initdata(jsonObject, type);
                return !NetworkState.getNetworkState(context);
            }
        });

    }

    private void initdata(JSONObject jsonObject, int type) {
        if (type == 1) {

            weChatSelectionList = WeChatSelectionList.getWechatSelectionlist(jsonObject);
            if (weChatSelectionList != null) {
                myapapter = new Myapapter();
                xRecyclerView.setAdapter(myapapter);
                xRecyclerView.refreshComplete();
            }
        } else {
            loadMorelist = WeChatSelectionList.getWechatSelectionlist(jsonObject);
            int size = loadMorelist.size();
            if (loadMorelist != null && size > 0) {
                for (int i = 0; i < size; i++) {

                    weChatSelectionList.add(loadMorelist.get(i));
                    myapapter.notifyItemInserted(weChatSelectionList.size());
                }
                xRecyclerView.loadMoreComplete();
                page++;

            }
        }
    }

    private class Myapapter extends XRecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.news_list, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final WeChatSelection weChatSelection = weChatSelectionList.get(position);
            if (!"".equals(weChatSelection.getFirstImg())) {
                PicassoHelper.noyuanjiaosetimg(context, weChatSelection.getFirstImg(), holder.ivnewspic);
                holder.ivnewspic.setVisibility(View.VISIBLE);
            }
            else {
                holder.ivnewspic.setVisibility(View.GONE);
            }
            holder.tvnewstitle.setText(weChatSelection.getTitle());
            holder.tvnewsdate.setText(weChatSelection.getSource());
            holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FinestWebView.Builder(context).toolbarColor(getResources().getColor(R.color.hongse)).iconDefaultColor(Color.parseColor("#FFFFFF")).progressBarColor(Color.parseColor("#F2B4AE")).titleDefault(weChatSelection.getSource()).urlColor(Color.parseColor("#FFFFFF")).toolbarScrollFlags(0).show(weChatSelection.getUrl());
                }
            });
        }

        @Override
        public int getItemCount() {
            return weChatSelectionList.size();
        }
    }

    public class MyViewHolder extends XRecyclerView.ViewHolder {
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
