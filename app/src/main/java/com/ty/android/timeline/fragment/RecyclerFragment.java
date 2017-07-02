package com.ty.android.timeline.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ty.android.timeline.MyApplication;
import com.ty.android.timeline.R;
import com.ty.android.timeline.adapter.AppAdapter;
import com.ty.android.timeline.database.MyDBDao;
import com.ty.android.timeline.entity.AppEntity;
import com.ty.android.timeline.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Android on 2017/4/9.
 */

public class RecyclerFragment extends BaseFragment {
    private View convertView;
    private TextView textView;
    private int num;
    private RecyclerView recyclerView;
    private int allPages = 300;
    private List<AppEntity> appLists;
    private RelativeLayout r1,r2;
    private int type =0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.app_fragment, null);
        }
        textView = (TextView) convertView.findViewById(R.id.text_date);
        //通过label得知时间日期
        textView.setText(TimeUtils.getDate(getLable(allPages - num)));
        recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        dealLists(type);
        recyclerView.setAdapter(new AppAdapter(getActivity(),appLists,type));
        r1 = (RelativeLayout) convertView.findViewById(R.id.r1);
        r2 = (RelativeLayout) convertView.findViewById(R.id.r2);
        initEvent();
        return convertView;
    }

    private void initEvent(){
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getType()==0){
                    return ;
                }
                dealLists(0);
                setType(0);
                recyclerView.setAdapter(new AppAdapter(getActivity(),appLists,getType()));


            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getType()==1){
                    return ;
                }
                dealLists(1);
                setType(1);
                recyclerView.setAdapter(new AppAdapter(getActivity(),appLists,getType()));

            }
        });
    }


    public String getLable(int witch) {
        String type = "yyyyMMdd";
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        Date d = new Date(System.currentTimeMillis() - witch * 24 * 60 * 60 * 1000);
        return dateformat.format(d);
    }

    @Override
    public void onDestroyView() {
        convertView = null;
        super.onDestroyView();
    }

    @Override
    public void updateDate(int num) {
        this.num = num;
    }

    private void dealLists(int type){
        if(appLists!=null&&appLists.size()>0){
            appLists.clear();
        }
        //按时间
        appLists = MyDBDao.getInstance(getActivity()).readPUtUc(getLable(allPages-num),type);
        //给list里的每一个appentity设置应用图标
        for(int i =0;i<appLists.size();i++){
            AppEntity appEntity = appLists.get(i);
//            appEntity.setAppIcon(MyDBDao.getInstance(getActivity()).readAppIcon(appEntity.getPackageName()));
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
