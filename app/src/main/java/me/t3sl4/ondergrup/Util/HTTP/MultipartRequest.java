package me.t3sl4.ondergrup.Util.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<NetworkResponse> {
    private final String boundary = "VolleyUploadBoundary";
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private final Response.Listener<NetworkResponse> mListener;
    private final File file;
    private final String fieldName;
    private final Map<String, String> params;

    public MultipartRequest(String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener, File file, String fieldName, Map<String, String> params) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.file = file;
        this.fieldName = fieldName;
        this.params = params;
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            buildMultipartEntity(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private void buildMultipartEntity(ByteArrayOutputStream bos) throws IOException {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            bos.write(("--" + boundary + "\r\n").getBytes());
            bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
            bos.write((entry.getValue() + "\r\n").getBytes());
        }
        bos.write(("--" + boundary + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        bos.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());

        // Write file content
        byte[] fileData = readFileToByteArray(file);
        bos.write(fileData);

        bos.write(("\r\n--" + boundary + "--\r\n").getBytes());
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        // Implement this method to read the file and convert it to a byte array.
        // You can use FileInputStream and a buffer to read the file.
        // This part may vary depending on your requirements.
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }
}


