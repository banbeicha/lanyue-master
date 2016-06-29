package com.wz.lanyue.banke.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.NewsContentActivity;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.DuZhe;
import com.wz.lanyue.banke.model.DuZheList;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by BANBEICHAS on 2016/5/25.
 */
public class duzhe extends Fragment {
    private RequestParams request;
    ArrayList<DuZhe> arrayList, moreArrayList;
    private XRecyclerView xRecyclerView;
    private int i = 2;
    private Myapapter myapapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shehui, container, false);
        bindview(v);
        getdata(0);
        return v;
    }

    private void bindview(View v) {
        xRecyclerView = (XRecyclerView) v.findViewById(R.id.xrlvshehui);
        xRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.HORIZONTAL, 1, Color.parseColor("#A3A3A3")));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getdata(0);
            }

            @Override
            public void onLoadMore() {
                getdata(1);
            }
        });
    }

    private void getdata(final int isMore) {
        if (isMore == 0) {
            request = new RequestParams(String.format("%s%d%s", MyApplication.DuZhe + MyApplication.DuZheListurl, 1, MyApplication.DuZheCookie) + new Random().nextInt(10000));
        } else {
            request = new RequestParams(String.format("%s%d%s", MyApplication.DuZhe + MyApplication.DuZheListurl, i, MyApplication.DuZheCookie) + new Random().nextInt(10000));
        }
        request.setCacheMaxAge(1000 * 60 * 60 * 24 * 7);
        x.http().get(request, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String s) {
                initdata(s, isMore);

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
            public boolean onCache(String s) {
                initdata(s, isMore);
                return !NetworkState.getNetworkState(getContext());
            }
        });


    }

    private void initdata(String s, int isMore) {
        try {
            if (isMore == 0) {
                if (s != null) {
                    arrayList = DuZheList.getDuZheList(new JSONArray(s));
                    myapapter = new Myapapter();
                    xRecyclerView.setAdapter(myapapter);
                } else {
                    ToastHelper.show(getContext(), "请求数据为空，请重试");
                }
                xRecyclerView.refreshComplete();
            } else {
                moreArrayList = DuZheList.getDuZheList(new JSONArray(s));
                if (moreArrayList != null) {
                    for (DuZhe duZhe :
                            moreArrayList) {
                        arrayList.add(duZhe);
                        myapapter.notifyItemInserted(arrayList.size());

                    }
                    i++;
                }
                xRecyclerView.loadMoreComplete();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class Myapapter extends XRecyclerView.Adapter<MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.news_list, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final DuZhe duZhe = arrayList.get(position);
            holder.tvnewstitle.setText(duZhe.getTitle());
            String date = duZhe.getDate();
            date = getDate(date);
            holder.tvnewsdate.setText(duZhe.getC_title() + "  " + DateUtil.getZHdate(date));
            if (!"".equals(duZhe.getImg())) {
                PicassoHelper.noyuanjiaosetimg(getContext(), duZhe.getImg(), holder.ivnewspic);
                holder.ivnewspic.setVisibility(View.VISIBLE);
            } else {
                holder.ivnewspic.setVisibility(View.GONE);
            }
            holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(getActivity(), NewsContentActivity.class).putExtra("DuZhe", duZhe).putExtra("isnews", false));
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }

    @NonNull
    public String getDate(String date) {
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(date);
        date = mat.replaceAll("-") + ":00";
        StringBuffer s = new StringBuffer(date);
        date = s.replace(date.lastIndexOf("-"), date.lastIndexOf("-") + 1, "").toString();
        date = s.replace(date.lastIndexOf("-"), date.lastIndexOf("-") + 1, "").toString();
        return date;
    }

    private class MyViewHolder extends XRecyclerView.ViewHolder {
        public ImageView ivnewspic;
        public TextView tvnewstitle;
        public TextView tvnewsdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivnewspic = (ImageView) itemView.findViewById(R.id.ivnewspic);
            tvnewstitle = (TextView) itemView.findViewById(R.id.tvnewstitle);
            tvnewsdate = (TextView) itemView.findViewById(R.id.tvnewsdate);
        }
    }
}
