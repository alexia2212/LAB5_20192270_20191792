package com.example.lab5_20192270_20191792.DTOs;

import com.example.lab5_20192270_20191792.Entity.Employees;

public class BusquedaTrabajadorDto {
    private Employees empleado;
    private String respuesta;


    public Employees getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Employees empleado) {
        this.empleado = empleado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
