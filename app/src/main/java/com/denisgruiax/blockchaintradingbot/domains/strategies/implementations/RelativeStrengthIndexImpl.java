package com.denisgruiax.blockchaintradingbot.domains.strategies.implementations;

import com.denisgruiax.blockchaintradingbot.domains.botfactory.BotBehavior;
import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;
import com.denisgruiax.blockchaintradingbot.utils.Order;

import java.util.List;

public class RelativeStrengthIndexImpl implements Strategy {
    private List<Double> prices;
    private BotBehavior botBehavior;
    private int period;

    public RelativeStrengthIndexImpl(List<Double> prices, BotBehavior botBehavior) {
        this.prices = prices;
        this.botBehavior = botBehavior;

        switch (this.botBehavior) {
            case CONSERVATIVE:
                period = 14;
                break;

            case MODERATE:
                period = 16;
                break;

            case RISKY:
                period = 18;
                break;

            case AGGRESSIVE:
                period = 20;
                break;
        }
    }

    public static double calculateRSI(List<Double> prices, int period) {
        if (prices.size() <= period) {
            throw new IllegalArgumentException("Insufficient data for the specified period.");
        }

        double[] gains = new double[prices.size()];
        double[] losses = new double[prices.size()];

        for (int i = 1; i < prices.size(); i++) {
            double priceDiff = prices.get(i) - prices.get(i - 1);
            gains[i] = Math.max(0, priceDiff);
            losses[i] = Math.max(0, -priceDiff);
        }

        double avgGain = calculateAverage(gains, period);
        double avgLoss = calculateAverage(losses, period);

        double relativeStrength = (avgGain / avgLoss);
        double rsi = 100 - (100 / (1 + relativeStrength));

        return rsi;
    }

    private static double calculateAverage(double[] values, int period) {
        double sum = 0;
        for (int i = values.length - period; i < values.length; i++) {
            sum += values[i];
        }
        return sum / period;
    }

    @Override
    public Order executeStrategy() {
        double rsiValue = calculateRSI(prices, period);
        if (rsiValue < 30.0)
            return Order.BUY;
        else if(rsiValue > 70)
            return Order.SELL;
        else
            return Order.NOT_READY;
    }

    @Override
    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }
}