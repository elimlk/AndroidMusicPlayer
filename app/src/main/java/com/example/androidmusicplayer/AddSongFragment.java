package com.example.androidmusicplayer;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class AddSongFragment extends DialogFragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    final int WRITE_PERMISSION_REQ =1;
    private ImageButton btn_add;
    private ImageButton btn_TakePic;
    private EditText et_songName;
    private EditText et_songArtist;
    private EditText et_songLink;
    private EditText et_picLink;
    MainActivity callBackActivity;
    File file;



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


    }

    public static AddSongFragment newInstance(String title) {
        AddSongFragment frag = new AddSongFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_song_fragment, null);
        btn_add =  v.findViewById(R.id.btn_fragAddSong);
        btn_TakePic = v.findViewById(R.id.btn_takePicFrag);
        et_songName = v.findViewById(R.id.et_frag_songName);
        et_songArtist = v.findViewById(R.id.et_frag_songArtist);
        et_songLink = v.findViewById(R.id.et_frag_songLink);
        et_picLink = v.findViewById(R.id.et_frag_songPicLink);

        btn_add.setOnClickListener(new View.OnClickListener() {
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
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    file = new File(Environment.getExternalStorageDirectory(),"Pic.jpg");
                    Uri fileUri = Uri.fromFile(file);
                    Uri photoURI = FileProvider.getUriForFile(getContext(),getContext().getApplicationContext().getPackageName() +".provider",file);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
                Toast toast = Toast.makeText(getContext(),"picture",Toast.LENGTH_SHORT);
                toast.show();
                et_picLink.setText("picture link from camera");
            }
        });

        return v;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0]!= PackageManager.PERMISSION_GRANTED) {
//            //Toast.makeText(this,"dfdf",Toast.LENGTH_SHORT);
//        }else{
//            btn_TakePic.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                et_picLink.setText(file.getAbsolutePath());
            }
        }
    }
}
