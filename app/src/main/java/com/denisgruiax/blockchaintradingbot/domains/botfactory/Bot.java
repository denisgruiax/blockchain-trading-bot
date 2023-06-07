package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;
import com.denisgruiax.blockchaintradingbot.utils.Order;

public abstract class Bot {
    private final Strategy strategy;

    public Bot(Strategy strategy) {
        this.strategy = strategy;
    }

    public Order executeStrategy() {
        return strategy.executeStrategy();
    }

    // Other common bot methods
}
