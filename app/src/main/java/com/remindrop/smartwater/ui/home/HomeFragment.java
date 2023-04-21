package com.remindrop.smartwater.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.remindrop.smartwater.R;
import com.remindrop.smartwater.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LayoutInflater inflater;
    private ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setData(0, 60, 100);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textCurrentDate = binding.textDate;
        homeViewModel.getDateText().observe(getViewLifecycleOwner(), textCurrentDate::setText);

        final TextView textWaterDrank = binding.textWaterDrank;
        textWaterDrank.setTextSize(20);
        homeViewModel.getWaterDrankText().observe(getViewLifecycleOwner(), textWaterDrank::setText);

        final TextView textNextRem = binding.textNextRem;
        homeViewModel.getNextRemText().observe(getViewLifecycleOwner(), textNextRem::setText);

        final ImageView progressWheel = binding.waterProgressBar;
        final WaterProgBar waterProgressWheel = (WaterProgBar) progressWheel.getDrawable();
        waterProgressWheel.setProgress(10);
        waterProgressWheel.setColor(
                ContextCompat.getColor(this.requireContext(), R.color.normal_blue),
                ContextCompat.getColor(this.requireContext(), R.color.lighter_blue)
        );

        ClickListener input = new ClickListener();

        binding.imageBellIcon.setOnClickListener(input);
        binding.imageLogWater.setOnClickListener(input);

        return root;
    }

    private void swapFragments(Fragment newFragment)
    {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, newFragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    private class ClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            Fragment newFragment = null;
            switch(view.getId())
            {
                case R.id.image_log_water:
                    newFragment = new LogWaterFragment();
                    break;
                case R.id.image_bell_icon:
                    newFragment = new ReminderFragment();
                    break;
            }

            if(newFragment != null)
            {
                swapFragments(newFragment);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}