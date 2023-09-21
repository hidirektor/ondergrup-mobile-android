package me.t3sl4.ondergrup.Util.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    public void sendRequestNormal(String requestURL, HttpRequestCallback callback) {
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

    public static void sendRequest4File(String url, String jsonBody, String localFilePath, HttpRequestCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonBody);
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        return false;
                    }

                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
                                fos.write(responseBody.bytes());
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    System.out.println("Photo saved to " + localFilePath);
                    JSONObject temp = new JSONObject();
                    try {
                        temp.put("path", localFilePath);
                        callback.onSuccess(temp);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Error during network request.");
                    callback.onFailure("Error during network request.");
                }
            }
        }.execute();
    }

    public interface HttpRequestCallback {
        void onSuccess(JSONObject response) throws IOException, JSONException;
        void onFailure(String errorMessage);
    }
}
