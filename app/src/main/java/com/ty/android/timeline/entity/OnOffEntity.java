package com.ty.android.timeline.entity;

/**
 * Created by Android on 2017/4/13.
 */

public class OnOffEntity {
    private long onOffTime;
    private int status;
    private String date;

    public OnOffEntity(long onOffTime, int status, String date) {
        this.onOffTime = onOffTime;
        this.status = status;
        this.date = date;
    }

    public OnOffEntity() {
    }

    public long getOnOffTime() {
        return onOffTime;
    }

    public void setOnOffTime(long onOffTime) {
        this.onOffTime = onOffTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
