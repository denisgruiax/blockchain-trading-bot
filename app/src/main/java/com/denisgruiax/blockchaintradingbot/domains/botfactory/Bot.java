package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;
import com.denisgruiax.blockchaintradingbot.utils.Order;

public class Bot {
    private String name;
    private BotBehavior behavior;
    private Strategy strategy;

    public Bot(String name, BotBehavior behavior, Strategy strategy) {
        this.name = name;
        this.behavior = behavior;
        this.strategy = strategy;
    }

    public Order executeStrategy() {
        return strategy.executeStrategy();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BotBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(BotBehavior behavior) {
        this.behavior = behavior;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
