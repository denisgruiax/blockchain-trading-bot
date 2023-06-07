package com.denisgruiax.blockchaintradingbot.application.mainactivity.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denisgruiax.blockchaintradingbot.data.remote.coingeckoapi.fetchprice.FetchListOfPrices;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentChartsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.denisgruiax.blockchaintradingbot.utils.CryptoId;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class ChartsFragment extends Fragment {

    private FragmentChartsBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ExecutorService executorService = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    private Runnable runnable;
    Future<List<Double>> prices;
    private BarChart chart;

    private Button bitcoin;
    private Button ethereum;
    private Button multiversx;
    private Button polkadot;
    private Button internetComputer;

    private TextView pair;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChartsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(ChartsViewModel.class);

        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeUI();

        pair.setText("BTC/USDT");
        fetchPricesForChart(CryptoId.bitcoin, "usd", "Bitcoin Price (USD)");

        setOnClickListenerS();
        updateInterface();

        return root;
    }

    private void initializeUI() {
        chart = binding.chart;
        pair = binding.pair;
        bitcoin = binding.bitcoin;
        ethereum = binding.ethereum;
        multiversx = binding.multiversX;
        polkadot = binding.polkadot;
        internetComputer = binding.internetComputer;
    }

    public void fetchPricesForChart(String cryptoId, String currency, String label){
        prices = executorService.submit(new FetchListOfPrices(cryptoId, currency));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (prices.isDone()){
            try {
                List<Double> listOfPrices = prices.get();

                ArrayList<BarEntry> entries = new ArrayList<>();

                for (int i = 0; i < listOfPrices.size(); i++) {
                    entries.add(new BarEntry((float)i, listOfPrices.get(i).floatValue()));
                }

                BarDataSet dataSet = new BarDataSet(entries, label);
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueTextSize(12f);

                BarData barData = new BarData(dataSet);
                chart.setData(barData);
                chart.setFitBars(true);
                chart.getDescription().setEnabled(false);

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                YAxis yAxis = chart.getAxisLeft();
                yAxis.setGranularity(1f);
                yAxis.setGranularityEnabled(true);

                chart.getAxisRight().setEnabled(false);

                chart.animateY(1000);
                chart.invalidate();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setBitcoinOnClickListener() {
        bitcoin.setOnClickListener(view -> {
            pair.setText("BTC/USDT");
            fetchPricesForChart(CryptoId.bitcoin, "usd", "Bitcoin Price (USD)");
        });
    }

    public void setEthereumOnClickListener() {
        ethereum.setOnClickListener(view -> {
            pair.setText("ETH/USDT");
            fetchPricesForChart(CryptoId.ethereum, "usd","Ethereum Price (USD)");
        });
    }

    public void setMultiversxOnClickListener() {
        multiversx.setOnClickListener(view -> {
            pair.setText("EGLD/USDT");
            fetchPricesForChart(CryptoId.multiversX, "usd","MultiversX Price (USD)");
        });
    }

    public void setPolkadotOnClickListener() {
        polkadot.setOnClickListener(view -> {
            pair.setText("DOT/USDT");
            fetchPricesForChart(CryptoId.polkadot, "usd", "Polkadot Price (USD)");
        });
    }

    public void setInternetComputerOnClickListener() {
        internetComputer.setOnClickListener(view -> {
            pair.setText("ICP/USDT");
            fetchPricesForChart(CryptoId.internetComputer, "usd", "Internet-Computer Price (USD)");
        });
    }

    public void setOnClickListenerS(){
        setBitcoinOnClickListener();
        setEthereumOnClickListener();
        setMultiversxOnClickListener();
        setPolkadotOnClickListener();
        setInternetComputerOnClickListener();
    }

    public void updateInterface() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(this, 1000);
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