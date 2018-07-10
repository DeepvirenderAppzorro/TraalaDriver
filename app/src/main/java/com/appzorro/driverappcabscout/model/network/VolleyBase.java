package com.appzorro.driverappcabscout.model.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appzorro.driverappcabscout.AppController;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by Varinder on 7/22/2016.
 */
public class VolleyBase {
    String request;
    OnApihit api;

    public VolleyBase(OnApihit api) {
        this.api = api;
    }


    public String main(final Map<String, String> params, String link, final int index) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        request = response;
                        try {
                            api.success(response, index);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        api.error(error, index);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(postRequest);
        return request;
    }

    public String mainGet(String link, final int index) {

        StringRequest postRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        request = response;
                        try {
                            api.success(response, index);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        api.error(error, index);
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(postRequest);
        return request;
    }

}
