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
public class ELIngresoPredecidoRCCResultado {
    
    public List<DeudaRCCItems> Deudas;
    public double nIngresoPredecidoRCC1;
    public double nIngresoPredecidoRCC2;
    public double nIngresoPredecidoRCC3;
    public boolean bError;
    public String sMensajeError;
    
    public void ELIngresoPredecidoRCCResultado()
    {
        this.Deudas = new LinkedList<DeudaRCCItems>();
        this.nIngresoPredecidoRCC1 = 0.0;
        this.nIngresoPredecidoRCC2 = 0.0;
        this.nIngresoPredecidoRCC3 = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}
