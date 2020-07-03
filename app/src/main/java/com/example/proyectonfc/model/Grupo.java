package com.example.proyectonfc.model;

import java.io.Serializable;

public class Grupo implements Serializable {

    private Integer id;
    private String grupo;
    private String h_entrada;
    private String h_salida;
    private String aula;


    public Grupo(Integer id, String grupo, String h_entrada, String h_salida, String aula) {
        this.id = id;
        this.grupo = grupo;
        this.h_entrada = h_entrada;
        this.h_salida = h_salida;
        this.aula = aula;

    }

    public Grupo(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getH_entrada() {
        return h_entrada;
    }

    public void setH_entrada(String h_entrada) {
        this.h_entrada = h_entrada;
    }

    public String getH_salida() {
        return h_salida;
    }

    public void setH_salida(String h_salida) {
        this.h_salida = h_salida;
    }

    public String getAula() { return aula; }

    public void setAula(String aula) {
        this.aula = aula;
    }


}
