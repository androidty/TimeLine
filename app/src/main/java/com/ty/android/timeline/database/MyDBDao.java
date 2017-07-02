package com.ty.android.timeline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ty.android.timeline.R;
import com.ty.android.timeline.entity.AppEntity;
import com.ty.android.timeline.entity.AppInfo;
import com.ty.android.timeline.entity.DayData;
import com.ty.android.timeline.entity.OnOffEntity;
import com.ty.android.timeline.fragment.RecyclerFragment;
import com.ty.android.timeline.utils.AppUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Android on 2017/3/31.
 */

public class MyDBDao {
    private static MyDBDao instance;
    private static SQLiteDatabase db;
    private static Context context;

    private MyDBDao(Context context) {
        this.context = context;
        db = MyDataBaseHelper.getInstance(context).getWritableDatabase();
    }

    public static MyDBDao getInstance(Context context) {
        if (instance == null) {
            instance = new MyDBDao(context);
        }
        return instance;
    }


    /**
     * 一些数据库增删改查的方法
     */
    //插入一条当前正在使用的app信息,同时也插入应用图标以防以后卸载找不到图标
    public static void insertAppTimeInfo(AppInfo appInfo) {
        ContentValues values = new ContentValues();
        if(appInfo.getAppName()==null||appInfo.getAppName().equals("")){
            return ;
        }
        values.put("packge_name", appInfo.getPakageName());
        values.put("start_time", appInfo.getStartTime());
        values.put("app_name", appInfo.getAppName());
        db.insert("appusetime", null, values);
        //根据ago查找数据库中的哪个信息更改了找到该包名
        AppInfo newAppInfo = AppUtils.getAppInfo(getPackageByAgo(appInfo.getStartTime()));
        Log.d("newAppInfo", "updateUsageTime: " + newAppInfo.getPakageName());
        newAppInfo.setStartTime(appInfo.getStartTime());
        insertDayData(newAppInfo, appInfo.getUsageTime());


        //插入图标前先判断下icon表中是否已经有了这个图标，没有才插入
//        if(!hasThisIcon(appInfo.getPakageName())) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ((BitmapDrawable) appInfo.getAppDrawable()).getBitmap().compress(
//                    Bitmap.CompressFormat.PNG, 100, baos);
//
//            Object[] args = new Object[]{baos.toByteArray()};
//            Log.d("readAppIcon", "insertAppTimeInfo: "+args.length);
//            String sql = "insert into appIcon(icon,packge_name) values(?,?)";
//            db.execSQL(sql, new Object[]{args, appInfo.getPakageName()});
//            try {
//                baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public static int updateUsageTime(AppInfo appInfo, int type) {
        Log.d("updateUsageTime", "updateUsageTime: ***");
        ContentValues values = new ContentValues();
        long ago = 0;
        if (type == 0) {
            ago = selectLastSecond();
        } else if (type == 1) {
            ago = selectLast();
        }
        if (ago == 0) {
            return 0;
        }
        int usageTime = (int) (appInfo.getStartTime() - ago);
        values.put("usage_time", usageTime);
        String condition = "start_time=?";
        Log.d("usageklkk", "updateUsageTime: " + usageTime + "    " + ago);
        db.update("appusetime", values, condition, new String[]{ago + ""});
        return usageTime;
        //通过starttime修改daytable表中的usagetime
//        updateDayTime(ago,usageTime);
    }

    public static void updateDayTime(long ago, int usageTime) {

        db.execSQL("update daytable set usage_time=usage_time+? where start_time=?", new Object[]{usageTime, ago});

    }


    public static void sumForAppUsageTime(AppInfo appInfo) {
        int allUsage = 0;
        String packageName = appInfo.getPakageName();
        long now = appInfo.getStartTime();
        long today = getToday(now);
        Cursor c = db.rawQuery("select sum(usage_time) from appusetime where packge_name=? and start_time<=? and start_time>=?",
                new String[]{packageName, now + "", today + ""});
        if (c.moveToFirst()) {
            Log.d("sumForAppUsageTime", "sumForAppUsageTime: " + c.getInt(0));
            allUsage = c.getInt(0);
        }

        db.execSQL("update daytable set usage_time=? where packge_name=? and date=?", new Object[]{allUsage, packageName, getDate(now)});
    }

