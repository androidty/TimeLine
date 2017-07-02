package com.ty.android.timeline.service;

import android.app.ActivityManager;
import android.content.Context;
import android.preference.Preference;

import java.util.List;

/**
 * Created by android_1 on 2016/9/18.
 */
public class ServiceUtils {
    public static boolean isRunningService(Context context, String serviceName){
        boolean isWork = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(Preference.DEFAULT_ORDER);
        if(runningServiceInfos.size()<=0){
            return false;
        }
        for(int i=0;i<runningServiceInfos.size();i++){
            String name = runningServiceInfos.get(i).service.getClassName().toString();
            if(name.equals(serviceName)){
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
