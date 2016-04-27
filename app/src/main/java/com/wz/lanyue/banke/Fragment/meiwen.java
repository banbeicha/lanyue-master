package com.wz.lanyue.banke.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.wz.lanyue.banke.MeiWenActivity;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.model.MeiWenType;
import com.wz.lanyue.banke.model.MeiWenTypeList;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;



/**
 * Created by BANBEICHAS on 2016/4/15.
 */
public class meiwen extends Fragment {

    RecyclerView recyclerView;
    Context context;
    ArrayList<MeiWenType> meiWenTypeArrayList;
    private RequestParams requestParams;
    MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meiwentype, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        context = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.rlvmeiwentype);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        requestParams = new RequestParams(MyApplication.meiwentypeurl);
        requestParams.setCacheMaxAge(1000*60*60*24*7);
        initdata();
    }

    private void initdata() {
        x.http().post(requestParams, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                meiWenTypeArrayList = MeiWenTypeList.getnewsList(jsonObject);
                if (meiWenTypeArrayList!=null){
                myAdapter=new MyAdapter();
                recyclerView.setAdapter(myAdapter);}
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
                meiWenTypeArrayList = MeiWenTypeList.getnewsList(jsonObject);
                if (meiWenTypeArrayList!=null){
                    myAdapter=new MyAdapter();
                    recyclerView.setAdapter(myAdapter);}
                return !NetworkState.getNetworkState(context);
            }
        });
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.meiwentypelist, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final MeiWenType meiWenType = meiWenTypeArrayList.get(position);

                if (!"".equals(meiWenType.getImage())) {
                    PicassoHelper.noyuanjiaosetimg(context, meiWenType.getImage(), holder.ivmeiwentypepic);
                }
                holder.tvmeiwentyprtitle.setText(meiWenType.getTitle());
             holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MeiWenActivity.class).putExtra("Which",meiWenType.getWhich()).putExtra("Type",meiWenType.getType()));
                }
            });


        }

        @Override
        public int getItemCount() {
            return meiWenTypeArrayList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivmeiwentypepic;
        TextView tvmeiwentyprtitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivmeiwentypepic = (ImageView) itemView.findViewById(R.id.ivmeiwentypepic);
            tvmeiwentyprtitle = (TextView) itemView.findViewById(R.id.tvmeiwentyprtitle);
        }
    }

}
