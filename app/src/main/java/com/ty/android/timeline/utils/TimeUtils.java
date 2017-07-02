package com.ty.android.timeline.utils;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.Date;

/**
 * Created by Android on 2017/4/7.
 */

public class TimeUtils {
    /**
     * 返回易读的日期
     * 如20170407返回2017/4/7
     */
    public static String getDate(String str) {
        char[] a = str.toCharArray();

        String res = "";
        Log.d("sssssop", "getDate: "+a.length+"  "+str);
        for (int i = 0; i < 4; i++) {
            res += a[i];
        }
        res = res + "/";
        if (a[4] == '0') {
            res = res + a[5] + "/";
        } else {
            res = res + a[4] + a[5] + "/";
        }
        if (a[6] == '0') {
            res = res + a[7];
        } else {
            res = res + a[6] + a[7];
        }
        Log.d("sssssop", "getDate: "+res);
        return res;
    }


    /**
     * 返回易读的使用时间
     */
    public static String getUsageTime(int usageTime){
        String str ="";
        usageTime = usageTime/1000;//得到秒数
        if(usageTime<30){
            str ="少于半分钟";
            return str;
        }else if(usageTime>60&&usageTime<3600){
            str = "已使用"+usageTime/60+"分钟";
            return str;
        }else if(usageTime>3600){
            int h = usageTime/3600;
            int m = usageTime%3600/60;
            str ="已使用"+h+"小时"+m+"分钟";
        }
        return str;
    }

}
