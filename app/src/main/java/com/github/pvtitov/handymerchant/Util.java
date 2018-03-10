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

public class Util {

    private static final int ALLOWED_FREQUENCY = 5;
    private static final long NONCE_REDUCER = 1520221402943L;

    public enum Period {
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


    public enum CurrencyPair {
        USDT_BTC
    }


    // Приостановить поток с целью не нарушить требование брокера к частоте запросов
    static void timeOut() {
        try {
            Thread.sleep(1000/ALLOWED_FREQUENCY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    // The nonce parameter is an integer which must always be greater than the previous nonce used. Used for Trading API.
    public static int nonce() {
        return (int) (new Date().getTime() - NONCE_REDUCER);
    }


    // Количество секунд с 1 января 1970
    public static long currentTimeUTC(){
        return new Date().getTime()/1000;
    }


    // Подписание алгоритмом HMAC_SHA512
    public static String signHmacSha512(String data, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
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
