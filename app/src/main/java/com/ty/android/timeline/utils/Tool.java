package com.ty.android.timeline.utils;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.util.TypedValue;

import com.ty.android.timeline.MyApplication;
import com.ty.android.timeline.database.MyDBDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Android on 2017/3/12.
 */

public class Tool {
    public static void someTag(Context context) {

    }

    /**
     * sp转px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static long date2Long(String date, String type) {
        type = "yyyy-mm-dd";
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        long time = 0;

        try {
            time = dateformat.parse(date).getTime();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        System.out.println(time);
        return time;
    }

    public static String long2Date(int time, String type) {
        type = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String dateStr = dateformat.format(new Date(System.currentTimeMillis()));
        return dateStr;
    }

    public static long getDays() {
        long start = MyDBDao.getInstance(MyApplication.getInstance()).readMinStartTime();
        long now = System.currentTimeMillis();
        int result = (int) (new Long(now) - new Long(start));
        result = result / 1000 / 3600 / 24;
        Log.d("getResult", "getDays: " + result);
        return result;
    }

    public static List<String> getDates(int much) {
        String type = "yyyyMMdd";
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        long now = System.currentTimeMillis();
        List<String> lists = new ArrayList<>();
//        Calendar calendar= Calendar.getInstance();
//        calendar.setTime(new Date());
        for (int i = much; i >=0; i--) {
            Calendar calendar= Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-i);//让日期加1
//            Log.d("testDate", "testDate: "+(calendar.get(Calendar.DATE)+"  "+(calendar.get(Calendar.MONTH)+1)));//加1之后的日期Top
            String res ="";

            res=calendar.get(Calendar.YEAR)+"";
            if(calendar.get(Calendar.MONTH)+1<10){
                res =res+"0"+(calendar.get(Calendar.MONTH)+1);
            }else{
                res =res+""+(calendar.get(Calendar.MONTH)+1);
            }
            if(calendar.get(Calendar.DATE)<10){
                res =res+"0"+(calendar.get(Calendar.DATE));
            }else{
                res =res+""+(calendar.get(Calendar.DATE));
            }
            lists.add(res);

        }
        return lists;
    }

    public static String testDate(){


        return null;
    }

}
