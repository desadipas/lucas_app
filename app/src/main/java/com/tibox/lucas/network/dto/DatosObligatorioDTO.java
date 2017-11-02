package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 23/03/2017.
 */

public class DatosObligatorioDTO implements Serializable{
    private String cNombre;
    private int nCodigo;

    public DatosObligatorioDTO(){
    }

    public String getcNombre() {
        return cNombre;
    }

    public void setcNombre(String cNombre) {
        this.cNombre = cNombre;
    }

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }
}
