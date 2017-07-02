package com.ty.android.timeline.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.ty.android.timeline.R;
import com.ty.android.timeline.adapter.GridAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2017/4/8.
 */

public class GridFragment extends Fragment{

    private List<String> lists;
    private View convertView;
    private GridView gridView1,gridView2,gridView3,gridView4;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.grid_layout,null);
        }
        gridView1 = (GridView) convertView.findViewById(R.id.icon_group1);
        gridView2 = (GridView) convertView.findViewById(R.id.icon_group2);
        gridView3 = (GridView) convertView.findViewById(R.id.icon_group3);
        gridView4 = (GridView) convertView.findViewById(R.id.icon_group4);

        lists = new ArrayList<>();

        for(int i =0;i<72;i++){
            lists.add(""+i);
        }


        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView1.setAdapter(new GridAdapter(getActivity(),lists));
        gridView2.setAdapter(new GridAdapter(getActivity(),lists));
        gridView3.setAdapter(new GridAdapter(getActivity(),lists));
        gridView4.setAdapter(new GridAdapter(getActivity(),lists));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), ""+(i+1), Toast.LENGTH_SHORT).show();
            }
        });gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), ""+(i+73), Toast.LENGTH_SHORT).show();
            }
        });gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), ""+(i+145), Toast.LENGTH_SHORT).show();
            }
        });gridView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), ""+(i+217), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
