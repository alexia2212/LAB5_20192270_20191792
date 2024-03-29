package com.example.lab5_20192270_20191792;

import com.example.lab5_20192270_20191792.DTOs.BusquedaTrabajadorDto;
import com.example.lab5_20192270_20191792.DTOs.BusquedaTrabajadoresDto;
import com.example.lab5_20192270_20191792.DTOs.RespuestaDto;
import com.example.lab5_20192270_20191792.Entity.Employees;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServiceTrabajador {

    @GET("/buscarEmployee")
    Call<BusquedaTrabajadorDto> buscarTrabajadorPorId(@Query("id") int id);

    @GET("/tutor/listaTrabajadores")
    Call<List<Employees>> buscarTrabajadorTodos(@Query("codigo") int codigo);


    @PUT("/tutor/tutoria")
    Call<RespuestaDto> asignar(@Query("idEmployee") int idTrab, @Query("idTutor") int idTutor);


}
