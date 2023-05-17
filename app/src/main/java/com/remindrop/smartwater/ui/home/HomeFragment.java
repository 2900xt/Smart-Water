package com.remindrop.smartwater.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.remindrop.smartwater.MainActivity;
import com.remindrop.smartwater.R;
import com.remindrop.smartwater.Util;
import com.remindrop.smartwater.databinding.FragmentHomeBinding;
import com.remindrop.smartwater.ui.deviceSettings.MyDeviceFragment;
import com.remindrop.smartwater.ui.home.logwater.LogWaterFragment;
import com.remindrop.smartwater.ui.home.reminders.ReminderFragment;

import org.json.JSONException;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private void init() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //We don't want to recreate the view-model every time we refresh

        if(homeViewModel == null)
        {
            init();
        }

        try {
            homeViewModel.setData();
        } catch (JSONException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textCurrentDate = binding.textDate;
        homeViewModel.getDateText().observe(getViewLifecycleOwner(), textCurrentDate::setText);

        final TextView textWaterDrank = binding.textWaterDrank;
        textWaterDrank.setTextSize(20);
        homeViewModel.getWaterDrankText().observe(getViewLifecycleOwner(), textWaterDrank::setText);

        final ImageView progressWheel = binding.waterProgressBar;
        final WaterProgBar waterProgressWheel = (WaterProgBar) progressWheel.getDrawable();
        waterProgressWheel.setProgress(homeViewModel.getProgress().getValue());
        waterProgressWheel.setColor(
                ContextCompat.getColor(this.requireContext(), R.color.normal_blue),
                ContextCompat.getColor(this.requireContext(), R.color.lighter_blue)
        );

        ClickListener input = new ClickListener(this);

        binding.imageBellIcon.setOnClickListener(input);
        binding.imageLogWater.setOnClickListener(input);


        AppCompatTextView title = requireActivity().findViewById(R.id.text_title_bar);

        if(title == null)
        {
            Log.e("Home Fragment", "Unable to edit title text");
        }
        else {
            title.setText("Dashboard");
        }

        HomeFragment fragment = this;

        new Thread(() -> {
            while(MainActivity.profileButton == null);
            MainActivity.profileButton.setOnClickListener(v -> Util.swapFragments(fragment, new MyDeviceFragment()));
        }).start();

        return root;
    }



    private class ClickListener implements View.OnClickListener
    {
        private Fragment parent;
        public ClickListener(Fragment parent)
        {
            this.parent = parent;
        }
        @Override
        public void onClick(View view) {
            Fragment newFragment;
            switch(view.getId())
            {
                case R.id.image_log_water:
                    newFragment = new LogWaterFragment(parent, homeViewModel);
                    break;
                case R.id.image_bell_icon:
                    newFragment = new ReminderFragment(parent, homeViewModel);
                    break;
                default:
                    newFragment = null;
            }

            if(newFragment != null)
            {
                Util.swapFragments(parent, newFragment);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}