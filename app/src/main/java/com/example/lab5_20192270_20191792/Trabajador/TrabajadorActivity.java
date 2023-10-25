package com.example.lab5_20192270_20191792.Trabajador;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.lab5_20192270_20191792.DTOs.EmployeeRepo;
import com.example.lab5_20192270_20191792.MainActivity;
import com.example.lab5_20192270_20191792.R;
import com.example.lab5_20192270_20191792.databinding.ActivityTrabajadorBinding;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorActivity extends AppCompatActivity {

    String channelId = "channelHighPriorTutor";
    ActivityTrabajadorBinding binding;
    String id;
    EmployeeRepo employeeRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        lanzarNotificacionInicio();

        binding.button7.setOnClickListener(view -> {

            Intent intent = new Intent(TrabajadorActivity.this, DescargarHorarioTrabajador.class);
            startActivity(intent);
        });
    }

    public void lanzarNotificacionInicio(){
        Intent intent = new Intent(TrabajadorActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(TrabajadorActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TrabajadorActivity.this, channelId)
                .setSmallIcon(R.drawable.baseline_accessibility_24)
                .setContentTitle("Empleado")
                .setContentText("Está entrando en modo Empleado")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(TrabajadorActivity.this);
        if (ActivityCompat.checkSelfPermission(TrabajadorActivity.this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(1, builder.build());
        }
    }

    public void createRetrofitService(){
        employeeRepo = new Retrofit.Builder()
                .baseUrl("https://8r7fm6zj-3010.brs.devtunnels.ms")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmployeeRepo.class);

    }
}