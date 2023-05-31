package com.denisgruiax.blockchaintradingbot.ui.walletstatistics;

import android.os.AsyncTask;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.WebSocketApiClient;
import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.exceptions.BinanceConnectorException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.WebSocketApiClientImpl;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentWalletStatisticsBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private String result;
    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=30";

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private Random random = new Random();
    Future<String> future;

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

        balanceText.setText("$100");
        dailyPNLText.setText("100%");

        incrementBalance.setOnClickListener(view ->
        {
            ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            SpotTrade spotTrade = new SpotTrade("BTCUSDT", "BUY", "LIMIT", "GTC", 0.01, 21000.0);
            future = executorService.submit(spotTrade);
        });

        // Start an instance of FetchJsonTask to fetch the JSON object
        // new FetchJsonTask().execute(API_URL);

        updateUserInterface();

        return root;
    }

    public void updateUserInterface() {
        runnable = new Runnable() {
            @Override
            public void run() {
                String randomNumber = String.valueOf(random.nextInt(100));
                balanceText.setText(randomNumber);
                Log.d("Result", randomNumber);

                if (future != null)
                    if (future.isDone()) {
                        try {
                            result = future.get();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        if (result != null) {
                            if (result.length() > 0)
                                dailyPNLText.setText(result);
                        } else dailyPNLText.setText("0!");
                    }

                handler.postDelayed(this, 2000);
            }
        };

        handler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class FetchJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];
            String result = null;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response from the API
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                inputStream.close();

                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the fetched JSON object here
            if (result != null) {
                Log.e("Result", result);
            }
        }
    }
}