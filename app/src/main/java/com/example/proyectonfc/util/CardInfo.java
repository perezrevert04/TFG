package com.example.proyectonfc.util;

import org.jetbrains.annotations.NotNull;

public class CardInfo {

    final public static String css_query = "td";

    final public static String DNI_TAG = "DNI:";
    final public static String NAME_TAG = "Nombre:";
    final public static String STATE_TAG = "Estado:";
    final public static String CARD_TAG = "Tarjeta:";
    final public static String VALIDITY_TAG = "Vigencia:";

    public String dni ;
    public String nombre = null;
    private String estado = null;
    private String tarjeta = null;
    private String vigencia = null;
    private String funcion = null;


    boolean sonIguales(String s1, String s2){
        return (s1.compareTo(s2) == 0);
    }

    public boolean isDNI(String in){
        return sonIguales(in, DNI_TAG);
    }

    public boolean isNombre(String in){
        return sonIguales(in, NAME_TAG);
    }

    public boolean isEstado(String in){
        return sonIguales(in, STATE_TAG);
    }

    public boolean isTarjeta(String in){
        return sonIguales(in, CARD_TAG);
    }

    public boolean isVigencia(String in){
        return sonIguales(in, VALIDITY_TAG);
    }

    public void setDNI(String dni){
        this.dni = dni;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setTarjeta(String in){ tarjeta = in; }

    public void setVigencia(String vigencia){ this.vigencia = vigencia; }

    public void setEstado(String estado){ this.estado = estado; }

    public void setFuncion(String in){ funcion = in; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nNombre: ").append(nombre).append("\n");
        sb.append("DNI: ").append(dni).append("\n");
        sb.append("Tipo de usuario: ").append(funcion).append("\n");
        sb.append("Num. tarjeta: ").append(tarjeta).append("\n");
        sb.append("Vigencia hasta: ").append(vigencia).append("\n");
        sb.append("Estado actual: ").append(estado).append("\n");
        return sb.toString();

    }

    public String getDNI(){
        return this.dni;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getFuncion() { return this.funcion;}



}


