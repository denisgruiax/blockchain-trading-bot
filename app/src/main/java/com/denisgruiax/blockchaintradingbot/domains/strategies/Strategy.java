package com.denisgruiax.blockchaintradingbot.domains.strategies;

import com.denisgruiax.blockchaintradingbot.utils.Order;

import java.util.List;

public interface Strategy {
    Order executeStrategy();
    void setPrices(List<Double> prices);
}
