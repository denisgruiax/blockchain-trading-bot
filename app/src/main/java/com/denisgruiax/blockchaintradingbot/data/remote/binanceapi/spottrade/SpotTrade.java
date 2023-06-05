package com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade;

import android.util.Log;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.denisgruiax.blockchaintradingbot.utils.Keys;

import java.util.LinkedHashMap;
import java.util.concurrent.Callable;

public class SpotTrade implements Callable<String> {
    private String symbol;
    private String side;
    private String type;
    private String timeInForce;
    private double quantity;
    private double price;
    private String result;

    public SpotTrade() {
        symbol = "BTCUSDT";
        side = "BUY";
        type = "LIMIT";
        timeInForce = "GTC";
        quantity = 0.001;
        price = 21000.;
    }

    public SpotTrade(String symbol, String side, String type, String timeInForce, double quantity, double price) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.timeInForce = timeInForce;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String call() {
        return createTrade();
    }

    public LinkedHashMap<String, Object> createDictionaryOfParameters(){
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        parameters.put("symbol", symbol);
        parameters.put("side", side);
        parameters.put("type", type);
        parameters.put("timeInForce", timeInForce);
        parameters.put("quantity", quantity);
        parameters.put("price", price);

        return parameters;
    }

    public String createTrade(){
        LinkedHashMap<String, Object> parameters = createDictionaryOfParameters();
        SpotClient spotClient = new SpotClientImpl(Keys.getApiKey(), Keys.getSecretKey(), "https://api.binance.com");

        try {
            result = spotClient.createTrade().testNewOrder(parameters);
            Log.e("CreateTrade", result);
        } catch (BinanceConnectorException e) {
            System.err.println((String) String.format("fullErrMessage: %s", e.getMessage()));
        } catch (BinanceClientException e) {
            System.err.println((String) String.format("fullErrMessage: %s \nerrMessage: %s \nerrCode: %d \nHTTPStatusCode: %d",
                    e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode()));
        }

        return result;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
