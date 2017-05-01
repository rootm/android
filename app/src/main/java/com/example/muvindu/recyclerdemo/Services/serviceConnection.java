package com.example.muvindu.recyclerdemo.Services;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.muvindu.recyclerdemo.UI.Main2Activity;

import java.sql.Wrapper;

/**
 * Created by Muvindu on 12/31/2016.
 */

public class serviceConnection {

    public static MediaService player;
    public static Boolean serviceBound=false;
    public ServiceConnection newServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.LocalBinder binder = (MediaService.LocalBinder) service;
            player = binder.getService();
       serviceBound = true;

            //Toast.makeText(, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          serviceBound=false;
        }
    };



}
