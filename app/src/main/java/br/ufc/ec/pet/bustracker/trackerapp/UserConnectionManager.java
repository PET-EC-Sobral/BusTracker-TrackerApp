package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by santana on 24/07/16.
 */
public class UserConnectionManager extends ConnectionManager {
    private boolean hasToken;
    private UserConnectionManagerListener listener;

    public UserConnectionManager(Context context, String serverPrefix) {
        super(context, serverPrefix);

    }

    public void login(UserConnectionManager.User user){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", user.getLogin());
            requestBody.put("password", user.getPassword());
        }catch (JSONException e){
            Log.d("Bus", e.getMessage());
        }
       // Log.d("Bus", "BodyRequest: "+requestBody.toString());
        Map requestHeader = new HashMap();

        String url = getServerPrefix() + "/users/tokens";
        AuthenticatedJsonObjectRequest jreq = new AuthenticatedJsonObjectRequest(JsonObjectRequest.Method.POST, url, requestBody, requestHeader,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setToken(response.getString("token"));
                            notifyLogin();
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
        getRequestQueue().add(jreq);
    }
    protected void setToken(String token){
        super.setToken(token);
        hasToken = true;
    }
    public boolean hasToken(){
        return hasToken;
    }
    public User getUser(String email, String password){
        return new User(email, password);
    }

    public void setUserConnectionManagerListener(UserConnectionManagerListener listener) {
        this.listener = listener;
    }
    public void notifyLogin(){
        if(listener != null)
            listener.onLogin(this);
    }

    public class User{
        private String email;
        private String password;

        public User(String email, String password) {
            setLogin(email);
            setPassword(password);
        }

        public String getLogin() {
            return email;
        }

        public void setLogin(String login) {
            this.email = login;
        }

        private String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
