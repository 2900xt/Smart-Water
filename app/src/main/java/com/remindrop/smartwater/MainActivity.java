package com.remindrop.smartwater;

import static java.lang.Double.NaN;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.remindrop.smartwater.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.net.URI;
import java.time.LocalTime;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.JSONInit(getApplicationContext());

        ReminderService.restartService(this);

        if(getSystemService(NotificationManager.class).getNotificationChannel("com.remindrop.smartwater") == null)
        {
            Util.createNotificationChannel(this);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_goal, R.id.navigation_consumption)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    protected void onDestroy()
    {
        Util.JSONSave();
        super.onDestroy();
    }

    @Override
    protected void onStop()
    {
        Util.JSONSave();
        super.onStop();
    }

    @Override
    protected void onPause()
    {
        Util.JSONSave();
        super.onPause();
    }

}