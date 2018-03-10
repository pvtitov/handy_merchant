package com.github.pvtitov.handymerchant;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.github.pvtitov.handymerchant.http.PoloniexTradingAPI;
import com.github.pvtitov.handymerchant.http.PoloniexPublicAPI;
import com.github.pvtitov.handymerchant.contracts.ChartDataContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;

/**
 * Created by pavel on 13.02.18.
 */

public class EngineService extends IntentService {

    public static final String ACTION_ENGINE_SERVICE = "com.github.pvtitov.handymerchant.RESPONSE";
    public static final String EXTRA_INTENT_RESPONSE = "extra_intent_response";

    public EngineService() {
        super("ENGINE_SERVICE");
    }

    private boolean mIsStopped;


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (true) {
            broadcast("Новая итерация...");
            if (mIsStopped) break;
            else
            {
                tradingEngineStub();
                Util.timeOut();
            }
        }
        broadcast("Конец работы");
    }

    private void tradingEngineStub() {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().create();

        //TODO 1. Скачать данные
        ChartDataContract[] chartDataContracts = new ChartDataContract[0];
        try {
            chartDataContracts = new PoloniexPublicAPI(EngineService.this, client, gson).returnChartData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO 2. Проанализировать данные и принять решение
        final String BUY = "BUY";
        final String SELL = "SELL";
        String direction = "Something went wrong";

        try {
            ChartDataContract lastData = chartDataContracts[chartDataContracts.length - 1];
            ChartDataContract oneStepBeforeLastData = chartDataContracts[chartDataContracts.length - 2];

            //==>
            broadcast("Предпоследняя: " + oneStepBeforeLastData.getClose() + "\nПоследняя: " + lastData.getClose());
            Util.timeOut();
            //<==

            if (lastData.getClose() - oneStepBeforeLastData.getClose() > 0) {
                direction = BUY;
            } else {
                direction = SELL;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        //TODO 3. Совершить сделку

        broadcast(new PoloniexTradingAPI(client, gson).toString() + "\n" + direction);
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
