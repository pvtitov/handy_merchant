package com.example.pavel.handymerchant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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

public class EngineService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                ChartData[] chartData = new ChartData[0];

                OkHttpClient client = new OkHttpClient();
                long todayUTC = new Date().getTime()/1000;
                Request request = new Request.Builder().url(
                        Util.buildUrl(Util.CurrencyPair.USDT_BTC,
                                todayUTC - 60*60*24*5,
                                todayUTC,
                                Util.Period.TWO_HOURS
                        )).build();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try (Response response = client.newCall(request).execute()) {
                    chartData = gson.fromJson(response.body().string(), ChartData[].class);
                    // Ограничение на количество запросов - не более 5 в секунду
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
                for (ChartData entry: chartData)
                    Log.d("happy", dateFormat.format(new java.util.Date(entry.getDate()*1000)) + " : " + entry.getClose() + "\n");
            }
        };
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
