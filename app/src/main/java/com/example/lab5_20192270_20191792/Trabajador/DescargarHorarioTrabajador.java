package com.example.lab5_20192270_20191792.Trabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lab5_20192270_20191792.DTOs.EmployeeRepo;
import com.example.lab5_20192270_20191792.databinding.ActivityDescargarHorarioTrabajadorBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DescargarHorarioTrabajador extends AppCompatActivity {

    EmployeeRepo employeeRepo;
    String id;
    String horario;
    Boolean hayTuto;
    ActivityDescargarHorarioTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDescargarHorarioTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d("msg-test", "Id ingresado: "+intent.getStringExtra("id"));
        horario = intent.getStringExtra("horario");
        //En caso el trabajor no tenga ninguna tutoría

        if(horario.equalsIgnoreCase("No hay ninguna asesoría")){
            binding.buttonDescargarHorario.setVisibility(View.GONE);
            binding.editTextTextMultiLineFeedback.setVisibility(View.GONE);
            hayTuto = false;
        }else{
            hayTuto = true;
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");
                LocalDateTime horario1 = LocalDateTime.parse(horario,formatter);

                LocalDateTime currentDateTime = LocalDateTime.now();
                if(horario1.isAfter(currentDateTime)){
                    binding.editTextTextMultiLineFeedback.setVisibility(View.GONE);
                    binding.button9.setVisibility(View.GONE);

                }
            }
        }

        binding.button9.setOnClickListener(view -> {
            if(hayTuto){

            }
        });
    }
}