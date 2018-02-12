 package com.example.pavel.handymerchant;

 import com.google.gson.annotations.Expose;
 import com.google.gson.annotations.SerializedName;

 public class Ticker {

     @SerializedName("USDT_BTC")
     @Expose
     private CurrencyPair mCurrencyPair;

     public CurrencyPair getCurrencyPair() {
         return mCurrencyPair;
     }

     public void setCurrencyPair(CurrencyPair pair) {
         mCurrencyPair = pair;
     }

     public class CurrencyPair {

         @SerializedName("id")
         @Expose
         private int id;
         @SerializedName("last")
         @Expose
         private String last;
         @SerializedName("lowestAsk")
         @Expose
         private String lowestAsk;
         @SerializedName("highestBid")
         @Expose
         private String highestBid;
         @SerializedName("percentChange")
         @Expose
         private String percentChange;
         @SerializedName("baseVolume")
         @Expose
         private String baseVolume;
         @SerializedName("quoteVolume")
         @Expose
         private String quoteVolume;
         @SerializedName("isFrozen")
         @Expose
         private String isFrozen;
         @SerializedName("high24hr")
         @Expose
         private String high24hr;
         @SerializedName("low24hr")
         @Expose
         private String low24hr;

         public int getId() {
             return id;
         }

         public void setId(int id) {
             this.id = id;
         }

         public String getLast() {
             return last;
         }

         public void setLast(String last) {
             this.last = last;
         }

         public String getLowestAsk() {
             return lowestAsk;
         }

         public void setLowestAsk(String lowestAsk) {
             this.lowestAsk = lowestAsk;
         }

         public String getHighestBid() {
             return highestBid;
         }

         public void setHighestBid(String highestBid) {
             this.highestBid = highestBid;
         }

         public String getPercentChange() {
             return percentChange;
         }

         public void setPercentChange(String percentChange) {
             this.percentChange = percentChange;
         }

         public String getBaseVolume() {
             return baseVolume;
         }

         public void setBaseVolume(String baseVolume) {
             this.baseVolume = baseVolume;
         }

         public String getQuoteVolume() {
             return quoteVolume;
         }

         public void setQuoteVolume(String quoteVolume) {
             this.quoteVolume = quoteVolume;
         }

         public String getIsFrozen() {
             return isFrozen;
         }

         public void setIsFrozen(String isFrozen) {
             this.isFrozen = isFrozen;
         }

         public String getHigh24hr() {
             return high24hr;
         }

         public void setHigh24hr(String high24hr) {
             this.high24hr = high24hr;
         }

         public String getLow24hr() {
             return low24hr;
         }

         public void setLow24hr(String low24hr) {
             this.low24hr = low24hr;
         }
     }
 }