package com.github.pvtitov.handymerchant.http_queries;

import com.github.pvtitov.handymerchant.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pavel on 04.03.18.
 */

public class RequestBalance {
    private static final String API_KEY = "QES1GZKE-VEC8R3XR-WB2WRDYT-ZYEW4Z9K";
    private static final String SECRET = "9ffdb5b8374eef265a73c33030701c3afd2bd20db0c590654af29ebbbeb556eb6b1195a117221c79e8aca5641039b6033d56a86e1a18f5d3284162906104cf5b";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private OkHttpClient mClient;
    private Gson mGson;

    public RequestBalance(OkHttpClient client, Gson gson) {
        mClient = client;
        mGson = gson;
    }


    private String makeRequest() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String parameters = "nonce=" + Util.nonce() + "&command=returnBalances";
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, parameters);
        String signedRequestBody = Util.signHmacSha512(parameters, SECRET);
        Headers headers = Headers.of(
                "Key", API_KEY,
                "Sign", signedRequestBody);
        Request request = new okhttp3.Request.Builder()
                .url("https://poloniex.com/tradingApi")
                .headers(headers)
                .post(requestBody)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public String toString() {
        String result = "oops";
        try {
            result = makeRequest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

