package com.example.muvindu.recyclerdemo.Fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.muvindu.recyclerdemo.DataLoader.Album_loader;
import com.example.muvindu.recyclerdemo.Model.Album;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.Utils.SpacesItemDecoration;
import com.example.muvindu.recyclerdemo.Utils.utils;
import com.example.muvindu.recyclerdemo.adapter.AlbumAdapter;
import com.example.muvindu.recyclerdemo.adapter.SongAdapter;

import java.util.ArrayList;

import static com.example.muvindu.recyclerdemo.Utils.utils.getscreenSize;


/**
 * A simple {@link Fragment} subclass.
 */
public class album_fragment extends Fragment {

private RecyclerView recView ;
    private AlbumAdapter adapter;
    private Album_loader albumLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        recView=(RecyclerView)inflater.inflate(R.layout.album_fragment,container,false);

        Toast.makeText(recView.getContext(),String.valueOf( getscreenSize (recView.getContext())),Toast.LENGTH_LONG).show();




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        albumLoader=new Album_loader(recView.getContext());
        adapter = new AlbumAdapter(albumLoader.AlbumList(),recView.getContext());
        recView.setAdapter(adapter);



        return recView;

    //    return inflater.inflate(R.layout.fragment_album_fragment, container, false);
    }
    public ArrayList<Album> AlbumList(){

        ContentResolver contentResolver = recView.getContext().getContentResolver();
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
