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
public class CuotaUtilizadaItems {
    
    public int nTipoId;
    public String sTipoDescripcion;
    public double nMonto;
    public int nPlazo;
    public double nTasa;
    public double nFactorUtilizacion;
    public double nCuota;
    
    public void CuotaUtilizadaItems()
    {
        this.nTipoId = 0;
        this.sTipoDescripcion = "";
        this.nMonto = 0.0;
        this.nPlazo = 0;
        this.nTasa = 0.0;
        this.nFactorUtilizacion = 0.0;
        this.nCuota = 0.0;        
    }
    
}
