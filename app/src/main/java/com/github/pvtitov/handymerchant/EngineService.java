package com.github.pvtitov.handymerchant;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pavel on 13.02.18.
 */

public class EngineService extends IntentService {

    public static final String ACTION_ENGINE_SERVICE = "com.github.pvtitov.handymerchant.RESPONSE";
    public static final String EXTRA_INTENT_RESPONSE = "extra_intent_response";

    ChartData[] mChartData = new ChartData[0];

    public EngineService() {
        super("ENGINE_SERVICE");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        OkHttpClient client = new OkHttpClient();
        long currentTimeUTC = new Date().getTime()/1000;
        Request request = new Request.Builder().url(
                Util.buildUrl(Util.CurrencyPair.USDT_BTC,
                        currentTimeUTC - 60*60*24*5,
                        currentTimeUTC,
                        Util.Period.FIVE_MINUTES
                )).build();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try (Response response = client.newCall(request).execute()) {
            mChartData = gson.fromJson(response.body().string(), ChartData[].class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        sendResponseViaBroadcast();

        timeOut();
    }

    private void timeOut() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendResponseViaBroadcast() {
        String responseMessage = "";
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
        for (ChartData entry: mChartData)
            responseMessage += dateFormat.format(new java.util.Date(entry.getDate()*1000)) + " : " + entry.getClose() + "\n";


        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_ENGINE_SERVICE);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra(EXTRA_INTENT_RESPONSE, responseMessage);
        sendBroadcast(intentResponse);
        Log.d("happy", responseMessage);
    }
}
