package com.example.hunminjungum;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;

public class SecondActivity extends AppCompatActivity{
    private File file;
    private AlertDialog dialog;
    TextView sessionid;
    String session_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
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
        sessionid = (TextView)findViewById(R.id.session_id);

        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        session_id = sharedPreferences.getString("id","");
        if(!session_id.equals("")){
            sessionid.setText(session_id+"님, 환영합니다.");
            getSupportActionBar().show();
            getSupportActionBar().setTitle("훈민점음");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.icon);
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

        Button community = (Button)findViewById(R.id.community);
        community.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, CommunityActivity.class);
                intent.putExtra("id", session_id);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menuformember, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                DialogInterface.OnClickListener yesButtonClickListener1= new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove("id");
                        editor.commit();
                        sessionid.setText("");
                        getSupportActionBar().hide();
                    }
                };
                AlertDialog.Builder builder1=new AlertDialog.Builder( SecondActivity.this );
                builder1.setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인",yesButtonClickListener1)
                        .create()
                        .show();
                break;
            case R.id.withdraw:
                DialogInterface.OnClickListener yesButtonClickListener2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Response.Listener<String> responseListener = new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if(success){
                                        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.remove("id");
                                        editor.commit();
                                        sessionid.setText("");
                                        getSupportActionBar().hide();
                                    }
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        DeleteRequest deleteRequest = new DeleteRequest(session_id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
                        queue.add(deleteRequest);
                    }
                };
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SecondActivity.this);
                builder2.setMessage("회원 탈퇴를 하시겠습니까?" +
                        "(정보는 영구적으로 삭제됩니다.)")
                        .setPositiveButton("확인", yesButtonClickListener2)
                        .create()
                        .show();
                break;
            case R.id.modifyinfo:
                Intent intent = new Intent(getApplicationContext(),ModifyActivity.class);
                startActivity(intent);
                break;
            case R.id.ocrlist:
                Intent mintent = new Intent(SecondActivity.this, OcrActivity.class);
                startActivity(mintent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void capture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(getBaseContext(), "com.onedelay.chap7.fileprovider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 101);
    }
}
