package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 23/06/2017.
 */

public class UsuarioLucasDTO {
    private String email;
    private String passwordencriptado;
    private int codpers;
    private String phonenumber;
    private String passwordanterior;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordencriptado() {
        return passwordencriptado;
    }

    public void setPasswordencriptado(String passwordencriptado) {
        this.passwordencriptado = passwordencriptado;
    }

    public int getCodpers() {
        return codpers;
    }

    public void setCodpers(int codpers) {
        this.codpers = codpers;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPasswordanterior() {
        return passwordanterior;
    }

    public void setPasswordanterior(String passwordanterior) {
        this.passwordanterior = passwordanterior;
    }
}
