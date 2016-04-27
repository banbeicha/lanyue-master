package com.wz.lanyue.banke;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wz.lanyue.banke.model.HuaBang;
import com.wz.lanyue.banke.model.HuaBangList;
import com.wz.lanyue.banke.util.PicassoHelper;
import com.wz.lanyue.banke.widgetview.RecycleViewDivider;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.ArrayList;
public class SearchHuaBangActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    LinearLayout llSearchHuaBang;
    ImageView ivSearcHuaBangBack;
    EditText etHuaBangSearchKeyord;
    TextView tvHuaBangSearch;
    XRecyclerView XRlvSearch;
   ArrayList<HuaBang> arrayList;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hua_bang);
        initViewdata();
        EnterAnimation();
    }

    private void initViewdata() {
        llSearchHuaBang = (LinearLayout) findViewById(R.id.llSearchHuaBang);
        ivSearcHuaBangBack = (ImageView) findViewById(R.id.ivSearcHuaBangBack);
        ivSearcHuaBangBack.setOnClickListener(this);
        etHuaBangSearchKeyord = (EditText) findViewById(R.id.etHuaBangSearchKeyord);
        etHuaBangSearchKeyord.addTextChangedListener(this);
        tvHuaBangSearch = (TextView) findViewById(R.id.tvHuaBangSearch);
        tvHuaBangSearch.setOnClickListener(this);
        XRlvSearch= (XRecyclerView) findViewById(R.id.XRlvSearch);
        XRlvSearch.setLayoutManager(new LinearLayoutManager(this));
        XRlvSearch.setPullRefreshEnabled(false);
        XRlvSearch.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
        XRlvSearch.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL, 1, Color.parseColor("#A3A3A3")));
    }

    private void EnterAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(1000);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(scaleAnimation, 0.5f);
        llSearchHuaBang.setLayoutAnimation(layoutAnimationController);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearcHuaBangBack:
                finish();
                break;
            case R.id.tvHuaBangSearch:
              search();
                break;
        }
    }

    private void search() {
        RequestParams request=new RequestParams(MyApplication.getHuabangSearchurl(etHuaBangSearchKeyord.getText().toString()));
        x.http().get(request, new Callback.CommonCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
              arrayList=  HuaBangList.getnewsList(jsonObject);
                if(arrayList!=null){
                    myAdapter=new MyAdapter();
                    XRlvSearch.setAdapter(myAdapter);

                }
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
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            tvHuaBangSearch.setTextColor(getResources().getColor(android.R.color.white));
            tvHuaBangSearch.setEnabled(true);
        } else {
            tvHuaBangSearch.setTextColor(Color.parseColor("#F39497"));
            tvHuaBangSearch.setEnabled(false);

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
   private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

       @Override
       public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view= LayoutInflater.from(SearchHuaBangActivity.this).inflate(R.layout.search_hua_bang_list,parent,false);
           MyViewHolder myViewHolder=new MyViewHolder(view);
           return myViewHolder;
       }

       @Override
       public void onBindViewHolder(MyViewHolder holder, int position) {
       final HuaBang huaBang=  arrayList.get(position);
           if(huaBang!=null&&!"".equals(huaBang)){
               holder.tvHuaBangSearchTitle.setText(huaBang.getTitle());
               holder.tvHuaBangSearchAuthor.setText(huaBang.getArtistName());
               holder.tvHuaSearchPlayfangNumber.setText("播放次数："+huaBang.getTotalViews());
               if(!"".equals(huaBang.getAlbumImg())){
                   PicassoHelper.setimg(SearchHuaBangActivity.this,huaBang.getAlbumImg(),holder.ivHuaSearchPic);
               }
              holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       startActivity(new Intent(SearchHuaBangActivity.this,HuaBangContentActivity.class).putExtra("huabang",huaBang));
                   }
               });
           }
       }

       @Override
       public int getItemCount() {
           return arrayList.size();
       }
   }
    private class MyViewHolder extends RecyclerView.ViewHolder{
       TextView tvHuaBangSearchTitle,tvHuaBangSearchAuthor,tvHuaSearchPlayfangNumber;
        ImageView ivHuaSearchPic;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivHuaSearchPic= (ImageView) itemView.findViewById(R.id.ivHuaSearchPic);
            tvHuaBangSearchTitle= (TextView) itemView.findViewById(R.id.tvHuaBangSearchTitle);
            tvHuaBangSearchAuthor= (TextView) itemView.findViewById(R.id.tvHuaBangSearchAuthor);
            tvHuaSearchPlayfangNumber= (TextView) itemView.findViewById(R.id.tvHuaSearchPlayfangNumber);

        }
    }

}
