package com.ty.android.timeline.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Android on 2017/4/2.
 */

public class CircleProAdapter<V extends View> extends PagerAdapter {
    private V[] views;



    public CircleProAdapter(V[] views){
        this.views = views;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public V[] getViews() {
        return views;
    }
}
