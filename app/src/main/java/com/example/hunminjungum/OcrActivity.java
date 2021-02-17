package com.example.hunminjungum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OcrActivity extends Activity {
    String session_id;
    ListView ocrlist;
    private AlertDialog dialog;
    ocrAdpater adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrlist);
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        session_id = sharedPreferences.getString("id","");
        ocrlist = (ListView)findViewById(R.id.ocrlist);
        adpater = new ocrAdpater();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String id = jsonObject.getString("id");
                            String contents = jsonObject.getString("contents");
                            if (session_id.equals(id)) {
                                adpater.addContents(new OcrList(contents));
                                ocrlist.setAdapter(adpater);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    /*StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Log.e("StackTrace", exceptionAsString);*/
                }
            }
        };
        OcrRequest ocrRequest = new OcrRequest(session_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(OcrActivity.this);
        queue.add(ocrRequest);
    }
    class ocrAdpater extends BaseAdapter{
        ArrayList<OcrList> contents = new ArrayList<OcrList>();
        @Override
        public int getCount() {
            return contents.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void addContents(OcrList list){
            contents.add(list);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            OcrListView ocrListView = null;
            if(convertView==null){
                ocrListView = new OcrListView(getApplicationContext());
            }else{
                ocrListView = (OcrListView)convertView;
            }
            OcrList list=contents.get(position);
            ocrListView.setContents(list.getContents());
            return ocrListView;
        }
    }
}
