package com.esdrasmorais.inspetoronline.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esdrasmorais.inspetoronline.data.interfaces.VolleyCallback;

import org.json.JSONObject;

public class GetVolleyResponse implements VolleyCallback {

    private Context context;

    public GetVolleyResponse(Context context) {
        this.context = context;
    }

    public void getResponse(
            int method, String url,
            JSONObject jsonValue, final VolleyCallback callback
    ) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String Response) {
                callback.onSuccessResponse(Response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e("GetVolleyResponse", e.getMessage());
                e.printStackTrace();
                Toast.makeText(context, e + "error", Toast.LENGTH_LONG).show();
            }
        }
        )
//        {
                // set headers
//            @Override
//            public Map < String, String > getHeaders() throws com.android.volley.AuthFailureError {
//                Map < String, String > params = new HashMap < String, String > ();
//                params.put("Authorization: Basic", TOKEN);
//                return params;
//            }
//        }
        ;
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void onSuccessResponse(String result) {
        //
    }
}
