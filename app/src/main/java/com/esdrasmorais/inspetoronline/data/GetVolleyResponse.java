package com.esdrasmorais.inspetoronline.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esdrasmorais.inspetoronline.data.interfaces.VolleyCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetVolleyResponse implements VolleyCallback {

    private Context context;
    private String cookie;
    Map<String, String> responseHeaders;
    Map<String, String> requestHeaders;

    public GetVolleyResponse(Context context) {
        this.context = context;
        this.requestHeaders = new HashMap<String, String>();
    }

    public void getResponse(
        int method, String url,
        final JSONObject jsonValue, final VolleyCallback callback
    ) {
        StringRequest stringRequest = new StringRequest(
            method, url, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String Response) {
                    callback.onSuccessResponse(Response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    //Log.d("GetVolleyResponse", e.getMessage());
                    //e.printStackTrace();
//                    Toast.makeText(context, e + "error",
//                       Toast.LENGTH_LONG).show();
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                requestHeaders = responseHeaders == null ?
                    super.getHeaders() : requestHeaders;
                //requestHeaders.put("Authorization: Basic", TOKEN);

                requestHeaders = new HashMap<String, String>();
                if (cookie != null && cookie.length() > 0)
                    requestHeaders.put("Cookie", cookie);
                return requestHeaders;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //iHttpAsync.onAsyncProgress();
                //return jsonValue.getBytes();
                return null;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                cookie = rawCookies != null ? rawCookies : cookie;
                return super.parseNetworkResponse(response);
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void onSuccessResponse(String result) {

    }

    public String getCookie() {
        return this.cookie;
    }
}