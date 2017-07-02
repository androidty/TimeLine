package com.ty.android.timeline.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ty.android.timeline.MyApplication;
import com.ty.android.timeline.R;
import com.ty.android.timeline.activity.AppActivity;
import com.ty.android.timeline.activity.LineChartActivity;
import com.ty.android.timeline.activity.MainActivity;
import com.ty.android.timeline.database.MyDBDao;
import com.ty.android.timeline.utils.TimeUtils;
import com.ty.android.timeline.utils.Tool;
import com.ty.android.timeline.view.CircleProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Android on 2017/4/3.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "MainFragment";


    private String date;

    View convertView;
    int progress=50;
    private CircleProgressBar circleProgressBar;
    private RelativeLayout r1,r2,r3;
    private TextView tvPinCi,tvGeShu,tvJieSuo;
    private TextView tvDay,tvAllTime;


    private Intent intent = new Intent();

    private int num;
    private int allPages = 300;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.circle_item,null);
        }

        initView();
        initEvent();
        initData();
        return convertView;
    }

    private void initView(){
        circleProgressBar = (CircleProgressBar) convertView.findViewById(R.id.circle_progress);
//        circleProgressBar.setProgress(MyDBDao.getInstance(getActivity()).readAllTimeADay(getLable(allPages - num)));
        r1 = (RelativeLayout) convertView.findViewById(R.id.r1);
        r2 = (RelativeLayout) convertView.findViewById(R.id.r2);
        r3 = (RelativeLayout) convertView.findViewById(R.id.r3);

        tvPinCi = (TextView) convertView.findViewById(R.id.pinci);
        tvGeShu = (TextView) convertView.findViewById(R.id.geshu);
        tvJieSuo = (TextView) convertView.findViewById(R.id.jiesuo);

        tvDay = (TextView) convertView.findViewById(R.id.daytext);
        tvDay.setText(TimeUtils.getDate(getLable(allPages - num)));
        tvAllTime = (TextView) convertView.findViewById(R.id.alltime);

    }

    public String getLable(int witch) {
        String type = "yyyyMMdd";
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        Date d = new Date(System.currentTimeMillis() - witch * 24 * 60 * 60 * 1000);
        return dateformat.format(d);
    }

    @Override
    public void onDestroyView() {
        convertView =null;
        super.onDestroyView();
    }

    private void initEvent(){
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        circleProgressBar.setOnClickListener(this);
        tvAllTime.setOnClickListener(this);
    }
    private void initData(){


        int x =MyDBDao.getInstance(getActivity()).readAllTimeADay(getLable(allPages - num));
        float res = x/10;
        Log.d("initData", "initData: "+res/3600/24);
        circleProgressBar.setmTargetProgress(res/3600/24);
        tvAllTime.setText(TimeUtils.getUsageTime(x));
        tvPinCi.setText((MyDBDao.getInstance(MyApplication.getInstance()).readPinciDay(getLable(allPages - num)))+"");
        tvJieSuo.setText(MyDBDao.getInstance(MyApplication.getInstance()).readAllOnADay(getLable(allPages - num))+"");
        tvGeShu.setText(MyDBDao.getInstance(MyApplication.getInstance()).readGeShuDay(getLable(allPages - num))+"");
    }





    @Override
    public void onClick(View view) {

        intent = new Intent(getActivity(), LineChartActivity.class);
        switch (view.getId()){
            case R.id.r1:
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.r2:
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.r3:
                intent.putExtra("type",3);
                startActivity(intent);
                break;
            case R.id.circle_progress:
            case R.id.alltime:
                startActivity(new Intent(getActivity(),AppActivity.class));
                break;
        }
    }

    @Override
    public void updateDate(int num) {
        this.num = num;
    }
}
