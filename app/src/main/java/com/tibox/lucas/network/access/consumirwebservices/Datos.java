/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tibox.lucas.network.access.consumirwebservices;

/**
 *
 * @author Raúl Alva de inversiones ralva
 */
public class Datos {
    public int nTipoServicio;
    public Object oDatosServicio;
    public String sDescripcion;
    
    public Datos()
    {
    this.nTipoServicio = 0;
    this.oDatosServicio = new Object();
    this.sDescripcion = "";
    }
}
