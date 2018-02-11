package com.example.pavel.handymerchant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final long BALANCE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установить баланс
        Account.INSTANCE.setBalance(BALANCE);

        TextView balanceTextView = findViewById(R.id.tv_balance);
        setTextOn(balanceTextView);

        Button sellButton = findViewById(R.id.btn_sell);
        sellButton.setOnClickListener(v -> {
            Account.INSTANCE.sell(1);
            setTextOn(balanceTextView);
        });

        Button buyButton = findViewById(R.id.btn_buy);
        buyButton.setOnClickListener(v -> {
            Account.INSTANCE.buy(1);
            setTextOn(balanceTextView);
        });
    }

    private void setTextOn(TextView textView) {
        String balanceText = String.valueOf(Account.INSTANCE.getBalance());
        textView.setText(balanceText);
    }
}
