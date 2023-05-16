package com.denisgruiax.blockchaintradingbot.ui.others;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denisgruiax.blockchaintradingbot.databinding.FragmentOthersBinding;

public class OthersFragment extends Fragment {

    private FragmentOthersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OthersViewModel slideshowViewModel =
                new ViewModelProvider(this).get(OthersViewModel.class);

        binding = FragmentOthersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}