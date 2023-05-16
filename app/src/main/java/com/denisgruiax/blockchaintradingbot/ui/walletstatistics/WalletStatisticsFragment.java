package com.denisgruiax.blockchaintradingbot.ui.walletstatistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denisgruiax.blockchaintradingbot.databinding.FragmentWalletStatisticsBinding;

public class WalletStatisticsFragment extends Fragment {

    private FragmentWalletStatisticsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WalletStatisticsModel walletStatisticsModel =
                new ViewModelProvider(this).get(WalletStatisticsModel.class);

        binding = FragmentWalletStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView balanceText = binding.balanceText;
        final TextView dailyPNLText= binding.dailyPNL;
        final Button getBalance = binding.button;

        walletStatisticsModel.getText().observe(getViewLifecycleOwner(), balanceText::setText);
        walletStatisticsModel.getText().observe(getViewLifecycleOwner(), dailyPNLText::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}