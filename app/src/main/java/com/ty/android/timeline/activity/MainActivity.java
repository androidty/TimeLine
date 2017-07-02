package com.ty.android.timeline.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ty.android.timeline.R;
import com.ty.android.timeline.adapter.CircleProAdapter;
import com.ty.android.timeline.database.MyDBDao;
import com.ty.android.timeline.fragment.BaseFragment;
import com.ty.android.timeline.fragment.MainFragment;
import com.ty.android.timeline.service.WatchDogService;
import com.ty.android.timeline.utils.Tool;
import com.ty.android.timeline.view.CircleProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.button;
import static android.R.attr.fragment;
import static android.R.attr.path;
import static android.R.attr.start;
import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.ty.android.timeline.database.MyDataBaseHelper.getInstance;
import static com.ty.android.timeline.utils.Tool.date2Long;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private List<BaseFragment> fragments;

    private MyAdapter adapter;
    private int mCurrentItem ;
    private int lable =301;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getStoragePermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);


        Intent intent = new Intent(this, WatchDogService.class);
        startService(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        Tool.testDate();
        initData();

        initView();
        adapter = new MyAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        // 这里设置当前ViewPager显示的哪个Fragment，让ViewPager开始就可以向左滑动
        mViewPager.setCurrentItem(lable);
        // 设置上下的值
        fondre(true, true);

    }


    private void initData(){
        fragments = new ArrayList<BaseFragment>();
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        if (position > lable) {
            lable = position;
            fondre(false, true);
        }
        if (position < lable) {
            lable = position;
            fondre(true, false);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void fondre(boolean lastBool, boolean nextBool) {
        // 设置当前页面的值
        fragments.get(lable % fragments.size()).updateDate(lable);
        // 设置上个页面的值
        if (lastBool)
            fragments.get((lable - 1) % fragments.size()).updateDate(
                    (lable - 1));
        // 设置下个页面的值
        if (nextBool)
            fragments.get((lable + 1) % fragments.size()).updateDate(
                    (lable + 1));
    }







    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        private List<BaseFragment> mFragments;

        public MyAdapter(FragmentManager fm, List<BaseFragment> Fragments) {
            super(fm);
            this.mFragments = Fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // 由于只有3个Fragment，所以这里根据arg0计算该显示哪个Fragment
            return mFragments.get(arg0 % mFragments.size());
        }

        @Override
        public int getCount() {
            return 301;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                } catch (Exception e) {
                    Toast.makeText(this, "无法开启允许查看使用情况的应用界面", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this
                    , TinyActivity.class));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent appIntent = new Intent(MainActivity.this,AppActivity.class);
            appIntent.putExtra("AppActivity",date);
            startActivity(appIntent);
        } else if (id == R.id.nav_share) {
//            int x = MyDBDao.getInstance(getApplicationContext()).readPinciDay("20170408");
//            int x = MyDBDao.getInstance(getApplicationContext()).readGeShuDay("20170408");
            int x = MyDBDao.getInstance(getApplicationContext()).readCiShuDay("20170408");
            Toast.makeText(this, "" + x, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
//            Toast.makeText(this, ""+MyDBDao.getInstance(getApplicationContext()).selectLast(), Toast.LENGTH_SHORT).show();
            long m = MyDBDao.getInstance(getApplicationContext()).selectLast();
//            String x = Tool.long2Date(m,"");
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
            long time = System.currentTimeMillis();
            String dateStr = dateformat.format(new Date(time));


            Toast.makeText(this, m + "  " + dateStr + "  " + time, Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "User refused to grant the permission", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Authorization failed", Toast.LENGTH_LONG).show();
        }
    }

    private void getStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "若不通过此权限则无法加载游戏数据", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {

        }
    }


    public String date = defaultDate();

    public String defaultDate() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        long time = System.currentTimeMillis();
        String dateStr = dateformat.format(new Date(time));
        return dateStr;
    }





}
