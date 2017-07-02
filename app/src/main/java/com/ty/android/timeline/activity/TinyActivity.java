package com.ty.android.timeline.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ty.android.timeline.R;
import com.ty.android.timeline.fragment.GridFragment;
import com.ty.android.timeline.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/4/8.
 */

public class TinyActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<GridFragment> gridFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiny);
        initView();
        for(int i =0;i<5;i++){
            gridFragments.add(new GridFragment());
        }
        setViewPager();
    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.tiny_vp);
    }

    private void setViewPager() {

            viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    Log.d("GITEM", "getItem: " + position);
                    GridFragment fragment = gridFragments.get(position % 5);
                    return fragment;
                }

                @Override
                public int getCount() {
                    return Integer.MAX_VALUE;
                }
            });

            viewPager.setCurrentItem(100);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                measureDirection(position);
//                updateView(position);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
