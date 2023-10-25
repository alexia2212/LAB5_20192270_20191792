package com.example.lab5_20192270_20191792.DTOs;

import com.example.lab5_20192270_20191792.Entity.TrabajadorTutoria;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmployeeRepo {
    @GET("/trabajador/tutoria")
    Call<TrabajadorTutoria> getFechaTutoria(@Query("codigo") int codigo);
    @POST("/trabajador/feedback")
    Call<UsuarioValidarDTO> postFeedback(@Query("employeeId") int id, @Query("employee_feedback") String feedback);
}
