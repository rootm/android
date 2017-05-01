package com.example.muvindu.recyclerdemo.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.muvindu.recyclerdemo.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.example.muvindu.recyclerdemo.Services.serviceConnection.player;
import static com.example.muvindu.recyclerdemo.UI.player_main.play;
import static com.example.muvindu.recyclerdemo.UI.player_main.running;
import static com.example.muvindu.recyclerdemo.Utils.utils.getscreenSize;
import static com.example.muvindu.recyclerdemo.Utils.utils.getscreenHeigth;
import static com.example.muvindu.recyclerdemo.adapter.SongAdapter.getUri;

public class nowPlaying extends AppCompatActivity {

    private SeekBar seekBar;
    private Handler handler=new Handler();
    public static Toolbar mActionBarToolbar;
    static  ImageView albumImage;

    private TextView album;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running=true;


        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/Aaargh.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "Fonts/ABeeZee-Regular.otf");

        setContentView(R.layout.activity_now_playing);

        seekBar=(SeekBar)findViewById(R.id.playing_seekbar) ;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });
        handler.postDelayed(thread,100);

        mActionBarToolbar = (Toolbar) findViewById(R.id.playerToolBar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("My title");

        Toast.makeText(this,String.valueOf(getscreenHeigth(this)) , Toast.LENGTH_LONG).show();
         albumImage =(ImageView) findViewById(R.id.albumImage);
        final float height=(getscreenHeigth(this));

        if(getscreenHeigth(this)<=500){
            albumImage.getLayoutParams().height= (int)(height/2.45);
            albumImage.getLayoutParams().width= (int)(height/2.45);
            albumImage.requestLayout();
        }

        else if (getscreenHeigth(this)<=540){
            albumImage.getLayoutParams().height= (int)(height/1.5);
            albumImage.getLayoutParams().width= (int)(height/1.5);
            albumImage.requestLayout();


        } else if (getscreenHeigth(this)<=940){
            albumImage.getLayoutParams().height= (int)(height/1.35);
            albumImage.getLayoutParams().width= (int)(height/1.35);
            albumImage.requestLayout();


        }else{
            albumImage.getLayoutParams().height= (int)(height/1.15);
            albumImage.getLayoutParams().width= (int)(height/1.15);
            albumImage.requestLayout();
        }


        album=(TextView) findViewById(R.id.albumName);
        TextView startTime =(TextView) findViewById(R.id.startTime);
        TextView endTime =(TextView) findViewById(R.id.endTime);
        startTime.setTypeface(font1);
        endTime.setTypeface(font1);
       album.setTypeface(font);
        setImage(getUri(),this.getApplicationContext(),0);

        mAdView = (AdView) findViewById(R.id.adView);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (height<=500){
                    albumImage.getLayoutParams().height= (int)(height/2.7);
                    albumImage.getLayoutParams().width= (int)(height/2.7);
                    albumImage.requestLayout();
                }
                else if (height<=540){
                    albumImage.getLayoutParams().height= (int)(height/1.75);
                    albumImage.getLayoutParams().width= (int)(height/1.75);
                    albumImage.requestLayout();}
             else if (height<=940){
                albumImage.getLayoutParams().height= (int)(height/1.45);
                albumImage.getLayoutParams().width= (int)(height/1.45);
                albumImage.requestLayout();


            }
                if (mAdView.getVisibility() == View.GONE) {
                    mAdView.setVisibility(View.VISIBLE);
                }
            }
        });


        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("9C5C4DD7BFBD799D2DEE86B698AE7F1E")
                .build();
        mAdView.loadAd(adRequest);
    }



    public void nowPlaying_playPause(View view) {
        ImageButton playBtn =(ImageButton) findViewById(R.id.nowPlayin_playbtn);
        if (player!=null) {
            if (player.mediaPlayer.isPlaying()) {
                play.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.pause_btn));
                playBtn.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.pause_btn));
                player.pause();
            }else{
                play.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.play_btn));
                playBtn.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.play_btn));
                player.resume();
            }
        }

    }

    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            if (player!=null) {
                if (player.mediaPlayer.isPlaying()) {
                     seekBar.setMax(player.mediaPlayer.getDuration());
                     seekBar.setProgress(player.mediaPlayer.getCurrentPosition());

                    // seekBar.setProgress(player.mediaPlayer.getCurrentPosition());
                }

            }
            handler.postDelayed(thread,100);


        }
    });


    @Override
    protected void onDestroy() {
        super.onDestroy();
        running=false;

    }

    public static void setImage(Uri uri, final Context context,int state){

        if (state==0){Glide.with(context).load(uri).centerCrop().into(albumImage);}
        else if (state==1){
            Glide.with(context).load(uri).centerCrop().animate(R.anim.right2mid).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                   Animation animSlide = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.right2mid);

                   albumImage.startAnimation(animSlide);
                    return false;
                }
            }).into(albumImage);
        }
        else if((state==2)){
            Glide.with(context).load(uri).centerCrop().animate(R.anim.left2mid).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    Animation animSlide = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.left2mid);

                    albumImage.startAnimation(animSlide);
                    return false;
                }
            }).into(albumImage);

        }


    }

    public void previous(View view) {
        if (player!=null) {


            Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mid2right);

            albumImage.startAnimation(animSlide);

            animSlide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    player.previous();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }


    }

    public void next(View view) {
        if (player!=null) {

          Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mid2left);

            albumImage.startAnimation(animSlide);

           animSlide.setAnimationListener(new Animation.AnimationListener() {
            @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                     player.next();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        }
    }




    public void newx(View view) {
        player.txt();
    }
}


