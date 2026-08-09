
package com.kendito.ote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GestionOrdenes {

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("resultGestionOrdenes")
    @Expose
    private List<ResultGestionOrdenes> resultGestionOrdenes = null;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<ResultGestionOrdenes> getResultGestionOrdenes() {
        return resultGestionOrdenes;
    }

    public void setResultGestionOrdenes(List<ResultGestionOrdenes> resultGestionOrdenes) {
        this.resultGestionOrdenes = resultGestionOrdenes;
    }

}
