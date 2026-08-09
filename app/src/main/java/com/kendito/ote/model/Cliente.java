
package com.kendito.ote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cliente {

    @SerializedName("valueCliente")
    @Expose
    private int valueCliente;
    @SerializedName("resultCliente")
    @Expose
    private List<ResultCliente> resultCliente = null;

    public int getValueCliente() {
        return valueCliente;
    }

    public void setValueCliente(int valueCliente) {
        this.valueCliente = valueCliente;
    }

    public List<ResultCliente> getResultCliente() {
        return resultCliente;
    }

    public void setResultCliente(List<ResultCliente> resultCliente) {
        this.resultCliente = resultCliente;
    }

}
