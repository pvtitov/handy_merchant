package com.example.pavel.handymerchant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int mAssetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView loginTextView = findViewById(R.id.tv_login);
        loginTextView.setText(ExchangeStub.INSTANCE.LOGIN);

        TextView balanceTextView = findViewById(R.id.tv_balance);
        balanceTextView.setText(String.valueOf(ExchangeStub.INSTANCE.BALANCE));

        TextView assetBoughtTextView = findViewById(R.id.tv_asset_bought);
        assetBoughtTextView.setText(String.valueOf(ExchangeStub.INSTANCE.ASSET_BOUGHT));

        TextView buyPriceTextView = findViewById(R.id.tv_buy_price);
        buyPriceTextView.setText(String.valueOf(ExchangeStub.INSTANCE.PRICE_BUY));

        TextView sellPriceTextView = findViewById(R.id.tv_sell_price);
        sellPriceTextView.setText(String.valueOf(ExchangeStub.INSTANCE.PRICE_CELL));

        Spinner assetAmountSpinner = findViewById(R.id.spn_asset_amount);
        assetAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAssetAmount = Integer.parseInt(assetAmountSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAssetAmount = 0;
            }
        });

        Button sellButton = findViewById(R.id.btn_sell);
        sellButton.setOnClickListener(v -> Toast.makeText(MainActivity.this,
                "Продано на " + ExchangeStub.INSTANCE.sell(mAssetAmount), Toast.LENGTH_SHORT)
                .show());

        Button buyButton = findViewById(R.id.btn_buy);
        buyButton.setOnClickListener(v -> Toast.makeText(MainActivity.this,
                "Куплено на " + ExchangeStub.INSTANCE.buy(mAssetAmount), Toast.LENGTH_SHORT)
                .show());
    }
}
