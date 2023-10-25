package com.example.lab5_20192270_20191792;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.lab5_20192270_20191792.DTOs.BusquedaTrabajadoresDto;
import com.example.lab5_20192270_20191792.Entity.Employees;
import com.example.lab5_20192270_20191792.databinding.ActivityListaTrabajadoresBinding;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaTrabajadoresActivity extends AppCompatActivity {

    ActivityListaTrabajadoresBinding binding;
    List<Employees> listadeEmpleados = new ArrayList<>();
    String codigoManager;

    ServiceTrabajador serviceTrabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_trabajadores);

        binding = ActivityListaTrabajadoresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        servicio();

        binding.descargarDatos.setOnClickListener(view -> {
            codigoManager = String.valueOf(binding.textIDtutor.getText());
            if(codigoManager.length()>0){
                lista(codigoManager);
            }else{
                Toast.makeText(this, "Ingresar codigo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void servicio(){
        serviceTrabajador = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3010")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceTrabajador.class);
    }

    public void lista(String codigoManager) {

        serviceTrabajador.buscarTrabajadorTodos(Integer.parseInt(codigoManager)).enqueue(new Callback<List<Employees>>() {
            @Override
            public void onResponse(Call<List<Employees>> call, Response<List<Employees>> response) {
                listadeEmpleados = response.body();

                guardarLista(listadeEmpleados);
                Toast.makeText(ListaTrabajadoresActivity.this, "Descarga de trabajadores exitosa", Toast.LENGTH_SHORT).show();
                Log.d("msg-test", "Se guardo la lista");

            }

            @Override
            public void onFailure(Call<List<Employees>> call, Throwable t) {
                Log.d("msg-test", "Algo paso");
                Log.d("msg-test", t.getMessage());

            }
        });

    }

    public void guardarLista(List<Employees> lista) {
        String fileNameTxt = "listaTrabajadores.txt";

        try (FileOutputStream fileOutputStream = openFileOutput(fileNameTxt, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {
            writer.write("Trabajadores bajo el mando del tutorid: "+codigoManager+"\n");
            for (Employees employee : lista) {

                writer.write(employee.getFirstName()+" "+ employee.getLastName() +" "+ employee.getPhoneNumber() +" "+ employee.getEmail() + "\n");
            }

            Log.d("msg-test", "Archivo de texto guardado correctamente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}