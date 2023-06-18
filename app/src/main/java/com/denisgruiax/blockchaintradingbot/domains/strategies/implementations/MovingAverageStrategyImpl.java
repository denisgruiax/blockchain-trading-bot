package com.denisgruiax.blockchaintradingbot.domains.strategies.implementations;

import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchListOfPrices;
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
    private int sizeOfShortMovingAverage;
    private int sizeOfLongMovingAverage;
    private double oldShortMovingAverage;
    private double oldLongMovingAverage;
    private double shortMovingAverage;
    private double longMovingAverage;
    private List<Double> prices;
    private ExecutorService executorService;

    public MovingAverageStrategyImpl(String cryptoId, String currency, int sizeOfShortMovingAverage, int sizeOfLongMovingAverage) {
        this.cryptoId = cryptoId;
        this.currency = currency;
        this.sizeOfShortMovingAverage = sizeOfShortMovingAverage;
        this.sizeOfLongMovingAverage = sizeOfLongMovingAverage;
        prices = new ArrayList<Double>();
        executorService = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public Order executeStrategy() {
        fetchPrices();
        return checkSignal();
    }


    private void fetchPrices() {
        futurePrices = executorService.submit(new FetchListOfPrices(cryptoId, currency));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (futurePrices.isDone()) {
            try {
                prices = futurePrices.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Double> fillMovingAverage(int sizeOfMovingAverage) {
        List<Double> movingAverage = new ArrayList<Double>(sizeOfMovingAverage);

        if (futurePrices.isDone()) {
            try {
                movingAverage = futurePrices.get();

                if (movingAverage.size() >= sizeOfMovingAverage)
                    return movingAverage.subList(movingAverage.size() - sizeOfMovingAverage, movingAverage.size());

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return movingAverage;
    }

    private double calculateMovingAverage(List<Double> movingAverage) {
        return movingAverage.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.00);
    }

    private Order checkSignal() {
        double lastPrice = 0.0;

        lastPrice = prices.get(prices.size());
        prices.remove(prices.size());
        oldShortMovingAverage = calculateMovingAverage(fillMovingAverage(sizeOfShortMovingAverage - 1));
        oldLongMovingAverage = calculateMovingAverage(fillMovingAverage(sizeOfLongMovingAverage - 1));

        prices.add(lastPrice);
        shortMovingAverage = calculateMovingAverage(fillMovingAverage(sizeOfShortMovingAverage));
        longMovingAverage = calculateMovingAverage(fillMovingAverage(sizeOfLongMovingAverage));

        if ((shortMovingAverage > longMovingAverage) && (oldShortMovingAverage < oldLongMovingAverage))
            return Order.BUY;
        else if ((shortMovingAverage < longMovingAverage) && (oldShortMovingAverage < oldLongMovingAverage))
            return Order.SELL;

        return Order.NOT_READY;
    }
}