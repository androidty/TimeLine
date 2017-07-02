package com.ty.android.timeline.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ty.android.timeline.MyApplication;
import com.ty.android.timeline.database.MyDBDao;
import com.ty.android.timeline.entity.AppInfo;
import com.ty.android.timeline.entity.OnOffEntity;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.app.WallpaperManager.getInstance;

/**
 * Created by Android on 2017/3/12.
 */

public class WatchDogService extends Service {

    private ScreenOnReceiver mScreenOnReceiver;
    private ScreenOffReceiver mScreenOffReceiver;
    private goToAppReceiver mGoToAppReceiver;

    private boolean flag;
    private String tempName="";
    private static String name="";

    private static ApplicationInfo mApplicationInfo;
    private static PackageManager pm;



    @Override
    public void onCreate() {

        pm = getPackageManager();
        register();
        flag = true;

        intent = new Intent();
        listening();

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void register() {

        mScreenOffReceiver = new ScreenOffReceiver();
        registerReceiver(mScreenOffReceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        mScreenOnReceiver = new ScreenOnReceiver();
        registerReceiver(mScreenOnReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        mGoToAppReceiver = new goToAppReceiver();
        registerReceiver(mGoToAppReceiver,new IntentFilter("startTime"));

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mScreenOnReceiver);
        mScreenOnReceiver = null;
        unregisterReceiver(mScreenOffReceiver);
        mScreenOffReceiver = null;
        super.onDestroy();
    }

    private AppInfo appInfo;
    /**
     * 
     */
    private Intent intent;
    private void listening(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               while(flag){
                   if(!ServiceUtils.isRunningService(WatchDogService.this,"com.ty.android.timeline.service.ProtectService")){
                       Intent intent = new Intent(WatchDogService.this,ProtectService.class);
                       startService(intent);
                   }
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   String pkgName = getTaskPackName();

                   if(WatchDogService.name.equals(pkgName)||tempName.equals(pkgName)){
//
                   }else {

                       appInfo = getAppInfo(pkgName);
                       //应用到应用，应用到桌面，桌面到应用
                       if(!isHome()) {
                           //从一个应用到另一个应用获取之前一个应用的usagetime，并存当前应用
                           //判断是从应用到应用还是桌面到应用
                           //桌面到应用
                           MyDBDao.getInstance(getApplicationContext()).insertAppTimeInfo(appInfo);
                          int usageTime = MyDBDao.getInstance(getApplicationContext()).updateUsageTime(appInfo,0);
                           long lastSecondST = MyDBDao.getInstance(getApplicationContext()).selectLastSecond();
                           MyDBDao.getInstance(getApplicationContext()).sumForAppUsageTime(appInfo);
                       }else{
                           //从一个应用到桌面，存放该应用的usagetime   应用到桌面
                           int usageTime =MyDBDao.getInstance(getApplicationContext()).updateUsageTime(appInfo,1);
                           long lastST = MyDBDao.getInstance(getApplicationContext()).selectLast();
                           MyDBDao.getInstance(getApplicationContext()).updateDayTime(lastST,usageTime);
                       }


                   }
                   intent.setAction("startTime");
                   intent.putExtra("pkgname", pkgName);
                   sendBroadcast(intent);
                   WatchDogService.name = pkgName;
               }
            }
        }).start();
    }

    //获取当前正在使用的应用的包名
    public String getTaskPackName(){
        String currentApp = "CurrentNULL";
        //5.0以上获取当前应用包名的方法

        if(Build.VERSION.SDK_INT>=21){
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,time - 1000 * 1000,time);
            if(appList!=null&&appList.size()>0){
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats:appList
                     ) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                }
                if(mySortedMap!=null&&!mySortedMap.isEmpty()){
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }else{
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        Log.d("getTaskPackName", "getTaskPackName: "+currentApp);
        return currentApp;
    }


    //开屏广播
    class ScreenOnReceiver extends BroadcastReceiver {
        int num = 0;
        @Override
        public void onReceive(Context context, Intent intent) {
            //插入熄灭屏幕时的数据
            OnOffEntity onOffEntity = new OnOffEntity();
            long time =System.currentTimeMillis();
            onOffEntity.setOnOffTime(time);
            onOffEntity.setStatus(1);
            onOffEntity.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
            MyDBDao.getInstance(getApplicationContext()).insertOnOff(onOffEntity);
            if(!isHome()){
                Log.d("xiangdeng", "onReceive 开机发现是在: "+getTaskPackName());
                MyDBDao.getInstance(getApplicationContext()).insertAppTimeInfo(getAppInfo(getTaskPackName()));
            }
        }
    }

    //关屏广播
    class ScreenOffReceiver extends BroadcastReceiver{
        int num=0;
        @Override
        public void onReceive(Context context, Intent intent) {

            //插入熄灭屏幕时的数据
            OnOffEntity onOffEntity = new OnOffEntity();
            long time =System.currentTimeMillis();
            onOffEntity.setOnOffTime(time);
            onOffEntity.setStatus(0);
            onOffEntity.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
            MyDBDao.getInstance(getApplicationContext()).insertOnOff(onOffEntity);
            if(!isHome()){
                //若不是在桌面通过熄灭时间计算出当前应用的usagetime
                int usageTime = MyDBDao.getInstance(getApplicationContext()).updateUsageTime(getAppInfo(getTaskPackName()),1);
                MyDBDao.getInstance(getApplicationContext()).updateDayTime(appInfo.getStartTime(),usageTime);
            }

        }
    }

    class goToAppReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            tempName = intent.getStringExtra("pkgname");

        }
    }

    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


    /**
     * 通过包名构造一个AppInfo
     * @param pkgName
     * @return
     */
    public static AppInfo getAppInfo(String pkgName){
        AppInfo appInfo = new AppInfo();
        appInfo.setPakageName(pkgName);
        appInfo.setStartTime( (long)System.currentTimeMillis());
        Log.d("insertAppTimeInfo", "getAppInfo: "+appInfo.getStartTime());
        try {
            mApplicationInfo = pm.getApplicationInfo(pkgName,PackageManager.GET_META_DATA);
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
