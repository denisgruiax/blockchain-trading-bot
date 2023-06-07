package com.denisgruiax.blockchaintradingbot.domains.strategies;

import com.denisgruiax.blockchaintradingbot.utils.Order;

public interface Strategy {
    Order executeStrategy();
}
