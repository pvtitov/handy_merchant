package com.github.pvtitov.handymerchant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by pavel on 12.02.18.
 */

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    Intent mIntent;
    EngineBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view);



        Button buttonStart = findViewById(R.id.btn_start);
        buttonStart.setOnClickListener(v -> {
            mIntent = new Intent(MainActivity.this, EngineService.class);
            startService(mIntent);
        });

        Button buttonStop = findViewById(R.id.btn_stop);
        buttonStop.setOnClickListener(v -> {
            if (mIntent != null) {
                stopService(mIntent);
                mIntent = null;
            }
        });

        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        mBroadcastReceiver = new EngineBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EngineService.ACTION_ENGINE_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    public class EngineBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String resultMessage = intent.getStringExtra(EngineService.EXTRA_INTENT_RESPONSE);
            mTextView.setText(resultMessage);
        }
    }
}
