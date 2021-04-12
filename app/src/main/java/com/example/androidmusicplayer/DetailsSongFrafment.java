package com.example.androidmusicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

public class DetailsSongFrafment extends DialogFragment {
    private Song song;
    private TextView tvName;
    private TextView tvArtist;
    private ImageView iv_pic;
    public DetailsSongFrafment(Song song) {
        super(R.layout.song_expand_fragment);
        this.song = song;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvName = view.findViewById(R.id.tv_songTitle_detailsFrag);
        tvArtist = view.findViewById(R.id.tv_songArtist_detailsFrag);
        iv_pic = view.findViewById(R.id.iv_songPic);
        tvName.setText(song.getName());
        tvArtist.setText(song.getArtist());
        Glide.with(getContext()).load(song.getLinkPic())
                .into(iv_pic);
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
