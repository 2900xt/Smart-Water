package com.remindrop.smartwater.ui.home.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.remindrop.smartwater.R;
import com.remindrop.smartwater.Util;
import com.remindrop.smartwater.databinding.FragmentReminderBinding;
import com.remindrop.smartwater.ui.home.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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
        int downtimeStart = 0;
        int downtimeEnd = 0;
        boolean remindersEnabled = false;

        try {
            if(!JSON.has("reminderInterval"))
            {
                Util.getJSON().put("reminderInterval", 0);
            }
            reminderInterval = JSON.getInt("reminderInterval");

            if(!JSON.has("remindersEnabled"))
            {
                JSON.put("remindersEnabled", false);
            }
            remindersEnabled = JSON.getBoolean("remindersEnabled");

            if(!JSON.has("downtimeStart"))
            {
                JSON.put("downtimeStart", 12);
            }
            downtimeStart = JSON.getInt("downtimeStart");

            if(!JSON.has("downtimeEnd"))
            {
                JSON.put("downtimeEnd", 12);
            }
            downtimeEnd = JSON.getInt("downtimeEnd");

        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        reminderIntervalInput = binding.reminderIntervalInput;
        if(reminderInterval == 0)
        {
            reminderIntervalInput.setText("");
        }
        else {
            reminderIntervalInput.setText(String.valueOf(reminderInterval));
        }
        remindersEnableSwitch = binding.switchEnableReminders;
        remindersEnableSwitch.setChecked(remindersEnabled);
        remindersEnableSwitch.setOnClickListener(new OnClickListener(parent, this));


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.am_or_pm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAmOrPmStart.setAdapter(adapter);
        binding.spinnerAmOrPmEnd.setAdapter(adapter);

        if(downtimeStart > 12)
        {
            downtimeStart -= 12;
            binding.spinnerAmOrPmStart.setSelection(1);
        }

        if(downtimeEnd > 12)
        {
            downtimeEnd -= 12;
            binding.spinnerAmOrPmEnd.setSelection(1);
        }

        binding.reminderDowntimeStart.setText(String.valueOf(downtimeStart));
        binding.reminderDowntimeEnd.setText(String.valueOf(downtimeEnd));

        binding.buttonGoBack.setOnClickListener(new OnClickListener(parent, this));

        binding.reminderDowntimeStart.setText(String.valueOf(downtimeStart));
        binding.reminderDowntimeEnd.setText(String.valueOf(downtimeEnd));
        return binding.getRoot();
    }

    private class OnClickListener implements View.OnClickListener
    {
        private Fragment parent;
        private Fragment current;
        public OnClickListener(Fragment parent, Fragment current)
        {
            this.parent = parent;
            this.current = current;
        }
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.button_go_back)
            {
                Util.swapFragments(current, parent);
            }
        }
    }

    @Override
    public void onDestroyView() {

        try {
            Util.getJSON().put("remindersEnabled", remindersEnableSwitch.isChecked());
            Util.getJSON().put("reminderInterval", Integer.parseInt( "0" + Objects.requireNonNull(reminderIntervalInput.getText()).toString()));

            int downtimeStart = Integer.parseInt(Objects.requireNonNull(binding.reminderDowntimeStart.getText()).toString()),
                    downtimeEnd = Integer.parseInt(Objects.requireNonNull(binding.reminderDowntimeEnd.getText()).toString());

            if(binding.spinnerAmOrPmStart.getSelectedItem().equals("PM"))
            {
                downtimeStart += 12;
            }

            if(binding.spinnerAmOrPmEnd.getSelectedItem().equals("PM"))
            {
                downtimeEnd += 12;
            }


            Util.getJSON().put("downtimeStart", downtimeStart);
            Util.getJSON().put("downtimeEnd", downtimeEnd);
        } catch (JSONException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        super.onDestroyView();
    }
}
