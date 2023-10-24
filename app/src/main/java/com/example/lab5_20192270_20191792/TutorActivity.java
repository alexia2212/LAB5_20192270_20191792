package com.example.lab5_20192270_20191792;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

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




}