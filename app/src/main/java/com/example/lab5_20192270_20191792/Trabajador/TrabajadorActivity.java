package com.example.lab5_20192270_20191792.Trabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab5_20192270_20191792.R;
import com.example.lab5_20192270_20191792.databinding.ActivityTrabajadorBinding;

public class TrabajadorActivity extends AppCompatActivity {

    ActivityTrabajadorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrabajadorBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.button7.setOnClickListener(view -> {
            Intent intent = new Intent(TrabajadorActivity.this, DescargarHorarioTrabajador.class);
            startActivity(intent);

        });
    }
}