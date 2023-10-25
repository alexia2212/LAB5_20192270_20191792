package com.example.lab5_20192270_20191792;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20192270_20191792.DTOs.BusquedaTrabajadorDto;
import com.example.lab5_20192270_20191792.Entity.Employees;
import com.example.lab5_20192270_20191792.databinding.ActivityBuscarTrabajadorBinding;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarTrabajadorActivity extends AppCompatActivity {

    ActivityBuscarTrabajadorBinding binding;
    BusquedaTrabajadorDto busquedaTrabajadorDto;

    ServiceTrabajador serviceTrabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuscarTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar Retrofit
        serviceTrabajador = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3010")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceTrabajador.class);

        binding.descargarDatos.setOnClickListener(view -> {
            String employeeId = binding.textIDtrabajador.getText().toString().trim();
            if (!employeeId.isEmpty()) {
                descargarInformacionEmpleado(employeeId);
            } else {
                Toast.makeText(this, "Ingrese un ID de empleado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void descargarInformacionEmpleado(String employeeId) {
        serviceTrabajador.buscarTrabajadorPorId(Integer.parseInt(employeeId)).enqueue(new Callback<BusquedaTrabajadorDto>() {
            @Override
            public void onResponse(Call<BusquedaTrabajadorDto> call, Response<BusquedaTrabajadorDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    busquedaTrabajadorDto = response.body();
                    guardarInformacionEnArchivo(employeeId, busquedaTrabajadorDto);
                } else {
                    Toast.makeText(BuscarTrabajadorActivity.this, "Empleado no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BusquedaTrabajadorDto> call, Throwable t) {
                Log.e("msg-test", "Error al descargar la información del empleado", t);
            }
        });
    }

    private void guardarInformacionEnArchivo(String employeeId, BusquedaTrabajadorDto dto) {
        String fileNameTxt = "informacionDe" + employeeId + ".txt";

        try (FileOutputStream fileOutputStream = openFileOutput(fileNameTxt, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {
            Employees employee = dto.getEmpleado();
            writer.write("Información del empleado con ID: " + employeeId + "\n");
            writer.write("Nombre: " + employee.getFirstName() + " " + employee.getLastName() + "\n");
            writer.write("Teléfono: " + employee.getPhoneNumber() + "\n");
            writer.write("Correo electrónico: " + employee.getEmail() + "\n");

            Toast.makeText(BuscarTrabajadorActivity.this, "Información del empleado guardada en " + fileNameTxt, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("msg-test", "Error al guardar la información del empleado en el archivo", e);
        }
    }
}
