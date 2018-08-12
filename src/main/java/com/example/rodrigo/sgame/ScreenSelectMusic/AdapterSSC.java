package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.R;

public class AdapterSSC extends RecyclerView.Adapter<AdapterSSC.ViewHolderSSC> {

    private int index;
    private SongsGroup songsGroup;


    public AdapterSSC(SongsGroup songsGroup, int index) {
        this.index = index;
        this.songsGroup = songsGroup;
    }


    public static class ViewHolderSSC extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tv1;
        TextView Artist;
        ImageView banner;

        public ViewHolderSSC(View v) {
            super(v);
            tv1 = v.findViewById(R.id.Name);
            Artist = v.findViewById(R.id.artist);
            banner = v.findViewById(R.id.banner);

        }

        public void setInfo(SongsGroup sg, int index) {
            tv1.setText(sg.listOfSongs.get(index).songinfo.get("TITLE"));
            Artist.setText(sg.listOfSongs.get(index).songinfo.get("ARTIST"));

            Bitmap bitmap = BitmapFactory.decodeFile(sg.listOfPaths.get(index).getPath() + "/" + sg.listOfSongs.get(index).songinfo.get("BANNER").toString());
            banner.setImageBitmap(bitmap);


        }


    }


    @Override
    public AdapterSSC.ViewHolderSSC onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, null, false);
        return new ViewHolderSSC(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterSSC.ViewHolderSSC holder, int position) {
        holder.setInfo(songsGroup, position);
    }

    @Override
    public int getItemCount() {
        return songsGroup.listOfSongs.size();
    }
}
