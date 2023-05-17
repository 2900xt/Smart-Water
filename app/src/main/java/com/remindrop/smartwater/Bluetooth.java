package com.remindrop.smartwater;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.polidea.rxandroidble3.RxBleClient;
import com.polidea.rxandroidble3.RxBleDevice;
import com.polidea.rxandroidble3.scan.ScanResult;
import com.polidea.rxandroidble3.scan.ScanSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

import io.reactivex.rxjava3.disposables.Disposable;

public class Bluetooth
{
    private static AppCompatActivity mainActivity;
    private static Disposable scanDisposable;
    private static boolean scanning;
    private static RxBleClient rxBleClient;
    private static ArrayList<RxBleDevice> btDevices;
    private static Consumer<RxBleDevice> onBtDevFound;
    public static void init(AppCompatActivity activity)
    {
        mainActivity = activity;
        Intent requestBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Bluetooth", "Error: Unable to acquire bluetooth permissions");
            return;
        }

        mainActivity.startActivityForResult(requestBluetoothEnable, 1);
        rxBleClient = RxBleClient.create(mainActivity);

        btDevices = new ArrayList<>();
        onBtDevFound = rxBleDevice -> {};
        scanning = false;
    }

    public static void startScan()
    {
        if(scanning) return;

        scanning = true;
        scanDisposable = rxBleClient.scanBleDevices(
                    new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build()
            )
            .doFinally(() -> scanDisposable.dispose())
            .subscribe(
                    scanResult -> {

                        RxBleDevice dev = scanResult.getBleDevice();

                        if(dev.getName() == null) return;

                        int index = btDevices.indexOf(dev);

                        if(index != -1)
                        {
                            btDevices.set(index, dev);
                            return;
                        }

                        btDevices.add(dev);
                        onBtDevFound.accept(dev);
                    },
                    throwable -> {
                        throwable.printStackTrace();
                        Log.e("Bluetooth", "Unable to scan devices!");
                    }
            );
    }

    public static void stopScan()
    {
        if(!scanning) return;

        scanning = false;
        scanDisposable.dispose();
    }

    public static ArrayList<RxBleDevice> getBtDevices()
    {
        return btDevices;
    }

    public static boolean isScanning()
    {
        return scanning;
    }

    public static void setOnBtDevFound(Consumer<RxBleDevice> btDevFound)
    {
        onBtDevFound = btDevFound;
    }

    public static void resetAdapter()
    {
        btDevices.clear();

        if(scanning)
        {
            stopScan();
        }
    }


}
