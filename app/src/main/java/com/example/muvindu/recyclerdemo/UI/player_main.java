package com.example.muvindu.recyclerdemo.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.muvindu.recyclerdemo.DataLoader.Album_loader;
import com.example.muvindu.recyclerdemo.Fragments.album_fragment;
import com.example.muvindu.recyclerdemo.Fragments.songList_fragment;

import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.Services.MediaService;
import com.example.muvindu.recyclerdemo.Services.serviceConnection;
import com.example.muvindu.recyclerdemo.Services.serviceStat;
import com.example.muvindu.recyclerdemo.adapter.pageAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import static com.example.muvindu.recyclerdemo.Services.serviceConnection.player;
import static com.example.muvindu.recyclerdemo.Utils.utils.getscreenSize;


public class player_main extends AppCompatActivity {
    private Album_loader album_loader = new Album_loader(this);
    private RecyclerView recview;
    public static ProgressBar progressBar;
    public static ImageButton play  ;
    public static ImageView small_albumArt;
    public static TextView topic ;
   public static LinearLayout btmPlayer;
   // public

    private AdView mAdView;
    public static boolean running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_main);

        btmPlayer=(LinearLayout) findViewById(R.id.bottomPlayer);
        play =(ImageButton) findViewById(R.id.playButton);
        small_albumArt=(ImageView) findViewById(R.id.albumArt);
        topic=(TextView) findViewById(R.id.songName_small);
        topic.setSelected(true);
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/Aaargh.ttf");
       topic.setTypeface(font);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        ViewPager viewPager = (ViewPager)  findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.mainTabLayout);

        tabs.setTabTextColors( ContextCompat.getColor(this, android.R.color.white),ContextCompat.getColor(this, android.R.color.holo_red_dark));
        tabs.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTab));
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
        changeTabsFont(tabs);
        if (player!=null){
            player.setSongName();
            setVisible();

        }else{
            topic.setText("");
        }

        mAdView = (AdView) findViewById(R.id.adView1);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Toast.makeText(mAdView.getContext().getApplicationContext(),String.valueOf(errorCode)+ " erroooor",Toast.LENGTH_LONG);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();



                if (mAdView.getVisibility() == View.GONE) {
                    mAdView.setVisibility(View.VISIBLE);
                }
            }
        });


        AdRequest adRequest = new AdRequest.Builder()

                .build();
        mAdView.loadAd(adRequest);

    }

    private void setupViewPager(ViewPager viewPager) {
        pageAdapter adapter = new pageAdapter(getSupportFragmentManager());
        adapter.addFragment(new songList_fragment(), "Songs");
        adapter.addFragment(new album_fragment(), "Album");
        adapter.addFragment(new songList_fragment(), "Artist");
        adapter.addFragment(new songList_fragment(), "Songs");
        adapter.addFragment(new album_fragment(), "Album");
        adapter.addFragment(new songList_fragment(), "Artist");


        viewPager.setAdapter(adapter);
    }

    private void changeTabsFont(TabLayout Tablayout) {
        float size;
        size=getscreenSize(this.getApplicationContext());
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/Aaargh.ttf");
        ViewGroup vg = (ViewGroup) Tablayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);


                }
            }
        }
    }


    public void playPause(View view) {

        if (player!=null) {
            if (player.mediaPlayer.isPlaying()) {
                play.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.pause_btn));
                player.pause();
            }else{
                play.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.play_btn));
                player.resume();
            }
        }
    }

    public void previousSong(View view) {
        if (player!=null) {
            player.previous();
        }


    }

    public void nextSong(View view) {
        if (player!=null) {
            player.next();
        }
    }

    public void playing_activity(View view) {

        if (player!=null){
            Intent intent = new Intent(this, nowPlaying.class);

            startActivity(intent);
        }

    }

    public void setVisible(){

        if (btmPlayer.getVisibility()==View.GONE){
            btmPlayer.setVisibility(View.VISIBLE);
        }
    }
}
