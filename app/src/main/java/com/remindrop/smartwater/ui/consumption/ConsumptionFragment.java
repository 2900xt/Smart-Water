package com.remindrop.smartwater.ui.consumption;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.remindrop.smartwater.databinding.FragmentConsumptionBinding;

public class ConsumptionFragment extends Fragment {

    private FragmentConsumptionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConsumptionViewModel consumptionViewModel =
                new ViewModelProvider(this).get(ConsumptionViewModel.class);

        binding = FragmentConsumptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textConsumption;
        consumptionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}