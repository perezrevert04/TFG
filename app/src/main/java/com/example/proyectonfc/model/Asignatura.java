package com.example.proyectonfc.model;

import java.io.Serializable;

public class Asignatura implements Serializable {

    private Integer id;
    private String nombre;


    public Asignatura(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    public Asignatura(){

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


}
