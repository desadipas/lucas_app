/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tibox.lucas.network.dto.DatosSalida;

/**
 *
 * @author Raúl Alva de inversiones ralva
 */
public class ELScoringDemograficoResultado {
    public int nDecicion;
    public double nCuotaMaxima;
    public double nCuotaUtilizada;
    public double nCuotaDisponible;
    public double nPrestamo1;
    public double nPrestamo2;
    public double nPrestamo3;
    public double nPrestamo4;
    public double nTasa;
    public int nPlazo;
    public double nPrestamoMinimo;
    public double nPrestamoMaximo;
    public double nPorcGarantiaAvaluo;
    public double nRCI;
    public double nRMA;
    public int nCategoria;
    public String sDescripcionRechazo;
    public double nPorcentajeInicial;
    public boolean bError;
    public String sMensajeError;
    
    public void ELScoringBuroResultado()
    {
        this.nDecicion = 0;
        this.nCuotaMaxima = 0.0;
        this.nCuotaUtilizada = 0.0;
        this.nCuotaDisponible = 0.0;
        this.nPrestamo1 = 0.0;
        this.nPrestamo2 = 0.0;
        this.nPrestamo3 = 0.0;
        this.nPrestamo4 = 0.0;
        this.nTasa = 0.0;
        this.nPlazo = 0;
        this.nPrestamoMinimo = 0.0;
        this.nPrestamoMaximo = 0.0;
        this.nPorcGarantiaAvaluo = 0.0;
        this.nRCI = 0.0;
        this.nRMA = 0.0;
        this.nCategoria = 0;
        this.sDescripcionRechazo = "";
        this.nPorcentajeInicial = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
}
