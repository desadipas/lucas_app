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
public class ELScoreBuroResultado {

    public double nScoreBuro;
    public Boolean bError;
    public String sMensajeError;
    
    public void ELScoreBuroResultado()
    {
        this.nScoreBuro = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}
