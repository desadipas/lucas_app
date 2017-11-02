package com.tibox.lucas.network.dto.DatosSalida;

import java.util.Date;

/**
 * Created by desa02 on 29/08/2017.
 */

public class Calendario {
    private String cFechaPago;
    private String fechaPago;
    private int cuota;
    private String montoCuota;
    private double capital;
    private double interes;
    private double gastos;
    private double saldos;
    private double saldoCalendario;

    public String getcFechaPago() {
        return cFechaPago;
    }

    public void setcFechaPago(String cFechaPago) {
        this.cFechaPago = cFechaPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public String getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(String montoCuota) {
        this.montoCuota = montoCuota;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getGastos() {
        return gastos;
    }

    public void setGastos(double gastos) {
        this.gastos = gastos;
    }

    public double getSaldos() {
        return saldos;
    }

    public void setSaldos(double saldos) {
        this.saldos = saldos;
    }

    public double getSaldoCalendario() {
        return saldoCalendario;
    }

    public void setSaldoCalendario(double saldoCalendario) {
        this.saldoCalendario = saldoCalendario;
    }
}
