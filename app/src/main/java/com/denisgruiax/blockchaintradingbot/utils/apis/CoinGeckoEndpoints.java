package com.denisgruiax.blockchaintradingbot.utils.apis;

public class CoinGeckoEndpoints {
    public static String cryptoId = "bitcoin";
    public static String currency = "usd";
    public static String balanceEndpoint = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

    public static String days = "1";
    public static String interval = "5m";
    public static String historicPriceEndpoint = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=1&interval=5m";

    public static String getBalanceEndpoint() {
        return balanceEndpoint;
    }

    public static void setBalanceEndpoint(String cryptoId, String currency) {
        CoinGeckoEndpoints.cryptoId = cryptoId;
        CoinGeckoEndpoints.currency = currency;
        balanceEndpoint = String.format("https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s", cryptoId, currency);
    }

    public static String getHistoricPriceEndpoint() {
        return historicPriceEndpoint;
    }

    public static void setHistoricPriceEndpoint(String days, String interval) {
        CoinGeckoEndpoints.days = days;
        CoinGeckoEndpoints.interval = interval;
        historicPriceEndpoint = String.format("https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=%s&interval=%s", days, interval);
    }
}
