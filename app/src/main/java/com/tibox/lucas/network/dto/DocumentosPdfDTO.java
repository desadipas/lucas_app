package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 29/05/2017.
 */

public class DocumentosPdfDTO {

    private int nAgencia;
    private int nCredito;
    private int nTipoDoc;

    public int getnAgencia() {
        return nAgencia;
    }

    public void setnAgencia(int nAgencia) {
        this.nAgencia = nAgencia;
    }

    public int getnCredito() {
        return nCredito;
    }

    public void setnCredito(int nCredito) {
        this.nCredito = nCredito;
    }

    public int getnTipoDoc() {
        return nTipoDoc;
    }

    public void setnTipoDoc(int nTipoDoc) {
        this.nTipoDoc = nTipoDoc;
    }
}
