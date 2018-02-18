package com.example.pavel.handymerchant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
 * Created by pavel on 12.02.18.
 */

public class MainActivity extends AppCompatActivity {

    ChartData[] mChartData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.tv_ticker);

        Button button = findViewById(R.id.btn_go);
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

                    // Отображение полученных данных о котировках в TextView
                    String editable = "";
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
                    for (ChartData entry: mChartData) editable = editable + dateFormat.format(new java.util.Date(entry.getDate()*1000)) + " : " + entry.getClose() + "\n";
                    final String textToDisplay = editable;
                    textView.post(() -> textView.setText(textToDisplay));
                }
            };
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        });
    }
}
