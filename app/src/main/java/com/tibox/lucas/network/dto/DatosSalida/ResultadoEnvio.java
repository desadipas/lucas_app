package com.tibox.lucas.network.dto.DatosSalida;

/**
 * Created by desa02 on 18/08/2017.
 */

public class ResultadoEnvio {
    private boolean bresultado;
    private boolean bError;
    private String cMensaje;

    public boolean isBresultado() {
        return bresultado;
    }

    public void setBresultado(boolean bresultado) {
        this.bresultado = bresultado;
    }

    public boolean isbError() {
        return bError;
    }

    public void setbError(boolean bError) {
        this.bError = bError;
    }

    public String getcMensaje() {
        return cMensaje;
    }

    public void setcMensaje(String cMensaje) {
        this.cMensaje = cMensaje;
    }
}
