package com.denisgruiax.blockchaintradingbot.ui.botstrategy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denisgruiax.blockchaintradingbot.databinding.FragmentBotStrategyBinding;

public class BotStrategyFragment extends Fragment {
    private FragmentBotStrategyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BotStrategyModel botStrategyModel =
                new ViewModelProvider(this).get(BotStrategyModel.class);

        binding = FragmentBotStrategyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        botStrategyModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}