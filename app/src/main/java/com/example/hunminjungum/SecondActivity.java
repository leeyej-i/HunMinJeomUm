package com.example.hunminjungum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;

public class SecondActivity extends Activity {
    private File file;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        File sdcard = Environment.getExternalStorageDirectory();
        String imageFileName = "capture.jpg";
        file = new File(sdcard, imageFileName);

        Button camera = (Button)findViewById(R.id.takecamera);
        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                capture();
            }
        });
        TextView sessionid = (TextView)findViewById(R.id.session_id);
        SharedPreferences sharedPreferences = getSharedPreferences("ex",MODE_PRIVATE);
        String session_id = sharedPreferences.getString("id","");
        if(!session_id.equals("")){
            sessionid.setText(session_id+"님, 환영합니다.");

        }
        Button loadfile = (Button)findViewById(R.id.loadfile);
        loadfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent mintent=new Intent(Intent.ACTION_GET_CONTENT);
                mintent.setType("application/*");
                mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mintent.createChooser(mintent,"Open"));
            }
        });

        Button loadpicture = (Button)findViewById(R.id.loadpicture);
        loadpicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent mintent=new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(mintent);
            }
        });

        Button takecamera = (Button)findViewById(R.id.takecamera);
        camera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent mintent=new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(mintent);
            }
        });

    }
    public void capture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(getBaseContext(), "com.onedelay.chap7.fileprovider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 101);
    }
}
