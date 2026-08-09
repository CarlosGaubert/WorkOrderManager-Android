package com.kendito.ote.model;

/**
 * Created by Kendo on 03-12-2017.
 */

public class valueMensaje {
    String value;
    String message;


    public valueMensaje() {
    }

    public valueMensaje(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
