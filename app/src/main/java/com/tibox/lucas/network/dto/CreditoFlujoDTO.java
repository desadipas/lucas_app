package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 19/04/2017.
 */

public class CreditoFlujoDTO {
    private double nTasaComp;
    private double nPrestamo;
    private int nCodAge;
    private int m_nCodCred;
    private double nPrestamoMax;
    private double nPrestamoMin;
    private double nPorGarantia;

    private int nPlazo;
    private double nCuotaDisp;
    private String cDesicion;
    private String cTipoBanca;

    public double getnTasaComp() {
        return nTasaComp;
    }

    public void setnTasaComp(double nTasaComp) {
        this.nTasaComp = nTasaComp;
    }

    public double getnPrestamo() {
        return nPrestamo;
    }

    public void setnPrestamo(double nPrestamo) {
        this.nPrestamo = nPrestamo;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public int getM_nCodCred() {
        return m_nCodCred;
    }

    public void setM_nCodCred(int m_nCodCred) {
        this.m_nCodCred = m_nCodCred;
    }

    public double getnPrestamoMax() {
        return nPrestamoMax;
    }

    public void setnPrestamoMax(double nPrestamoMax) {
        this.nPrestamoMax = nPrestamoMax;
    }

    public double getnPrestamoMin() {
        return nPrestamoMin;
    }

    public void setnPrestamoMin(double nPrestamoMin) {
        this.nPrestamoMin = nPrestamoMin;
    }

    public double getnPorGarantia() {
        return nPorGarantia;
    }

    public void setnPorGarantia(double nPorGarantia) {
        this.nPorGarantia = nPorGarantia;
    }

    public int getnPlazo() {
        return nPlazo;
    }

    public void setnPlazo(int nPlazo) {
        this.nPlazo = nPlazo;
    }

    public double getnCuotaDisp() {
        return nCuotaDisp;
    }

    public void setnCuotaDisp(double nCuotaDisp) {
        this.nCuotaDisp = nCuotaDisp;
    }

    public String getcDesicion() {
        return cDesicion;
    }

    public void setcDesicion(String cDesicion) {
        this.cDesicion = cDesicion;
    }

    public String getcTipoBanca() {
        return cTipoBanca;
    }

    public void setcTipoBanca(String cTipoBanca) {
        this.cTipoBanca = cTipoBanca;
    }
}
