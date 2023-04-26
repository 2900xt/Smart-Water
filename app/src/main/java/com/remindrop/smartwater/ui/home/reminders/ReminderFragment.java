package com.remindrop.smartwater.ui.home.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.remindrop.smartwater.databinding.FragmentHomeBinding;
import com.remindrop.smartwater.databinding.FragmentReminderBinding;
import com.remindrop.smartwater.ui.home.HomeViewModel;

public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;
    private Fragment parent;
    private HomeViewModel viewModel;

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

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
