package com.tibox.lucas.network.dto.calendario;

import java.util.Date;

/**
 * Created by desa02 on 09/10/2017.
 */

public class ObjFecha {
    private int nId;
    private Date dFechaProgamada;

    public int getnId() {
        return nId;
    }

    public void setnId(int nId) {
        this.nId = nId;
    }

    public Date getdFechaProgamada() {
        return dFechaProgamada;
    }

    public void setdFechaProgamada(Date dFechaProgamada) {
        this.dFechaProgamada = dFechaProgamada;
    }
}
