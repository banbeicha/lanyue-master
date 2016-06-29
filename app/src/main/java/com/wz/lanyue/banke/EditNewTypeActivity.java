package com.wz.lanyue.banke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.wz.lanyue.banke.model.SimpleDragEntity;
import com.wz.lanyue.banke.widgetview.TileView;
import java.util.ArrayList;
import java.util.List;
import cc.solart.dragdrop.DragDropListView;
import cc.solart.dragdrop.IDragEntity;
import cc.solart.dragdrop.adapter.AbsTileAdapter;

public class EditNewTypeActivity extends AppCompatActivity implements AbsTileAdapter.DragDropListener, View.OnClickListener, TileView.OnSelectedListener {
    DragDropListView dragDropListView;
    private SimpleTileAdapter simpleTileAdapter;
    ArrayList<String> arrayList, recommendList, deledList;
    ImageView iv_close;
    GridView gv_recommended;
    private MyAdapter adapter;
    String[] recommendtitle;
    private StringBuffer stringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        arrayList = getIntent().getStringArrayListExtra("newtype");
        initview();

    }

    private void initview() {
        dragDropListView = (DragDropListView) findViewById(R.id.lv_sort);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        gv_recommended = (GridView) findViewById(R.id.gv_recommended);
        simpleTileAdapter = new SimpleTileAdapter(this, this, this);
        recommendList = new ArrayList<String>();
        simpleTileAdapter.setData(bindData());
        final ImageView dragShadowOverlay =
                (ImageView) findViewById(R.id.tile_drag_shadow_overlay);
        dragDropListView.setVerticalScrollBarEnabled(false);
        dragDropListView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_RIGHT);
        dragDropListView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        dragDropListView.setDragShadowOverlay(dragShadowOverlay);
        dragDropListView.getDragDropController().addOnDragDropListener(simpleTileAdapter);
        final LayoutAnimationController controller = new LayoutAnimationController(
                AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        controller.setDelay(0);
        dragDropListView.setLayoutAnimation(controller);
        dragDropListView.setAdapter(simpleTileAdapter);
        iv_close.setOnClickListener(this);
        remmendNewTitles();

    }

    private void remmendNewTitles() {
        String s = getSharedPreferences("recommend_news", MODE_PRIVATE).getString("recommendtitle", null);
        if (s != null) {
            recommendtitle = s.split("\\|");
            for (int i = 0; i < recommendtitle.length; i++) {
                recommendList.add(recommendtitle[i]);
            }
        }
        adapter = new MyAdapter();
        gv_recommended.setAdapter(adapter);
    }

    private List<IDragEntity> bindData() {
        List<IDragEntity> list = new ArrayList<IDragEntity>();
        for (int i = 0; i < arrayList.size(); i++) {
            SimpleDragEntity entry = new SimpleDragEntity(i, arrayList.get(i));
            list.add(entry);
        }
        return list;
    }

    @Override
    public DragDropListView getDragDropListView() {
        return dragDropListView;
    }

    @Override
    public void onDataSetChangedForResult(ArrayList<IDragEntity> list) {
        stringBuffer = new StringBuffer();
        deledList =new ArrayList<String>();
        for (IDragEntity entry : list
                ) {
            int id = entry.getId();
            stringBuffer.append(arrayList.get(id) + "|");
            deledList.add(arrayList.get(id));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
             startActivity(new Intent(this,HomeActivity.class));
                finish();
                break;
        }

    }

    @Override
    public void onTileSelected(IDragEntity entity) {
        stringBuffer=null;

        if (deledList!=null){
        arrayList= deledList;}
        recommendList.add(arrayList.get(entity.getId()));
        arrayList.remove(entity.getId());
        simpleTileAdapter.setData(bindData());
        adapter.notifyDataSetChanged();
    }

    class SimpleTileAdapter extends AbsTileAdapter {
        private TileView.OnSelectedListener mListener;

        public SimpleTileAdapter(Context context, DragDropListener dragDropListener, TileView.OnSelectedListener listener) {
            super(context, dragDropListener);
            this.mListener = listener;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TileView tileView = null;

            if (convertView != null && convertView instanceof TileView) {
                tileView = (TileView) convertView;
            }

            if (tileView == null) {
                tileView = (TileView) View.inflate(mContext,
                        R.layout.lv_sort_item, null);
            }
            TextView mName = (TextView) tileView.findViewById(R.id.tv_type);
            tileView.renderData(getItem(position));
            tileView.setListener(mListener);
            return tileView;
        }

        @Override
        protected IDragEntity getDragEntity(View view) {
            return ((TileView) view).getDragEntity();
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return recommendList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(EditNewTypeActivity.this).inflate(R.layout.lv_sort_item, parent, false);
            }
            TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            tv_type.setText(recommendList.get(position));
            tv_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stringBuffer=null;
                    if (deledList!=null){
                        arrayList= deledList;}
                    arrayList.add(recommendList.get(position));
                    recommendList.remove(position);
                    adapter.notifyDataSetChanged();
                    simpleTileAdapter.setData(bindData());
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("recommend_news", MODE_PRIVATE).edit();
        StringBuffer stringBuffers = new StringBuffer();
        for (int i = 0; i < recommendList.size(); i++) {
            stringBuffers.append(recommendList.get(i) + "|");
        }
        editor.putString("recommendtitle", stringBuffers.toString());
        editor.commit();

        SharedPreferences.Editor meeditor = getSharedPreferences("me_news", MODE_PRIVATE).edit();
        if (stringBuffer==null) {
            stringBuffer = new StringBuffer();
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuffer.append(arrayList.get(i) + "|");
            }
        }
            meeditor.putString("titleset", stringBuffer.toString());
            meeditor.commit();

    }
}
