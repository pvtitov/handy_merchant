package com.example.pavel.handymerchant;

/**
 * Created by pavel on 11.02.18.
 */

public enum ExchangeStub {

    INSTANCE;

    public final String LOGIN = "MyLogin";
    public final long BALANCE = 2000;
    public final long PRICE_BUY = 17;
    public final long PRICE_CELL = 13;
    public final int ASSET_BOUGHT = 20;

    public long buy(int amount){
        return PRICE_BUY*amount;
    }

    public long sell(int amount){
        return PRICE_CELL*amount;
    }

}
