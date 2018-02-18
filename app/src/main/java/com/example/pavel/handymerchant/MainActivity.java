package com.example.pavel.handymerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by pavel on 12.02.18.
 */

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.tv_ticker);

        Button button = findViewById(R.id.btn_go);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EngineService.class);
            startService(intent);
        });
    }
}
