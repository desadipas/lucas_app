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
public class ELIngresoPredecidoDemograficoResultado {
    
    public List<PuntajeIPDItems> PuntajeIPDItems;
    public double nIngresoPredecidoDemografico1;
    public double nIngresoPredecidoDemografico2;
    public double nIngresoPredecidoDemografico3;
    public boolean bError;
    public String sMensajeError;
    
    public void ELIngresoPredecidoDemograficoResultado()
    {
        this.PuntajeIPDItems = new LinkedList<PuntajeIPDItems>();
        this.nIngresoPredecidoDemografico1 = 0.0;
        this.nIngresoPredecidoDemografico2 = 0.0;
        this.nIngresoPredecidoDemografico3 = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}
