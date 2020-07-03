package com.example.proyectonfc.model;

import java.io.Serializable;

public class Profesor implements Serializable {

    private Integer id;
    private String nombre;
    private String dni;


    public Profesor(Integer id, String nombre, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;

    }

    public Profesor(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }


}
