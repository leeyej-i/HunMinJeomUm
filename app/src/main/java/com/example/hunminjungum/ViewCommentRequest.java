package com.example.hunminjungum;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ViewCommentRequest extends StringRequest {
    final static private String URL  = "http://192.168.43.21:80/viewcomment.php";
    private Map<String, String> map;

    public ViewCommentRequest(int postnum, int boardid, Response.Listener<String> listener){

        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("postnum", postnum+"");
        map.put("boardid", boardid+"");
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
