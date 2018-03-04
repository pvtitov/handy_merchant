package com.github.pvtitov.handymerchant;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by pavel on 13.02.18.
 */

class Util {

    private static final int ALLOWED_FREQUENCY = 5;
    private static final long UTC_01_01_2018 = 1514764800000L;

    enum Period {
        FIVE_MINUTES(300),
        FIFTEEN_MINUTES(900),
        HALF_AN_HOUR(1800),
        TWO_HOURS(7200),
        FOUR_HOURS(14400),
        DAY(86400);

        private int period;
        Period(int period) {this.period = period;}
        public int getValue() {return period;}
    }

    enum Currency {
        USDT, BTC
    }

    enum CurrencyPair {
        USDT_BTC
    }


    // Вспомогательный метод для построения URL запросов к серверу брокера
    static String buildUrl(CurrencyPair currencyPair, long startUTC, long stopUTC, Period period){
        return "https://poloniex.com/public?command=returnChartData"
                + "&currencyPair=" + currencyPair
                + "&start=" + startUTC
                + "&end=" + stopUTC
                + "&period=" + period.getValue();
    }


    static void timeOut() {
        try {
            Thread.sleep(1000/ALLOWED_FREQUENCY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    static int nonce() {
        return (int) ((new Date().getTime() - UTC_01_01_2018)/(1000/ALLOWED_FREQUENCY));
    }


    static long currentTimeUTC(){
        return new Date().getTime()/1000;
    }


    static String signHmacSha512(String data, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        final String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), HMAC_SHA512);
        Mac mac = Mac.getInstance(HMAC_SHA512);
        mac.init(secretKeySpec);
        return toHexString(
                mac.doFinal(
                        data.getBytes("UTF-8")
                )
        );
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();
    }
}
