package com.example.lab5_20192270_20191792;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lab5_20192270_20191792.DTOs.BusquedaTrabajadorDto;
import com.example.lab5_20192270_20191792.Entity.Countries;
import com.example.lab5_20192270_20191792.Entity.Departments;
import com.example.lab5_20192270_20191792.Entity.Employees;
import com.example.lab5_20192270_20191792.Entity.Locations;
import com.example.lab5_20192270_20191792.Entity.Regions;
import com.example.lab5_20192270_20191792.databinding.ActivityBuscarTrabajadorBinding;
import com.example.lab5_20192270_20191792.databinding.ActivityTutorBinding;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarTrabajadorActivity extends AppCompatActivity {

    String channelId = "channelHighPriorTutor";
    ActivityBuscarTrabajadorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_trabajador);

        binding = ActivityBuscarTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ServiceTrabajador trabajadorService = new Retrofit.Builder()
                .baseUrl("https://8r7fm6zj-3010.brs.devtunnels.ms")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceTrabajador.class);

        binding.descargarDatos.setOnClickListener(view -> {
                binding.errorID2.setText("");
                String id = binding.textIDtrabajador.getText().toString();
                if (!id.equals("")){
                    try{
                        int idTrab = Integer.parseInt(id);
                        trabajadorService.buscarTrabajadorPorId(idTrab).enqueue(new Callback<BusquedaTrabajadorDto>() {
                            @Override
                            public void onResponse(Call<BusquedaTrabajadorDto> call, Response<BusquedaTrabajadorDto> response) {
                                if (response.isSuccessful()){
                                    BusquedaTrabajadorDto busquedaTrabajadorDto = response.body();
                                    if (busquedaTrabajadorDto.getRespuesta().equals("no encontrado")){
                                        binding.errorID2.setText("Empleado inexistente");
                                    }else {
                                        HashMap<String, Object> datosDisplay = obtenerDatosDisplay(busquedaTrabajadorDto.getEmpleado());
                                        guardarDatosComoJson(datosDisplay);
                                        Toast.makeText(BuscarTrabajadorActivity.this, "Descarga Exitosa!", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    binding.errorID2.setTextSize(24);
                                    binding.errorID2.setText("No encontrado");
                                }
                            }

                            @Override
                            public void onFailure(Call<BusquedaTrabajadorDto> call, Throwable t) {
                                binding.errorID2.setText("Trabajador no encontrado");
                                t.printStackTrace();
                            }
                        });
                    }catch (Exception e){
                        binding.errorID2.setText("Debe ingresar un número");
                    }
                }else {
                    binding.errorID2.setText("Debe ingresar un ID");
                }
            });

        }



        public HashMap<String, Object> obtenerDatosDisplay(Employees employee){

            HashMap<String, Object> info = new HashMap<>();

            info.put("Id", employee.getEmployeeId().toString());

            String firstName = employee.getFirstName();
            if (firstName!=null || !firstName.equals(""))
                info.put("Nombre", employee.getEmployeeId().toString());
            else
                info.put("Nombre", "Sin nombre");

            info.put("Apellido",employee.getLastName().toString());
            info.put("Correo", employee.getEmail().toString());

            String phoneNumber = employee.getPhoneNumber();
            if (phoneNumber!=null || !phoneNumber.equals(""))
                info.put("Celular",employee.getPhoneNumber().toString());
            else
                info.put("Celular", "Sin celular");

            info.put("Puesto", employee.getJobId().getJobTitle().toString());

            Float salario = employee.getSalary();
            if (salario!=null)
                info.put("Salario", String.valueOf(employee.getSalary()));
            else
                info.put("Salario", "Sin informacion");

            Departments departmentId = employee.getDepartmentId();
            if (departmentId==null){
                info.put("Departamento", "Sin informacion");
                info.put("Pais", "Sin informacion");
                info.put("Region", "Sin informacion");
            }else {
                info.put("Departamento", employee.getDepartmentId().getDepartmentName().toString());
                Locations locationId = employee.getDepartmentId().getLocationId();
                if (locationId==null){
                    info.put("Pais", "Sin informacion");
                    info.put("Region", "Sin informacion");
                }else{
                    Countries countryId = employee.getDepartmentId().getLocationId().getCountryId();
                    if (countryId==null || countryId.equals("")){
                        info.put("Pais", "Sin informacion");
                        info.put("Region", "Sin informacion");
                    }else{
                        info.put("Pais", employee.getDepartmentId().getLocationId().getCountryId().getCountryName().toString());
                        Regions regionId = employee.getDepartmentId().getLocationId().getCountryId().getRegionsId();
                        if (regionId==null){
                            info.put("Region", "Sin informacion");
                        }else {
                            info.put("Region", employee.getDepartmentId().getLocationId().getCountryId().getRegionsId().getRegionName().toString());
                        }
                    }
                }
            }

            return info;
        }

        public void guardarDatosComoJson(HashMap<String, Object> listaDatos){
            String idTrabajador = (String) listaDatos.get("Id");
            Gson gson = new Gson();
            String listaJson = gson.toJson(listaDatos);
            String fileNameJson = "informacionDe"+idTrabajador+".txt";
            try(FileOutputStream fileOutputStream = this.openFileOutput(fileNameJson, Context.MODE_PRIVATE);
                FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());){
                fileWriter.write(listaJson);
            }catch (IOException e){
                e.printStackTrace();
            }
        }


    }

