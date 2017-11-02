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
public class DeudaRCCItems {
    
    public int nTipoId;
    public String sTipoDescripcion;
    public String sCaracteristica;
    public double nMonto;
    public double nFactor;
    public double nInferencia;
    
    public void DeudaRCCItems()
    {
        this.nTipoId = 0;
        this.sTipoDescripcion = "";
        this.sCaracteristica = "";
        this.nMonto = 0.0;
        this.nFactor = 0.0;
        this.nInferencia = 0.0;
    }
}
