package com.wz.lanyue.banke.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wz.lanyue.banke.EditNewTypeActivity;
import com.wz.lanyue.banke.MyApplication;
import com.wz.lanyue.banke.R;
import com.wz.lanyue.banke.data.ShouYeAdapter;
import com.wz.lanyue.banke.model.DuZhe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by BANBEICHA on 2016/3/28.
 */
public class shouye extends Fragment implements View.OnClickListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    List<Fragment> fragmentList;
    List<String> title;
    ImageView imageView;
    LinkedHashMap<String, Fragment> hashMap;
    String[] titles = {"微博",
            "推荐",
            "读者",
            "社会",
            "国际",
            "科技",
            "苹果",
            "娱乐",
            "体育",
            "生活",
            "奇闻"};
    Fragment[] fragments = {new WBTimeLine(),
            new weixinjingxuan(),
            new duzhe(),
            new shehui(),
            new word(),
            new keji(),
            new apple(),
            new yule(),
            new tiyu(),
            new health(),
            new qiwen()};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.shouyeviewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.shouyetablayout);
        imageView = (ImageView) view.findViewById(R.id.iv_edit);
        fragmentList = new ArrayList<Fragment>();
        title = new ArrayList<String>();
        initview();
        return view;
    }

    private void initview() {
        hashMap = new LinkedHashMap<String, Fragment>();
        for (int i = 0; i < titles.length; i++) {
            if (!MyApplication.isLogin) {
                if (i == 0) {
                    continue;
                }
            }
            hashMap.put(titles[i], fragments[i]);
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("me_news", getActivity().MODE_PRIVATE);
        String titleset = sharedPreferences.getString("titleset", null);
        if (titleset == null) {
            Iterator<Map.Entry<String, Fragment>> stringIterator = hashMap.entrySet().iterator();
            while (stringIterator.hasNext()) {
                Map.Entry<String, Fragment> entry = stringIterator.next();
                title.add(entry.getKey());
                fragmentList.add(entry.getValue());
            }
        } else {


            String[] strings = titleset.split("\\|");
            for (int i = 0; i < strings.length; i++) {
                title.add(strings[i]);
                fragmentList.add(hashMap.get(strings[i]));
            }
            if (MyApplication.isLogin&&!fragmentList.contains(fragments[0])) {
                title.add(0,titles[0]);
                fragmentList.add(0,fragments[0]);
            }

        }
        for (int i = 0; i < title.size(); i++) {
                tabLayout.addTab(tabLayout.newTab().setText(title.get(i)));
        }
        viewPager.setAdapter(new ShouYeAdapter(getChildFragmentManager(), fragmentList, title));
        tabLayout.setupWithViewPager(viewPager);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                startActivityForResult(new Intent(getActivity(), EditNewTypeActivity.class).putStringArrayListExtra("newtype", (ArrayList<String>) title), 0);
                getActivity().finish();
                break;

        }
    }


}
