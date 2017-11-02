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
public class ELIngresoPredecidoResultado {
    
    public double nIngresoPredecidoFinal1;
    public double nIngresoPredecidoFinal2;
    public boolean bError;
    public String sMensajeError;
    
    public void ELIngresoPredecidoResultado()
    {
        this.nIngresoPredecidoFinal1 = 0.0;
        this.nIngresoPredecidoFinal2 = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}
