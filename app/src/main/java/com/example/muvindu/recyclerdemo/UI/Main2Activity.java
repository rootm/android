package com.example.muvindu.recyclerdemo.UI;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.muvindu.recyclerdemo.Fragments.album_fragment;
import com.example.muvindu.recyclerdemo.Fragments.songList_fragment;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.Services.MediaService;
import com.example.muvindu.recyclerdemo.Services.serviceConnection;
import com.example.muvindu.recyclerdemo.Services.serviceStat;
import com.example.muvindu.recyclerdemo.adapter.SongAdapter;
import com.example.muvindu.recyclerdemo.adapter.pageAdapter;

import static com.example.muvindu.recyclerdemo.Services.serviceConnection.player;

public class Main2Activity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public static final String Broadcast_stop = "com.example.muvindu.recyclerdemo.Stop";
    public static SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        tabs.setTabTextColors(ContextCompat.getColor(this, android.R.color.holo_red_dark), ContextCompat.getColor(this, android.R.color.white));
        tabs.setBackgroundColor(ContextCompat.getColor(this, android.R.color.background_dark));
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        tabs.setSmoothScrollingEnabled(true);
        tabs.setupWithViewPager(viewPager);
        serviceStat state = new serviceStat();
        Boolean serviceState = state.isMyServiceRunning(this);
        serviceConnection connection = new serviceConnection();
        if (serviceState) {
            Intent playerIntent = new Intent(this, MediaService.class);

            bindService(playerIntent, connection.newServiceConnection, Context.BIND_AUTO_CREATE);

            //  }// else{
        }
        //handler.postDelayed(progress,100);

    }


    private void setupViewPager(ViewPager viewPager) {
        pageAdapter adapter = new pageAdapter(getSupportFragmentManager());
        adapter.addFragment(new songList_fragment(), "Songs");
        adapter.addFragment(new album_fragment(), "Album");
        adapter.addFragment(new songList_fragment(), "Artist");
        viewPager.setAdapter(adapter);
    }


    public void stopService(View view) {
        Intent broadcastIntent = new Intent(Broadcast_stop);
        sendBroadcast(broadcastIntent);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            player.mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}