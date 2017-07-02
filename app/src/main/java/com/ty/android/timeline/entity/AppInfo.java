package com.ty.android.timeline.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Android on 2017/3/17.
 */

public class AppInfo {
    private String appName;
    private Drawable appDrawable;

    private String pakageName;
    private long startTime;
    private int statu ;
    private int usageTime;

    public AppInfo() {
    }

    public AppInfo(String pakageName, Integer startTime, int statu, int usageTime) {
        this.pakageName = pakageName;
        this.startTime = startTime;
        this.statu = statu;
        this.usageTime = usageTime;
    }

    public String getPakageName() {
        return pakageName;
    }

    public void setPakageName(String pakageName) {
        this.pakageName = pakageName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public Drawable getAppDrawable() {
        return appDrawable;
    }

    public void setAppDrawable(Drawable appDrawable) {
        this.appDrawable = appDrawable;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
