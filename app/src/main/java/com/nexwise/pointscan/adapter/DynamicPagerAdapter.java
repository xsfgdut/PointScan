package com.nexwise.pointscan.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nexwise.pointscan.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态加载viewPager页面适配器
 * Created by adolf_dong on 2016/7/12.
 */
public class DynamicPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "DynamicPagerAdapter";

    private List<BaseFragment> baseFragments = new ArrayList<>();

    public DynamicPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 添加适配器
     */
    public void addFrag(BaseFragment baseFragment) {
        if (baseFragment.isAdded())
            return;
        baseFragments.add(baseFragment);
    }

    @Override
    public int getCount() {
        return baseFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return baseFragments.get(position);
    }
}
