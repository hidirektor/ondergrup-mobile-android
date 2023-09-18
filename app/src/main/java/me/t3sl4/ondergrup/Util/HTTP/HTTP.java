package me.t3sl4.ondergrup.Util.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HTTP {

    private static RequestQueue requestQueue;
    private static final String TAG = "HTTP";
    private static final String BOUNDARY = "Boundary-" + System.currentTimeMillis();
    private static final String LINE_FEED = "\r\n";

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

    public static void sendMultipartRequest(String url, String username, File file, RequestCallback callback) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        callback.onSuccess(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFailure();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "multipart/form-data; boundary=" + BOUNDARY;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        writeFormField("username", username, outputStream);
                        writeFilePart("file", file, outputStream);
                        writeBoundaryEnd(outputStream);

                        return outputStream.toByteArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure();
        }
    }

    private static void writeFormField(String fieldName, String value, OutputStream outputStream) throws IOException {
        outputStream.write(("--" + BOUNDARY + LINE_FEED).getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + fieldName + "\"" + LINE_FEED).getBytes());
        outputStream.write(LINE_FEED.getBytes());
        outputStream.write(value.getBytes());
        outputStream.write(LINE_FEED.getBytes());
    }

    private static void writeFilePart(String fieldName, File uploadFile, OutputStream outputStream) throws IOException {
        outputStream.write(("--" + BOUNDARY + LINE_FEED).getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + uploadFile.getName() + "\"" + LINE_FEED).getBytes());
        outputStream.write(("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile.getName()) + LINE_FEED).getBytes());
        outputStream.write(("Content-Transfer-Encoding: binary" + LINE_FEED).getBytes());
        outputStream.write(LINE_FEED.getBytes());

        try (FileInputStream fileInputStream = new FileInputStream(uploadFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        outputStream.write(LINE_FEED.getBytes());
    }

    private static void writeBoundaryEnd(OutputStream outputStream) throws IOException {
        outputStream.write(("--" + BOUNDARY + "--" + LINE_FEED).getBytes());
    }

    public interface HttpRequestCallback {
        void onSuccess(JSONObject response) throws IOException;
        void onFailure(String errorMessage);
    }

    public interface RequestCallback {
        void onSuccess(String response) throws IOException;
        void onFailure();
    }
}
