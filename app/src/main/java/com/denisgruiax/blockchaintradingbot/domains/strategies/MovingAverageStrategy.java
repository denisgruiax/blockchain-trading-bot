package com.denisgruiax.blockchaintradingbot.domains.strategies;

import com.denisgruiax.blockchaintradingbot.utils.Order;

public class MovingAverageStrategy implements Strategy {

    public Order executeStrategy(){
        return Order.BUY;
    }
}
