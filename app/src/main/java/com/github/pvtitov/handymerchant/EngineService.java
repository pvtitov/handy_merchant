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
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pavel on 13.02.18.
 */

public class EngineService extends IntentService {

    public static final String ACTION_ENGINE_SERVICE = "com.github.pvtitov.handymerchant.RESPONSE";
    public static final String EXTRA_INTENT_RESPONSE = "extra_intent_response";
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");

    boolean mIsStopped;

    public EngineService() {
        super("ENGINE_SERVICE");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        OkHttpClient client = new OkHttpClient();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();


        //broadcast(requestChartData(client, gson));
        broadcast(requestBalance(client, gson) + "\n" + requestChartData(client, gson));

        // Ограничение Poloniex по частоте запросов
        Util.timeOut();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mIsStopped = true;
    }


    private String requestChartData(OkHttpClient client, Gson gson) {

        Request request = new Request.Builder().url(
                Util.buildUrl(Util.CurrencyPair.USDT_BTC,
                        Util.currentTimeUTC() - 60*60*24*5,
                        Util.currentTimeUTC(),
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

        String message = Util.nonce() + "\n";
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
        for (ChartData entry: chartData) {
            // досрочно остановить сервис
            if (mIsStopped) break;

            message += dateFormat.format(new java.util.Date(entry.getDate()*1000)) + " : " + entry.getClose() + "\n";
        }

        return message;
    }


    private String requestBalance(OkHttpClient client, Gson gson) {
        //TODO post-request
        //Key, Sign, https://poloniex.com/tradingApi, command=returnBalances
        //nonce - an integer which must always be greater than the previous nonce used
        Headers headers = Headers.of("Key=Your API key",
                "Sign=The query's POST data signed by your key's \"secret\" according to the HMAC-SHA512 method");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE,
                "nonce=" + Util.nonce() + "command=returnBalances");
        Request request = new Request.Builder().url("https://poloniex.com/tradingApi").post(requestBody).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void broadcast(String message){
        Intent intent = new Intent();
        intent.setAction(ACTION_ENGINE_SERVICE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(EXTRA_INTENT_RESPONSE, message);
        sendBroadcast(intent);
    }
}
