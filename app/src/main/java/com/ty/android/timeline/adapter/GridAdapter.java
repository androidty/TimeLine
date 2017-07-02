package com.ty.android.timeline.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ty.android.timeline.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    private List<String> lists = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<String> lsits) {
        this.context = context;
        this.lists = lists;
        for (int i = 0; i < 72; i++) {
            lists.add("" + i);
        }
        Log.d("getview45", "GridAdapter: " + lists.size());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 72;
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i % 2 == 0) {
            viewHolder.imageView.getBackground().setAlpha(50);
        }
        if(i%3==0){
            viewHolder.imageView.getBackground().setAlpha(100);
        }
        Log.d("getview45", "getView: ");

        return view;
    }

    private static class ViewHolder {
        public ImageView imageView;
    }
}