    private static long getToday(long now) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateformat.format(new Date(now));
        long time = 0;
        try {
            time = dateformat.parse(dateStr).getTime();
            Log.d("sumForAppUsageTime", "getToday: " + time + "    " + dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static boolean hasThisIcon(String packageName){
        boolean result = false;
        Cursor c = db.rawQuery("select * from appIcon where packge_name=?",new String[]{packageName});
        if(c.moveToFirst()){
            result = true;
        }
        return result;
    }

    public static String getDate(long now) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateformat.format(new Date(now));
        return dateStr;
    }

    private static String getPackageByAgo(long startTime) {
        Cursor c = db.rawQuery("select packge_name from appusetime where start_time=?", new String[]{startTime + ""});
        String result = "";
        if (c.moveToFirst()) {
            result = c.getString(c.getColumnIndex("packge_name"));
        }
        return result;
    }

    //插入解锁和熄屏的数据
    public static void insertOnOff(OnOffEntity onOffEntity){
        ContentValues values = new ContentValues();
        values.put("time", onOffEntity.getOnOffTime());
        values.put("status", onOffEntity.getStatus());
        values.put("date", onOffEntity.getDate());
        db.insert("onOff", null, values);
    }


    public static long selectLast() {
        Cursor c = db.rawQuery("select start_time from appusetime order by start_time desc limit 0,1", null);
        long result = 0;
        if (c.moveToFirst()) {
            result = c.getLong(c.getColumnIndex("start_time"));
        }
        return result;
    }


    //找倒数第二个
    public static long selectLastSecond() {
        Cursor c = db.rawQuery("select start_time from appusetime order by start_time desc limit 1,1", null);
        long result = 0;
        if (c.moveToFirst()) {
            result = c.getLong(c.getColumnIndex("start_time"));
        }
        return result;
    }

    //当更新usage_time时候同时更新那一天的时间表
    public static void insertDayData(AppInfo appInfo, long usageTime) {
        /**
         * 通过当前更新数据的包名和当天日期判断是否当天存在该包
         * 若有则更新usagetime和count
         * 若没有则追加数据
         */
        Log.d("updateUsageTime", "insertDayData: +++");
        String packageName = appInfo.getPakageName();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        String date = dateformat.format(new Date());


        if (hasDataInDay(packageName, date)) {
            //如果已经有这种数据则count+1，usageTime相加
            db.execSQL("update daytable set usage_count=usage_count+1 where packge_name=" + "'" + packageName + "'" + "and date=" + "'" + date + "'");
            db.execSQL("update daytable set start_time=? where packge_name=? and date=?", new Object[]{appInfo.getStartTime(), packageName, date});
        } else {
            DayData dayData = new DayData();
            dayData.setPackageName(packageName);
            dayData.setAppName(appInfo.getAppName());
            dayData.setDate(date);
            dayData.setCount(1);
            dayData.setUsageTime(0);
            dayData.setStartTime(appInfo.getStartTime());
            Log.d("insertDayData", "insertDayData: " + dayData);
            ContentValues values = new ContentValues();
            values.put("packge_name", dayData.getPackageName());
            values.put("app_name", dayData.getAppName());
            values.put("usage_time", dayData.getUsageTime());
            values.put("usage_count", dayData.getCount());
            values.put("date", dayData.getDate());
            values.put("start_time", dayData.getStartTime());
            db.insert("daytable", null, values);

        }
    }

    /**
     * 判断是否已经有要做修改的那条数据
     *
     * @param packageName
     * @param date
     * @return
     */
    public static boolean hasDataInDay(String packageName, String date) {
        boolean flag = false;
        Cursor c = db.rawQuery("select * from daytable where packge_name=? and date=?",
                new String[]{packageName, date});
        if (c.moveToFirst()) {
            flag = true;
        }
        Log.d("hasDataInDay", "hasDataInDay: " + flag);
        return flag;
    }


    //查找最大的使用时间


    /**
     * 以下为读取数据的相关方法
     */

    //读应用图标
    public static byte[] readIcon(String packageName) {
        byte[] bytes = null;
        Cursor c = db.rawQuery("select icon from appIcon where packge_name=?", new String[]{packageName});
        if (c.moveToFirst()) {
            bytes = c.getBlob(0);
        }
        return bytes;
    }

    //读取最小启动时间
    public static long readMinStartTime() {
        Cursor c = db.rawQuery("select min(start_time) from daytable", null);
        long result = 0;
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        return result;
    }


    //读某天的使用频次
    public static int readPinciDay(String date) {
        Cursor c = db.rawQuery("select sum(usage_count) from daytable where date=?", new String[]{date});
        int result = 0;
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        return result;

    }



    //读某天使用app的个数
    public static int readGeShuDay(String date) {
        Cursor c = db.rawQuery("select count(*) from daytable where date=?", new String[]{date});
        int result = 0;
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        return result;
    }

    //读某天使用app的次数
    public static int readCiShuDay(String date) {
        Cursor c = db.rawQuery("select sum(usage_count) from daytable where date=?", new String[]{date});
        int result = 0;
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        return result;
    }

    //读取某天使用的总时间数
    public static int readAllTimeADay(String date) {
        Cursor c = db.rawQuery("select sum(usage_time) from daytable where date=?", new String[]{date});
        int result = 0;
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        return result;
    }

    //读取某天的使用应用的包名和使用时间和使用次数
    public static List<AppEntity> readPUtUc(String date, int type) {
        List<AppEntity> list = new ArrayList<>();
        String sql = "";
        if (type == 0) {//按时间排序
            sql = "select packge_name,usage_time,usage_count from daytable where date=? order by usage_time desc";
        } else if (type == 1) {//按次数排序
            sql = "select packge_name,usage_time,usage_count from daytable where date=? order by usage_count desc";
        }
        Cursor c = db.rawQuery(sql, new String[]{date});
        while (c.moveToNext()) {
            AppEntity appEntity = new AppEntity();
            appEntity.setPackageName(c.getString(c.getColumnIndex("packge_name")));
            appEntity.setUsagePinci(c.getInt(c.getColumnIndex("usage_count")));
            appEntity.setUsageTime(c.getInt(c.getColumnIndex("usage_time")));
            list.add(appEntity);
        }
        Log.d("AppAdapter", "readPUtUc: "+list.size());
        return list;
    }

    //读取应用图标
    public static Drawable readAppIcon(String pakageName){
        Drawable drawable =null;
        byte[] bytes =null ;
        Cursor c = db.rawQuery("select icon from appIcon where packge_name=?",new String[]{pakageName});
        if(c.moveToFirst()){
            bytes = c.getBlob(c.getColumnIndex("icon"));
        }
        ByteArrayInputStream bais = null;
        if(bytes!=null){
            bais = new ByteArrayInputStream(bytes);
        }
        return Drawable.createFromStream(bais,"icon");
    }


    //根据日期读当天解锁总次数
    public static int readAllOnADay(String date){
        int result =0;
        Cursor c = db.rawQuery("select sum(status) from onOff where date=?",new String[]{date});
        if(c.moveToFirst()){
            result = c.getInt(0);
        }
        return result;
    }

}
