package com.example.androidmusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private List<Song> songs;
    private SongListener listener;

    interface SongListener {
        void onSongClicked(int position,View view);
        void onSongLongCliked(int position,View view);
    }

    public void setListener(SongListener listener){
        this.listener = listener;
    }

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        TextView tv_songName;
        TextView tv_artistName;
        TextView tv_songDuration;
        ImageView iv_songPic;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_songName = itemView.findViewById(R.id.song_name);
            tv_artistName = itemView.findViewById(R.id.song_artist);
            tv_songDuration = itemView.findViewById(R.id.song_duration);
            iv_songPic = itemView.findViewById(R.id.songPic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null)
                        listener.onSongClicked(getAbsoluteAdapterPosition(),v);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener !=null)
                        listener.onSongLongCliked(getAbsoluteAdapterPosition(),v);
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_cell,parent,false);
        SongViewHolder countryViewHolder = new SongViewHolder(view);
        return countryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tv_songName.setText(song.getName());
        holder.tv_artistName.setText(song.getArtist());
        holder.tv_songDuration.setText(song.getLength()+" min");
        Glide.with(context).load(song.getLinkPic())
                .into(holder.iv_songPic);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }
}
