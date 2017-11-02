package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 23/06/2017.
 */

public class RespuestaDTO {
    private int nRespuesta;
    private String cMensaje;

    public int getnRespuesta() {
        return nRespuesta;
    }

    public void setnRespuesta(int nRespuesta) {
        this.nRespuesta = nRespuesta;
    }

    public String getcMensaje() {
        return cMensaje;
    }

    public void setcMensaje(String cMensaje) {
        this.cMensaje = cMensaje;
    }
}
