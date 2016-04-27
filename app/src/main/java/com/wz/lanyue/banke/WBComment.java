package com.wz.lanyue.banke;

import android.content.Intent;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.User;
import com.wz.lanyue.banke.util.ActionBarSetting;
import com.wz.lanyue.banke.util.DateUtil;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.util.StringUtil;
import com.wz.lanyue.banke.util.ToastHelper;
import com.wz.lanyue.banke.util.UserAPIhelper;
import com.wz.lanyue.banke.widgetview.CustomClipLoading;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;
import java.util.ArrayList;


public class WBComment extends AppCompatActivity implements View.OnClickListener {
    XRecyclerView rlvcomment;
    TextView tvcomment, tvcommentempty;
    String weiboid;
    Intent it;
    ArrayList<Comment> commentlist;
    int WeiBocommentnumber;
    myadapter myadapter;
    CustomClipLoading commentcustomClipLoading;
    EditText etcommentcontent;
    Button btncomment;
    private CommentsAPI commentsAPI;
    ArrayList<Comment> Loadcommentlist;
int page=2,i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbcomment);
        ActionBarSetting.setTitle(getSupportActionBar(),"评论列表");
        initview();
    }

    private void initview() {
        rlvcomment = (XRecyclerView) findViewById(R.id.rlvcomment);
        tvcomment = (TextView) findViewById(R.id.tvcomment);
        tvcommentempty = (TextView) findViewById(R.id.tvcommentempty);
        etcommentcontent = (EditText) findViewById(R.id.etcommentcontent);
        btncomment = (Button) findViewById(R.id.btncomment);
        commentsAPI = new CommentsAPI(this, MyApplication.appKey, MyApplication.getToken(this));
        commentsAPI = new CommentsAPI(this, MyApplication.appKey, MyApplication.getToken(this));
        commentcustomClipLoading = (CustomClipLoading) findViewById(R.id.commentcustomClipLoading);
        it = getIntent();
        weiboid = it.getStringExtra("id");
        WeiBocommentnumber=it.getIntExtra("commentnumber",0);
        btncomment.setOnClickListener(this);
        rlvcomment.setLayoutManager(new LinearLayoutManager(this));
        rlvcomment.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL, 1, Color.parseColor("#E1E1E1")));
        initcommentdata();
        rlvcomment.setPullRefreshEnabled(false);
        rlvcomment.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        loading();
    }

    private void loading() {
        final RequestListener listener = new RequestListener() {
            @Override
            public void onComplete(String s) {
                Loadcommentlist = CommentList.parse(s).commentList;
                if (Loadcommentlist != null) {
                    for (int i = 0; i < Loadcommentlist.size(); i++) {
                        commentlist.add(commentlist.size(), Loadcommentlist.get(i));
                        myadapter.notifyItemInserted(commentlist.size());
                    }
                    page++;
                    rlvcomment.loadMoreComplete();
                }
                else{
                    rlvcomment.loadMoreComplete();
                }

            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastHelper.show(WBComment.this, "加载失败");
            }
        };
        rlvcomment.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                commentsAPI.show(Long.parseLong(weiboid), 0, 0, 20, page, 0, listener);
            }
        });

    }

    private void initcommentdata() {
        commentsAPI.show(Long.parseLong(weiboid), 0, 0, 50, 1, 0, myRequestListener);
    }

    RequestListener myRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            commentlist = CommentList.parse(s).commentList;
            if (commentlist != null) {
                if(commentlist.size()<49){
                tvcomment.setText("评论 " + commentlist.size());
                rlvcomment.setLoadingMoreEnabled(false);
                }
                else{
                    tvcomment.setText("评论 " + WeiBocommentnumber);
                }
                myadapter = new myadapter();
                rlvcomment.setAdapter(myadapter);
                commentcustomClipLoading.setVisibility(View.GONE);
                rlvcomment.setVisibility(View.VISIBLE);
            } else {
                tvcommentempty.setVisibility(View.VISIBLE);
                commentcustomClipLoading.setVisibility(View.GONE);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastHelper.show(WBComment.this, "服务器错误");
            commentcustomClipLoading.setVisibility(View.GONE);
            tvcommentempty.setText("网络请求出错，请检查网络是否可用");
            tvcommentempty.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onClick(View v) {
        commentsAPI.create(etcommentcontent.getText().toString(), Long.parseLong(weiboid), false, MyommentListener);
    }

    RequestListener MyommentListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            initcommentdata();
            ToastHelper.show(WBComment.this, "评论成功");
            etcommentcontent.setText("");
            tvcommentempty.setVisibility(View.GONE);
           InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(inputMethodManager!=null){
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastHelper.show(WBComment.this, "评论失败");
        }
    };

    class myadapter extends RecyclerView.Adapter<myViewHolder> {
        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(WBComment.this).inflate(R.layout.comment_list, parent, false);
            myViewHolder myViewHolder = new myViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final myViewHolder holder, final int position) {
            Comment comment = commentlist.get(position);
            User user = comment.getUser();
            holder.tvcommentcontent.setText(StringUtil.getWeiboContent(WBComment.this, holder.tvcommentcontent, comment.getText()));
            if (!"".equals(user.getProfile_image_url())) {
                PicassoHelper.setimg(WBComment.this, user.getProfile_image_url(), holder.ivcommenthead);
            }
            holder.tvcommentname.setText(user.getScreen_name());
            holder.tvcommentdate.setText(DateUtil.getDate(comment.getCreated_at()));
            holder.tvcommentname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserAPIhelper.startUserInfoactivity(WBComment.this,holder.tvcommentname.getText().toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            return commentlist.size();
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView ivcommenthead;
        TextView tvcommentname, tvcommentdate, tvcommentcontent;
        public myViewHolder(View itemView) {
            super(itemView);
            ivcommenthead = (ImageView) itemView.findViewById(R.id.ivcommenthead);
            tvcommentcontent = (TextView) itemView.findViewById(R.id.tvcommentcontent);
            tvcommentname = (TextView) itemView.findViewById(R.id.tvcommentname);
            tvcommentdate = (TextView) itemView.findViewById(R.id.tvcommentdate);
        }
    }

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
