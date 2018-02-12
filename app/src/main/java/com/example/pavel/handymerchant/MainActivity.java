package com.example.pavel.handymerchant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    private static final String URL_RETURN_TICKER = "https://poloniex.com/public?command=returnTicker";

    Ticker mTicker;
    OkHttpClient mClient = new OkHttpClient();
    GsonBuilder mBuilder = new GsonBuilder();
    Gson mGson = mBuilder.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.tv_ticker);

        Button buttonTicker = findViewById(R.id.btn_get_ticker);
        buttonTicker.setOnClickListener(v -> {
            Thread thread = new Thread(){
                Request request = new Request.Builder().url(URL_RETURN_TICKER).build();
                @Override
                public void run() {
                    try(Response response = mClient.newCall(request).execute()) {
                        // Ограничение на количество запросов - не более 5 в секунду
                        Thread.sleep(200);
                        mTicker = mGson.fromJson(response.body().string(), Ticker.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    textView.post(() -> textView.setText(
                            "Последняя цена: " + mTicker.getCurrencyPair().getLast() +
                            "\nЦена покупки: " + mTicker.getCurrencyPair().getLowestAsk() +
                            "\nЦена предложения: " + mTicker.getCurrencyPair().getHighestBid()
                            )
                    );
                }
            };
            thread.start();
            //RequestPublic requestTask = new RequestPublic("returnTicker", textView);
            //requestTask.execute();
        });
    }
}
