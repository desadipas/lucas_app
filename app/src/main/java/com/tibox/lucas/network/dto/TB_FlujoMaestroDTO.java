package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 05/04/2017.
 */

public class TB_FlujoMaestroDTO implements Serializable{

    private int nIdFlujoMaestro;
    private int nCodPers;
    private String cNroDoc;
    private int nCodAge;
    private int nCodCred;
    private int nIdFlujo;
    private boolean bEstado;
    private int nProd;
    private int nSubProd;
    private String cNomForm;
    private int nCodPersReg;
    private String cUsuReg;
    private int nOrdenFlujo;
    private boolean bActivo;
    private boolean bNecesario;
    private String cNombreProceso;
    //agragado para documento
    private String cDNI;
    private int nCodFamilia;
    private int nCodArticulo;
    private int nCodMarca;
    private int nCodLinea;
    private int nModelo;
    //CO_Documentos
    private int nIdCredito;
    private String cRutaOriginal;
    private String iImagen;
    private String FileLocation;
    private String cNomArchivo;
    private String cExtension;
    private String cTipoArchivo;
    private String cComentarioAnt;
    //
    // agregado para insertar garantia = Creditos
    private double nMontoCuota;
    private double nTasaComp;
    private double nTasaMor;
    private double nNroCuotas;
    private double nNroProxCuota;
    private double nMora;
    private int nDiasAtraso;
    private int nCondCred;
    private int nDestino;
    private int nCampaña;
    private int nPeriodo;
    private double nPrestamo;
    private String nCodUsu;
    //

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public String getcNroDoc() {
        return cNroDoc;
    }

