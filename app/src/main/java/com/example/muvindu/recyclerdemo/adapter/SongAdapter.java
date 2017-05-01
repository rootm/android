package com.example.muvindu.recyclerdemo.adapter;


import android.app.ActivityManager;
import android.app.LauncherActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muvindu.recyclerdemo.Audio.music;

import com.example.muvindu.recyclerdemo.Interface.recInterface;
import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.UI.nowPlaying;
import com.example.muvindu.recyclerdemo.UI.player_main;


import java.util.List;


import static com.example.muvindu.recyclerdemo.Services.MediaService.getListType;
import static com.example.muvindu.recyclerdemo.Services.MediaService.setSonglist;
import static com.example.muvindu.recyclerdemo.Services.serviceConnection.player;
import static com.example.muvindu.recyclerdemo.UI.nowPlaying.setImage;
import static com.example.muvindu.recyclerdemo.UI.player_main.btmPlayer;
import static com.example.muvindu.recyclerdemo.UI.player_main.running;
import static com.example.muvindu.recyclerdemo.UI.player_main.small_albumArt;

import static com.example.muvindu.recyclerdemo.UI.player_main.topic;

/**
 * Created by Muvindu on 12/6/2016.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    public static List<Song> songList;
    private LayoutInflater inflater;
    private music Music=new music();
    private int currentID = 0;
    private Context contextView;

    private static long id;

    public static Uri getUri() {
        final Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri,id);
        return uri;
    }
// private Boolean serviceBound=false;

    public static void setText(String name,long aid,Context context,int state){
        topic.setText(name);
         id=aid;
        final Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri,id);
        Glide.with(context).load(uri).centerCrop().into(small_albumArt);


        if (running){
            setImage(uri,context,state);
        }

    }


    public SongAdapter(List<Song> list, Context context){
        this.songList=list;
        contextView=context;
        this.inflater=LayoutInflater.from(context);

    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.song_item,parent,false);

        return new ViewHolder(view);
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song=songList.get(position);
        holder.songName.setText(song.getSongName());
        Typeface font = Typeface.createFromAsset(contextView.getAssets(), "Fonts/Aaargh.ttf");
        holder.songName.setTypeface(font);
        //holder.songName.setSelected(true);

      // final Uri sArtworkUri = Uri
         //       .parse("content://media/external/audio/albumart");

     //  Uri uri = ContentUris.withAppendedId(sArtworkUri,song.getAlbumId());
      //  Glide.with(holder.albumArt.getContext()).load(uri).centerCrop().into(holder.albumArt);
       // if (song.getAlbum()!=null){  holder.albumArt.setImageBitmap(song.getAlbum());}
        //Uri uri = ContentUris.withAppendedId(sArtworkUri,5);
       //

    }




    @Override
    public int getItemCount() {
        return songList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,recInterface {

        public TextView songName;
        public ImageView albumArt;
        public View container;
        public ImageView fav;


        public ViewHolder(final View itemView) {
            super(itemView);
            songName = (TextView) itemView.findViewById(R.id.songName);
          //  albumArt = (ImageView) itemView.findViewById(R.id.album_image);
            container = itemView.findViewById(R.id.songContainer);
            fav = (ImageView) itemView.findViewById(R.id.favButton);
            container.setOnClickListener(this);
            fav.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.songContainer){
                songClick(getAdapterPosition(),v);
            }else if (v.getId()== R.id.favButton){
                favClick(getAdapterPosition(),v);

            }
        }



        @Override
        public void songClick(int position,View v) {
            contextView=v.getContext();


            Music.playAudio(songList.get(position).getData(),position,contextView);
            if (getListType() != "all") {
                setSonglist(songList,"all");
            }
            setText(songList.get(position).getSongName(),songList.get(position).getAlbumId(),contextView,0);
            Toast.makeText(v.getContext(),songList.get(position).getSongName(),Toast.LENGTH_SHORT).show();
            if (btmPlayer.getVisibility()==View.GONE){
                btmPlayer.setVisibility(View.VISIBLE);
            }

        }



        @Override
        public void favClick(int position,View v) {
            Drawable notfav=ContextCompat.getDrawable(v.getContext(),android.R.drawable.btn_star_big_off);
            Drawable fav=ContextCompat.getDrawable(v.getContext(),android.R.drawable.btn_star_big_on);
            if (songList.get(position).getFavarite() ){
                //Toast.makeText(v.getContext(),"hi",Toast.LENGTH_SHORT).show();
           //  Glide.with(this).load(notfav).into(star);
               // star.setImageDrawable(notfav);
                songList.get(position).setFavarite(false);
            }else{
              //  star.setImageDrawable(fav);
              //  songList.get(position).setFavarite(true);
           //    Glide.with(star.getContext()).load(fav).into(star);
            }
        }
    }




}
