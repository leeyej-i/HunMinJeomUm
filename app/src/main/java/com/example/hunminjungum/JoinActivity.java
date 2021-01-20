package com.example.hunminjungum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class JoinActivity extends Activity {
    EditText et_name, et_email, et_id, et_passwd, et_passwdchk;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        et_name = (EditText)findViewById(R.id.name);
        et_email = (EditText)findViewById(R.id.email);
        et_id = (EditText)findViewById(R.id.id);
        et_passwd = (EditText)findViewById(R.id.passwd);
        et_passwdchk = (EditText)findViewById(R.id.passwd_check);
        Button id_ck = (Button)findViewById(R.id.id_check);
        Button signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = et_name.getText().toString();
                String sEmail = et_email.getText().toString();
                String sId = et_id.getText().toString();
                String sPw = et_passwd.getText().toString();
                String sPw_chk = et_passwdchk.getText().toString();
                insertoToDatabase(sName,sEmail,sId,sPw);
            }

        });
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();

            }
        });
    }

    private void insertoToDatabase(final String name, String mail, String id, String passwd) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Log.d("Tag : ", s); // php에서 가져온 값을 최종 출력함
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String et_name = (String) params[0];
                    String et_email = (String) params[1];
                    String et_id = (String) params[2];
                    String et_passwd = (String) params[3];

                    String link = "http://192.168.43.21/join.php";
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(et_name, "UTF-8");
                    data += "&" + URLEncoder.encode("mail", "UTF-8") + "=" + URLEncoder.encode(et_email, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(et_id, "UTF-8");
                    data += "&" + URLEncoder.encode("passwd", "UTF-8") + "=" + URLEncoder.encode(et_passwd, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    outputStreamWriter.write(data);
                    outputStreamWriter.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    Log.d("tag : ", sb.toString()); // php에서 결과값을 리턴
                    return sb.toString();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(name, mail, id, passwd);
    }

}
