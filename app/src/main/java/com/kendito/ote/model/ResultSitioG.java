
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultSitioG {

    @SerializedName("IDSITIO")
    @Expose
    private String iDSITIO;
    @SerializedName("NOMBRESITIO")
    @Expose
    private String nOMBRESITIO;
    @SerializedName("NOMBREAREA")
    @Expose
    private String nOMBREAREA;

    public String getIDSITIO() {
        return iDSITIO;
    }

    public void setIDSITIO(String iDSITIO) {
        this.iDSITIO = iDSITIO;
    }

    public String getNOMBRESITIO() {
        return nOMBRESITIO;
    }

    public void setNOMBRESITIO(String nOMBRESITIO) {
        this.nOMBRESITIO = nOMBRESITIO;
    }

    public String getNOMBREAREA() {
        return nOMBREAREA;
    }

    public void setNOMBREAREA(String nOMBREAREA) {
        this.nOMBREAREA = nOMBREAREA;
    }

}
