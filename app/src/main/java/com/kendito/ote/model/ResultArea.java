
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultArea {

    @SerializedName("IDAREA")
    @Expose
    private String iDAREA;
    @SerializedName("NOMBREAREA")
    @Expose
    private String nOMBREAREA;

    public String getIDAREA() {
        return iDAREA;
    }

    public void setIDAREA(String iDAREA) {
        this.iDAREA = iDAREA;
    }

    public String getNOMBREAREA() {
        return nOMBREAREA;
    }

    public void setNOMBREAREA(String nOMBREAREA) {
        this.nOMBREAREA = nOMBREAREA;
    }

}
