
package com.kendito.ote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("valueArea")
    @Expose
    private int valueArea;
    @SerializedName("resultArea")
    @Expose
    private List<ResultArea> resultArea = null;

    public int getValueArea() {
        return valueArea;
    }

    public void setValueArea(int valueArea) {
        this.valueArea = valueArea;
    }

    public List<ResultArea> getResultArea() {
        return resultArea;
    }

    public void setResultArea(List<ResultArea> resultArea) {
        this.resultArea = resultArea;
    }

}
