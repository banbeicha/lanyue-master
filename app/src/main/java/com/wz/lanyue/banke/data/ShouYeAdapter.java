package com.wz.lanyue.banke.data;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * Created by BANBEICHA on 2016/3/29.
 */
public class ShouYeAdapter extends FragmentStatePagerAdapter {
List<Fragment> fragmentList;
    List<String> titles;
    public ShouYeAdapter(FragmentManager fm,List<Fragment> fragmentList,List<String> titles) {
        super(fm);
        this.fragmentList=fragmentList;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  titles.get(position);

    }

}
