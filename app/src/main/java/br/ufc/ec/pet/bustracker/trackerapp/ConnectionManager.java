package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufc.ec.pet.bustracker.trackerapp.types.Bus;
import br.ufc.ec.pet.bustracker.trackerapp.types.Route;


public class ConnectionManager{
    private RequestQueue requestQueue;
    private String serverPrefix;
    private Context context;
    private String token;


    public ConnectionManager(Context context, String serverPrefix){
        this.serverPrefix = serverPrefix;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //setToken("JpURojQBMP7fD5gYC6t26jb9A40FPae2JNjRBzJpo4NoBgpkRKOW6U8b8naLPa+dEvR0Z0+tHcHEnp8Wxjj4MGfVYarqY73d2j9cF5cCAuEpus2Oj9bmuCVQjbxF7wPIViWRi99yO0YZOGF4EEpaZGRiTJCMaAV+CcyHa15soT5AY+qOJgHEsqK2irem9TYuPZP0DKipQK0sWYNoDGyszYPo0x71W8H7uVT69GFzgJQ=");

    }
    public void postLocation(Bus bus, Route route){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("latitude", bus.getLatitude());
            requestBody.put("longitude", bus.getLongitude());
        }catch (JSONException e){Log.d("Bus", e.getMessage());}
        Log.d("Bus", "BodyRequest: "+requestBody.toString());
        Map requestHeader = new HashMap();
        requestHeader.put("Token", getToken());

        String url = serverPrefix + "/routes/"+route.getId()+"/buses/"+bus.getId()+"/positions";
        AuthenticatedJsonObjectRequest jreq = new AuthenticatedJsonObjectRequest(JsonObjectRequest.Method.POST, url, requestBody, requestHeader,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Bus", "response from send location: "+response.toString());
                        } catch (Exception e){
                            Log.e("Bus", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Bus", "Error"+error.getMessage());
                        //Log.d("Bus", "StatusCode"+error.networkResponse.statusCode);
                    }
                });
        requestQueue.add(jreq);
    }
    public void putRoute(Route route) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", route.getName());
            requestBody.put("description", route.getDescription());
        } catch (JSONException e) {
            Log.d("Bus", e.getMessage());
        }
        Log.d("Bus", "BodyRequest: " + requestBody.toString());
        Map requestHeader = new HashMap();
        requestHeader.put("Token", getToken());

        String url = serverPrefix + "/routes/" + route.getId();
        AuthenticatedJsonObjectRequest jreq = new AuthenticatedJsonObjectRequest(JsonObjectRequest.Method.PUT, url, requestBody, requestHeader,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Bus", "response from send location: " + response.toString());
                        } catch (Exception e) {
                            Log.e("Bus", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Bus", "Error" + error.getMessage());
                        //Log.d("Bus", "StatusCode"+error.networkResponse.statusCode);
                    }
                });
        requestQueue.add(jreq);
    }
    protected String getToken(){
        return this.token;
    }
    protected void setToken(String token){
        this.token = token;
    }
    public String getServerPrefix(){
        return serverPrefix;
    }
    public void setServerPrefix(String serverPrefix){
        this.serverPrefix = serverPrefix;
    }
    protected RequestQueue getRequestQueue(){
        return requestQueue;
    }
}