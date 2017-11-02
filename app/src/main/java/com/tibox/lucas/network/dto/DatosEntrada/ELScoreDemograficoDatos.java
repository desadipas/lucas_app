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
public class ELScoreDemograficoDatos {
    
    public ELDocumento oDocumento;
    public int nCondicionLaboral;
    public int nGenero;
    public String nDepartamento;
    public double nIngresoSalarial;
    public int nEdad;
    public double nMoraComercial;
    public int nEstadoCivil;
    public int nVivienda;
    public double nRankingTop10000;
    
    
    public void ELScoreDemograficoDatos()
    {
        this.oDocumento = new ELDocumento();
        this.nCondicionLaboral = 0;
        this.nGenero = 0;
        this.nDepartamento = "";
        this.nIngresoSalarial = 0.0;
        this.nEdad = 0;
        this.nMoraComercial = 0.0;
        this.nEstadoCivil = 0;
        this.nVivienda = 0;
        this.nRankingTop10000 = 0.0;
    }
}
