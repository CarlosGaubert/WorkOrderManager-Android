
package com.kendito.ote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SitioG {

    @SerializedName("value")
    @Expose
    private int value;
    @SerializedName("resultSitioG")
    @Expose
    private List<ResultSitioG> resultSitioG = null;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<ResultSitioG> getResultSitioG() {
        return resultSitioG;
    }

    public void setResultSitioG(List<ResultSitioG> resultSitioG) {
        this.resultSitioG = resultSitioG;
    }

}
