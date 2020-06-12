package com.example.proyectonfc.util;

public class CardInfo {

    final public static String css_query = "td";

    final private static String DNI_TAG = "DNI:";
    final private static String Nombre_TAG = "Nombre:";
    final private static String Estado_TAG = "Estado:";
    final private static String Tarjeta_TAG = "Tarjeta:";
    final private static String Vigencia_TAG = "Vigencia:";

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
        return sonIguales(in, Nombre_TAG);
    }

    public boolean isEstado(String in){
        return sonIguales(in, Estado_TAG);
    }

    public boolean isTarjeta(String in){
        return sonIguales(in, Tarjeta_TAG);
    }

    public boolean isVigencia(String in){
        return sonIguales(in, Vigencia_TAG);
    }

    public void setDNI(String dni){
        this.dni = dni;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setTarjeta(String in){
        tarjeta = in;

    }

    public void setVigencia(String in){
        vigencia = in;

    }

    public void setEstado(String in){
        estado = in;

    }

    public void setFuncion(String in){
        funcion = in;

    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(nombre).append("\n");
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

    public String getFuncion() { return  this.funcion;}



}


