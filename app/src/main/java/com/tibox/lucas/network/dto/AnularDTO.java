package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 21/06/2017.
 */

public class AnularDTO {
    private String cUser;
    private String cComentario;
    private int nIdFlujoMaestro;

    public String getcUser() {
        return cUser;
    }

    public void setcUser(String cUser) {
        this.cUser = cUser;
    }

    public String getcComentario() {
        return cComentario;
    }

    public void setcComentario(String cComentario) {
        this.cComentario = cComentario;
    }

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }
}
