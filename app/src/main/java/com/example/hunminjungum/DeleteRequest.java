package com.example.hunminjungum;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {
    final static private String URL = "http://192.168.43.21:80/delete.php";
    private Map<String, String> map;
    public DeleteRequest(String id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("id", id);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
