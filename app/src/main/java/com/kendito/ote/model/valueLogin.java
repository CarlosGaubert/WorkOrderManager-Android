package com.kendito.ote.model;

/**
 * Created by Kendo on 30-11-2017.
 */

public class valueLogin {

    String value;
    String rut;
    String contrasena;
    String nombre;
    String idTipoPersonal;

    public valueLogin() {
    }

    public valueLogin(String value, String rut, String contrasena, String nombre, String idTipoPersonal) {
        this.value = value;
        this.rut = rut;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.idTipoPersonal = idTipoPersonal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdTipoPersonal() {
        return idTipoPersonal;
    }

    public void setIdTipoPersonal(String idTipoPersonal) {
        this.idTipoPersonal = idTipoPersonal;
    }
}
