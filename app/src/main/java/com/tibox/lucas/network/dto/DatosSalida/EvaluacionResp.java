package com.tibox.lucas.network.dto.DatosSalida;

/**
 * Created by desa02 on 15/08/2017.
 */

public class EvaluacionResp {
    private int nIdFlujoMaestro;
    private int nRechazado;
    private String cMensajeTry;
    private int nPEP;

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }

    public int getnRechazado() {
        return nRechazado;
    }

    public void setnRechazado(int nRechazado) {
        this.nRechazado = nRechazado;
    }

    public String getcMensajeTry() {
        return cMensajeTry;
    }

    public void setcMensajeTry(String cMensajeTry) {
        this.cMensajeTry = cMensajeTry;
    }

    public int getnPEP() {
        return nPEP;
    }

    public void setnPEP(int nPEP) {
        this.nPEP = nPEP;
    }
}
