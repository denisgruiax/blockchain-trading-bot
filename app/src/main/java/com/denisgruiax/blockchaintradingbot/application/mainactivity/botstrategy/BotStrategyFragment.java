package com.denisgruiax.blockchaintradingbot.application.mainactivity.botstrategy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.denisgruiax.blockchaintradingbot.data.remote.binanceapi.spottrade.SpotTrade;
import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchListOfPrices;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentBotStrategyBinding;
import com.denisgruiax.blockchaintradingbot.domains.botfactory.Bot;
import com.denisgruiax.blockchaintradingbot.domains.botfactory.BotBehavior;
import com.denisgruiax.blockchaintradingbot.domains.strategies.implementations.MovingAverageStrategyImpl;
import com.denisgruiax.blockchaintradingbot.domains.strategies.implementations.RelativeStrengthIndexImpl;
import com.denisgruiax.blockchaintradingbot.utils.CryptoId;
import com.denisgruiax.blockchaintradingbot.utils.Order;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class BotStrategyFragment extends Fragment {
    private static final String PREF_NAME = "BotPrefs";
    private static final String KEY_BOT_LIST = "botList";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private FragmentBotStrategyBinding binding;
    private BotStrategyModel botStrategyModel;
    private TextView textViewCreateAndConfigureBot;
    private TextView textViewBotName;
    private EditText editTextSetBotName;
    private TextView textViewBehavior;
    private Spinner spinnerSelectBotBehavior;
    private TextView textViewStrategy;
    private Spinner spinnerSelectStrategy;
    private TextView textViewCrypto;
    private Spinner spinnerSelectCrypto;
    private Button buttonCreateBot;
    private TextView textViewLaunchBot;
    private TextView textViewBotNameForLaunch;
    private Spinner spinnerSelectBotForLaunch;
    private Button buttonLaunchBot;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String apiKey;
    private String secretKey;

    private Bot currentBot;
    private Boolean isLaunched = false;

    private List<Bot> bots = new ArrayList<>();
    private Future<List<Double>> futurePrices;
    private List<Double> prices;

    private BinanceApiClientFactory binanceApiRestClient;
    private Future<Long> futureOrderId;
    private Long orderId;

    private ExecutorService executorService = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private Runnable runnable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        botStrategyModel =
                new ViewModelProvider(this).get(BotStrategyModel.class);

        binding = FragmentBotStrategyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getApiKeysFromMemory();

        initializeElements();
        observeElements();

        setSpinnerSelectBotBehaviorValues();
        setSpinnerSelectStrategyValues();
        setSpinnerSelectPair();

        createAndConfigureBotButton();
        launchOrStopTradingBot();

        updateUserInterface();

        return root;
    }

    private void getApiKeysFromMemory() {
        apiKey = sharedPreferences.getString("apiKey", null);
        secretKey = sharedPreferences.getString("secretKey", null);
    }

    private void initializeElements() {
        textViewCreateAndConfigureBot = binding.textViewCreateAndConfigureBot;

        textViewBotName = binding.textViewBotName;
        editTextSetBotName = binding.editTextSetBotName;

        textViewBehavior = binding.textViewBehavior;
        spinnerSelectBotBehavior = binding.spinnerSelectBotBehavior;

        textViewStrategy = binding.textViewStrategy;
        spinnerSelectStrategy = binding.spinnerSelectStrategy;

        textViewCrypto = binding.textViewCrypto;
        spinnerSelectCrypto = binding.spinnerSelectCrypto;

        buttonCreateBot = binding.buttonCreateBot;

        textViewLaunchBot = binding.textViewLaunchBot;

        textViewBotNameForLaunch = binding.textViewBotNameForLaunch;
        spinnerSelectBotForLaunch = binding.spinnerSelectBotForLaunch;

        buttonLaunchBot = binding.buttonLaunchBot;
    }

    private void observeElements() {
        botStrategyModel.getTextViewCreateAndConfigureBot().observe(getViewLifecycleOwner(), textViewCreateAndConfigureBot::setText);

        botStrategyModel.getTextViewBotName().observe(getViewLifecycleOwner(), textViewBotName::setText);
        botStrategyModel.getEditTextSetBotName().observe(getViewLifecycleOwner(), editTextSetBotName::setText);

        botStrategyModel.getTextViewBehavior().observe(getViewLifecycleOwner(), textViewBehavior::setText);
        botStrategyModel.getSpinnerSelectBotBehavior().observe(getViewLifecycleOwner(), spinnerData -> {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerData);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSelectBotBehavior.setAdapter(spinnerAdapter);
        });

        botStrategyModel.getTextViewStrategy().observe(getViewLifecycleOwner(), textViewStrategy::setText);
        botStrategyModel.getSpinnerSelectStrategy().observe(getViewLifecycleOwner(), spinnerData -> {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerData);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSelectBotBehavior.setAdapter(spinnerAdapter);
        });

        botStrategyModel.getTextViewCrypto().observe(getViewLifecycleOwner(), textViewCrypto::setText);
        botStrategyModel.getSpinnerSelectCrypto().observe(getViewLifecycleOwner(), spinnerData -> {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerData);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSelectBotBehavior.setAdapter(spinnerAdapter);
        });

        botStrategyModel.getTextViewLaunchBot().observe(getViewLifecycleOwner(), textViewLaunchBot::setText);

        botStrategyModel.getTextViewBotNameForLaunch().observe(getViewLifecycleOwner(), textViewBotNameForLaunch::setText);
        botStrategyModel.getSpinnerSelectBotForLaunch().observe(getViewLifecycleOwner(), spinnerData -> {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerData);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSelectBotBehavior.setAdapter(spinnerAdapter);
        });
    }

    private void setSpinnerSelectBotBehaviorValues() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item);
        spinnerSelectBotBehavior.setAdapter(spinnerAdapter);

        List<String> spinnerData = new ArrayList<>();

        for (BotBehavior botBehavior : BotBehavior.values())
            spinnerData.add(botBehavior.getDisplayName());

        spinnerAdapter.clear();
        spinnerAdapter.addAll(spinnerData);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void setSpinnerSelectStrategyValues() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item);
        spinnerSelectStrategy.setAdapter(spinnerAdapter);

        List<String> spinnerData = new ArrayList<>();

        spinnerData.add("Moving Average");
        spinnerData.add("Relative Strength Index");

        spinnerAdapter.clear();
        spinnerAdapter.addAll(spinnerData);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void setSpinnerSelectPair() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item);
        spinnerSelectCrypto.setAdapter(spinnerAdapter);

        List<String> spinnerData = new ArrayList<>();

        spinnerData.add(CryptoId.bitcoin);
        spinnerData.add(CryptoId.ethereum);
        spinnerData.add(CryptoId.multiversX);
        spinnerData.add(CryptoId.internetComputer);
        spinnerData.add(CryptoId.polkadot);

        spinnerAdapter.clear();
        spinnerAdapter.addAll(spinnerData);
        spinnerAdapter.notifyDataSetChanged();
    }

    private BotBehavior selectBehaviorFromString(String behavior) {
        for (BotBehavior botBehavior : BotBehavior.values()) {
            if (botBehavior.getDisplayName().equals(behavior)) {
                return botBehavior;
            }
        }

        return BotBehavior.CONSERVATIVE;
    }

    private void createAndConfigureBot() {
        Bot botContainer;
        String name = editTextSetBotName.getText().toString();

        bots.removeIf(bot -> bot.getName().equals(name));

        BotBehavior behavior = selectBehaviorFromString(spinnerSelectBotBehavior.getSelectedItem().toString());
        String strategy = spinnerSelectStrategy.getSelectedItem().toString();
        String crypto = spinnerSelectCrypto.getSelectedItem().toString();

        switch (strategy) {
            case "Moving Average":
                fetchPricesForChart(crypto, "usd");
                botContainer = new Bot(name, crypto, behavior, new MovingAverageStrategyImpl(behavior, prices));
                bots.add(botContainer);
                break;

            case "Relative Strength Index":
                fetchPricesForChart(crypto, "usd");
                botContainer = new Bot(name, crypto, behavior, new RelativeStrengthIndexImpl(prices, behavior));
                bots.add(botContainer);
                break;
        }
    }

    private void createAndConfigureBotButton() {
        buttonCreateBot.setOnClickListener(view -> {
            createAndConfigureBot();

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item);
            spinnerSelectBotForLaunch.setAdapter(spinnerAdapter);

            List<String> spinnerData = new ArrayList<>();

            for (Bot bot : bots)
                spinnerData.add(bot.getName());

            spinnerAdapter.clear();
            spinnerAdapter.addAll(spinnerData);
            spinnerAdapter.notifyDataSetChanged();
        });
    }

    private void launchOrStopTradingBot() {
        buttonLaunchBot.setOnClickListener(view -> {
            if (!isLaunched) {
                String currentBot = spinnerSelectBotForLaunch.getSelectedItem().toString();

                for (Bot bot : bots)
                    if (bot.getName().equals(currentBot)) {
                        this.currentBot = bot;
                        isLaunched = true;
                        break;
                    }

                buttonLaunchBot.setText("STOP ❌");
            } else {
                buttonLaunchBot.setText("LAUNCH 🚀");
                isLaunched = false;
            }
        });
    }

    private void checkStrategyOfTheCurrentBot() {
        if (isLaunched) {
            fetchPricesForChart(currentBot.getCrypto(), "usd");
            currentBot.getStrategy().setPrices(prices);

            Order order = currentBot.executeStrategy();

            switch (order) {
                case BUY:
                    binanceApiRestClient = BinanceApiClientFactory.newInstance(apiKey, secretKey);
                    futureOrderId = executorService.submit(new SpotTrade(binanceApiRestClient, currentBot.getCrypto(), Order.BUY));
                    toastShortMessage("Buy Order");
                    break;

                case SELL:
                    binanceApiRestClient = BinanceApiClientFactory.newInstance(apiKey, secretKey);
                    futureOrderId = executorService.submit(new SpotTrade(binanceApiRestClient, currentBot.getCrypto(), Order.SELL));
                    toastShortMessage("Sell order");
                    break;

                case NOT_READY:
                    toastShortMessage("Not ready!");
                    break;
            }

            if (futureOrderId.isDone()) {
                try {
                    if (futureOrderId.get()!=null)
                        toastShortMessage("Order ID: " + futureOrderId.get().toString());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void fetchPricesForChart(String cryptoId, String currency) {
        futurePrices = executorService.submit(new FetchListOfPrices(cryptoId, currency));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (futurePrices.isDone()) {
            try {
                prices = futurePrices.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateUserInterface() {
        runnable = new Runnable() {
            @Override
            public void run() {
                checkStrategyOfTheCurrentBot();
                handler.postDelayed(this, 5000);
            }
        };

        handler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void toastShortMessage(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}