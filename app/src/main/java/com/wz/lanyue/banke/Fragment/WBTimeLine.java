package com.wz.lanyue.banke.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.sina.weibo.sdk.api.share.ui.EditBlogView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.WBComment;
import com.wz.lanyue.banke.WriteWBActivity;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.util.StringUtil;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.util.UserAPIhelper;
import com.wz.lanyue.banke.widgetview.CircleImageView;
import com.wz.lanyue.banke.widgetview.CustomClipLoading;
import com.wz.lanyue.banke.widgetview.MyGridView;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;
import java.util.ArrayList;

public class WBTimeLine extends Fragment implements View.OnClickListener{
    private StatusesAPI statusesAPI;
    ArrayList<Status> statusList;
    XRecyclerView recyclerView;
    ArrayList<String> weibopic;
    ArrayList<String> zhaungfaweibopic;
    Context context;
    CustomClipLoading customClipLoading;
    RelativeLayout relativeLayout;
    FloatingActionButton floatingActionButton;
    private MyAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_wbtime_line, container, false);
        initView(view);
        shuaxinweibo();
        return view;
    }

    private void initView(View view) {
        customClipLoading = (CustomClipLoading) view.findViewById(R.id.customClipLoading);
        floatingActionButton= (FloatingActionButton) view.findViewById(R.id.fabfaweibo);
        floatingActionButton.setOnClickListener(this);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rlwbtimeline);
        recyclerView = (XRecyclerView) view.findViewById(R.id.rlvweibo);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL));
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        recyclerView.setLoadingMoreEnabled(false);
        statusesAPI = new StatusesAPI(context, MyApplication.appKey, MyApplication.getToken(context));
        statusesAPI.friendsTimeline(0, 0, 50, 1, false, 0, false, requestListener);

    }

    private void shuaxinweibo() {

       recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
           @Override
           public void onRefresh() {
               statusesAPI.friendsTimeline(0, 0, 50, 1, false, 0, false, shuaxinrequestListener);
           }

           @Override
           public void onLoadMore() {

           }
       });
    }

    RequestListener shuaxinrequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            statusList = StatusList.parse(s).statusList;
            if (statusList != null && statusList.size() != 0) {
                myAdapter = new MyAdapter();
                recyclerView.setAdapter(myAdapter);
            }
            recyclerView.refreshComplete();
            myAdapter.notifyDataSetChanged();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastHelper.show(context, "服务器错误");
            recyclerView.refreshComplete();
        }
    };
    RequestListener requestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            statusList = StatusList.parse(s).statusList;
            if (statusList != null && statusList.size() != 0) {
                myAdapter = new MyAdapter();
                recyclerView.setAdapter(myAdapter);
            }
            customClipLoading.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastHelper.show(context, "服务器错误");
            customClipLoading.setVisibility(View.GONE);
        }
    };

    @Override
    public void onClick(View v) {
      startActivityForResult((new Intent(context, WriteWBActivity.class)),0);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.content_timelinelist, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
             final Status status = statusList.get(position);
            User user = status.getUser();
            holder.tvfriendname.setText(user.getScreen_name());
            String url = user.getProfile_url();
            PicassoHelper.setimg(context, user.getProfile_image_url(), holder.ivfriendhead);
            String source = status.getSource();
            if ("".equals(source)) {
                holder.tvfriendsource.setText(DateUtil.getDate(status.getCreated_at()));
            } else {
                holder.tvfriendsource.setText(DateUtil.getDate(status.getCreated_at()) + "  来自" + Html.fromHtml(source));
            }
            holder.tvfriendcontent.setText(StringUtil.getWeiboContent(context, holder.tvfriendcontent, status.getText()));
            if (status.getComments_count() != 0) {
                holder.tvcommentnumber.setText(""+status.getComments_count());

            }
           if(status.getReposts_count()!=0){
               holder.tvzhaungfaWB.setText("  "+status.getReposts_count());
           }
            Status retweeted_status = status.getRetweeted_status();
            zhuangfacaozuo(holder, retweeted_status);
            initweibopicdata(status, holder.myGridView);
           holder.tvcommentnumber.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent it=new Intent(getActivity(), WBComment.class);
                   it.putExtra("id",status.getId());
                   it.putExtra("commentnumber",status.getComments_count());
                  startActivity(it);
               }
           });
            holder.tvfriendname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserAPIhelper.startUserInfoactivity(context,holder.tvfriendname.getText().toString());
                }
            });
            holder.tvzhaungfaWB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  View view=  LayoutInflater.from(context).inflate(R.layout.zhuangfaweibodialog,null,false);
                    DialogPlus dialogPlus=DialogPlus.newDialog(context).setContentHolder(new ViewHolder(view)).setGravity(Gravity.BOTTOM).create();
                   dialogPlus.show();
                   zhuangfaweibo(view,status,dialogPlus);

                }
            });
        }

        @Override
        public int getItemCount() {
            return statusList == null ? 0 : statusList.size();
        }


    }

    private void zhuangfaweibo(View view, final Status status, final DialogPlus dialogPlus) {
        final EditBlogView ebzhuangfaliyou= (EditBlogView) view.findViewById(R.id.ebzhuangfaliyou);
        Button btnzhuangfa= (Button) view.findViewById(R.id.btnzhuangfa);
        final RequestListener requestListener=new RequestListener() {
            @Override
            public void onComplete(String s) {
                ToastHelper.show(context,"转发成功");
                statusesAPI.friendsTimeline(0, 0, 50, 1, false, 0, false, shuaxinrequestListener);
                dialogPlus.dismiss();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastHelper.show(context,"转发失败");
            }
        };
      btnzhuangfa.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              com.sina.weibo.sdk.openapi.legacy.StatusesAPI statusesAPI1=new com.sina.weibo.sdk.openapi.legacy.StatusesAPI(context,MyApplication.appKey,MyApplication.getToken(context));
              statusesAPI1.repost(Long.parseLong(status.getIdstr()),ebzhuangfaliyou.getText().toString(),0,requestListener);
          }
      });

    }

    private void zhuangfacaozuo(MyViewHolder holder, Status retweeted_status) {
        if (retweeted_status != null) {
            User retweeted_User = retweeted_status.getUser();
            if(retweeted_User!=null) {
                holder.tvzhuangfafriendcontent.setVisibility(View.VISIBLE);
                holder.tvzhuangfafriendcontent.setText(StringUtil.getWeiboContent(context, holder.tvzhuangfafriendcontent, "@" + retweeted_User.getScreen_name() + ":" + retweeted_status.getText()));
                zhaungfaweibopic = retweeted_status.getPic_urls();
                if (zhaungfaweibopic != null && zhaungfaweibopic.size() != 0) {
                    holder.zhuangfamyGridView.setAdapter(new MyzhaungfapicAdapter());
                    holder.zhuangfamyGridView.setVisibility(View.VISIBLE);
                } else {
                    holder.zhuangfamyGridView.setVisibility(View.GONE);
                }
            }
        } else {
            holder.tvzhuangfafriendcontent.setVisibility(View.GONE);
            holder.zhuangfamyGridView.setVisibility(View.GONE);
        }
    }

    private void initweibopicdata(Status status, MyGridView myGridView) {

        weibopic = status.getPic_urls();
        if (weibopic != null && weibopic.size() != 0) {

            myGridView.setAdapter(new MypicAdapter());
            myGridView.setVisibility(View.VISIBLE);
        } else {
            myGridView.setVisibility(View.GONE);
        }

    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivfriendhead;
        TextView tvfriendname, tvfriendsource, tvfriendcontent, tvzhuangfafriendcontent, tvcommentnumber,tvzhaungfaWB;
        MyGridView myGridView;
        MyGridView zhuangfamyGridView;


        public MyViewHolder(View v) {
            super(v);
            ivfriendhead = (CircleImageView) v.findViewById(R.id.ivfriendhead);
            tvfriendname = (TextView) v.findViewById(R.id.tvfriendname);
            tvfriendsource = (TextView) v.findViewById(R.id.tvfriendsource);
            tvfriendcontent = (TextView) v.findViewById(R.id.tvfriendcontent);
            tvzhuangfafriendcontent = (TextView) v.findViewById(R.id.tvzhuangfafriendcontent);
            myGridView = (MyGridView) v.findViewById(R.id.gvweibopic);
            zhuangfamyGridView = (MyGridView) v.findViewById(R.id.gvzhuangfaweibopic);
            tvcommentnumber = (TextView) v.findViewById(R.id.tvcommentnumber);
            tvzhaungfaWB= (TextView) v.findViewById(R.id.tvzhaungfaWB);
        }
    }

    class MypicAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return weibopic == null ? 0 : weibopic.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.timeline_weibopiclist, null, false);
            }
            ImageView ivtimelineweibopic = (ImageView) convertView.findViewById(R.id.ivtimelineweibopic);
            if (weibopic != null) {
                if (position < weibopic.size()) {
                    PicassoHelper.noyuanjiaosetimg(context, weibopic.get(position), ivtimelineweibopic);
                }

            }
            return convertView;
        }
    }

    class MyzhaungfapicAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return zhaungfaweibopic == null ? 0 : zhaungfaweibopic.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.timeline_weibopiclist, null, false);
            }
            ImageView ivtimelineweibopic = (ImageView) convertView.findViewById(R.id.ivtimelineweibopic);
            if (zhaungfaweibopic != null) {
                if (position < zhaungfaweibopic.size()) {
                    PicassoHelper.noyuanjiaosetimg(context, zhaungfaweibopic.get(position), ivtimelineweibopic);
                }

            }
            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){

            statusesAPI.friendsTimeline(0, 0, 50, 1, false, 0, false, shuaxinrequestListener);


        }
    }
}
