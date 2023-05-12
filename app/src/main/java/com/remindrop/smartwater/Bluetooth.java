package com.remindrop.smartwater;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.polidea.rxandroidble3.RxBleClient;
import com.polidea.rxandroidble3.scan.ScanResult;
import com.polidea.rxandroidble3.scan.ScanSettings;

import java.util.ArrayList;
import java.util.HashSet;

import io.reactivex.rxjava3.disposables.Disposable;

public class Bluetooth
{
    private AppCompatActivity mainActivity;
    private Disposable scanDisposable;
    private boolean isScanning;
    private RxBleClient rxBleClient;
    private ArrayList<ScanResult> btDevices;
    public Bluetooth(AppCompatActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        Intent requestBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Bluetooth", "Error: Unable to acquire bluetooth permissions");
            return;
        }

        mainActivity.startActivityForResult(requestBluetoothEnable, 1);
        rxBleClient = RxBleClient.create(mainActivity);

        btDevices = new ArrayList<>();
        isScanning = false;

        startScan();
    }

    public void startScan()
    {
        isScanning = true;
        scanDisposable = rxBleClient.scanBleDevices(
                    new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build()
            )
            .doFinally(() -> scanDisposable.dispose())
            .subscribe(
                    scanResult -> {
                        for(int i = 0; i < btDevices.size(); i++)
                        {
                            if(btDevices.get(i).getBleDevice().equals(scanResult.getBleDevice())) {
                                btDevices.set(i, scanResult);
                                return;
                            }
                        }

                        btDevices.add(scanResult);
                    },
                    throwable -> {
                        throwable.printStackTrace();
                        Log.e("Bluetooth", "Unable to scan devices!");
                    }
            );
    }

    public void stopScan()
    {
        isScanning = false;
        scanDisposable.dispose();
    }

    public ArrayList<ScanResult> getBtDevices()
    {
        return btDevices;
    }


}
