package com.github.pvtitov.handymerchant;

import com.github.pvtitov.handymerchant.contracts.CurrenciesContract;

/**
 * Created by pavel on 09.03.18.
 */

public class Analyst {

    public static DealApplication analize(){
        CurrenciesContract currency;
        byte direction;
        double amount;
        double priceOpen;
        double priceClose;

        // TODO Do actual analysis. This is stub.
        currency = null;
        direction = 0;
        amount = 0;
        priceOpen = 0;
        priceClose = 0;

        return new DealApplication(currency, direction, amount, priceOpen, priceClose);
    }
}
