/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tibox.lucas.network.dto.DatosSalida;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Raúl Alva de inversiones ralva
 */
public class ELScoringBuroCuotaUtilizadaResultado {
    
    public List<CuotaUtilizadaItems> DatosCuotaUtilizada;
    public double nCuotaUtilizada;
    public boolean bError;
    public String sMensajeError;
    
    public void ELScoringBuroCuotaUtilizadaResultado()
    {
        this.DatosCuotaUtilizada = new LinkedList<CuotaUtilizadaItems>();
        this.nCuotaUtilizada = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}
