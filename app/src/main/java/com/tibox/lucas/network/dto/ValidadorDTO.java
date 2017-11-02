package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 20/06/2017.
 */

public class ValidadorDTO {
    private boolean bValor;
    private String cMensaje;

    public boolean isbValor() {
        return bValor;
    }

    public void setbValor(boolean bValor) {
        this.bValor = bValor;
    }

    public String getcMensaje() {
        return cMensaje;
    }

    public void setcMensaje(String cMensaje) {
        this.cMensaje = cMensaje;
    }
}
