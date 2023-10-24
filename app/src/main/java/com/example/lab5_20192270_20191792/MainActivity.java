package com.example.lab5_20192270_20191792;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab5_20192270_20191792.Trabajador.TrabajadorActivity;
import com.example.lab5_20192270_20191792.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonTrabajador.setOnClickListener(view -> {

            Intent intent= new Intent(MainActivity.this, TrabajadorActivity.class);
            startActivity(intent);
        });
    }
}