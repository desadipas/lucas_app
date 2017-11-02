package com.tibox.lucas.network.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by desa02 on 07/04/2017.
 */

public class CreditoGarantiaDTO implements Serializable{

    private int nMoneda;
    private int nCodAge;
    private double nPlazo;
    private double nTasaComp;
    private int nProd;
    private int nSubProd;
    private int nIdFlujoMaestro;
    private String cNomForm;
    private String cUsuReg;
    private int nCodPersReg;
    private int nIdFlujo;
    private int nOrdenFlujo;
    private List<GarantiasDTO> oXmlLista;
    private int nTipoEnvio;
    private int nCodCred;
    private int nCodPers;

    public int getnMoneda() {
        return nMoneda;
    }

    public void setnMoneda(int nMoneda) {
        this.nMoneda = nMoneda;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public double getnPlazo() {
        return nPlazo;
    }

    public void setnPlazo(double nPlazo) {
        this.nPlazo = nPlazo;
    }

    public double getnTasaComp() {
        return nTasaComp;
    }

    public void setnTasaComp(double nTasaComp) {
        this.nTasaComp = nTasaComp;
    }

    public int getnProd() {
        return nProd;
    }

    public void setnProd(int nProd) {
        this.nProd = nProd;
    }

    public int getnSubProd() {
        return nSubProd;
    }

    public void setnSubProd(int nSubProd) {
        this.nSubProd = nSubProd;
    }

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }


    public String getcUsuReg() {
        return cUsuReg;
    }

    public void setcUsuReg(String cUsuReg) {
        this.cUsuReg = cUsuReg;
    }

    public int getnCodPersReg() {
        return nCodPersReg;
    }

    public void setnCodPersReg(int nCodPersReg) {
        this.nCodPersReg = nCodPersReg;
    }

    public int getnIdFlujo() {
        return nIdFlujo;
    }

    public void setnIdFlujo(int nIdFlujo) {
        this.nIdFlujo = nIdFlujo;
    }

    public int getnOrdenFlujo() {
        return nOrdenFlujo;
    }

    public void setnOrdenFlujo(int nOrdenFlujo) {
        this.nOrdenFlujo = nOrdenFlujo;
    }


    public String getcNomForm() {
        return cNomForm;
    }

    public void setcNomForm(String cNomForm) {
        this.cNomForm = cNomForm;
    }

    public List<GarantiasDTO> getoXmlLista() {
        return oXmlLista;
    }

    public void setoXmlLista(List<GarantiasDTO> oXmlLista) {
        this.oXmlLista = oXmlLista;
    }

    public int getnTipoEnvio() {
        return nTipoEnvio;
    }

    public void setnTipoEnvio(int nTipoEnvio) {
        this.nTipoEnvio = nTipoEnvio;
    }

    public int getnCodCred() {
        return nCodCred;
    }

    public void setnCodCred(int nCodCred) {
        this.nCodCred = nCodCred;
    }

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }
}
