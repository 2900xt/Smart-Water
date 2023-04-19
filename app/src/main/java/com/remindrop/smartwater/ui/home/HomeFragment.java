package com.remindrop.smartwater.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.remindrop.smartwater.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setData(0, 60, 100);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textCurrentDate = binding.textDate;
        homeViewModel.getDateText().observe(getViewLifecycleOwner(), textCurrentDate::setText);

        final TextView textWaterDrank = binding.textWaterDrank;
        homeViewModel.getWaterDrankText().observe(getViewLifecycleOwner(), textWaterDrank::setText);

        final TextView textNextRem = binding.textNextRem;
        homeViewModel.getNextRemText().observe(getViewLifecycleOwner(), textNextRem::setText);

        final ProgressBar waterProgressBar = binding.waterProgBar;
        waterProgressBar.setAnimation(null);
        homeViewModel.getProgress().observe(getViewLifecycleOwner(), waterProgressBar::setProgress);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}