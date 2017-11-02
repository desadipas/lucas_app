/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tibox.lucas.network.dto.DatosEntrada;

/**
 *
 * @author Raúl Alva de inversiones ralva
 */
public class ELIngresoPredecidoDemograficoDatos {
    
    public double nConstante;
    public int nGenero;
    public int nEstadoCivil;
    public int nEdad;
    public String sUbiGeo;
    public int sCIIU;
    
    public void ELIngresoPredecidoDemograficoDatos()
    {
        this.nConstante = 0.0;
        this.nGenero = 0;
        this.nEstadoCivil = 0;
        this.nEdad = 0;
        this.sUbiGeo = "";
        this.sCIIU = 0;
    }
    
}
