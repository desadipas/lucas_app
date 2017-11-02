package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 01/03/2017.
 */
//implements Serializable
public class Autenticacion implements Serializable {

    private int nCodAge;
    private int nCodPers;
    private int nCodUsu;
    private String cNomUsu;
    private int nRol;
    private String cUsername;
    private int nCodDepAge;
    private String access_token; // como DNI
    private String nTipo;
    private int nIdRol;
    private String cRol;
    private int nTipoEmpresa;
    private int nCodEmpresa;
    private int nCodPais;
    private String cMoneda;

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public int getnCodUsu() {
        return nCodUsu;
    }

    public void setnCodUsu(int nCodUsu) {
        this.nCodUsu = nCodUsu;
    }

    public String getcNomUsu() {
        return cNomUsu;
    }

    public void setcNomUsu(String cNomUsu) {
        this.cNomUsu = cNomUsu;
    }

    public int getnRol() {
        return nRol;
    }

    public void setnRol(int nRol) {
        this.nRol = nRol;
    }



    public int getnCodDepAge() {
        return nCodDepAge;
    }

    public void setnCodDepAge(int nCodDepAge) {
        this.nCodDepAge = nCodDepAge;
    }



    public int getnIdRol() {
        return nIdRol;
    }

    public void setnIdRol(int nIdRol) {
        this.nIdRol = nIdRol;
    }

    public String getcRol() {
        return cRol;
    }

    public void setcRol(String cRol) {
        this.cRol = cRol;
    }


    public String getcMoneda() {
        return cMoneda;
    }

    public void setcMoneda(String cMoneda) {
        this.cMoneda = cMoneda;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getcUsername() {
        return cUsername;
    }

    public void setcUsername(String cUsername) {
        this.cUsername = cUsername;
    }

    public String getnTipo() {
        return nTipo;
    }

    public void setnTipo(String nTipo) {
        this.nTipo = nTipo;
    }

    public int getnTipoEmpresa() {
        return nTipoEmpresa;
    }

    public void setnTipoEmpresa(int nTipoEmpresa) {
        this.nTipoEmpresa = nTipoEmpresa;
    }

    public int getnCodEmpresa() {
        return nCodEmpresa;
    }

    public void setnCodEmpresa(int nCodEmpresa) {
        this.nCodEmpresa = nCodEmpresa;
    }

    public int getnCodPais() {
        return nCodPais;
    }

    public void setnCodPais(int nCodPais) {
        this.nCodPais = nCodPais;
    }
}
