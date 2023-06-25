package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.Strategy;

public class BotFactory {
    public Bot createBot(String name,String crypto,BotBehavior behavior, Strategy strategy){
        return new Bot(name, crypto, behavior, strategy);
    }
}
