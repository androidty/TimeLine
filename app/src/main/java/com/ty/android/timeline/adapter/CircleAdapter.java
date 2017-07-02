package com.ty.android.timeline.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Android on 2017/4/3.
 */

public class CircleAdapter extends FragmentPagerAdapter{
    private Fragment[] fragments;
    public CircleAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return null;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


}
