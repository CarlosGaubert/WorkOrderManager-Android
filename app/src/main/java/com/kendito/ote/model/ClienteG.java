
package com.kendito.ote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClienteG {

    @SerializedName("valueCliente")
    @Expose
    private Integer valueCliente;
    @SerializedName("resultClienteG")
    @Expose
    private List<ResultClienteG> resultClienteG = null;

    public Integer getValueCliente() {
        return valueCliente;
    }

    public void setValueCliente(Integer valueCliente) {
        this.valueCliente = valueCliente;
    }

    public List<ResultClienteG> getResultClienteG() {
        return resultClienteG;
    }

    public void setResultClienteG(List<ResultClienteG> resultClienteG) {
        this.resultClienteG = resultClienteG;
    }

}
