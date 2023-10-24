package com.example.lab5_20192270_20191792.Trabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab5_20192270_20191792.R;
import com.example.lab5_20192270_20191792.databinding.ActivityDescargarHorarioTrabajadorBinding;

public class DescargarHorarioTrabajador extends AppCompatActivity {

    ActivityDescargarHorarioTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDescargarHorarioTrabajadorBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());




    }
}