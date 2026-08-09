
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCliente {

    @SerializedName("RUTCLIENTE")
    @Expose
    private String rUTCLIENTE;
    @SerializedName("IDTIPOPERSONAL")
    @Expose
    private String iDTIPOPERSONAL;
    @SerializedName("IDSITIO")
    @Expose
    private String iDSITIO;
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

    public String getIDTIPOPERSONAL() {
        return iDTIPOPERSONAL;
    }

    public void setIDTIPOPERSONAL(String iDTIPOPERSONAL) {
        this.iDTIPOPERSONAL = iDTIPOPERSONAL;
    }

    public String getIDSITIO() {
        return iDSITIO;
    }

    public void setIDSITIO(String iDSITIO) {
        this.iDSITIO = iDSITIO;
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
