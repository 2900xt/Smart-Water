package com.remindrop.smartwater;

import android.app.NotificationManager;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import com.remindrop.smartwater.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static AppCompatImageButton profileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.JSONInit(getApplicationContext());

        ReminderService.restartService(this);

        if (getSystemService(NotificationManager.class).getNotificationChannel("com.remindrop.smartwater") == null) {
            Util.createNotificationChannel(this);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        profileButton = findViewById(R.id.device_connection_settings);
        System.out.println(profileButton);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_goal, R.id.navigation_consumption)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Bluetooth.init(this);
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