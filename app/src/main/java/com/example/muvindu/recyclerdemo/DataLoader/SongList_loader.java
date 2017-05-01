package com.example.muvindu.recyclerdemo.DataLoader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;

import java.util.ArrayList;

/**
 * Created by Muvindu on 12/4/2016.
 */

public class SongList_loader extends ContextWrapper {

    public SongList_loader(Context base) {
        super(base);
    }

    public ArrayList<Song> SongList(){

        ContentResolver contentResolver =getContentResolver();
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
                //  if (bm!=null){song.setAlbum(bm);}
                songs.add(song);

            } while(songCursor.moveToNext());
        }


        return songs;
    }

}
