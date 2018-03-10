package com.github.pvtitov.handymerchant.http;

import android.content.Context;

import com.github.pvtitov.handymerchant.R;
import com.github.pvtitov.handymerchant.Util;
import com.github.pvtitov.handymerchant.contracts.ChartDataContract;
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

public class PoloniexPublicAPI {

    private OkHttpClient mClient;
    private Gson mGson;
    private Context mContext;

    public PoloniexPublicAPI(Context context, OkHttpClient client, Gson gson) {
        mContext = context;
        mClient = client;
        mGson = gson;
    }

    public ChartDataContract[] returnChartData() throws IOException {
        Request request = new okhttp3.Request.Builder().url(
                mContext.getString(R.string.chart_data_url,
                        Util.CurrencyPair.USDT_BTC.name(),
                        Util.currentTimeUTC() - 60*60*24*5,
                        Util.currentTimeUTC(),
                        Util.Period.FIVE_MINUTES.getValue())
                ).build();
        Response response = mClient.newCall(request).execute();
        return mGson.fromJson(response.body().string(), ChartDataContract[].class);
    }


    @Override
    public String toString(){
        ChartDataContract[] chartData = new ChartDataContract[0];
        try {
            chartData = returnChartData();
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
