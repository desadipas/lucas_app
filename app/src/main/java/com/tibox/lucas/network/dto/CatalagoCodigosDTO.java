package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 10/03/2017.
 */

public class CatalagoCodigosDTO implements Serializable{
    private String cNomCod;
    private int nCodigo;
    private String nValor;

    public String getcNomCod() {
        return cNomCod;
    }

    public void setcNomCod(String cNomCod) {
        this.cNomCod = cNomCod;
    }

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }

    public String getnValor() {
        return nValor;
    }

    public void setnValor(String nValor) {
        this.nValor = nValor;
    }
}
