package com.denisgruiax.blockchaintradingbot.ui.walletstatistics;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denisgruiax.blockchaintradingbot.databinding.FragmentWalletStatisticsBinding;
import com.denisgruiax.blockchaintradingbot.domains.BalanceFetchTask;

public class WalletStatisticsFragment extends Fragment implements BalanceFetchTask.BalanceUpdateListener {

    private FragmentWalletStatisticsBinding binding;
    private Handler handler;
    private TextView balanceText;
    private TextView dailyPNLText;
    private Button getBalance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WalletStatisticsModel walletStatisticsModel =
                new ViewModelProvider(this).get(WalletStatisticsModel.class);

        binding = FragmentWalletStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView balanceText = binding.balanceText;
        final TextView dailyPNLText= binding.dailyPNL;
        final Button getBalance = binding.button;

        walletStatisticsModel.getBalanceText().observe(getViewLifecycleOwner(), balanceText::setText);
        walletStatisticsModel.getDailyPNLText().observe(getViewLifecycleOwner(), dailyPNLText::setText);

        return root;
    }

    private void startFetchingBalance() {
        BalanceFetchTask balanceFetchTask = new BalanceFetchTask(this);
        balanceFetchTask.execute();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startFetchingBalance();
            }
        }, 5000);
    }

    @Override
    public void onBalanceUpdated(String balance) {
        balanceText.setText(balance);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}