package com.example.lab5_20192270_20191792.DTOs;

import com.example.lab5_20192270_20191792.Entity.Employees;

import java.util.List;

public class BusquedaTrabajadoresDto {
    private List<Employees> empleado;
    private String respuesta;


    public List<Employees> getEmpleados() {
        return empleado;
    }

    public void setEmpleados(List<Employees> empleados) {
        this.empleado = empleados;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
