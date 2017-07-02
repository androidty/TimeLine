package com.ty.android.timeline.entity;

/**
 * Created by Android on 2017/4/7.
 */

public class DayData {
    private String packageName;
    private String AppName;
    private int count;
    private int usageTime;
    private String date;
    private long startTime;

    public DayData(String packageName, String appName, int count, int usageTime, String date) {
        this.packageName = packageName;
        AppName = appName;
        this.count = count;
        this.usageTime = usageTime;
        this.date = date;
    }

    public DayData() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "DayData{" +
                "packageName='" + packageName + '\'' +
                ", AppName='" + AppName + '\'' +
                ", count=" + count +
                ", usageTime=" + usageTime +
                ", date='" + date + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
