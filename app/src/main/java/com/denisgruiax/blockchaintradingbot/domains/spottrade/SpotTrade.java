package com.denisgruiax.blockchaintradingbot.domains.spottrade;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.denisgruiax.blockchaintradingbot.keys.Keys;
import com.denisgruiax.blockchaintradingbot.ui.walletstatistics.WalletStatisticsFragment;

import java.util.LinkedHashMap;

public class SpotTrade implements Runnable{
    private String result;

    @Override
    public void run() {
        double quantity = 0.01;
        double price = 18000;

        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        SpotClient client = new SpotClientImpl(Keys.getApiKey(), Keys.getSecretKey(), "https://api.binance.com");

        parameters.put("symbol", "BTCUSDT");
        parameters.put("side", "SELL");
        parameters.put("type", "LIMIT");
        parameters.put("timeInForce", "GTC");
        parameters.put("quantity", quantity);
        parameters.put("price", price);

        try {
            result = client.createTrade().newOrder(parameters);
            Log.e("CreateTrade", result);
        } catch (BinanceConnectorException e) {
            System.err.println((String) String.format("fullErrMessage: %s", e.getMessage()));
        } catch (BinanceClientException e) {
            System.err.println((String) String.format("fullErrMessage: %s \nerrMessage: %s \nerrCode: %d \nHTTPStatusCode: %d",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode()));
        }
    }

    public String getResult() {
        return result;
    }
}
