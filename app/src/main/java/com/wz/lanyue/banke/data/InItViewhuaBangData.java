package com.wz.lanyue.banke.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wz.lanyue.banke.HuaBangContentActivity;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.HuaBang;
import com.wz.lanyue.banke.model.HuaBangList;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.util.ToastHelper;


import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by BANBEICHAS on 2016/4/20.
 */
public class InItViewhuaBangData implements ActionSheet.ActionSheetListener {
    XRecyclerView xRecyclerView;
    FragmentActivity context;
    ArrayList<HuaBang> huaBangArrayList,loadMoreHuaBangArrayList;
    MyAdapter myAdapter;
    private RequestParams requestParams;
    int number=20;
    int flag;
    public void initview( FragmentActivity context, String type, View view) {
        this.context = context;
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.rlvhuangbang);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        initdata(type, 0);
        initdata(type);


    }

    private void initdata(final String type) {
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initdata(type, 0);
            }

            @Override
            public void onLoadMore() {
                initdata(type, 1);

            }
        });

    }

    private void initdata(String type, final int i) {
        if (i==0){
        requestParams = new RequestParams(MyApplication.getHuaBangtUrl(type, 0, 20));
        }
        else if (i==1){
            requestParams = new RequestParams(MyApplication.getHuaBangtUrl(type, number, 20));

        }
        requestParams.setCacheMaxAge(1000 * 60 * 60 * 24 * 7);
        x.http().get(requestParams, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                InItViewhuaBangData.this.initdata(jsonObject, i);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
               if(i==0){
                   xRecyclerView.refreshComplete();
               }
                else{
                   xRecyclerView.loadMoreComplete();
               }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(JSONObject jsonObject) {
                InItViewhuaBangData.this.initdata(jsonObject, i);
                return !NetworkState.getNetworkState(context);
            }
        });
    }

    private void initdata(JSONObject jsonObject, int i) {
        if(i==0){
        if (!"".equals(jsonObject) && jsonObject != null) {
            huaBangArrayList = HuaBangList.getnewsList(jsonObject);
            myAdapter = new MyAdapter();
            xRecyclerView.setAdapter(myAdapter);
        }
        xRecyclerView.refreshComplete();
        }
        else if (i==1){
            if (!"".equals(jsonObject) && jsonObject != null) {
                loadMoreHuaBangArrayList = HuaBangList.getnewsList(jsonObject);
                for (int j = 0; j <loadMoreHuaBangArrayList.size() ; j++) {
                    huaBangArrayList.add(loadMoreHuaBangArrayList.get(j));
                    myAdapter.notifyItemInserted(huaBangArrayList.size());
                }
                number+=loadMoreHuaBangArrayList.size();

            }
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
             switch (index){
               case 0: shared(); break;
                 case 1:donwLoad();break;
             }
    }

    private void donwLoad() {

    }

    private void shared() {
        OnekeyShare oks = new OnekeyShare();
      HuaBang huaBang=  huaBangArrayList.get(flag);
        oks.setTitle(huaBang.getTitle());
        oks.setTitleUrl(huaBang.getUrl());
        oks.setText(huaBang.getTitle()+huaBang.getUrl()+"来自半刻MV分享");
        oks.setSite("半刻");
        oks.setVenueName("半刻");
       oks.show(context);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.huabang_list, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final HuaBang huaBang = huaBangArrayList.get(position);
            if (!"".equals(huaBang.getAlbumImg())) {
                PicassoHelper.noyuanjiaosetimg(context, huaBang.getAlbumImg(), holder.ivhuabangbg);
            }
            holder.tvhuabangtitle.setText(huaBang.getTitle());
            holder.tvhuabangauthor.setText(huaBang.getArtistName());
            holder.tvhuabangbofangcishu.setText("播放次数："+huaBang.getTotalViews());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkState.getNetworkState(context)){
                    context.startActivity(new Intent(context, HuaBangContentActivity.class).putExtra("huabang",huaBang));}
                    else {
                        ToastHelper.show(context,"网络不可用");
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
              ActionSheet.createBuilder(context,context.getSupportFragmentManager()).setCancelButtonTitle("取消").setOtherButtonTitles("分享","下载").setCancelableOnTouchOutside(true).setListener(InItViewhuaBangData.this).show();
                flag=position;
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return huaBangArrayList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvhuabangtitle, tvhuabangauthor, tvhuabangbofangcishu;
        ImageView ivhuabangbg;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvhuabangtitle = (TextView) itemView.findViewById(R.id.tvhuabangtitle);
            tvhuabangauthor = (TextView) itemView.findViewById(R.id.tvhuabangauthor);
            tvhuabangbofangcishu = (TextView) itemView.findViewById(R.id.tvhuabangbofangcishu);
            ivhuabangbg = (ImageView) itemView.findViewById(R.id.ivhuabangbg);

        }
    }
}
