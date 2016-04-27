package com.wz.lanyue.banke.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.data.ShouYeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BANBEICHA on 2016/3/28.
 */
public class shouye extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    List<Fragment> fragmentList;
    List<String> title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=  inflater.inflate(R.layout.news,container,false);
       viewPager= (ViewPager) view.findViewById(R.id.shouyeviewpager);
        tabLayout= (TabLayout) view.findViewById(R.id.shouyetablayout);
        fragmentList=new ArrayList<Fragment>();
        title=new ArrayList<String>();
        initview();
        return view;
    }

    private void initview() {
        if (MyApplication.isLogin){
        title.add("微博");}
        title.add("推荐");
        title.add("社会");
        title.add("国际");
        title.add("科技");
        title.add("苹果");
        title.add("娱乐");
        title.add("体育");
        title.add("生活");
        title.add("奇闻");
        if (MyApplication.isLogin){
        fragmentList.add(new WBTimeLine());}
        fragmentList.add(new weixinjingxuan());
        fragmentList.add(new shehui());
        fragmentList.add(new word());
        fragmentList.add(new keji());
        fragmentList.add(new apple());
        fragmentList.add(new yule());
        fragmentList.add(new tiyu());
        fragmentList.add(new health());
        fragmentList.add(new qiwen());
        for (int i=0;i<title.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(title.get(i)));
        }
        viewPager.setAdapter(new ShouYeAdapter(getChildFragmentManager(),fragmentList,title));
        tabLayout.setupWithViewPager(viewPager);
    }
}
