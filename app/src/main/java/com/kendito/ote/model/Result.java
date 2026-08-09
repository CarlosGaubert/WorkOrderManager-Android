
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("IDSITIO")
    @Expose
    private String iDSITIO;
    @SerializedName("IDAREA")
    @Expose
    private String iDAREA;
    @SerializedName("NOMBRESITIO")
    @Expose
    private String nOMBRESITIO;

    public String getIDSITIO() {
        return iDSITIO;
    }

    public void setIDSITIO(String iDSITIO) {
        this.iDSITIO = iDSITIO;
    }

    public String getIDAREA() {
        return iDAREA;
    }

    public void setIDAREA(String iDAREA) {
        this.iDAREA = iDAREA;
    }

    public String getNOMBRESITIO() {
        return nOMBRESITIO;
    }

    public void setNOMBRESITIO(String nOMBRESITIO) {
        this.nOMBRESITIO = nOMBRESITIO;
    }

}
