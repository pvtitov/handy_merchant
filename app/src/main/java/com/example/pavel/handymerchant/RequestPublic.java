package com.example.pavel.handymerchant;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pavel on 11.02.18.
 */

public class RequestPublic extends AsyncTask<Void, Void, Ticker> {

    private static final String BASE_URL = "https://poloniex.com/public?command=";

    private static OkHttpClient client = new OkHttpClient();

    public RequestPublic(String param, TextView textView){
        mTextViewWeakRef = new WeakReference<>(textView);
        mUrl = BASE_URL + param;
    }

    private WeakReference<TextView> mTextViewWeakRef;
    private String mUrl;

    @Override
    protected Ticker doInBackground(Void... voids) {
        Request request = new Request.Builder().url(mUrl).build();
        String json = null;
        try(Response response = client.newCall(request).execute()) {
            json = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(json, Ticker.class);
    }

    @Override
    protected void onPostExecute(Ticker ticker) {
        super.onPostExecute(ticker);
        mTextViewWeakRef.get().setText(ticker.getCurrencyPair().getLast());
    }
}
