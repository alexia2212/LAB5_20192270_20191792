package com.example.lab5_20192270_20191792;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab5_20192270_20191792.DTOs.BusquedaTrabajadoresDto;
import com.example.lab5_20192270_20191792.Entity.Countries;
import com.example.lab5_20192270_20191792.Entity.Departments;
import com.example.lab5_20192270_20191792.Entity.Employees;
import com.example.lab5_20192270_20191792.Entity.Locations;
import com.example.lab5_20192270_20191792.Entity.Regions;
import com.example.lab5_20192270_20191792.Trabajador.TrabajadorActivity;
import com.example.lab5_20192270_20191792.databinding.ActivityMainBinding;
import com.example.lab5_20192270_20191792.databinding.ActivityTrabajadorBinding;
import com.example.lab5_20192270_20191792.databinding.ActivityTutorBinding;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutorActivity extends AppCompatActivity {

    String channelId2 = "channelHighPriorTutor";

    ActivityTutorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        lanzarNotificacionInicio();

        binding = ActivityTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.descargaboton.setOnClickListener(view -> {

            Intent intent= new Intent(TutorActivity.this, ListaTrabajadoresActivity.class);
            startActivity(intent);
        });

        binding.buscartrabajador.setOnClickListener(view -> {

            Intent intent= new Intent(TutorActivity.this, BuscarTrabajadorActivity.class);
            startActivity(intent);
        });

        binding.asignartutoria.setOnClickListener(view -> {

            Intent intent= new Intent(TutorActivity.this, AsignarTutoriaActivity.class);
            startActivity(intent);
        });




    }

    public void lanzarNotificacionInicio(){
        Intent intent = new Intent(TutorActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(TutorActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TutorActivity.this, channelId2)
                .setSmallIcon(R.drawable.baseline_accessibility_24)
                .setContentTitle("Tutor")
                .setContentText("Está entrando en modo Tutor")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(TutorActivity.this);
        if (ActivityCompat.checkSelfPermission(TutorActivity.this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(1, builder.build());
        }
    }




}