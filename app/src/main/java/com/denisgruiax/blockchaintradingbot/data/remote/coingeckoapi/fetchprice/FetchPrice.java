package com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice;

import com.denisgruiax.blockchaintradingbot.utils.apis.CoinGeckoEndpoints;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchPrice implements Callable<Double> {
    private String cryptoId;
    private String currency;
    private String priceEndpoint;
    private Double price;

    public FetchPrice(String cryptoId, String currency) {
        CoinGeckoEndpoints.setBalanceEndpoint(cryptoId, currency);

        this.cryptoId = cryptoId;
        this.currency = currency;
        priceEndpoint = CoinGeckoEndpoints.getBalanceEndpoint();
    }

    public Double call() {
        price = extractPriceFromJson(getJson(priceEndpoint));

        return price;
    }

    private String getJson(String priceEndpoint) {
        Request request = new Request.Builder().url(priceEndpoint).build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        try (Response response = client.newCall(request).execute()) {
            assert response.isSuccessful();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            return "{}";
        }
    }

    private double extractPriceFromJson(String json) {
        JsonObject response = new JsonParser().parse(json).getAsJsonObject();
        JsonObject crypto = response.getAsJsonObject(cryptoId);

        return crypto.get(currency).getAsDouble();
    }
}