    public void setcNroDoc(String cNroDoc) {
        this.cNroDoc = cNroDoc;
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

    public int getnIdFlujo() {
        return nIdFlujo;
    }

    public void setnIdFlujo(int nIdFlujo) {
        this.nIdFlujo = nIdFlujo;
    }

    public boolean isbEstado() {
        return bEstado;
    }

    public void setbEstado(boolean bEstado) {
        this.bEstado = bEstado;
    }

    public int getnProd() {
        return nProd;
    }

    public void setnProd(int nProd) {
        this.nProd = nProd;
    }

    public int getnSubProd() {
        return nSubProd;
    }

    public void setnSubProd(int nSubProd) {
        this.nSubProd = nSubProd;
    }

    public String getcNomForm() {
        return cNomForm;
    }

    public void setcNomForm(String cNomForm) {
        this.cNomForm = cNomForm;
    }

    public int getnCodPersReg() {
        return nCodPersReg;
    }

    public void setnCodPersReg(int nCodPersReg) {
        this.nCodPersReg = nCodPersReg;
    }

    public String getcUsuReg() {
        return cUsuReg;
    }

    public void setcUsuReg(String cUsuReg) {
        this.cUsuReg = cUsuReg;
    }

    public int getnOrdenFlujo() {
        return nOrdenFlujo;
    }

    public void setnOrdenFlujo(int nOrdenFlujo) {
        this.nOrdenFlujo = nOrdenFlujo;
    }

    public boolean isbActivo() {
        return bActivo;
    }

    public void setbActivo(boolean bActivo) {
        this.bActivo = bActivo;
    }

    public boolean isbNecesario() {
        return bNecesario;
    }

    public void setbNecesario(boolean bNecesario) {
        this.bNecesario = bNecesario;
    }

    public String getcNombreProceso() {
        return cNombreProceso;
    }

    public void setcNombreProceso(String cNombreProceso) {
        this.cNombreProceso = cNombreProceso;
    }

    public String getcDNI() {
        return cDNI;
    }

    public void setcDNI(String cDNI) {
        this.cDNI = cDNI;
    }

    public int getnCodFamilia() {
        return nCodFamilia;
    }

    public void setnCodFamilia(int nCodFamilia) {
        this.nCodFamilia = nCodFamilia;
    }

    public int getnCodArticulo() {
        return nCodArticulo;
    }

    public void setnCodArticulo(int nCodArticulo) {
        this.nCodArticulo = nCodArticulo;
    }

    public int getnCodMarca() {
        return nCodMarca;
    }

    public void setnCodMarca(int nCodMarca) {
        this.nCodMarca = nCodMarca;
    }

    public int getnCodLinea() {
        return nCodLinea;
    }

    public void setnCodLinea(int nCodLinea) {
        this.nCodLinea = nCodLinea;
    }

    public int getnModelo() {
        return nModelo;
    }

    public void setnModelo(int nModelo) {
        this.nModelo = nModelo;
    }

    public int getnIdCredito() {
        return nIdCredito;
    }

    public void setnIdCredito(int nIdCredito) {
        this.nIdCredito = nIdCredito;
    }

    public String getcRutaOriginal() {
        return cRutaOriginal;
    }

    public void setcRutaOriginal(String cRutaOriginal) {
        this.cRutaOriginal = cRutaOriginal;
    }

    public String getiImagen() {
        return iImagen;
    }

    public void setiImagen(String iImagen) {
        this.iImagen = iImagen;
    }

    public String getFileLocation() {
        return FileLocation;
    }

    public void setFileLocation(String fileLocation) {
        FileLocation = fileLocation;
    }

    public String getcNomArchivo() {
        return cNomArchivo;
    }

    public void setcNomArchivo(String cNomArchivo) {
        this.cNomArchivo = cNomArchivo;
    }

    public String getcExtension() {
        return cExtension;
    }

    public void setcExtension(String cExtension) {
        this.cExtension = cExtension;
    }

    public double getnMontoCuota() {
        return nMontoCuota;
    }

    public void setnMontoCuota(double nMontoCuota) {
        this.nMontoCuota = nMontoCuota;
    }

    public double getnTasaComp() {
        return nTasaComp;
    }

    public void setnTasaComp(double nTasaComp) {
        this.nTasaComp = nTasaComp;
    }

    public double getnTasaMor() {
        return nTasaMor;
    }

    public void setnTasaMor(double nTasaMor) {
        this.nTasaMor = nTasaMor;
    }

    public double getnNroCuotas() {
        return nNroCuotas;
    }

    public void setnNroCuotas(double nNroCuotas) {
        this.nNroCuotas = nNroCuotas;
    }

    public double getnNroProxCuota() {
        return nNroProxCuota;
    }

    public void setnNroProxCuota(double nNroProxCuota) {
        this.nNroProxCuota = nNroProxCuota;
    }

    public double getnMora() {
        return nMora;
    }

    public void setnMora(double nMora) {
        this.nMora = nMora;
    }

    public int getnDiasAtraso() {
        return nDiasAtraso;
    }

    public void setnDiasAtraso(int nDiasAtraso) {
        this.nDiasAtraso = nDiasAtraso;
    }

    public int getnCondCred() {
        return nCondCred;
    }

    public void setnCondCred(int nCondCred) {
        this.nCondCred = nCondCred;
    }

    public int getnDestino() {
        return nDestino;
    }

    public void setnDestino(int nDestino) {
        this.nDestino = nDestino;
    }

    public int getnCampaña() {
        return nCampaña;
    }

    public void setnCampaña(int nCampaña) {
        this.nCampaña = nCampaña;
    }

    public int getnPeriodo() {
        return nPeriodo;
    }

    public void setnPeriodo(int nPeriodo) {
        this.nPeriodo = nPeriodo;
    }

    public double getnPrestamo() {
        return nPrestamo;
    }

    public void setnPrestamo(double nPrestamo) {
        this.nPrestamo = nPrestamo;
    }

    public String getnCodUsu() {
        return nCodUsu;
    }

    public void setnCodUsu(String nCodUsu) {
        this.nCodUsu = nCodUsu;
    }

    public String getcTipoArchivo() {
        return cTipoArchivo;
    }

    public void setcTipoArchivo(String cTipoArchivo) {
        this.cTipoArchivo = cTipoArchivo;
    }

    public String getcComentarioAnt() {
        return cComentarioAnt;
    }

    public void setcComentarioAnt(String cComentarioAnt) {
        this.cComentarioAnt = cComentarioAnt;
    }
    //

    //private bool bRefinanciado { get; set; }
    //private int nCondVigente;
    //private int nCiclo;
    //private int nGrupo;
    //private bool bPorDesemb { get; set; }
    //private bool bAplicaItf { get; set; }
    //private int nPlazoSol;
    //private int nTipoEnvio;
    //private String cCodCta;
    //private int nIfi;
    //private int nMoneda;
    //private String cPagare;
    //private double nSaldoK;
    //private DateTime dFecVig { get; set; }

}
