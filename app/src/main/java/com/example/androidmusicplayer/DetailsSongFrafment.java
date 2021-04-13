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
    public DetailsSongFrafment() {

    }
    public static DetailsSongFrafment newInstance(Song song) {
        DetailsSongFrafment frag = new DetailsSongFrafment();
        Bundle args = new Bundle();
        args.putString("songTitle", song.getName());
        args.putString("songArtist", song.getArtist());
        args.putString("songLink",song.getLinkSong());
        args.putString("songPicLink", song.getLinkPic());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tv_songTitle_detailsFrag);
        tvArtist = view.findViewById(R.id.tv_songArtist_detailsFrag);
        iv_pic = view.findViewById(R.id.iv_songPic);
        //getArguments().getString("songTitle","null")
        tvName.setText(getArguments().getString("songTitle","null"));
        tvArtist.setText(getArguments().getString("songArtist","null"));
        Glide.with(getContext()).load(getArguments().getString("songPicLink","null"))
                .into(iv_pic);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_expand_fragment, container);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
