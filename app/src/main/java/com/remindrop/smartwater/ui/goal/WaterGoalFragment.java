package com.remindrop.smartwater.ui.goal;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.remindrop.smartwater.R;
import com.remindrop.smartwater.Util;
import com.remindrop.smartwater.databinding.FragmentWatarGoalBinding;

import org.json.JSONException;

public class WaterGoalFragment extends Fragment {

    private FragmentWatarGoalBinding binding;
    private WaterGoalViewModel waterGoalViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WaterGoalViewModel waterGoalViewModel =
                new ViewModelProvider(this).get(WaterGoalViewModel.class);

        binding = FragmentWatarGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button textView = binding.buttonSave;
        waterGoalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        OnKeyPress keyPressListener = new OnKeyPress(waterGoalViewModel);

        textView.setOnClickListener(keyPressListener);
        binding.goalTextExcercize.setOnClickListener(keyPressListener);
        binding.goalTextWeight.setOnClickListener(keyPressListener);

        AppCompatTextView title = (AppCompatTextView) getActivity().findViewById(R.id.text_title_bar);
        title.setText("Water Goal");

        return root;
    }

    private class OnKeyPress implements View.OnClickListener
    {
        private WaterGoalViewModel waterGoalViewModel;
        public OnKeyPress(WaterGoalViewModel waterGoalViewModel)
        {
            this.waterGoalViewModel = waterGoalViewModel;
        }
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.button_save)
            {
                try {
                    Util.getJSON().put("waterGoal", waterGoalViewModel.optimalGoal);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            waterGoalViewModel.calculateOptimalGoal(
                    Double.parseDouble("0" + binding.goalTextExcercize.getText().toString()),
                    Double.parseDouble("0" + binding.goalTextWeight.getText().toString())
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}