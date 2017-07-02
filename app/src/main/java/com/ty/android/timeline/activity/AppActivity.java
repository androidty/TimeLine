package com.ty.android.timeline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ty.android.timeline.R;
import com.ty.android.timeline.fragment.BaseFragment;
import com.ty.android.timeline.fragment.RecyclerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/4/12.
 */

public class AppActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private List<BaseFragment> mFragments;
    private int lable = 301;

    private MyAdapter adapter;
    String date;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        initData();
        viewPager = (ViewPager) findViewById(R.id.app_viewpager);

        adapter = new MyAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        // 这里设置当前ViewPager显示的哪个Fragment，让ViewPager开始就可以向左滑动
        viewPager.setCurrentItem(lable);
        // 设置上下的值
        fondre(true, true);
    }

    private void initData() {
        mFragments = new ArrayList<BaseFragment>();
        mFragments.add(new RecyclerFragment());
        mFragments.add(new RecyclerFragment());
        mFragments.add(new RecyclerFragment());
        mFragments.add(new RecyclerFragment());
        mFragments.add(new RecyclerFragment());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        if (position > lable) {
            lable = position;
            fondre(false, true);
        }
        if (position < lable) {
            lable = position;
            fondre(true, false);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void fondre(boolean lastBool, boolean nextBool) {
        // 设置当前页面的值
        mFragments.get(lable % mFragments.size()).updateDate(lable);
        // 设置上个页面的值
        if (lastBool)
            mFragments.get((lable - 1) % mFragments.size()).updateDate(
                     (lable - 1));
        // 设置下个页面的值
        if (nextBool)
            mFragments.get((lable + 1) % mFragments.size()).updateDate(
                    (lable + 1));
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        private List<BaseFragment> mFragments;

        public MyAdapter(FragmentManager fm, List<BaseFragment> Fragments) {
            super(fm);
            this.mFragments = Fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // 由于只有3个Fragment，所以这里根据arg0计算该显示哪个Fragment
            return mFragments.get(arg0 % mFragments.size());
        }

        @Override
        public int getCount() {
            return 301;
        }

    }
}
