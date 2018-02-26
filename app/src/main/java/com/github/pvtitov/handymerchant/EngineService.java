package com.github.pvtitov.handymerchant;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.github.pvtitov.handymerchant.response_model.ChartData;
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

    boolean mIsStopped;

    public EngineService() {
        super("ENGINE_SERVICE");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        broadcast(requestChartData(client, gson));

        // Ограничение Poloniex по частоте запросов
        Util.timeOut();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mIsStopped = true;
    }


    private String requestChartData(OkHttpClient client, Gson gson) {

        long currentTimeUTC = new Date().getTime()/1000;
        Request request = new Request.Builder().url(
                Util.buildUrl(Util.CurrencyPair.USDT_BTC,
                        currentTimeUTC - 60*60*24*5,
                        currentTimeUTC,
                        Util.Period.FOUR_HOURS
                )).build();


        ChartData[] chartData = new ChartData[0];
        try (Response response = client.newCall(request).execute()) {
            chartData = gson.fromJson(response.body().string(), ChartData[].class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        String message = "";
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
        for (ChartData entry: chartData) {
            message += dateFormat.format(new java.util.Date(entry.getDate()*1000)) + " : " + entry.getClose() + "\n";
            // досрочно остановить сервис
            if (mIsStopped) break;
        }

        return message;
    }


    private void requestBalance(OkHttpClient client, Gson gson) {

        //TODO post-request
    }


    private void broadcast(String message){
        Intent intent = new Intent();
        intent.setAction(ACTION_ENGINE_SERVICE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(EXTRA_INTENT_RESPONSE, message);
        sendBroadcast(intent);
    }
}
