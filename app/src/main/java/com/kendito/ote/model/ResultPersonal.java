
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultPersonal {

    @SerializedName("RUTPERSONAL")
    @Expose
    private String rUTPERSONAL;
    @SerializedName("IDTIPOPERSONAL")
    @Expose
    private String iDTIPOPERSONAL;
    @SerializedName("NOMBRE")
    @Expose
    private String nOMBRE;
    @SerializedName("PERSONAL_ASIGNADOR")
    @Expose
    private String pERSONALASIGNADOR;
    @SerializedName("CONTRASENA")
    @Expose
    private String cONTRASENA;

    public String getRUTPERSONAL() {
        return rUTPERSONAL;
    }

    public void setRUTPERSONAL(String rUTPERSONAL) {
        this.rUTPERSONAL = rUTPERSONAL;
    }

    public String getIDTIPOPERSONAL() {
        return iDTIPOPERSONAL;
    }

    public void setIDTIPOPERSONAL(String iDTIPOPERSONAL) {
        this.iDTIPOPERSONAL = iDTIPOPERSONAL;
    }

    public String getNOMBRE() {
        return nOMBRE;
    }

    public void setNOMBRE(String nOMBRE) {
        this.nOMBRE = nOMBRE;
    }

    public String getPERSONALASIGNADOR() {
        return pERSONALASIGNADOR;
    }

    public void setPERSONALASIGNADOR(String pERSONALASIGNADOR) {
        this.pERSONALASIGNADOR = pERSONALASIGNADOR;
    }

    public String getCONTRASENA() {
        return cONTRASENA;
    }

    public void setCONTRASENA(String cONTRASENA) {
        this.cONTRASENA = cONTRASENA;
    }

}
