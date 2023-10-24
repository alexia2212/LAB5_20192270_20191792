package com.example.lab5_20192270_20191792;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorActivity extends AppCompatActivity {

    String channelId2 = "channelHighPriorTutor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        lanzarNotificacionInicio();



        Button botondescargar = findViewById(R.id.button7);
        botondescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, ListaTrabajadoresActivity.class);
                startActivity(intent);
            }
        });

        Button botonbuscar = findViewById(R.id.button8);
        botonbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, BuscarTrabajadorActivity.class);
                startActivity(intent);
            }
        });
        Button botonasignar = findViewById(R.id.button9);
        botonasignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, AsignarTutoriaActivity.class);
                startActivity(intent);
            }
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