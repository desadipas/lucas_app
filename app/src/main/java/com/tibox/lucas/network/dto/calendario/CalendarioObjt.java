package com.tibox.lucas.network.dto.calendario;

import java.util.Date;

/**
 * Created by desa02 on 05/10/2017.
 */

public class CalendarioObjt {
    private double monto;
    private Date fecha;

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
