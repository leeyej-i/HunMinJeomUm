package com.example.hunminjungum;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ViewInfoBoardRequest extends StringRequest {
    final static  private String URL="http://192.168.43.21:80/viewinfoboard.php";
    private Map<String,String> map;

    public ViewInfoBoardRequest(Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);
        map=new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
