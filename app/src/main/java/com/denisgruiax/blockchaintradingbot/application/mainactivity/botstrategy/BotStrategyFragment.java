package com.denisgruiax.blockchaintradingbot.application.mainactivity.botstrategy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binance.api.client.BinanceApiClientFactory;
import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchListOfPrices;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentBotStrategyBinding;
import com.denisgruiax.blockchaintradingbot.utils.CryptoId;
import com.denisgruiax.blockchaintradingbot.utils.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class BotStrategyFragment extends Fragment {
    private FragmentBotStrategyBinding binding;
    private TextView textView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ExecutorService executorService = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private Runnable runnable;
    Future<List<Double>> prices;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BotStrategyModel botStrategyModel =
                new ViewModelProvider(this).get(BotStrategyModel.class);

        binding = FragmentBotStrategyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textBot;
        botStrategyModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        prices = executorService.submit(new FetchListOfPrices(CryptoId.bitcoin, "usd"));

        updateUserInterface();

        return root;
    }

    public void updateListOfPrices(){}
    public void checkStrategyOfTheCurrentBot(){}

    private void updateUserInterface() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (prices.isDone()) {
                    try {
                        List<Double> listOfPrices = prices.get();
                        updateListOfPrices();
                        checkStrategyOfTheCurrentBot();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                handler.postDelayed(this, 5000);
            }
        };

        handler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}