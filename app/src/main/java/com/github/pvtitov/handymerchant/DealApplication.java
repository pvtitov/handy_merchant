package com.github.pvtitov.handymerchant;

import com.github.pvtitov.handymerchant.contracts.CurrenciesContract;

/**
 * Created by pavel on 09.03.18.
 */

public class DealApplication {
    public static final byte BUY = 1;
    public static final byte SELL = -1;

    private CurrenciesContract mCurrency;
    private byte mDirection;
    private double mAmount;
    private double mPriceOpen;
    private double mPriceClose;

    public DealApplication(CurrenciesContract currency,
                           byte direction,
                           double amount,
                           double priceOpen,
                           double priceClose) {
    }


    public CurrenciesContract getCurrency() {
        return mCurrency;
    }

    public byte getDirection() {
        return mDirection;
    }

    public double getAmount() {
        return mAmount;
    }

    public double getPriceOpen() {
        return mPriceOpen;
    }

    public double getPriceClose() {
        return mPriceClose;
    }
}