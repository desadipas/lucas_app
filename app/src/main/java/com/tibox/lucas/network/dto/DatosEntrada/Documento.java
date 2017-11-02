package com.tibox.lucas.network.dto.DatosEntrada;

/**
 * Created by desa02 on 22/08/2017.
 */

public class Documento {
    private int nCodigo;
    private String cNombreDoc;
    private int nCodAge;
    private int nCodCred;
    private byte[] iImagen;
    private String cNomArchivo;
    private String cExtencion;
    private int nIdFlujoMaestro;
    private String cTipoArchivo;
    private String oDocumento;

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }

    public String getcNombreDoc() {
        return cNombreDoc;
    }

    public void setcNombreDoc(String cNombreDoc) {
        this.cNombreDoc = cNombreDoc;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public int getnCodCred() {
        return nCodCred;
    }

    public void setnCodCred(int nCodCred) {
        this.nCodCred = nCodCred;
    }

    public byte[] getiImagen() {
        return iImagen;
    }

    public void setiImagen(byte[] iImagen) {
        this.iImagen = iImagen;
    }

    public String getcNomArchivo() {
        return cNomArchivo;
    }

    public void setcNomArchivo(String cNomArchivo) {
        this.cNomArchivo = cNomArchivo;
    }

    public String getcExtencion() {
        return cExtencion;
    }

    public void setcExtencion(String cExtencion) {
        this.cExtencion = cExtencion;
    }

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }

    public String getcTipoArchivo() {
        return cTipoArchivo;
    }

    public void setcTipoArchivo(String cTipoArchivo) {
        this.cTipoArchivo = cTipoArchivo;
    }

    public String getoDocumento() {
        return oDocumento;
    }

    public void setoDocumento(String oDocumento) {
        this.oDocumento = oDocumento;
    }
}
