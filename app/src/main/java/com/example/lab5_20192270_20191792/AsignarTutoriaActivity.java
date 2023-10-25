package com.example.lab5_20192270_20191792;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab5_20192270_20191792.DTOs.RespuestaDto;
import com.example.lab5_20192270_20191792.databinding.ActivityAsignarTutoriaBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsignarTutoriaActivity extends AppCompatActivity {

    private ActivityAsignarTutoriaBinding binding;

    private ServiceTrabajador trabajadorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsignarTutoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        trabajadorService = new Retrofit.Builder()
                .baseUrl("https://8r7fm6zj-3010.brs.devtunnels.ms")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceTrabajador.class);

        binding.btnasignar.setOnClickListener(view -> {
            binding.errorIDDescargar2.setText("");
            String idTutor = binding.idTextTutor.getText().toString();
            String idEmpleado = binding.idTextEmpleado.getText().toString();

            if ((!idTutor.equals("")) && (!idEmpleado.equals(""))) {
                try {
                    int idTutorInt = Integer.parseInt(idTutor);
                    int idEmpleadoInt = Integer.parseInt(idEmpleado);

                    trabajadorService.asignar(idEmpleadoInt, idTutorInt).enqueue(new Callback<RespuestaDto>() {
                        @Override
                        public void onResponse(Call<RespuestaDto> call, Response<RespuestaDto> response) {
                            if (response.isSuccessful()) {
                                RespuestaDto respuestaDto = response.body();
                                binding.errorIDDescargar2.setText(respuestaDto.getEstado());
                            } else {
                                binding.errorIDDescargar2.setText("No encontrado");
                            }
                        }

                        @Override
                        public void onFailure(Call<RespuestaDto> call, Throwable t) {

                        }
                    });

                } catch (Exception e) {
                    binding.errorIDDescargar2.setText("Debe ingresar un número");
                }
            } else {
                binding.errorIDDescargar2.setText("Debe ingresar un ID");
            }
        });
    }
}
