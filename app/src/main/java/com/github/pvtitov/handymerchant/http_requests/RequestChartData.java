package com.github.pvtitov.handymerchant.http_requests;

import com.github.pvtitov.handymerchant.Util;
import com.github.pvtitov.handymerchant.response_contracts.ChartDataContract;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pavel on 04.03.18.
 */

public class RequestChartData{

    private OkHttpClient mClient;
    private Gson mGson;

    public RequestChartData(OkHttpClient client, Gson gson) {
        mClient = client;
        mGson = gson;
    }

    private ChartDataContract[] makeRequest() throws IOException {
        Request request = new okhttp3.Request.Builder().url(
                buildUrl(Util.CurrencyPair.USDT_BTC,
                        Util.currentTimeUTC() - 60*60*24*5,
                        Util.currentTimeUTC(),
                        Util.Period.HALF_AN_HOUR
                )).build();
        Response response = mClient.newCall(request).execute();
        return mGson.fromJson(response.body().string(), ChartDataContract[].class);
    }

    // Вспомогательный метод для построения URL запросов к серверу брокера
    private String buildUrl(Util.CurrencyPair currencyPair, long startUTC, long stopUTC, Util.Period period){
        return "https://poloniex.com/public?command=returnChartData"
                + "&currencyPair=" + currencyPair
                + "&start=" + startUTC
                + "&end=" + stopUTC
                + "&period=" + period.getValue();
    }


    @Override
    public String toString(){
        ChartDataContract[] chartData = new ChartDataContract[0];
        try {
            chartData = makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = "";
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
        for (ChartDataContract entry: chartData) {
            message += dateFormat.format(new java.util.Date(entry.getDate()*1000)) + " : " + entry.getClose() + "\n";
        }
        return message;
    }
}
