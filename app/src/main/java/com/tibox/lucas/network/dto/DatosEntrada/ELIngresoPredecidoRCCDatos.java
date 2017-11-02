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
public class ELIngresoPredecidoRCCDatos {
    
    public int nEstadoCivil;
    public ELDocumento oDocumentoTitular;
    public ELDocumento oDocumentoConyuge;
    
    public void ELIngresoPredecidoRCCDatos()
    {
        this.nEstadoCivil = 0;
        this.oDocumentoTitular = new ELDocumento();
        this.oDocumentoConyuge = new ELDocumento();
    }
    
}
