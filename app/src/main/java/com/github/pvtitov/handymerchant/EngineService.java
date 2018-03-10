package com.github.pvtitov.handymerchant;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.github.pvtitov.handymerchant.contracts.CurrenciesContract;
import com.github.pvtitov.handymerchant.poloniex.TradingApi;
import com.github.pvtitov.handymerchant.poloniex.PublicApi;
import com.github.pvtitov.handymerchant.contracts.ChartDataContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

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
            chartDataContracts = new PublicApi(EngineService.this, client, gson)
                    .returnChartData(
                            Util.CurrencyPair.USDT_BTC,
                            Util.Period.DAY,
                            100
                    );
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

            broadcast("Предпоследняя: " + oneStepBeforeLastData.getClose() + "\nПоследняя: " + lastData.getClose());

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
        try {
            Map<String, String> balancies = new TradingApi(EngineService.this, client, gson).returnBalances();
            if (TradingApi.isError(balancies)) {
                broadcast(TradingApi.printError(balancies));
                Util.timeOut();
            } else {
                String currency = CurrenciesContract.USDT.name();
                broadcast(currency + ": " + balancies.get(currency) + "\n" + direction);
                Util.timeOut();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
