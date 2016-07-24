package br.ufc.ec.pet.bustracker.trackerapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by santana on 24/07/16.
 */
public class AuthenticatedJsonObjectRequest extends JsonObjectRequest
{
    Map mHeader;
    public AuthenticatedJsonObjectRequest(int method, String url, JSONObject jsonRequest, Map header, Response.Listener listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
        mHeader = header;
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        return mHeader;
    }

}