package com.denisgruiax.blockchaintradingbot.ui.others;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.denisgruiax.blockchaintradingbot.databinding.FragmentOthersBinding;
import com.denisgruiax.blockchaintradingbot.keys.Keys;

import java.util.List;

public class OthersFragment extends Fragment {

    private FragmentOthersBinding binding;
    private TextView textView;
    private List<AssetBalance> assetBalance;
    private Handler handler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OthersViewModel slideshowViewModel =
                new ViewModelProvider(this).get(OthersViewModel.class);

        binding = FragmentOthersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.textOthers;

        final TextView textView = binding.textOthers;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        updateInterface();

        return root;
    }

    public void updateInterface(){
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