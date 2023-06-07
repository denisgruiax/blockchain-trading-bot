package com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class FetchListOfPricesOLD implements Callable<List<Candlestick>> {
    private BinanceApiClientFactory binanceApiClientFactory;
    private String symbol;
    private CandlestickInterval interval;
    private List<Double> prices;

    public FetchListOfPricesOLD(BinanceApiClientFactory binanceApiClientFactory, String symbol, CandlestickInterval interval){
        this.binanceApiClientFactory = binanceApiClientFactory;
        this.symbol = symbol;
        this.interval = interval;
    }

    public List<Candlestick> call() {
        return fetchCandles();
    }

    private  List<Candlestick> fetchCandles(){
        BinanceApiRestClient client = binanceApiClientFactory.newRestClient();
        return client.getCandlestickBars(symbol, interval);
    }

    private List<Double> getPricesFromCandles(List<Candlestick> candlesticks){
        return candlesticks.stream()
                .map(Candlestick::getClose)
                .map(Double::valueOf)
                .mapToDouble(Double::doubleValue)
                .boxed()
                .collect(Collectors.toList());
    }
}
