package com.example.lab5_20192270_20191792.Trabajador;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20192270_20191792.DTOs.EmployeeRepo;
import com.example.lab5_20192270_20191792.Entity.TrabajadorTutoria;
import com.example.lab5_20192270_20191792.MainActivity;
import com.example.lab5_20192270_20191792.R;
import com.example.lab5_20192270_20191792.databinding.ActivityTrabajadorBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrabajadorActivity extends AppCompatActivity {

    String channelId = "channelHighPriorTutor";
    ActivityTrabajadorBinding binding;
    String id;
    String TAG = "msg-test";
    String salida;
    EmployeeRepo employeeRepo = new Retrofit.Builder()
            .baseUrl("http://")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(EmployeeRepo.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lanzarNotificacionInicio();
        canalesNotificacion();
        binding.button7.setOnClickListener(view -> {
            id = String.valueOf(binding.TextEditID.getText());
            //validación
            if(id.length()>0){
                gestionarTurorias(id);
            }else{
                Toast.makeText(this, "Ingrese un codigo", Toast.LENGTH_SHORT).show();
            }
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

    public void canalesNotificacion(){
        NotificationChannel canal = null;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            canal = new NotificationChannel(channelId, "trabajador", NotificationManager.IMPORTANCE_HIGH);
            canal.setDescription("TRABAJADOR CHANNEL");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(canal);
        }
    }


    public void gestionarTurorias(String id){
        employeeRepo.getFechaTutoria(Integer.parseInt(id)).enqueue(new Callback<TrabajadorTutoria>() {
            @Override
            public void onResponse(Call<TrabajadorTutoria> call, Response<TrabajadorTutoria> response) {
                salida = response.body().getTutoria();

                if (salida != null){
                    notifyImportanceDefault();
                    Intent intent = new Intent(TrabajadorActivity.this, DescargarHorarioTrabajador.class);
                    intent.putExtra("hr", salida);
                    intent.putExtra("codigo", id);
                    startActivity(intent);
                }else{
                    System.out.println(response.body().getTutoria());
                    Toast.makeText(TrabajadorActivity.this, "No cuenta con tutorias",  Toast.LENGTH_SHORT).show();                }
            }

            @Override
            public void onFailure(Call<TrabajadorTutoria> call, Throwable t) {
                Log.d(TAG,"error");
                Log.d(TAG, t.getMessage());
            }
        });
    }

    public void notifyImportanceDefault(){
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Trabajador")
                .setSmallIcon(R.drawable.baseline_accessibility_24)
                .setContentText("Usted acaba de ingresar al flujo de Trabajador")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification1 = builder2.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if(ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(1, notification1);

        }

        if(!salida.equalsIgnoreCase("No presenta ninguna tutoría")){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime horario = LocalDateTime.parse(salida,dateTimeFormatter);
                LocalDateTime currentTime = LocalDateTime.now();
                if(horario.isAfter(currentTime)){
                    NotificationCompat.Builder builder3 = new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.baseline_accessibility_24)
                            .setContentTitle("Trabajador")
                            .setContentText("Tiene tutoria agendada: "+salida)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);
                    Notification notification2 = builder3.build();

                    if(ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                        notificationManagerCompat.notify(2, notification2);
                    }

                }
            }
        }
    }

}