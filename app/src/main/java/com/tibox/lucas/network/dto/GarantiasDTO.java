package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 07/04/2017.
 */

public class GarantiasDTO implements Serializable {
    private String Familia;
    private String Articulo;
    private String Marca;
    private String Linea;
    private String Modelo;
    private int nCodFamilia;
    private int nCodArticulo;
    private int nCodMarca;
    private int nCodLinea;
    private int nCodModelo;
    private double Prestamo;
    private double VCompra;
    private String Descripción;

    public String getFamilia() {
        return Familia;
    }

    public void setFamilia(String familia) {
        Familia = familia;
    }

    public String getArticulo() {
        return Articulo;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getLinea() {
        return Linea;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
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

    public int getnCodModelo() {
        return nCodModelo;
    }

    public void setnCodModelo(int nCodModelo) {
        this.nCodModelo = nCodModelo;
    }

    public double getPrestamo() {
        return Prestamo;
    }

    public void setPrestamo(double prestamo) {
        Prestamo = prestamo;
    }

    public double getVCompra() {
        return VCompra;
    }

    public void setVCompra(double VCompra) {
        this.VCompra = VCompra;
    }

    public String getDescripción() {
        return Descripción;
    }

    public void setDescripción(String descripción) {
        Descripción = descripción;
    }
}
