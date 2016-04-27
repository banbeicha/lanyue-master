package com.wz.lanyue.banke;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wz.lanyue.banke.model.MeiWen;
import com.wz.lanyue.banke.model.MeiWenList;
import com.wz.lanyue.banke.util.ActionBarSetting;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.NetworkState;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class MeiWenActivity extends AppCompatActivity {
    Intent intent;
    String type;
    int which;
    ArrayList<MeiWen> meiWenArrayList,meiWenLoadList;
    private XRecyclerView xRecyclerView;
    Myapapter myapapter;
    int minid=0;
    private RequestParams requestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shehui);
        initViewData();
    }

    private void initViewData() {
        xRecyclerView = (XRecyclerView) findViewById(R.id.xrlvshehui);
        xRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL, 1, Color.parseColor("#A3A3A3")));
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        xRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        ActionBarSetting.setTitle(getSupportActionBar(), "美文欣赏");
        intent = getIntent();
        which = intent.getIntExtra("Which", 0);
        type = intent.getStringExtra("Type");
        xRecyclerView.setLoadingListener(loadingListener);
        initdata(MyApplication.meiwenurl,1);
    }
     XRecyclerView.LoadingListener loadingListener=new XRecyclerView.LoadingListener() {
         @Override
         public void onRefresh() {
             initdata(MyApplication.meiwenurl,1);
         }

         @Override
         public void onLoadMore() {
             initdata(MyApplication.meiwenurl,2);
         }
     };
    private void initdata(String url, final int postcaozuo) {
         requestParams= new RequestParams(url);
        if (postcaozuo==1){
        requestParams.addBodyParameter("minId", "0");}
        else{
        requestParams.addBodyParameter("minId", minid+"");
        }
            requestParams.setCacheMaxAge(1000 * 60 * 60 * 24 * 7);
            requestParams.addBodyParameter("which", which + "");
            requestParams.addBodyParameter("type", type);
            requestParams.addBodyParameter("uid", 0 + "");
        x.http().post(requestParams, new Callback.CacheCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                MeiWenActivity.this.initdata(jsonObject, postcaozuo);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
             if (postcaozuo==1){
                 xRecyclerView.refreshComplete();
             }
                else {

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
                MeiWenActivity.this.initdata(jsonObject, postcaozuo);
                return !NetworkState.getNetworkState(MeiWenActivity.this);
            }
        });
    }

    private void initdata(JSONObject jsonObject, int postcaozuo) {
        if(postcaozuo==1){
        meiWenArrayList = MeiWenList.getnewsList(jsonObject);
        if (meiWenArrayList != null) {
            myapapter = new Myapapter();
            xRecyclerView.setAdapter(myapapter);
            xRecyclerView.refreshComplete();
         minid=meiWenArrayList.get(meiWenArrayList.size()-1).getAid();
        }}
        else if (postcaozuo==2){
            meiWenLoadList= MeiWenList.getnewsList(jsonObject);
            if (meiWenLoadList!=null){
                for (int i = 0; i <meiWenLoadList.size() ; i++) {
                    meiWenArrayList.add(meiWenLoadList.get(i));
                    myapapter.notifyItemInserted(meiWenArrayList.size());
                }
                xRecyclerView.loadMoreComplete();
                minid=meiWenArrayList.get(meiWenArrayList.size()-1).getAid();
            }
        }
    }

    private class Myapapter extends XRecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(MeiWenActivity.this).inflate(R.layout.news_list, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final MeiWen meiWen = meiWenArrayList.get(position);
            if (!"".equals(meiWen.getImage())) {
                PicassoHelper.noyuanjiaosetimg(MeiWenActivity.this, meiWen.getImage(), holder.ivnewspic);
               holder.ivnewspic.setVisibility(View.VISIBLE);
            }
            else{
                holder.ivnewspic.setVisibility(View.GONE);
            }
            holder.tvnewstitle.setText(meiWen.getTitle());
            holder.tvnewsdate.setText(meiWen.getAuthor() + "     " + DateUtil.getZHdate(meiWen.getDate()));
            ActionBarSetting.setTitle(getSupportActionBar(), meiWen.getAuthor());
            holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                startActivity(new Intent(MeiWenActivity.this, MeiWenContentActivity.class).putExtra("aid",meiWen.getAid()).putExtra("which",meiWen.getWhich()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return meiWenArrayList.size();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
