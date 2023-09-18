package me.t3sl4.ondergrup.Util.HTTP;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class HTTP {

    private static RequestQueue requestQueue;
    private static final String TAG = "HTTP";

    public HTTP(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static void sendRequest(String requestURL, String jsonBody, HttpRequestCallback callback) {
        JSONObject jsonBodyObj;
        try {
            jsonBodyObj = new JSONObject(jsonBody);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSON oluşturma hatası: " + e.getMessage()); // Hata durumunda logla
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestURL, jsonBodyObj,
                response -> {
                    Log.d(TAG, "Başarılı yanıt alındı: " + response.toString()); // Yanıtı logla
                    if (callback != null) {
                        try {
                            callback.onSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    Log.d(TAG, "Hata oluştu: " + error.getMessage());
                    if (callback != null) {
                        callback.onFailure(error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "İstek gönderildi: " + requestURL);
    }

    public void sendRequestNormal(String requestURL, HttpRequestCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestURL, null,
                response -> {
                    if (callback != null) {
                        try {
                            callback.onSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    if (callback != null) {
                        callback.onFailure(error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public interface HttpRequestCallback {
        void onSuccess(JSONObject response) throws IOException;
        void onFailure(String errorMessage);
    }
}
