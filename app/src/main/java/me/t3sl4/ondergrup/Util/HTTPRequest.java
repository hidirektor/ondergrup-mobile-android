package me.t3sl4.ondergrup.Util;

import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPRequest {

    public interface OnHttpRequestComplete {
        void onRequestComplete(String response);
    }

    public static void get(String url, OnHttpRequestComplete listener) {
        new HttpRequestTask(listener).execute(url);
    }

    public static void post(String url, JSONObject jsonBody, OnHttpRequestComplete listener) {
        new JsonHttpRequestTask(jsonBody, listener).execute(url);
    }

    public static void postPhoto(String url, RequestBody requestBody, OnHttpRequestComplete listener) {
        new JsonHttpRequestTaskPhoto(requestBody, listener).execute(url);
    }

    private static class HttpRequestTask extends AsyncTask<String, Void, String> {
        private OnHttpRequestComplete listener;

        public HttpRequestTask(OnHttpRequestComplete listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            if (listener != null) {
                listener.onRequestComplete(response);
            }
        }
    }

    private static class JsonHttpRequestTask extends AsyncTask<String, Void, String> {
        private JSONObject jsonBody;
        private OnHttpRequestComplete listener;

        public JsonHttpRequestTask(JSONObject jsonBody, OnHttpRequestComplete listener) {
            this.jsonBody = jsonBody;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            if (listener != null) {
                listener.onRequestComplete(response);
            }
        }
    }

    private static class JsonHttpRequestTaskPhoto extends AsyncTask<String, Void, String> {
        private RequestBody requestBody;
        private OnHttpRequestComplete listener;

        public JsonHttpRequestTaskPhoto(RequestBody requestBody, OnHttpRequestComplete listener) {
            this.requestBody = requestBody;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(urls[0])
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (listener != null) {
                listener.onRequestComplete(response);
            }
        }
    }
}