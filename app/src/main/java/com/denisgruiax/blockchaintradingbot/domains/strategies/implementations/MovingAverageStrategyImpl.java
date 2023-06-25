package com.denisgruiax.blockchaintradingbot.domains.strategies.implementations;

import androidx.annotation.NonNull;

import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchListOfPrices;
import com.denisgruiax.blockchaintradingbot.domains.botfactory.BotBehavior;
import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;
import com.denisgruiax.blockchaintradingbot.utils.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MovingAverageStrategyImpl implements Strategy {
    Future<List<Double>> futurePrices;
    private String cryptoId;
    private String currency;
    private BotBehavior botBehavior;
    private int sizeOfShortMovingAverage;
    private int sizeOfLongMovingAverage;
    private double shortMovingAverage;
    private double longMovingAverage;
    private List<Double> prices;
    private ExecutorService executorService;

    public MovingAverageStrategyImpl(BotBehavior botBehavior, List<Double> prices) {
        this.botBehavior = botBehavior;
        this.prices = prices;

        switch (this.botBehavior) {
            case CONSERVATIVE:
                this.sizeOfShortMovingAverage = 10;
                this.sizeOfLongMovingAverage = 20;
                break;

            case MODERATE:
                this.sizeOfShortMovingAverage = 9;
                this.sizeOfLongMovingAverage = 20;
                break;

            case RISKY:
                this.sizeOfShortMovingAverage = 9;
                this.sizeOfLongMovingAverage = 22;
                break;

            case AGGRESSIVE:
                this.sizeOfShortMovingAverage = 7;
                this.sizeOfLongMovingAverage = 25;
                break;
        }

    }

    @Override
    public Order executeStrategy() {
        return checkSignal();
    }

    private List<Double> getPricesForMovingAverage(int sizeOfMovingAverage) {
        return prices.subList(prices.size() - sizeOfMovingAverage, prices.size());
    }

    private double calculateMovingAverage(List<Double> movingAverage) {
        return movingAverage.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.00);
    }

    private Order checkSignal() {
        shortMovingAverage = calculateMovingAverage(getPricesForMovingAverage(sizeOfShortMovingAverage));
        longMovingAverage = calculateMovingAverage(getPricesForMovingAverage(sizeOfLongMovingAverage));

        if (shortMovingAverage > longMovingAverage)
            return Order.BUY;
        else if (shortMovingAverage < longMovingAverage)
            return Order.SELL;

        return Order.NOT_READY;
    }

    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }
}