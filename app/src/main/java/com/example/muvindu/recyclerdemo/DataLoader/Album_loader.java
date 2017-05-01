package com.example.muvindu.recyclerdemo.DataLoader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.muvindu.recyclerdemo.Model.Album;
import com.example.muvindu.recyclerdemo.Model.Song;

import java.util.ArrayList;

/**
 * Created by Muvindu on 1/3/2017.
 */

public class Album_loader extends ContextWrapper {
    public Album_loader(Context base) {
        super(base);
    }

    public ArrayList<Album> AlbumList(){

        ContentResolver contentResolver =getContentResolver();
        Uri AlbumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor AlbumCursor = contentResolver.query(AlbumUri,  new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"},null,null,null);
        ArrayList<Album> Albums=new ArrayList<>();
        if(AlbumCursor != null && AlbumCursor.moveToFirst())
        {
           do {
               Albums.add(new Album(AlbumCursor.getLong(0), AlbumCursor.getString(1), AlbumCursor.getString(2), AlbumCursor.getLong(3), AlbumCursor.getInt(4), AlbumCursor.getInt(5)));

            } while(AlbumCursor.moveToNext());
            if (AlbumCursor!=null){
                AlbumCursor.close();
            }
        }


        return Albums;
    }

}
