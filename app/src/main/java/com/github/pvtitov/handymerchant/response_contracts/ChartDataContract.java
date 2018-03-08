package com.github.pvtitov.handymerchant.response_contracts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartDataContract {

    @SerializedName("date")
    @Expose
    private long date;
    @SerializedName("high")
    @Expose
    private double high;
    @SerializedName("low")
    @Expose
    private double low;
    @SerializedName("open")
    @Expose
    private double open;
    @SerializedName("close")
    @Expose
    private double close;
    @SerializedName("volume")
    @Expose
    private double volume;
    @SerializedName("quoteVolume")
    @Expose
    private double quoteVolume;
    @SerializedName("weightedAverage")
    @Expose
    private double weightedAverage;

    public long getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public double getWeightedAverage() {
        return weightedAverage;
    }

    public void setWeightedAverage(double weightedAverage) {
        this.weightedAverage = weightedAverage;
    }

}