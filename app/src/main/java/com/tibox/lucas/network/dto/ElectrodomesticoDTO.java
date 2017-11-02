package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 15/03/2017.
 */

public class ElectrodomesticoDTO implements Serializable {
    private String Flujomaestro;
    private String cNomArticulo;
    private String cNomFamilia;
    private String cNomLinea;
    private String cNomMarca;
    private String cNomModelo;
    private int nCodArticulo;
    private int nCodElectrodomestico;
    private int nCodFamilia;
    private int nCodLinea;
    private int nCodMarca;
    private int nCodModelo;
    private String nCodUsu;
    private int nItem;
    private int nModelo;
    private int nMoneda;
    private int nNroCuota;
    private double nPlazo;
    private double nPorValorCompra;
    private double nPorValorTasado;
    private double nPrestamo;
    private double nPrestamoTotal;
    private double nTasaComp;
    private int nTotalItem;
    private double nValorCompra;
    private double nValorTasado;
    private String oXmlLista;

    public ElectrodomesticoDTO(){}

    public String getFlujomaestro() {
        return Flujomaestro;
    }

    public void setFlujomaestro(String flujomaestro) {
        Flujomaestro = flujomaestro;
    }

    public String getcNomArticulo() {
        return cNomArticulo;
    }

    public void setcNomArticulo(String cNomArticulo) {
        this.cNomArticulo = cNomArticulo;
    }

    public String getcNomFamilia() {
        return cNomFamilia;
    }

    public void setcNomFamilia(String cNomFamilia) {
        this.cNomFamilia = cNomFamilia;
    }

    public String getcNomLinea() {
        return cNomLinea;
    }

    public void setcNomLinea(String cNomLinea) {
        this.cNomLinea = cNomLinea;
    }

    public String getcNomMarca() {
        return cNomMarca;
    }

    public void setcNomMarca(String cNomMarca) {
        this.cNomMarca = cNomMarca;
    }

    public String getcNomModelo() {
        return cNomModelo;
    }

    public void setcNomModelo(String cNomModelo) {
        this.cNomModelo = cNomModelo;
    }

    public int getnCodArticulo() {
        return nCodArticulo;
    }

    public void setnCodArticulo(int nCodArticulo) {
        this.nCodArticulo = nCodArticulo;
    }

    public int getnCodElectrodomestico() {
        return nCodElectrodomestico;
    }

    public void setnCodElectrodomestico(int nCodElectrodomestico) {
        this.nCodElectrodomestico = nCodElectrodomestico;
    }

    public int getnCodFamilia() {
        return nCodFamilia;
    }

    public void setnCodFamilia(int nCodFamilia) {
        this.nCodFamilia = nCodFamilia;
    }

    public int getnCodLinea() {
        return nCodLinea;
    }

    public void setnCodLinea(int nCodLinea) {
        this.nCodLinea = nCodLinea;
    }

    public int getnCodMarca() {
        return nCodMarca;
    }

    public void setnCodMarca(int nCodMarca) {
        this.nCodMarca = nCodMarca;
    }

    public int getnCodModelo() {
        return nCodModelo;
    }

    public void setnCodModelo(int nCodModelo) {
        this.nCodModelo = nCodModelo;
    }

    public String getnCodUsu() {
        return nCodUsu;
    }

    public void setnCodUsu(String nCodUsu) {
        this.nCodUsu = nCodUsu;
    }

    public int getnItem() {
        return nItem;
    }

    public void setnItem(int nItem) {
        this.nItem = nItem;
    }

    public int getnModelo() {
        return nModelo;
    }

    public void setnModelo(int nModelo) {
        this.nModelo = nModelo;
    }

    public int getnMoneda() {
        return nMoneda;
    }

    public void setnMoneda(int nMoneda) {
        this.nMoneda = nMoneda;
    }

    public int getnNroCuota() {
        return nNroCuota;
    }

    public void setnNroCuota(int nNroCuota) {
        this.nNroCuota = nNroCuota;
    }

    public double getnPlazo() {
        return nPlazo;
    }

    public void setnPlazo(double nPlazo) {
        this.nPlazo = nPlazo;
    }

    public double getnPorValorCompra() {
        return nPorValorCompra;
    }

    public void setnPorValorCompra(double nPorValorCompra) {
        this.nPorValorCompra = nPorValorCompra;
    }

    public double getnPorValorTasado() {
        return nPorValorTasado;
    }

    public void setnPorValorTasado(double nPorValorTasado) {
        this.nPorValorTasado = nPorValorTasado;
    }

    public double getnPrestamo() {
        return nPrestamo;
    }

    public void setnPrestamo(double nPrestamo) {
        this.nPrestamo = nPrestamo;
    }

    public double getnPrestamoTotal() {
        return nPrestamoTotal;
    }

    public void setnPrestamoTotal(double nPrestamoTotal) {
        this.nPrestamoTotal = nPrestamoTotal;
    }

    public double getnTasaComp() {
        return nTasaComp;
    }

    public void setnTasaComp(double nTasaComp) {
        this.nTasaComp = nTasaComp;
    }

    public int getnTotalItem() {
        return nTotalItem;
    }

    public void setnTotalItem(int nTotalItem) {
        this.nTotalItem = nTotalItem;
    }

    public double getnValorCompra() {
        return nValorCompra;
    }

    public void setnValorCompra(double nValorCompra) {
        this.nValorCompra = nValorCompra;
    }

    public double getnValorTasado() {
        return nValorTasado;
    }

    public void setnValorTasado(double nValorTasado) {
        this.nValorTasado = nValorTasado;
    }

    public String getoXmlLista() {
        return oXmlLista;
    }

    public void setoXmlLista(String oXmlLista) {
        this.oXmlLista = oXmlLista;
    }
}
