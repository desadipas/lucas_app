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
public class ELScoreComportamientoResultado {
    
    public double nScoreComportamiento;
    public boolean bError;
    public String sMensajeError;
    
    public void ELScoreComportamientoResultado()
    {
        this.nScoreComportamiento = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}
