package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;
import com.denisgruiax.blockchaintradingbot.utils.Order;

public class Bot {
    private String name;
    private String crypto;
    private BotBehavior behavior;
    private Strategy strategy;

    public Bot(String name, String crypto, BotBehavior behavior, Strategy strategy) {
        this.name = name;
        this.crypto = crypto;
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

    public String getCrypto(){return crypto;}

    public void setCrypto(String crypto){this.crypto = crypto;}

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
