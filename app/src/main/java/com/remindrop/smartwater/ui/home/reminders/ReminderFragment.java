package com.remindrop.smartwater.ui.home.reminders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.remindrop.smartwater.R;
import com.remindrop.smartwater.Util;
import com.remindrop.smartwater.databinding.FragmentHomeBinding;
import com.remindrop.smartwater.databinding.FragmentReminderBinding;
import com.remindrop.smartwater.ui.home.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;
    private Fragment parent;
    private HomeViewModel viewModel;
    private TextInputEditText reminderIntervalInput;
    private SwitchMaterial remindersEnableSwitch;

    public ReminderFragment()
    {

    }

    public ReminderFragment(Fragment parent, HomeViewModel viewModel)
    {
        this.parent = parent;
        this.viewModel = viewModel;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        binding = FragmentReminderBinding.inflate(inflater, container, false);

        JSONObject JSON = Util.getJSON();
        int reminderInterval = 0;
        boolean remindersEnabled = false;
        try {
            if(!JSON.has("reminderInterval"))
            {
                Util.getJSON().put("reminderInterval", 600);
            }
            reminderInterval = JSON.getInt("reminderInterval");

            if(!JSON.has("remindersEnabled"))
            {
                JSON.put("remindersEnabled", false);
            }
            remindersEnabled = JSON.getBoolean("remindersEnabled");
        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        reminderIntervalInput = binding.reminderIntervalInput;
        reminderIntervalInput.setText(String.valueOf(reminderInterval));

        remindersEnableSwitch = binding.switchEnableReminders;
        remindersEnableSwitch.setChecked(remindersEnabled);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {

        try {
            Util.getJSON().put("remindersEnabled", remindersEnableSwitch.isChecked());
            Util.getJSON().put("reminderInterval", Integer.parseInt(reminderIntervalInput.getText().toString()));
        } catch (JSONException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        super.onDestroyView();
    }
}
