package com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.connector.client.SpotClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.denisgruiax.blockchaintradingbot.utils.CryptoId;
import com.denisgruiax.blockchaintradingbot.utils.Keys;
import com.denisgruiax.blockchaintradingbot.utils.Order;

import java.util.LinkedHashMap;
import java.util.concurrent.Callable;

public class SpotTrade implements Callable<Long> {
    private static Order oldSide;
    private BinanceApiClientFactory binanceApiClientFactory;
    private String cryptoId;
    private Order side;
    private double quantity;
    private String result;

    public SpotTrade(BinanceApiClientFactory binanceApiClientFactory, String cryptoId, Order side) {
        this.binanceApiClientFactory = binanceApiClientFactory;
        this.cryptoId = cryptoId;
        this.side = side;
    }

    @Override
    public Long call() {
        if (oldSide != side){
            oldSide = side;
            return createTrade();
        }

        return -1L;
    }

    private String getSymbolFromId(String cryptoId) {
        switch (cryptoId) {
            case "bitcoin":
                return "BTCUSDT";
            case "ethereum":
                return "ETHUSDT";
            case "elrond-erd-2":
                return "EGLDUSDT";
            case "polkadot":
                return "DOTUSDT";
            case "internet-computer":
                return "ICPUSDT";

            default:
                return "BTCUSDT";
        }
    }

    private String getQuantityFromId(String cryptoId) {
        switch (cryptoId) {
            case "ethereum":
                return "0.1";
            case "elrond-erd-2":
                return "5.0";
            case "polkadot":
                return "20.0";
            case "internet-computer":
                return "30";

            default:
                return "0.01";
        }
    }

    public Long createTrade() {
        BinanceApiRestClient client = binanceApiClientFactory.newRestClient();
        NewOrder newOrder;
        NewOrderResponse newOrderResponse;

        switch (side) {
            case BUY:
                newOrder = NewOrder.marketBuy(getSymbolFromId(cryptoId), getQuantityFromId(cryptoId));
                newOrderResponse = client.newOrder(newOrder);
                return newOrderResponse.getOrderId();

            case SELL:
                newOrder = NewOrder.marketSell(getSymbolFromId(cryptoId), getQuantityFromId(cryptoId));
                newOrderResponse = client.newOrder(newOrder);
                return newOrderResponse.getOrderId();

            default:
                return -1L;
        }
    }
}
