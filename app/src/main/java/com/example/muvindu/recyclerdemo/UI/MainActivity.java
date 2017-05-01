package com.example.muvindu.recyclerdemo.UI;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.muvindu.recyclerdemo.DataLoader.SongList_loader;
import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.adapter.SongAdapter;

import java.io.FileDescriptor;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recView;
    private SongAdapter adapter;
    private SongList_loader loader=new SongList_loader(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recView=(RecyclerView)findViewById(R.id.recView_layout);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongAdapter(loader.SongList(),this);
        recView.setAdapter(adapter);


    }


    public ArrayList<Song> SongList(){

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, MediaStore.Audio.Media.IS_MUSIC + " !=0", null, null);
        ArrayList<Song> songs=new ArrayList<>();
        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            long art;//=//songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID);





           do {
               // for (int i=0;i<5;i++){
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


                song.setAlbumId(art);
                song.setSongName(currentTitle);
                song.setId(currentId);
                //  if (bm!=null){song.setAlbum(bm);}
                songs.add(song);

          } while(songCursor.moveToNext());
                   // ;
                   // }
        }


        return songs;
    }


    public Bitmap getAlbumart(Long album_id)
    {
        Bitmap bm =null;

        try
        {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd =getBaseContext().getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null)
            {
                FileDescriptor fd = pfd.getFileDescriptor();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=4;
                options.inScaled=true;
               //options.inJustDecodeBounds=true;

                bm = BitmapFactory.decodeFileDescriptor(fd,null,options);
               // bm=Bitmap.createScaledBitmap(bm,75,75,true);

            }
        } catch (Exception e) {
            String x = e.getMessage().toString();
        }
        return bm;
    }


}
