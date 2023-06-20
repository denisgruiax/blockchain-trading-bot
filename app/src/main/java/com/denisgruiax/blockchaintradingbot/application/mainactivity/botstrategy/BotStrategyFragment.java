package com.denisgruiax.blockchaintradingbot.application.mainactivity.botstrategy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private final Handler handler = new Handler(Looper.getMainLooper());
    Future<List<Double>> prices;
    private FragmentBotStrategyBinding binding;
    private TextView textViewCreateAndConfigureBot;
    private TextView textViewBotName;
    private EditText editTextBotName;

    private TextView textViewBehavior;
    private Spinner spinnerBehaviors;

    private TextView textViewStrategy;
    private Spinner spinnerStrategies;

    private TextView textViewSelectBot;
    private Spinner spinnerSelectBot;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private Runnable runnable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BotStrategyModel botStrategyModel =
                new ViewModelProvider(this).get(BotStrategyModel.class);

        binding = FragmentBotStrategyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewCreateAndConfigureBot = binding.textViewCreateAndConfigureBot;
        botStrategyModel.getCreateAndConfigureBot().observe(getViewLifecycleOwner(), textViewCreateAndConfigureBot::setText);

        prices = executorService.submit(new FetchListOfPrices(CryptoId.bitcoin, "usd"));

        Spinner spinnerBehaviors = binding.spinnerBehaviors;

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item);
        spinnerBehaviors.setAdapter(spinnerAdapter);
        Button buttonLaunchBot = binding.buttonLaunchBot;

        buttonLaunchBot.setOnClickListener(view -> {
            List<String> spinnerData = new ArrayList<>();
            spinnerData.add("Option 1");
            spinnerData.add("Option 2");

            spinnerAdapter.clear();
            spinnerAdapter.addAll(spinnerData);
            spinnerAdapter.notifyDataSetChanged();
        });

        updateUserInterface();

        return root;
    }

    public void updateListOfPrices() {
    }

    public void checkStrategyOfTheCurrentBot() {
    }

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