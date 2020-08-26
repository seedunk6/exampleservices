package com.android.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;


public class MyService extends JobIntentService {

    public static final String TAG = MyService.class.getSimpleName();

    private static final int JOB_ID = 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        startMyOwnForeground(this, JOB_ID);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i(TAG, "Executing work ");

        executingwork(intent);

    }

    public void executingwork(@NonNull Intent intent){

        toast("Executing service... ");

        Log.i(TAG, "Running service... ");
        for (int i = 0; i < 5; i++) {
            Log.i(TAG, "Running service " + (i + 1)
                    + "/5 @ " + SystemClock.elapsedRealtime());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toast("Completed service");
        Log.i(TAG, "onDestroy");
        stopForeground(true);
    }

    final Handler mHandler = new Handler();

    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override public void run() {
                Toast.makeText(MyService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startMyOwnForeground(JobIntentService service, int JOB_ID) {
        String NOTIFICATION_CHANNEL_ID = service.getPackageName();
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(chan);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        service.startForeground(JOB_ID, notification);
    }

}