package com.tibox.lucas.network.dto.DatosEntrada;

import java.io.Serializable;

/**
 * Created by desa02 on 14/08/2017.
 */

public class User implements Serializable{
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private int nTipo;
    private int nCodPers;
    private String cMovil;


    public int getnTipo() {
        return nTipo;
    }

    public void setnTipo(int nTipo) {
        this.nTipo = nTipo;
    }

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public String getcMovil() {
        return cMovil;
    }

    public void setcMovil(String cMovil) {
        this.cMovil = cMovil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
