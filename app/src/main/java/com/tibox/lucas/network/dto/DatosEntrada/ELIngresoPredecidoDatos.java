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
public class ELIngresoPredecidoDatos {
    public ELIngresoPredecidoDemograficoDatos oDatosDemografico;
    public ELIngresoPredecidoRCCDatos oDatosRCC;
    public ELDocumento oDocumentoTitular;
    public double nIngresoPredecidoDemografico;
    public double nIngresoPredecidoRCC;
    
    public void ELIngresoPredecidoDatos()
    {
        this.oDatosDemografico = new ELIngresoPredecidoDemograficoDatos();
        this.oDatosRCC = new ELIngresoPredecidoRCCDatos();
        this.oDocumentoTitular = new ELDocumento();
        this.nIngresoPredecidoDemografico = 0.0;
        this.nIngresoPredecidoRCC = 0.0;
    }
    
}
