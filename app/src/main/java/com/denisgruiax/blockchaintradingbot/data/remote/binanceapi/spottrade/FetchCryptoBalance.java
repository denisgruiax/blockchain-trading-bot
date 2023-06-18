package com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.exception.BinanceApiException;

import java.util.concurrent.Callable;

public class FetchCryptoBalance implements Callable<String> {
    private String symbol;
    private BinanceApiClientFactory binanceApiClientFactory;

    public FetchCryptoBalance(String symbol, BinanceApiClientFactory binanceApiClientFactory){
        this.symbol = symbol;
        this.binanceApiClientFactory = binanceApiClientFactory;
    }

    public String call(){
        BinanceApiRestClient client = binanceApiClientFactory.newRestClient();

        try {
            Account account = client.getAccount();
            return account.getAssetBalance(symbol).getFree();
        } catch (BinanceApiException binanceApiException) {
            return "0.0";
        }
    }
}
