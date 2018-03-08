package com.github.pvtitov.handymerchant;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.github.pvtitov.handymerchant.http_requests.RequestBalances;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;

/**
 * Created by pavel on 13.02.18.
 */

public class EngineService extends IntentService {

    public static final String ACTION_ENGINE_SERVICE = "com.github.pvtitov.handymerchant.RESPONSE";
    public static final String EXTRA_INTENT_RESPONSE = "extra_intent_response";

    private boolean mIsStopped;
    private OkHttpClient mClient;
    private Gson mGson;

    public EngineService() {
        super("ENGINE_SERVICE");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        mClient = new OkHttpClient();
        mGson = new GsonBuilder().create();

        //broadcast(new RequestChartData(mClient, mGson).toString());
        broadcast(new RequestBalances(mClient, mGson).toString());

        // Ограничение Poloniex по частоте запросов
        Util.timeOut();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mIsStopped = true;
    }


    private void broadcast(String message){
        Intent intent = new Intent();
        intent.setAction(ACTION_ENGINE_SERVICE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(EXTRA_INTENT_RESPONSE, message);
        sendBroadcast(intent);
    }
}
