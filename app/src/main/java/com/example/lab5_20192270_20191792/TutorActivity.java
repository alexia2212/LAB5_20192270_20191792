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

        ServiceTrabajador trabajadorService = new Retrofit.Builder()
                .baseUrl("https://8r7fm6zj-3010.brs.devtunnels.ms")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceTrabajador.class);

        binding.descargaboton.setOnClickListener(view -> {
            trabajadorService.buscarTrabajadorTodos().enqueue(new Callback<BusquedaTrabajadoresDto>() {
                @Override
                public void onResponse(Call<BusquedaTrabajadoresDto> call, Response<BusquedaTrabajadoresDto> response) {
                    BusquedaTrabajadoresDto busquedaTrabajadoresDto = response.body();
                    HashMap<String, HashMap<String, Object>> datosDisplay = obtenerDatosDisplay2(busquedaTrabajadoresDto.getEmpleados());
                    guardarDatosComoJson2(datosDisplay);
                    Toast.makeText(TutorActivity.this, "Descarga Exitosa!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BusquedaTrabajadoresDto> call, Throwable t) {

                }
            });
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

    public HashMap<String, HashMap<String, Object>> obtenerDatosDisplay2(List<Employees> employees){

        HashMap<String, HashMap<String, Object>> diccionario = new HashMap<>();

        for(int i = 0; i < employees.size(); i++) {
            HashMap<String, Object> info = new HashMap<>();

            info.put("Id", employees.get(i).getEmployeeId().toString());
            String firstName = employees.get(i).getFirstName();
            if (firstName!=null || !firstName.equals(""))
                info.put("Nombre", employees.get(i).getEmployeeId().toString());
            else
                info.put("Nombre", "Sin nombre");

            info.put("Apellido",employees.get(i).getLastName().toString());
            info.put("Correo", employees.get(i).getEmail().toString());

            String phoneNumber = employees.get(i).getPhoneNumber();
            if (phoneNumber!=null || !phoneNumber.equals(""))
                info.put("Celular",employees.get(i).getPhoneNumber().toString());
            else
                info.put("Celular", "Sin celular");

            info.put("Puesto", employees.get(i).getJobId().getJobTitle().toString());

            Float salario = employees.get(i).getSalary();
            if (salario!=null)
                info.put("Salario", String.valueOf(employees.get(i).getSalary()));
            else
                info.put("Salario", "Sin informacion");

            Departments departmentId = employees.get(i).getDepartmentId();
            if (departmentId==null){
                info.put("Departamento", "Sin informacion");
                info.put("Pais", "Sin informacion");
                info.put("Region", "Sin informacion");
            }else {
                info.put("Departamento", employees.get(i).getDepartmentId().getDepartmentName().toString());
                Locations locationId = employees.get(i).getDepartmentId().getLocationId();
                if (locationId==null){
                    info.put("Pais", "Sin informacion");
                    info.put("Region", "Sin informacion");
                }else{
                    Countries countryId = employees.get(i).getDepartmentId().getLocationId().getCountryId();
                    if (countryId==null || countryId.equals("")){
                        info.put("Pais", "Sin informacion");
                        info.put("Region", "Sin informacion");
                    }else{
                        info.put("Pais", employees.get(i).getDepartmentId().getLocationId().getCountryId().getCountryName().toString());
                        Regions regionId = employees.get(i).getDepartmentId().getLocationId().getCountryId().getRegionsId();
                        if (regionId==null){
                            info.put("Region", "Sin informacion");
                        }else {
                            info.put("Region", employees.get(i).getDepartmentId().getLocationId().getCountryId().getRegionsId().getRegionName().toString());
                        }
                    }
                }
            }
            diccionario.put("Trabajador" + String.valueOf(i), info);
        }
        return diccionario;
    }

    public void guardarDatosComoJson2(HashMap<String, HashMap<String, Object>> listaDatos){
        Gson gson = new Gson();
        String listaJson = gson.toJson(listaDatos);
        String fileNameJson = "listaDeTrabajadores.txt";
        try(FileOutputStream fileOutputStream = openFileOutput(fileNameJson, Context.MODE_PRIVATE);
            FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());){
            fileWriter.write(listaJson);
        }catch (IOException e){
            e.printStackTrace();
        }
    }




}