package com.tibox.lucas.network.dto.DatosEntrada;

/**
 * Created by desa02 on 21/08/2017.
 */

public class Alerta {
    private String cMovil;
    private String cTexto;
    private String cEmail;
    private String cTitulo;

    public String getcMovil() {
        return cMovil;
    }

    public void setcMovil(String cMovil) {
        this.cMovil = cMovil;
    }

    public String getcTexto() {
        return cTexto;
    }

    public void setcTexto(String cTexto) {
        this.cTexto = cTexto;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcTitulo() {
        return cTitulo;
    }

    public void setcTitulo(String cTitulo) {
        this.cTitulo = cTitulo;
    }
}
