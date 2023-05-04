package com.remindrop.smartwater.ui.goal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.remindrop.smartwater.R;
import com.remindrop.smartwater.databinding.FragmentWatarGoalBinding;

public class WaterGoalFragment extends Fragment {

    private FragmentWatarGoalBinding binding;
    private WaterGoalViewModel waterGoalViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WaterGoalViewModel waterGoalViewModel =
                new ViewModelProvider(this).get(WaterGoalViewModel.class);

        binding = FragmentWatarGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        waterGoalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.goalTextExcercize.setOnClickListener(new OnKeyPress());
        binding.goalTextWeight.setOnClickListener(new OnKeyPress());

        return root;
    }

    private class OnKeyPress implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            waterGoalViewModel.calculateOptimalGoal(
                    Double.parseDouble(binding.goalTextExcercize.getText().toString()),
                    Double.parseDouble(binding.goalTextWeight.getText().toString())
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}