package com.example.androidmusicplayer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddSongFragment extends DialogFragment {
    private ImageButton btn_add;
    private EditText et_name;
    private EditText et_artist;
    MainActivity callBackActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

       // callBackActivity = new MainActivity();
/*        btn_add = getView().findViewById(R.id.btn_fragAddSong);
        et_name = getView().findViewById(R.id.et_frag_songName);
        et_artist = getView().findViewById(R.id.et_frag_songArtist);*/

        super.onCreate(savedInstanceState);
    }


    public AddSongFragment() {
        super(R.layout.add_song_fragment);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.add_song_fragment,container,true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }




}
