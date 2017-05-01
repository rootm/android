package com.example.muvindu.recyclerdemo.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.muvindu.recyclerdemo.Model.Album;
import com.example.muvindu.recyclerdemo.Model.Song;
import com.example.muvindu.recyclerdemo.R;
import com.example.muvindu.recyclerdemo.UI.AlbumSongs;
import com.example.muvindu.recyclerdemo.UI.nowPlaying;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.muvindu.recyclerdemo.Services.serviceConnection.player;

/**
 * Created by Muvindu on 1/3/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.viewHolder> {

    private ArrayList<Album> albums = new ArrayList<>();
    private LayoutInflater inflator;
    private Context contextView;

    public AlbumAdapter(ArrayList<Album> list, Context context) {
        this.albums = list;
        this.inflator = LayoutInflater.from(context);
        contextView = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.album_item, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
        Album album = albums.get(position);
        holder.albumName.setText(album.title);
        holder.numSongs.setText(String.valueOf(album.songCount));
        Typeface font = Typeface.createFromAsset(contextView.getAssets(), "Fonts/ABeeZee-Regular.otf");
        holder.albumName.setTypeface(font);
        holder.numSongs.setTypeface(font);
        final Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri, album.id);
        // if (song.getAlbum()!=null){  holder.albumArt.setImageBitmap(song.getAlbum());}
        //Uri uri = ContentUris.withAppendedId(sArtworkUri,5);
        final  ImageView albumImage = holder.albumImage;
        final View container=holder.container;
        final TextView albumname=holder.albumName;
        final TextView numsongs= holder.numSongs;
         Bitmap bitmap=null;
       // Glide.with(holder.albumImage.getContext()).load(R.drawable.albumartxxx).fitCenter().into(holder.albumImage);
        Glide.with(holder.albumImage.getContext()).load(uri).asBitmap().fitCenter().listener(new RequestListener<Uri, Bitmap>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                Bitmap bitmap=null;
                Drawable drawable;
                drawable= ContextCompat.getDrawable(contextView,R.drawable.albumartxxx);
                bitmap = ((BitmapDrawable) drawable).getBitmap();
                Glide.with(holder.albumImage.getContext()).load(R.drawable.albumartxxx).into(holder.albumImage);
                if (bitmap!=null){

                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (vibrantSwatch != null) {
                                container.setBackgroundColor(vibrantSwatch.getRgb());
                                // Update the title TextView with the proper text color
                                albumname.setTextColor(vibrantSwatch.getBodyTextColor());
                                numsongs.setTextColor(vibrantSwatch.getBodyTextColor());
                            }
                        }
                    });



                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (resource!=null){

                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (vibrantSwatch != null) {
                                container.setBackgroundColor(vibrantSwatch.getRgb());
                                // Update the title TextView with the proper text color
                                albumname.setTextColor(vibrantSwatch.getBodyTextColor());
                                numsongs.setTextColor(vibrantSwatch.getBodyTextColor());
                            }
                        }
                    });



                }
                return false;
            }
        }).into(holder.albumImage);




    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView albumImage;
        private TextView numSongs;
        private TextView albumName;
        private View container;
        private ImageView cardContainer;
        private CardView cardView;
        public viewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView) itemView.findViewById(R.id.thumbnail);
            albumName = (TextView) itemView.findViewById(R.id.title);
            numSongs = (TextView) itemView.findViewById(R.id.count);
            container = itemView.findViewById(R.id.albumBack);
            cardContainer =(ImageView) itemView.findViewById(R.id.thumbnail);
            cardContainer.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.thumbnail){
                AlbumClick(getAdapterPosition(),v.getContext());

            }
        }

        void AlbumClick(int position,Context context){
                Album album=new Album();
                album=albums.get(position);

                Intent intent = new Intent(context, AlbumSongs.class);
                intent.putExtra("id",album.id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);


        }

    }
}
