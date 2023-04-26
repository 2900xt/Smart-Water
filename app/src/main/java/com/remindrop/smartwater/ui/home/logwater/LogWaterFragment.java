package com.remindrop.smartwater.ui.home.logwater;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.remindrop.smartwater.R;
import com.remindrop.smartwater.Util;
import com.remindrop.smartwater.databinding.FragmentLogWaterBinding;
import com.remindrop.smartwater.ui.home.HomeViewModel;


public class LogWaterFragment extends Fragment {

    private FragmentLogWaterBinding binding;
    private MutableLiveData<String> amountWater;
    private StringBuilder keypadBuffer;
    private Fragment parent;
    private HomeViewModel viewModel;
    private Button addWaterButton;

    public LogWaterFragment()
    {

    }

    public LogWaterFragment(Fragment parent, HomeViewModel viewModel)
    {
        this.parent = parent;
        this.viewModel = viewModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        binding = FragmentLogWaterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final OnKeyPressed clickListener = new OnKeyPressed(parent, this);

        keypadBuffer = new StringBuilder();
        amountWater = new MutableLiveData<>("_ oz");
        final TextView waterLoggedText = binding.textAmountLogged;
        amountWater.observe(getViewLifecycleOwner(), waterLoggedText::setText);

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

        binding.buttonAddWater.setOnClickListener(clickListener);
        addWaterButton = binding.buttonAddWater;

        return root;
    }

    private class OnKeyPressed implements View.OnClickListener
    {
        private Fragment parent;
        private Fragment current;
        public OnKeyPressed(Fragment parent, Fragment current)
        {
            this.parent = parent;
            this.current = current;
        }
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch(view.getId())
            {
                case R.id.button_log_water_0:
                    keypadBuffer.append(0);
                    break;
                case R.id.button_log_water_1:
                    keypadBuffer.append(1);
                    break;
                case R.id.button_log_water_2:
                    keypadBuffer.append(2);
                    break;
                case R.id.button_log_water_3:
                    keypadBuffer.append(3);
                    break;
                case R.id.button_log_water_4:
                    keypadBuffer.append(4);
                    break;
                case R.id.button_log_water_5:
                    keypadBuffer.append(5);
                    break;
                case R.id.button_log_water_6:
                    keypadBuffer.append(6);
                    break;
                case R.id.button_log_water_7:
                    keypadBuffer.append(7);
                    break;
                case R.id.button_log_water_8:
                    keypadBuffer.append(8);
                    break;
                case R.id.button_log_water_9:
                    keypadBuffer.append(9);
                    break;
                case R.id.button_log_water_del:
                    if(keypadBuffer.length() != 0)
                    {
                        keypadBuffer.deleteCharAt(keypadBuffer.length() - 1);
                    }
                    break;
                case R.id.button_add_water:
                    keypadBuffer.insert(0, "0");
                    int amount = Integer.parseInt(keypadBuffer.toString());
                    viewModel.addWaterDrank(amount);
                    Util.swapFragments(current, parent);
                    break;
            }

            if(keypadBuffer.length() == 0)
            {
                addWaterButton.setText("Go Back");
            } else
            {
                addWaterButton.setText("Add Water");
            }

            amountWater.setValue(keypadBuffer.toString() + "_ oz");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
