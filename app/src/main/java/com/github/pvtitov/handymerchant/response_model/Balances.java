package com.github.pvtitov.handymerchant.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public enum  Balances {

    @SerializedName("BTC") @Expose BTC,
    @SerializedName("USDT") @Expose USDT;


    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}