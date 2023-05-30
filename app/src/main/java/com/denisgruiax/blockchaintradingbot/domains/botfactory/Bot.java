package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;

public abstract class Bot {
    private final Strategy strategy;

    public Bot(Strategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy() {
        strategy.executeStrategy();
    }

    // Other common bot methods
}
