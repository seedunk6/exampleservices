package com.android.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SystemBootReceiver extends BroadcastReceiver {
    public static final String TAG = SystemBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.e(TAG, "Reboot");

            MyService.enqueueWork(context, new Intent(context, MyService.class));

        }

    }

}
