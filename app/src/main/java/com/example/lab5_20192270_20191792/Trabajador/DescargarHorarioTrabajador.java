package com.example.lab5_20192270_20191792.Trabajador;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.lab5_20192270_20191792.DTOs.EmployeeRepo;
import com.example.lab5_20192270_20191792.DTOs.UsuarioValidarDTO;
import com.example.lab5_20192270_20191792.R;
import com.example.lab5_20192270_20191792.databinding.ActivityDescargarHorarioTrabajadorBinding;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescargarHorarioTrabajador extends AppCompatActivity {

    EmployeeRepo employeeRepo;
    String id;
    String hr;
    Boolean hayTuto;
    String TAG= "msg-test";
    ActivityDescargarHorarioTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDescargarHorarioTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createRetrofitService();
        createNotificationChannelTrabajador();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d(TAG, "Id ingresado: "+id);
        hr = intent.getStringExtra("hr");
        Log.d(TAG, "HR: "+hr);

        //En caso el trabajor no tenga ninguna tutoría
        if(hr.equalsIgnoreCase("No hay ninguna asesoría")){
            binding.buttonDescargarHorario.setVisibility(View.GONE);
            binding.editTextTextMultiLineFeedback.setVisibility(View.GONE);
            hayTuto = false;
        }else{
            hayTuto = true;
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");
                LocalDateTime horario1 = LocalDateTime.parse(hr,formatter);
                LocalDateTime currentDateTime = LocalDateTime.now();
                if(horario1.isAfter(currentDateTime)){
                    binding.editTextTextMultiLineFeedback.setVisibility(View.GONE);
                    binding.button9.setVisibility(View.GONE);
                }
            }
        }
        binding.buttonDescargarHorario.setOnClickListener(view -> {
            if(hayTuto){
                String permiss = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(this, permiss) == PackageManager.PERMISSION_GRANTED){

                    downloadImagen();

                }else{
                    launcher.launch(permiss);
                }

            }else {
                notifyImportanceDefault();
            }

        });

        binding.button9.setOnClickListener(view -> {
            String feedback = String.valueOf(binding.editTextTextMultiLineFeedback.getText());
            String encodeFeedback = feedback.replace("", "_");
            sendFeedback(encodeFeedback,id);
        });

    }

    public void createRetrofitService(){
        employeeRepo = new Retrofit.Builder()
                .baseUrl("https://8r7fm6zj-3010.brs.devtunnels.ms")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(EmployeeRepo.class);

    }
    public void sendFeedback(String feedback, String id){
        employeeRepo.postFeedback(Integer.parseInt(id), feedback).enqueue(new Callback<UsuarioValidarDTO>() {
            @Override
            public void onResponse(Call<UsuarioValidarDTO> call, Response<UsuarioValidarDTO> response) {
                Log.d("msg-test", "Se envio de manera exiosa");
            }

            @Override
            public void onFailure(Call<UsuarioValidarDTO> call, Throwable t) {
                Log.d("msg-test", t.getMessage());
            }
        });
    }

    String channelId = "channelHighPriorTrabajador";
    public void askPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(DescargarHorarioTrabajador.this, new String[]{POST_NOTIFICATIONS}, 101);
        }
    }
    public void createNotificationChannelTrabajador(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel =new NotificationChannel(channelId, "Canal notificaciones High", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para notificaciones con prioridad Alta");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }
    public void notifyImportanceDefault(){
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Trabajador sin Tutorias")
                .setSmallIcon(R.drawable.baseline_accessibility_24)
                .setContentText("Aún no cuenta con tutorias, espere a ser programado")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification1 = builder2.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if(ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(1, notification1);

        }
    }



    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {

                if (isGranted) { // permiso concedido
                    downloadImagen();
                } else {
                    Log.e("msg-test", "Permiso denegado");
                }
            });
    public void downloadImagen(){
        String nameFile = "listaHorarios.jpg";
        String url = "https://i.pinimg.com/564x/4e/8e/a5/4e8ea537c896aa277e6449bdca6c45da.jpg";
        Uri downloadUri = Uri.parse(url);

        DownloadManager.Request requests = new DownloadManager.Request(downloadUri);
        requests.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        requests.setTitle(nameFile);
        requests.setAllowedOverRoaming(false);
        requests.setMimeType("image/jpeg");
        requests.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        requests.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + nameFile);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        try{
            downloadManager.enqueue(requests);
        }catch (RuntimeException e){
            e.printStackTrace();
        }


    }
}