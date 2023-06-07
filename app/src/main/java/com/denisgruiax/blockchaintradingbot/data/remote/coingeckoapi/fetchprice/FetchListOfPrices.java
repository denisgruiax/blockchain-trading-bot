package com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchListOfPrices implements Callable<List<Double>> {
    private String coinGeckoEndpoint;


    public FetchListOfPrices(String cryptoId, String currency){
        this.coinGeckoEndpoint = String.format("https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=1", cryptoId, currency);
    }

    public List<Double> call(){
        return extractPriceFromJson(getJson(coinGeckoEndpoint));
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

    private List<Double> extractPriceFromJson(String json) {
        List<Double> prices = new ArrayList<Double>();
        JsonArray pricesArray = JsonParser.parseString(json).getAsJsonObject().getAsJsonArray("prices");

        for (int i = 0;i<pricesArray.size();i++)
        {
            JsonArray pair = pricesArray.get(i).getAsJsonArray();
            prices.add(pair.get(1).getAsDouble());
        }
        return prices;
    }
}
