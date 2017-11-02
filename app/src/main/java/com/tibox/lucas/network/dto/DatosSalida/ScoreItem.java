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
public class ScoreItem {
    
    public int nScoreId;
    public String sScore;
    public String sScoreDescripcion;
    public double nScorePuntaje;
    
    public void ScoreItem()
    {
        this.nScoreId = 0;
        this.sScore = "";
        this.sScoreDescripcion = "";
        this.nScorePuntaje = 0.0;
    }
    
}
