package com.example.hunminjungum;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteCommentRequest extends StringRequest {
    final static private String URL = "http://192.168.43.21:80/deletecomment.php";
    private Map<String, String> map;
    public DeleteCommentRequest(int commentnum, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("commentnum", commentnum+"");
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
