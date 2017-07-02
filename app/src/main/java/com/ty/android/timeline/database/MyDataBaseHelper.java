package com.ty.android.timeline.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.view.ViewPager;

import java.io.File;

import static android.R.attr.version;

/**
 * Created by Android on 2017/3/14.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "apptime.db";
    public static final int VERSION = 1;
    public static MyDataBaseHelper myDataBaseHelper;
    private MyDataBaseHelper(Context context) {
        super(context, getDbName(context), null, VERSION);
    }

    public static MyDataBaseHelper getInstance(Context context) {
        if (myDataBaseHelper == null) {
            myDataBaseHelper = new MyDataBaseHelper(context);
        }
        return myDataBaseHelper;
    }


    public static String getDbName(Context context) {
        String dataBaseName = DB_NAME;
        String dbPath = null;
        boolean isSdCardEnable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            isSdCardEnable = true;
        }
        if (isSdCardEnable) {
            dbPath = Environment.getExternalStorageDirectory().getPath() + "/database/";
        } else {
            dbPath = context.getFilesDir().getPath() + "/database/";
        }
        File file = new File(dbPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        dataBaseName = dbPath + DB_NAME;
//        Log.d("dataRecovery", "getDbName: "+dataBaseName);
        return dataBaseName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlForTime = "create table if not exists appusetime(packge_name text not null,app_name text," +
                "start_time integer,usage_time integer,statu integer)";
        String sqlForAppIcon = "create table if not exists appIcon(packge_name text,icon blob)";
        String sqlForDayTable = "create table if not exists daytable(" +
                "packge_name text,app_name text,usage_time integer,usage_count integer,date text,start_time integer)";
        String sqlForOnOff = "create table if not exists onOff(time integer,status tinyint,date text)";

        db.execSQL(sqlForTime);
        db.execSQL(sqlForAppIcon);
        db.execSQL(sqlForDayTable);
        db.execSQL(sqlForOnOff);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists appusetime");
        db.execSQL("drop table if exists apppIcon");
        db.execSQL("drop table if exists daytable");
        db.execSQL("drop table if exists onOff");
        onCreate(db);
    }
}
