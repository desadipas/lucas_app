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
public class ELScoreDemograficoResultado {
    
    public List<ScoreItem> oScoreItems;
    public double nScoreDemografico;
    public boolean bError;
    public String sMensajeError;
    
    public void ELScoreDemograficoResultado()
    {
        this.oScoreItems = new LinkedList<ScoreItem>();
        this.nScoreDemografico = 0.0;
        this.bError = false;
        this.sMensajeError = "";
    }
    
}

