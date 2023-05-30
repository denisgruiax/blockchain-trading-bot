package com.denisgruiax.blockchaintradingbot.ui.walletstatistics;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.WebSocketApiClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.WebSocketApiClientImpl;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentWalletStatisticsBinding;

import java.util.LinkedHashMap;
import java.util.Random;

import com.binance.connector.client.utils.signaturegenerator.HmacSignatureGenerator;
import com.denisgruiax.blockchaintradingbot.domains.spottrade.SpotTrade;
import com.denisgruiax.blockchaintradingbot.keys.Keys;

import org.json.JSONException;
import org.json.JSONObject;

public class WalletStatisticsFragment extends Fragment {

    private FragmentWalletStatisticsBinding binding;
    private TextView balanceText;
    private TextView dailyPNLText;
    private Button incrementBalance;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private Random random = new Random();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WalletStatisticsModel walletStatisticsModel =
                new ViewModelProvider(this).get(WalletStatisticsModel.class);

        binding = FragmentWalletStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        balanceText = binding.balance;
        dailyPNLText = binding.dailyPNL;
        incrementBalance = binding.button;

        walletStatisticsModel.getBalanceText().observe(getViewLifecycleOwner(), balanceText::setText);
        walletStatisticsModel.getDailyPNLText().observe(getViewLifecycleOwner(), dailyPNLText::setText);

        SpotTrade spotTrade = new SpotTrade();
        new Thread(spotTrade).start();

        if (spotTrade.getResult().length() > 0)
            dailyPNLText.setText(spotTrade.getResult());
        else
            dailyPNLText.setText("0!!");

        incrementBalance.setOnClickListener(view -> {
            balanceText.setText("$20");
            dailyPNLText.setText("12%");
        });

        //updateUserInterface();

        return root;
    }

    public void updateUserInterface() {
        runnable = new Runnable() {
            @Override
            public void run() {
                String randomNumber = String.valueOf(random.nextInt(100));
                balanceText.setText(randomNumber);
                Log.d("Result", randomNumber);
                handler.postDelayed(this, 2500);
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