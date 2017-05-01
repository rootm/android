package com.example.muvindu.recyclerdemo.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muvindu.recyclerdemo.Audio.music;
import com.example.muvindu.recyclerdemo.Interface.Album_songList;
import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;

import java.util.List;

import static com.example.muvindu.recyclerdemo.Services.MediaService.getListType;
import static com.example.muvindu.recyclerdemo.Services.MediaService.setSonglist;
import static com.example.muvindu.recyclerdemo.UI.nowPlaying.setImage;
import static com.example.muvindu.recyclerdemo.UI.player_main.running;
import static com.example.muvindu.recyclerdemo.UI.player_main.small_albumArt;
import static com.example.muvindu.recyclerdemo.UI.player_main.topic;
import static com.example.muvindu.recyclerdemo.adapter.SongAdapter.setText;

/**
 * Created by Muvindu on 3/17/2017.
 */

public class AlbumSong_Adapter extends RecyclerView.Adapter<AlbumSong_Adapter.ViewHolder> {


    public  List<Song> AlbumSongList;
    private LayoutInflater inflater;
    private music Music=new music();

    private Context contextView;



    public AlbumSong_Adapter(List<Song> list,Context context){
        this.AlbumSongList =list;
        contextView=context;
        this.inflater=LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.album_song_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song= AlbumSongList.get(position);
        holder.albumSong.setText(song.getSongName());
        Typeface font = Typeface.createFromAsset(contextView.getAssets(), "Fonts/Aaargh.ttf");
        holder.albumSong.setTypeface(font);
    }

    @Override
    public int getItemCount() {
        return this.AlbumSongList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,Album_songList{
        public TextView albumSong;
        public View albumSong_container;
        public ImageButton option;

        public ViewHolder(View itemView) {
            super(itemView);
            albumSong = (TextView) itemView.findViewById(R.id.Album_songName);
            //  albumArt = (ImageView) itemView.findViewById(R.id.album_image);
            albumSong_container = itemView.findViewById(R.id.Album_songContainer);
            option = (ImageButton) itemView.findViewById(R.id.Album_optionButton);
            albumSong_container.setOnClickListener(this);
            option.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.Album_songContainer){
                AlbumSongClick(getAdapterPosition(),v);
            }else if (v.getId()== R.id.Album_optionButton){
                OptionClick(getAdapterPosition(),v);

            }
        }

        @Override
        public void AlbumSongClick(int position, View v) {
            contextView=v.getContext();


            Music.playAudio(AlbumSongList.get(position).getData(),position,contextView);
            if (getListType() != "album") {
                setSonglist(AlbumSongList,"album");
            }
            setText(AlbumSongList.get(position).getSongName(),AlbumSongList.get(position).getAlbumId(),contextView,0);

            Toast.makeText(v.getContext(), AlbumSongList.get(position).getSongName(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OptionClick(int position, View v) {

        }
    }



}
