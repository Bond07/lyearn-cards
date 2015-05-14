package com.example.nirav.lyearn;

/**
 * Created by nirav on 5/5/15.
 */
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by nirav on 2015/3/14.
 */

public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private static final String FILE_PART_NAME = "file";
    private static final String STRING_PART_NAME = "text";

    private final Response.Listener<String> mListener;
    private final File mFilePart;
    private final String mStringPart;

    public MultipartRequest(String url, File file, String stringPart, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mFilePart = file;
        mStringPart = stringPart;
        buildMultipartEntity();
    }

    private void buildMultipartEntity()
    {
        entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
        try
        {
            entity.addPart(STRING_PART_NAME, new StringBody(mStringPart));
        }
        catch (UnsupportedEncodingException e)
        {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType()
    {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            entity.writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        byte[] responseData = response.data;
        String responseString = null;
        try {
            responseString = new String(responseData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
             Log.d("UTF-8 Encoding Error", e.toString());
        }
        return Response.success(responseString, getCacheEntry());

    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }
}