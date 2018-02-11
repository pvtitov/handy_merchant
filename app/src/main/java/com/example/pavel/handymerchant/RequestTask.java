package com.example.pavel.handymerchant;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pavel on 11.02.18.
 */

public class RequestTask extends AsyncTask<Void, Void, String> {

    public RequestTask(Context context){mContextWeakRef = new WeakReference<>(context);}

    private WeakReference<Context> mContextWeakRef;

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://poloniex.com/public?command=returnTicker").build();
        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return "Произошла ошибка";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(mContextWeakRef.get(), s, Toast.LENGTH_LONG).show();
    }
}
