package com.example.muvindu.recyclerdemo.Fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muvindu.recyclerdemo.DataLoader.SongList_loader;
import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muvindu on 12/8/2016.
 */

public class songList_fragment extends Fragment{
    public static RecyclerView recView;
    private SongAdapter adapter;
    private SongList_loader loader;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recView=(RecyclerView)inflater.inflate(R.layout.songlist_fragment,container,false);
        recView.setLayoutManager(new LinearLayoutManager(recView.getContext()));
        adapter = new SongAdapter(SongList(),recView.getContext());
        recView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(createHelperCallback());

        itemTouchHelper.attachToRecyclerView(recView);


        return recView;
    }

    private ItemTouchHelper.Callback createHelperCallback(){

       ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
           @Override
           public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
             //
               return true;
           }

           @Override
           public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
               super.clearView(recyclerView, viewHolder);
               viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);

               viewHolder.itemView.setAlpha(1);
           }

           @Override
           public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

               if (actionState==ItemTouchHelper.ACTION_STATE_IDLE){

               }else{
                   viewHolder.itemView.setAlpha(0.6f);
                   viewHolder.itemView.setBackgroundColor(Color.LTGRAY) ;
               }


               super.onSelectedChanged(viewHolder, actionState);
           }

           @Override
           public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

           }
       };
return simpleCallback;
    }

    public void moveItem(int oldPosition, int newPosition){
        Song oldsong=(Song) adapter.songList.get(oldPosition);
        adapter.songList.remove(oldPosition);
        adapter.songList.add(newPosition,oldsong);

        adapter.notifyItemMoved(oldPosition,newPosition);

    }


    public ArrayList<Song> SongList(){

        ContentResolver contentResolver = recView.getContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, MediaStore.Audio.Media.IS_MUSIC + " !=0", null, null);
        ArrayList<Song> songs=new ArrayList<>();
        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            long art;//=//songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID);
            int dataID=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);




            do {
                // for (int i=0;i<5;i++){
                String data = songCursor.getString(dataID);
                art=Long.valueOf(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)));
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                Bitmap bm=null;
                String artist = songCursor.getString(songCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = songCursor.getString(songCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int duration = songCursor.getInt(songCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Song song = new Song();
                // Drawable d = ContextCompat.getDrawable(this,R.drawable.albumart);
                //     Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.albumart);
                //     bm=getAlbumart(art);
                //      if (bm!=null){ song.setAlbum(bm);}
                //      else{song.setAlbum(bImage);}

                song.setData(data);
                song.setAlbumId(art);
                song.setSongName(currentTitle);
                song.setId(currentId);
                song.setAlbum(album);
                song.setArtist(artist);
                song.setDuration(duration);;
                //  if (bm!=null){song.setAlbum(bm);}
                songs.add(song);

            } while(songCursor.moveToNext());
            // ;
            // }
        }


        return songs;
    }






}