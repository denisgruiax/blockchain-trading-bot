package com.denisgruiax.blockchaintradingbot.domains.botfactory;

import com.denisgruiax.blockchaintradingbot.domains.strategies.MovingAverageStrategy;

public class BotLaky extends Bot {
    public BotLaky() {
        super(new MovingAverageStrategy());
    }
}
