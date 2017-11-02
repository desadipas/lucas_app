package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 11/04/2017.
 */

public class SeguimientoDTO {

    private String cImei;
    private String cLatitud;
    private String cLongitud;
    private String cUsuReg;
    private int nCodPersReg;
    private int nCodAge;

    public String getcImei() {
        return cImei;
    }

    public void setcImei(String cImei) {
        this.cImei = cImei;
    }

    public String getcLatitud() {
        return cLatitud;
    }

    public void setcLatitud(String cLatitud) {
        this.cLatitud = cLatitud;
    }

    public String getcLongitud() {
        return cLongitud;
    }

    public void setcLongitud(String cLongitud) {
        this.cLongitud = cLongitud;
    }

    public String getcUsuReg() {
        return cUsuReg;
    }

    public void setcUsuReg(String cUsuReg) {
        this.cUsuReg = cUsuReg;
    }

    public int getnCodPersReg() {
        return nCodPersReg;
    }

    public void setnCodPersReg(int nCodPersReg) {
        this.nCodPersReg = nCodPersReg;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }
}
