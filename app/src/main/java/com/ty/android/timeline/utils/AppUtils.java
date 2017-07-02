package com.ty.android.timeline.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ty.android.timeline.MyApplication;
import com.ty.android.timeline.entity.AppInfo;

import java.util.List;

/**
 * Created by Android on 2017/4/7.
 */

public class AppUtils {
    /**
     * 通过包名构造一个AppInfo
     * @param pkgName
     * @return
     */
    public static PackageManager pm = MyApplication.getInstance().getPackageManager();
    private static ApplicationInfo mApplicationInfo;

    public static AppInfo getAppInfo(String pkgName){
        AppInfo appInfo = new AppInfo();
        appInfo.setPakageName(pkgName);
        appInfo.setStartTime( (long)System.currentTimeMillis());
        Log.d("insertAppTimeInfo", "getAppInfo: "+appInfo.getStartTime());
        try {
            mApplicationInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos
                ) {
            if(packageInfo.packageName.equals(pkgName)){
                appInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
                appInfo.setAppDrawable(packageInfo.applicationInfo.loadIcon(pm));
            }
        }

        return appInfo;
    }
}
