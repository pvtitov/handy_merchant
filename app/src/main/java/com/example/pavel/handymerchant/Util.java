package com.example.pavel.handymerchant;

import java.util.Date;

/**
 * Created by pavel on 13.02.18.
 */

class Util {

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

    enum CurrencyPair {
        USDT_BTC;
    }


    // Вспомогательный класс для построения URL запросов к серверу брокера
    static String buildUrl(CurrencyPair currencyPair, long startUTC, long stopUTC, Period period){
        return "https://poloniex.com/public?command=returnChartData"
                + "&currencyPair=" + currencyPair
                + "&start=" + startUTC
                + "&end=" + stopUTC
                + "&period=" + period.getValue();
    }
}
