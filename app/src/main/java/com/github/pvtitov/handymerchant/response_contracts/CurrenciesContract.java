package com.github.pvtitov.handymerchant.response_contracts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pavel on 08.03.18.
 */

public enum CurrenciesContract {

    @SerializedName("BTC") @Expose BTC,
    @SerializedName("USDT") @Expose USDT
}
