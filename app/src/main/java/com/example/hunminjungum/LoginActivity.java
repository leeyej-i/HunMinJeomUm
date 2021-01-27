package com.example.hunminjungum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends Activity {
    EditText et_id, et_passwd;
    String sId, sPw;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_id = (EditText) findViewById(R.id.login_id);
        et_passwd = (EditText) findViewById(R.id.login_passwd);
        Button login_ok = (Button) findViewById(R.id.login_ok);
        Button login_cancel = (Button) findViewById(R.id.login_cancel);
        login_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        login_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sId = et_id.getText().toString();
                sPw = et_passwd.getText().toString();
                Response.Listener<String> responseLitsner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                SharedPreferences sharedPreferences = getSharedPreferences("ex",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id", sId);
                                editor.commit();

                                String userID = jsonObject.getString("id");
                                String userPass = jsonObject.getString("passwd");
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인에 성공했습니다").setPositiveButton("확인", null).create();

                                dialog.show();
                                Intent intent = new Intent(LoginActivity.this, SecondActivity.class);
                                LoginActivity.this.startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("아이디과 비밀번호가 일치하지 않습니다.").setNegativeButton("다시 시도", null).create();
                                dialog.show();
                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                LoginActivity.this.startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(sId, sPw, responseLitsner);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

    }
    @Override
    protected void onStop(){
        super.onStop();
        if (dialog != null) {//다이얼로그가 켜져있을때 함부로 종료가 되지 않게함
            dialog.dismiss();
            dialog = null;
        }
    }
}