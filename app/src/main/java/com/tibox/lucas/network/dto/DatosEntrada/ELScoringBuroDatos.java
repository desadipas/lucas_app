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
public class ELScoringBuroDatos {
    public int nProducto;
    public int nModalidad;
    public ELDocumento oDocumentoTitual;
    public double nIngresoPredecido;
    public double nMoraComercial;
    public double nCuotaUtilizada;
    public double nScore;
    public double nScoreOtros;
    public double nGarantia;
    
    public void ELScoringBuroDatos()
    {
        this.nProducto = 0;
        this.nModalidad = 0;
        this.oDocumentoTitual = new ELDocumento();
        this.nIngresoPredecido = 0.0;
        this.nMoraComercial = 0.0;
        this.nCuotaUtilizada = 0.0;
        this.nScore = 0.0;
        this.nScore = 0.0;
        this.nGarantia = 0.0;
    }
    
}
