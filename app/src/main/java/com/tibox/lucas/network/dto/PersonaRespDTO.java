package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 27/03/2017.
 */

public class PersonaRespDTO implements Serializable{
    private int nCodPers;
    private int nIdFlujoMaestro;
    private String cMensaje;

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }

    public String getcMensaje() {
        return cMensaje;
    }

    public void setcMensaje(String cMensaje) {
        this.cMensaje = cMensaje;
    }
}
