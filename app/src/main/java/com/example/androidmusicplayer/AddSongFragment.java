package com.example.androidmusicplayer;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddSongFragment extends DialogFragment {
    final int CAMERA_REQ = 1;
    final int WRITE_PERMISSION_REQ =1;
    private ImageButton btn_add;
    private EditText et_name;
    private EditText et_artist;
    MainActivity callBackActivity;



    public interface AddSongEventListener{
        public void addSong(Song s);
    }

    AddSongEventListener addSongEventListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addSongEventListener = (AddSongEventListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException((context.toString()+"must impla"));
        }
    }

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
        View v = inflater.inflate(R.layout.add_song_fragment, null);
        ImageButton imageButton =  v.findViewById(R.id.btn_fragAddSong);
        ImageButton btn_TakePic = v.findViewById(R.id.btn_takePicFrag);
        EditText et_songName = v.findViewById(R.id.et_frag_songName);
        EditText et_songArtist = v.findViewById(R.id.et_frag_songArtist);
        EditText et_songLink = v.findViewById(R.id.et_frag_songLink);
        EditText et_picLink = v.findViewById(R.id.et_frag_songPicLink);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song newSong = new Song(et_songName.getText().toString(),et_songArtist.getText().toString(),"details",et_songLink.getText().toString(),et_picLink.getText().toString());
                addSongEventListener.addSong(newSong);
                dismiss();
            }
        });

        btn_TakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(),"picture",Toast.LENGTH_SHORT);
                toast.show();
                et_picLink.setText("picture link from camera");
            }
        });

        return v;
    }



}
