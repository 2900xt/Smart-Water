package com.remindrop.smartwater.ui.deviceSettings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.polidea.rxandroidble3.RxBleDevice;
import com.remindrop.smartwater.Bluetooth;
import com.remindrop.smartwater.R;
import com.remindrop.smartwater.databinding.FragmentMyDeviceBinding;

import java.util.ArrayList;

public class MyDeviceFragment extends Fragment
{
    private FragmentMyDeviceBinding binding;
    private ArrayList<String> data;
    private RecyclerView.LayoutManager layoutMgr;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        data = new ArrayList<>();
        data.add("test!");

        for(RxBleDevice dev : Bluetooth.getBtDevices())
        {
            data.add(dev.getName());
        }

        Bluetooth.setOnBtDevFound(dev ->
        {
            Log.v("Bluetooth", "Device Found: " + dev.getName());
            data.add(dev.getName());
        });

        if(!Bluetooth.isScanning())
        {
            Bluetooth.startScan();
        }
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentMyDeviceBinding.inflate(inflater, container, false);
        recyclerView = binding.myDeviceBtConnections;

        RecyclerView rv = binding.myDeviceBtConnections;

        layoutMgr = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutMgr);

        rv.setAdapter(new BtDevicesListAdapter(data,
                string -> {
                    Log.v("My Device Fragment", "Trying to connect to device: " + string);
                }
        ));

        AppCompatTextView title = requireActivity().findViewById(R.id.text_title_bar);

        if(title == null)
        {
            Log.e("My Device Fragment", "Unable to edit title text");
        }
        else
        {
            title.setText("My Device");
        }

        return binding.getRoot();
    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        if(Bluetooth.isScanning())
        {
            Bluetooth.stopScan();
        }
    }
}