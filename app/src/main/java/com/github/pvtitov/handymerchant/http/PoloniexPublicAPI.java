package com.github.pvtitov.handymerchant.http;

import android.content.Context;

import com.github.pvtitov.handymerchant.R;
import com.github.pvtitov.handymerchant.Util;
import com.github.pvtitov.handymerchant.contracts.ChartDataContract;
import com.google.gson.Gson;

import java.io.IOException;

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

    public ChartDataContract[] returnChartData(Util.CurrencyPair pair, Util.Period period, int numberPeriodsBack) throws IOException {
        Request request = new okhttp3.Request.Builder().url(
                mContext.getString(R.string.url_poloniex_chart_data,
                        pair.name(),
                        Util.currentTimeUTC() - period.getValue()*numberPeriodsBack,
                        Util.currentTimeUTC(),
                        period.getValue())
                ).build();
        Response response = mClient.newCall(request).execute();
        return mGson.fromJson(response.body().string(), ChartDataContract[].class);
    }
}
