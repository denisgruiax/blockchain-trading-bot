package com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.exception.BinanceApiException;

import java.util.concurrent.Callable;

public class FetchBinanceAccount implements Callable<Account> {
    private final BinanceApiClientFactory binanceApiClientFactory;

    public FetchBinanceAccount(BinanceApiClientFactory binanceApiClientFactory) {
        this.binanceApiClientFactory = binanceApiClientFactory;
    }

    public Account call() {
        BinanceApiRestClient client = binanceApiClientFactory.newRestClient();

        try {
            return client.getAccount();
        } catch (BinanceApiException binanceApiException) {
            return null;
        }
    }
}
