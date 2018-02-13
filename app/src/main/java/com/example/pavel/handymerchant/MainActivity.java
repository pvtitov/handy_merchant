package com.example.pavel.handymerchant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pavel on 12.02.18.
 */

public class MainActivity extends AppCompatActivity {

    private static final String URL_CHART_DATA = "https://poloniex.com/public?command=returnChartData&currencyPair=USDT_BTC&start=1518393600&end=9999999999&period=900";

    ChartData[] mChartData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.tv_ticker);

        Button button = findViewById(R.id.btn_go);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL_CHART_DATA).build();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        button.setOnClickListener(v -> {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try (Response response = client.newCall(request).execute()) {
                        mChartData = gson.fromJson(response.body().string(), ChartData[].class);
                        // Ограничение на количество запросов - не более 5 в секунду
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    String editable = "";
                    for (ChartData entry: mChartData) editable = editable + entry.getDate() + " : " + entry.getClose() + "\n";
                    final String textToDisplay = editable;
                    textView.post(() -> textView.setText(textToDisplay));
                }
            };
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        });
    }
}
