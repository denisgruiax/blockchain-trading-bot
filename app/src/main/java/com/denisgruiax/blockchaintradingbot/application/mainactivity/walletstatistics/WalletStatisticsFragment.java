package com.denisgruiax.blockchaintradingbot.application.mainactivity.walletstatistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.binance.api.client.BinanceApiClientFactory;
import com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade.FetchCryptoBalance;
import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchPrice;
import com.denisgruiax.blockchaintradingbot.databinding.ActivityMainBinding;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentWalletStatisticsBinding;
import com.denisgruiax.blockchaintradingbot.databinding.NavHeaderMainBinding;
import com.denisgruiax.blockchaintradingbot.utils.Keys;
import com.denisgruiax.blockchaintradingbot.utils.CryptoId;
import com.denisgruiax.blockchaintradingbot.utils.Symbol;
import com.google.android.material.navigation.NavigationView;

public class WalletStatisticsFragment extends Fragment {

    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=30";

    private FragmentWalletStatisticsBinding binding;
    private SharedPreferences sharedPreferences;
    private TextView totalBalanceText;

    private String apiKey;
    private String secretKey;

    private TextView name2;
    private TextView price2;
    private TextView balance2;
    private Future<Double> futurePrice2;
    private Future<String> futureBalance2;

    private TextView name3;
    private TextView price3;
    private TextView balance3;
    private Future<Double> futurePrice3;
    private Future<String> futureBalance3;

    private TextView name4;
    private TextView price4;
    private TextView balance4;
    private Future<Double> futurePrice4;
    private Future<String> futureBalance4;

    private TextView name5;
    private TextView price5;
    private TextView balance5;
    private Future<Double> futurePrice5;
    private Future<String> futureBalance5;

    private TextView name6;
    private TextView price6;
    private TextView balance6;
    private Future<Double> futurePrice6;
    private Future<String> futureBalance6;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private ExecutorService executorService = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private Runnable runnable;
    private BinanceApiClientFactory binanceApiClientFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WalletStatisticsModel walletStatisticsModel =
                new ViewModelProvider(this).get(WalletStatisticsModel.class);

        binding = FragmentWalletStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getApiKeysFromMemory();

        binanceApiClientFactory = BinanceApiClientFactory.newInstance(apiKey, secretKey);

        initializeViewElements();

        setCryptoNames();

        observeElements(walletStatisticsModel);

        fetchPrices();
        fetchBalances();
        totalBalance();

        updateUserInterface();

