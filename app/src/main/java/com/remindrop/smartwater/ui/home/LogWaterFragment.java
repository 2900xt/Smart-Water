package com.remindrop.smartwater.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.remindrop.smartwater.R;
import com.remindrop.smartwater.databinding.FragmentLogWaterBinding;

public class LogWaterFragment extends Fragment {

    private FragmentLogWaterBinding binding;
    private MutableLiveData<String> amountWater;
    private StringBuilder keypadBuffer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        binding = FragmentLogWaterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final OnKeyPressed clickListener = new OnKeyPressed();
        keypadBuffer = new StringBuilder();

        binding.buttonLogWater0.setOnClickListener(clickListener);
        binding.buttonLogWater1.setOnClickListener(clickListener);
        binding.buttonLogWater2.setOnClickListener(clickListener);
        binding.buttonLogWater3.setOnClickListener(clickListener);
        binding.buttonLogWater4.setOnClickListener(clickListener);
        binding.buttonLogWater5.setOnClickListener(clickListener);
        binding.buttonLogWater6.setOnClickListener(clickListener);
        binding.buttonLogWater7.setOnClickListener(clickListener);
        binding.buttonLogWater8.setOnClickListener(clickListener);
        binding.buttonLogWater9.setOnClickListener(clickListener);
        binding.buttonLogWaterDel.setOnClickListener(clickListener);

        final TextView waterLoggedText = binding.textAmountLogged;
        amountWater = new MutableLiveData<>();
        amountWater.observe(getViewLifecycleOwner(), waterLoggedText::setText);

        return root;
    }

    private class OnKeyPressed implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            switch(view.getId())
            {
                case R.id.button_log_water_0:
                    keypadBuffer.append(0);
                    break;
                case R.id.button_log_water_del:
                    keypadBuffer.deleteCharAt(keypadBuffer.length() - 1);
                    break;
            }

            amountWater.setValue(keypadBuffer.toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
