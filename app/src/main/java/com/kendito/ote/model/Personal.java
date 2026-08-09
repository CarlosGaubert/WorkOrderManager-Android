
package com.kendito.ote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Personal {

    @SerializedName("valuePersonal")
    @Expose
    private int valuePersonal;
    @SerializedName("resultPersonal")
    @Expose
    private List<ResultPersonal> resultPersonal = null;

    public int getValuePersonal() {
        return valuePersonal;
    }

    public void setValuePersonal(int valuePersonal) {
        this.valuePersonal = valuePersonal;
    }

    public List<ResultPersonal> getResultPersonal() {
        return resultPersonal;
    }

    public void setResultPersonal(List<ResultPersonal> resultPersonal) {
        this.resultPersonal = resultPersonal;
    }

}
