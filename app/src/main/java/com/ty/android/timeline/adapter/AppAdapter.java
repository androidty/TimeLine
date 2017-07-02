package com.ty.android.timeline.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ty.android.timeline.R;
import com.ty.android.timeline.entity.AppEntity;
import com.ty.android.timeline.fragment.RecyclerFragment;
import com.ty.android.timeline.service.WatchDogService;
import com.ty.android.timeline.utils.TimeUtils;
import com.ty.android.timeline.view.SpringProgressView;

import java.util.List;

import static android.R.attr.x;
import static android.R.id.list;
import static com.ty.android.timeline.R.id.img_icon;

/**
 * Created by Android on 2017/4/13.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder> {
    private Context context;
    private List<AppEntity> appEntityList;
    private int type;

    public AppAdapter(Context context, List<AppEntity> list, int type) {
        this.context = context;
        this.appEntityList = list;
        this.type = type;
        Log.d("AppAdapter", "AppAdapter: " + list.size());
//        Log.d("AppAdapter", "AppAdapter: is null ? "+(list.get(0).getAppIcon()!=null));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AppEntity appEntity = appEntityList.get(position);
        if(type==0) {
            holder.textView1.setText(TimeUtils.getUsageTime(appEntity.getUsageTime()));
            holder.textView2.setText(appEntity.getUsagePinci()+"次");
            holder.springProgressView.setCurrentCount(appEntity.getUsageTime());
        }else if(type==1){
            holder.textView2.setText(TimeUtils.getUsageTime(appEntity.getUsageTime()));
            holder.textView1.setText(appEntity.getUsagePinci()+"次");
            holder.springProgressView.setCurrentCount(appEntity.getUsagePinci());
        }


        holder.icon.setImageDrawable(WatchDogService.getAppInfo(appEntity.getPackageName()).getAppDrawable());
    }


    @Override
    public int getItemCount() {
        return appEntityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView textView1, textView2;
        private SpringProgressView springProgressView;


        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.img_icon);
            textView1 = (TextView) itemView.findViewById(R.id.t1);
            textView2 = (TextView) itemView.findViewById(R.id.t2);
            springProgressView = (SpringProgressView) itemView.findViewById(R.id.progress);
            if(type==0){
            springProgressView.setMaxCount(appEntityList.get(0).getUsageTime());
            }else if(type==1){
                springProgressView.setMaxCount(appEntityList.get(0).getUsagePinci());
            }
        }
    }
}
