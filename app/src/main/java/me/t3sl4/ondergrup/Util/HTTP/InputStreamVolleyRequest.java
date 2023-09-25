package me.t3sl4.ondergrup.Util.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private Map<String, String> mParams;
    private Map<String, String> mRequestHeaders;
    //create a static map for directly accessing headers
    public Map<String, String> responseHeaders;

    public InputStreamVolleyRequest(int post, String mUrl, Response.Listener<byte[]> listener,
                                    Response.ErrorListener errorListener, HashMap<String, String> params,
                                    Map<String, String> requestHeaders) {

        super(post, mUrl, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;
        mParams = params;
        mRequestHeaders = requestHeaders;
    }

    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> normalHeader = super.getHeaders();
        Map<String, String> fullHeaders = new HashMap<>();
        fullHeaders.putAll(normalHeader);
        fullHeaders.putAll(mRequestHeaders);
        return fullHeaders;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
