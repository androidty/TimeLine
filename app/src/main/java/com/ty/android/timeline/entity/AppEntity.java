package com.ty.android.timeline.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Android on 2017/4/13.
 */

public  class AppEntity {
    private String packageName;
    private Drawable appIcon;
    private int usageTime;
    private int usagePinci;

    public AppEntity() {
    }

    public AppEntity(String packageName, Drawable appIcon, int usageTime, int usagePinci) {
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.usageTime = usageTime;
        this.usagePinci = usagePinci;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public int getUsagePinci() {
        return usagePinci;
    }

    public void setUsagePinci(int usagePinci) {
        this.usagePinci = usagePinci;
    }
}