        return root;
    }

    private void getApiKeysFromMemory() {
        apiKey = sharedPreferences.getString("apiKey", null);
        secretKey = sharedPreferences.getString("secretKey", null);
    }

    private void initializeViewElements() {
        totalBalanceText = binding.totalBalance;

        name2 = binding.name2;
        price2 = binding.price2;
        balance2 = binding.balance2;

        name3 = binding.name3;
        price3 = binding.price3;
        balance3 = binding.balance3;

        name4 = binding.name4;
        price4 = binding.price4;
        balance4 = binding.balance4;

        name5 = binding.name5;
        price5 = binding.price5;
        balance5 = binding.balance5;

        name6 = binding.name6;
        price6 = binding.price6;
        balance6 = binding.balance6;
    }

    private void setCryptoNames() {
        name2.setText("BTC");
        name3.setText("ETH");
        name4.setText("EGLD");
        name5.setText("DOT");
        name6.setText("ICP");
    }

    private void observeElements(WalletStatisticsModel walletStatisticsModel) {
        walletStatisticsModel.getTotalBalanceText().observe(getViewLifecycleOwner(), totalBalanceText::setText);

        walletStatisticsModel.getPrice2().observe(getViewLifecycleOwner(), price2::setText);
        walletStatisticsModel.getPrice3().observe(getViewLifecycleOwner(), price3::setText);
        walletStatisticsModel.getPrice4().observe(getViewLifecycleOwner(), price4::setText);
        walletStatisticsModel.getPrice5().observe(getViewLifecycleOwner(), price5::setText);
        walletStatisticsModel.getPrice6().observe(getViewLifecycleOwner(), price6::setText);
    }

    private void fetchPrices() {
        futurePrice2 = executorService.submit(new FetchPrice(CryptoId.bitcoin, "usd"));
        futurePrice3 = executorService.submit(new FetchPrice(CryptoId.ethereum, "usd"));
        futurePrice4 = executorService.submit(new FetchPrice(CryptoId.multiversX, "usd"));
        futurePrice5 = executorService.submit(new FetchPrice(CryptoId.polkadot, "usd"));
        futurePrice6 = executorService.submit(new FetchPrice(CryptoId.internetComputer, "usd"));
    }

    private void futurePricesAreDone() {
        boolean areNotNull = false;
        boolean areDone = false;

        areNotNull = (futurePrice2 != null) && (futurePrice3 != null) && (futurePrice4 != null) && (futurePrice5 != null) && (futurePrice6 != null);

        if (areNotNull)
            areDone = futurePrice2.isDone() && futurePrice3.isDone() && futurePrice4.isDone() && futurePrice5.isDone() && futurePrice6.isDone();

        if (areDone) {
            try {
                price2.setText(futurePrice2.get().toString() + "$");
                price3.setText(futurePrice3.get().toString() + "$");
                price4.setText(futurePrice4.get().toString() + "$");
                price5.setText(futurePrice5.get().toString() + "$");
                price6.setText(futurePrice6.get().toString() + "$");
            } catch (ExecutionException executionException) {
                throw new RuntimeException(executionException);
            } catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
        }
    }

    private void totalBalance() {
        boolean futureBalancesAreNotNull = false;
        boolean futurePricesAreNotNull = false;
        boolean balancesAreDone = false;
        boolean pricesAreDone = false;

        futureBalancesAreNotNull = (futureBalance2 != null) && (futureBalance3 != null) && (futureBalance4 != null) && (futureBalance5 != null) && (futureBalance6 != null);

        if (futureBalancesAreNotNull)
            balancesAreDone = futureBalance2.isDone() && futureBalance3.isDone() && futureBalance4.isDone() && futureBalance5.isDone() && futureBalance6.isDone();

        futurePricesAreNotNull = (futurePrice2 != null) && (futurePrice3 != null) && (futurePrice4 != null) && (futurePrice5 != null) && (futurePrice6 != null);

        if (futurePricesAreNotNull)
            pricesAreDone = futurePrice2.isDone() && futurePrice3.isDone() && futurePrice4.isDone() && futurePrice5.isDone() && futurePrice6.isDone();

        if (balancesAreDone && pricesAreDone)
            try {
                Double totalBalance = (futurePrice2.get() * Double.parseDouble(futureBalance2.get()) + futurePrice3.get() * Double.parseDouble(futureBalance3.get()) + futurePrice4.get() * Double.parseDouble(futureBalance4.get()) + futurePrice5.get() * Double.parseDouble(futureBalance5.get()) + futurePrice6.get() * Double.parseDouble(futureBalance6.get()));
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                totalBalanceText.setText(decimalFormat.format(totalBalance).toString() + "$");
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    private void updateUserInterface() {
        runnable = new Runnable() {
            @Override
            public void run() {
                futurePricesAreDone();
                fetchBalance();
                totalBalance();
                handler.postDelayed(this, 5000);
            }
        };

        handler.post(runnable);
    }

    private void fetchBalances() {
        futureBalance2 = executorService.submit(new FetchCryptoBalance(Symbol.BTC, binanceApiClientFactory));
        futureBalance3 = executorService.submit(new FetchCryptoBalance(Symbol.ETH, binanceApiClientFactory));
        futureBalance4 = executorService.submit(new FetchCryptoBalance(Symbol.EGLD, binanceApiClientFactory));
        futureBalance5 = executorService.submit(new FetchCryptoBalance(Symbol.DOT, binanceApiClientFactory));
        futureBalance6 = executorService.submit(new FetchCryptoBalance(Symbol.ICP, binanceApiClientFactory));
    }

    private void fetchBalance() {
        boolean areNotNull = (futureBalance2 != null) && (futureBalance3 != null) && (futureBalance4 != null) && (futureBalance5 != null) && (futureBalance6 != null);
        boolean areDone = futureBalance2.isDone() && futureBalance3.isDone() && futureBalance4.isDone() && futureBalance5.isDone() && futureBalance6.isDone();

        if (areDone)
            try {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                balance2.setText(decimalFormat.format(Double.valueOf(futureBalance2.get())));
                balance3.setText(decimalFormat.format(Double.valueOf(futureBalance3.get())));
                balance4.setText(decimalFormat.format(Double.valueOf(futureBalance4.get())));
                balance5.setText(decimalFormat.format(Double.valueOf(futureBalance5.get())));
                balance6.setText(decimalFormat.format(Double.valueOf(futureBalance6.get())));
            } catch (ExecutionException executionException) {
                throw new RuntimeException(executionException);
            } catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}