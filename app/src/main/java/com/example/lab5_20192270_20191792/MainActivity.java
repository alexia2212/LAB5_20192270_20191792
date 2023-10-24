package com.example.lab5_20192270_20191792;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lab5_20192270_20191792.Trabajador.TrabajadorActivity;
import com.example.lab5_20192270_20191792.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        createNotificationChannelTrabajador();
        createNotificationChannelTutor();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonTrabajador.setOnClickListener(view -> {

            Intent intent= new Intent(MainActivity.this, TrabajadorActivity.class);
            startActivity(intent);
        });

        binding.buttonTutor.setOnClickListener(view -> {

            Intent intent= new Intent(MainActivity.this, TutorActivity.class);
            startActivity(intent);
        });




    }


    String channelId = "channelHighPriorTrabajador";
    String channelId2 = "channelHighPriorTutor";

    public void createNotificationChannelTrabajador(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel =new NotificationChannel(channelId, "Canal notificaciones High", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para notificaciones con prioridad Alta");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }

    public void createNotificationChannelTutor(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel =new NotificationChannel(channelId2, "Canal notificaciones High", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para notificaciones con prioridad Alta");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }

    public void askPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }
}