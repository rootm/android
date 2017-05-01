package com.example.muvindu.recyclerdemo.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Muvindu on 1/1/2017.
 */

public class serviceStat {
    public boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MediaService.class.getName().equals(service.service.getClassName())) {
                Toast.makeText(context.getApplicationContext(), "Service running", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        Toast.makeText(context.getApplicationContext(), "Service not", Toast.LENGTH_LONG).show();
        return false;
    }
}
