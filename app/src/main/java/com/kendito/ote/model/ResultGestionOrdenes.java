
package com.kendito.ote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultGestionOrdenes {

    @SerializedName("CODIGOORDEN")
    @Expose
    private String cODIGOORDEN;
    @SerializedName("RUTPERSONAL")
    @Expose
    private String rUTPERSONAL;
    @SerializedName("RUTCLIENTE")
    @Expose
    private String rUTCLIENTE;
    @SerializedName("RUTPERSONALREALIZARORDEN")
    @Expose
    private String rUTPERSONALREALIZARORDEN;
    @SerializedName("ORDENREALIZADA")
    @Expose
    private String oRDENREALIZADA;
    @SerializedName("FECHAPEDIDAORDEN")
    @Expose
    private String fECHAPEDIDAORDEN;
    @SerializedName("FECHAACEPTADAORDEN")
    @Expose
    private String fECHAACEPTADAORDEN;
    @SerializedName("DESCRIPCIONORDEN")
    @Expose
    private String dESCRIPCIONORDEN;
    @SerializedName("ORDENACEPTADA")
    @Expose
    private String oRDENACEPTADA;
    @SerializedName("NOMBRECLIENTE")
    @Expose
    private String nOMBRECLIENTE;
    @SerializedName("IDSITIO")
    @Expose
    private String iDSITIO;
    @SerializedName("NOMBRESITIO")
    @Expose
    private String nOMBRESITIO;
    @SerializedName("IDAREA")
    @Expose
    private String iDAREA;
    @SerializedName("NOMBREAREA")
    @Expose
    private String nOMBREAREA;

    public String getCODIGOORDEN() {
        return cODIGOORDEN;
    }

    public void setCODIGOORDEN(String cODIGOORDEN) {
        this.cODIGOORDEN = cODIGOORDEN;
    }

    public String getRUTPERSONAL() {
        return rUTPERSONAL;
    }

    public void setRUTPERSONAL(String rUTPERSONAL) {
        this.rUTPERSONAL = rUTPERSONAL;
    }

    public String getRUTCLIENTE() {
        return rUTCLIENTE;
    }

    public void setRUTCLIENTE(String rUTCLIENTE) {
        this.rUTCLIENTE = rUTCLIENTE;
    }

    public String getRUTPERSONALREALIZARORDEN() {
        return rUTPERSONALREALIZARORDEN;
    }

    public void setRUTPERSONALREALIZARORDEN(String rUTPERSONALREALIZARORDEN) {
        this.rUTPERSONALREALIZARORDEN = rUTPERSONALREALIZARORDEN;
    }

    public String getORDENREALIZADA() {
        return oRDENREALIZADA;
    }

    public void setORDENREALIZADA(String oRDENREALIZADA) {
        this.oRDENREALIZADA = oRDENREALIZADA;
    }

    public String getFECHAPEDIDAORDEN() {
        return fECHAPEDIDAORDEN;
    }

    public void setFECHAPEDIDAORDEN(String fECHAPEDIDAORDEN) {
        this.fECHAPEDIDAORDEN = fECHAPEDIDAORDEN;
    }

    public String getFECHAACEPTADAORDEN() {
        return fECHAACEPTADAORDEN;
    }

    public void setFECHAACEPTADAORDEN(String fECHAACEPTADAORDEN) {
        this.fECHAACEPTADAORDEN = fECHAACEPTADAORDEN;
    }

    public String getDESCRIPCIONORDEN() {
        return dESCRIPCIONORDEN;
    }

    public void setDESCRIPCIONORDEN(String dESCRIPCIONORDEN) {
        this.dESCRIPCIONORDEN = dESCRIPCIONORDEN;
    }

    public String getORDENACEPTADA() {
        return oRDENACEPTADA;
    }

    public void setORDENACEPTADA(String oRDENACEPTADA) {
        this.oRDENACEPTADA = oRDENACEPTADA;
    }

    public String getNOMBRECLIENTE() {
        return nOMBRECLIENTE;
    }

    public void setNOMBRECLIENTE(String nOMBRECLIENTE) {
        this.nOMBRECLIENTE = nOMBRECLIENTE;
    }

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
