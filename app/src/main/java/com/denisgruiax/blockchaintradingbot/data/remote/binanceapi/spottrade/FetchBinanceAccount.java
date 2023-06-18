package com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.exception.BinanceApiException;

import java.util.concurrent.Callable;

public class FetchBinanceAccount implements Callable<String> {
    private final BinanceApiClientFactory binanceApiClientFactory;

    public FetchBinanceAccount(BinanceApiClientFactory binanceApiClientFactory) {
        this.binanceApiClientFactory = binanceApiClientFactory;
    }

    public String call() {

        BinanceApiRestClient client = binanceApiClientFactory.newRestClient();

        try {
            Account account = client.getAccount();
            return account.getAssetBalance("BTC").getFree() + "AccountOK";
        } catch (BinanceApiException binanceApiException) {
            return "AccountX";
        }
    }
}
