package com.ty.android.timeline.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by android_1 on 2016/9/18.
 */
public class ProtectService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
//                    Log.d("watchDogService", "run: 123456489786456489");
                    if(!ServiceUtils.isRunningService(ProtectService.this,"com.ty.android.timeline.service.WatchDogService")){
                        Intent intent = new Intent(ProtectService.this,WatchDogService.class);
                        startService(intent);
                    }
                }
            }
        });
        thread.start();
        return START_REDELIVER_INTENT;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
