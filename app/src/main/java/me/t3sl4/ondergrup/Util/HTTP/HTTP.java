package me.t3sl4.ondergrup.Util.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HTTP {
    private static final String TAG = "HTTP";

    public static void sendRequest(String requestURL, String jsonBody, HttpRequestCallback callback, RequestQueue requestQueue) {
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
                        } catch (JSONException e) {
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

    public void sendRequestNormal(String requestURL, HttpRequestCallback callback, RequestQueue requestQueue) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestURL, null,
                response -> {
                    if (callback != null) {
                        try {
                            callback.onSuccess(response);
                        } catch (IOException | JSONException e) {
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
        void onSuccess(JSONObject response) throws IOException, JSONException;
        void onFailure(String errorMessage);
    }
}
