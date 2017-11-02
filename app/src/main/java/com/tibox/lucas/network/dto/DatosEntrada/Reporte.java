package com.tibox.lucas.network.dto.DatosEntrada;

/**
 * Created by desa02 on 18/08/2017.
 */

public class Reporte {
    private int nCodCred;
    private int nCodAge;
    private int nPEP;
    private String cEmail;
    private String cNombres;
    private double nPrestamo;
    private boolean bError;
    private String cMensajeError;
    private String oDocumento;

    public int getnCodCred() {
        return nCodCred;
    }

    public void setnCodCred(int nCodCred) {
        this.nCodCred = nCodCred;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public int getnPEP() {
        return nPEP;
    }

    public void setnPEP(int nPEP) {
        this.nPEP = nPEP;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcNombres() {
        return cNombres;
    }

    public void setcNombres(String cNombres) {
        this.cNombres = cNombres;
    }

    public double getnPrestamo() {
        return nPrestamo;
    }

    public void setnPrestamo(double nPrestamo) {
        this.nPrestamo = nPrestamo;
    }

    public boolean isbError() {
        return bError;
    }

    public void setbError(boolean bError) {
        this.bError = bError;
    }

    public String getcMensajeError() {
        return cMensajeError;
    }

    public void setcMensajeError(String cMensajeError) {
        this.cMensajeError = cMensajeError;
    }

    public String getoDocumento() {
        return oDocumento;
    }

    public void setoDocumento(String oDocumento) {
        this.oDocumento = oDocumento;
    }
}
