package com.tibox.lucas.network.dto.DatosEntrada;

/**
 * Created by desa02 on 19/07/2017.
 */

public class ELIngresoCliente {
    public double nIngresoPredecido;
    public double nIngresoDeclarado;
    public double nIngresoFinal;

    public ELIngresoCliente(){
        this.nIngresoDeclarado = 0;
        this.nIngresoPredecido = 0;
        this.nIngresoFinal = 0;
    }
}
