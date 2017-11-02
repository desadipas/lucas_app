package com.tibox.lucas.network.dto.DatosEntrada;

/**
 * Created by desa02 on 01/09/2017.
 */

public class MontoCuota {
    private double nPrestamo;
    private int nNroCuotas;
    private int nPeriodo;
    private double nTasa;
    private String dFechaSistema;
    private double nSeguro;

    public double getnPrestamo() {
        return nPrestamo;
    }

    public void setnPrestamo(double nPrestamo) {
        this.nPrestamo = nPrestamo;
    }

    public int getnNroCuotas() {
        return nNroCuotas;
    }

    public void setnNroCuotas(int nNroCuotas) {
        this.nNroCuotas = nNroCuotas;
    }

    public int getnPeriodo() {
        return nPeriodo;
    }

    public void setnPeriodo(int nPeriodo) {
        this.nPeriodo = nPeriodo;
    }

    public double getnTasa() {
        return nTasa;
    }

    public void setnTasa(double nTasa) {
        this.nTasa = nTasa;
    }

    public String getdFechaSistema() {
        return dFechaSistema;
    }

    public void setdFechaSistema(String dFechaSistema) {
        this.dFechaSistema = dFechaSistema;
    }

    public double getnSeguro() {
        return nSeguro;
    }

    public void setnSeguro(double nSeguro) {
        this.nSeguro = nSeguro;
    }
}
