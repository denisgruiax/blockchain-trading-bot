package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;

public class BotFactory {
    public Bot createBot(String name, BotBehavior behavior, Strategy strategy){
        return new Bot(name, behavior, strategy);
    }
}
