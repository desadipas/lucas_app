package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 18/04/2017.
 */

public class BandParamDTO {
    private int nCodPers;
    private String cUsername;
    private String cTitular;
    private String cNro;
    private int nCodAge;
    private int nTamPagina;

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public String getcUsername() {
        return cUsername;
    }

    public void setcUsername(String cUsername) {
        this.cUsername = cUsername;
    }

    public String getcTitular() {
        return cTitular;
    }

    public void setcTitular(String cTitular) {
        this.cTitular = cTitular;
    }

    public String getcNro() {
        return cNro;
    }

    public void setcNro(String cNro) {
        this.cNro = cNro;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public int getnTamPagina() {
        return nTamPagina;
    }

    public void setnTamPagina(int nTamPagina) {
        this.nTamPagina = nTamPagina;
    }
}
