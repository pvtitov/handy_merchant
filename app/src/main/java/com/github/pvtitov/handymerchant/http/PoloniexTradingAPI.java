package com.github.pvtitov.handymerchant.http;

import android.content.Context;

import com.github.pvtitov.handymerchant.R;
import com.github.pvtitov.handymerchant.Util;
import com.github.pvtitov.handymerchant.contracts.CurrenciesContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pavel on 04.03.18.
 */

public class PoloniexTradingAPI {
    private static final String API_KEY = "A7QCAN3T-DSEKLUS9-AE1J4XJM-JF02C0CD";
    private static final String SECRET = "072f18275685e4bebdf0d8a8b85b93d814889f3fabe520348f63ae3e050355cd7ecde3ea5acdfe3de5c1e4b26fd399a89817577f009de1ae9f46e48012dfa33c";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private OkHttpClient mClient;
    private Gson mGson;
    private Context mContext;

    public PoloniexTradingAPI(Context context, OkHttpClient client, Gson gson) {
        mContext = context;
        mClient = client;
        mGson = gson;
    }


    public Map<String, String> returnBalances() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Response response = abstractPostRequest("command=returnBalances");
        return mGson.fromJson(response.body().string(), new TypeToken<Map<String, String>>(){}.getType());
    }


    private Response abstractPostRequest(String apiMethod) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String parameters = "nonce=" + Util.nonce() + "&" + apiMethod;
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, parameters);
        String signedRequestBody = Util.signHmacSha512(parameters, SECRET);
        Headers headers = Headers.of(
                "Key", API_KEY,
                "Sign", signedRequestBody);
        Request request = new okhttp3.Request.Builder()
                .url(mContext.getString(R.string.url_poloniex_trading))
                .headers(headers)
                .post(requestBody)
                .build();
        return mClient.newCall(request).execute();
    }


    @Override
    public String toString() {
        String message = "";
        try {
            Map<String, String> balances = returnBalances();

            for (CurrenciesContract currency: CurrenciesContract.values())
                message += currency.name() + ": " + balances.get(currency.name()) + "\n";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }
}

