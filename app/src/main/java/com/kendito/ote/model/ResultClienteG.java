
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultClienteG {

    @SerializedName("RUTCLIENTE")
    @Expose
    private String rUTCLIENTE;
    @SerializedName("CARGO")
    @Expose
    private String cARGO;
    @SerializedName("NOMBRESITIO")
    @Expose
    private String nOMBRESITIO;
    @SerializedName("NOMBREAREA")
    @Expose
    private String nOMBREAREA;
    @SerializedName("NOMBRECLIENTE")
    @Expose
    private String nOMBRECLIENTE;
    @SerializedName("CONTRASENA")
    @Expose
    private String cONTRASENA;

    public String getRUTCLIENTE() {
        return rUTCLIENTE;
    }

    public void setRUTCLIENTE(String rUTCLIENTE) {
        this.rUTCLIENTE = rUTCLIENTE;
    }

    public String getCARGO() {
        return cARGO;
    }

    public void setCARGO(String cARGO) {
        this.cARGO = cARGO;
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

    public String getNOMBRECLIENTE() {
        return nOMBRECLIENTE;
    }

    public void setNOMBRECLIENTE(String nOMBRECLIENTE) {
        this.nOMBRECLIENTE = nOMBRECLIENTE;
    }

    public String getCONTRASENA() {
        return cONTRASENA;
    }

    public void setCONTRASENA(String cONTRASENA) {
        this.cONTRASENA = cONTRASENA;
    }

}
