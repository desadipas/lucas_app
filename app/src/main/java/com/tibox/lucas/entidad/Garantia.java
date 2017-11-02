package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 14/03/2017.
 */

public class Garantia implements Parcelable {
    private int IdGarantia;
    private int Familia;
    private int Articulo;
    private int Marca;
    private int Linea;
    private int Modelo;
    private String Descripcion;
    private String DescripcionGarantia;
    private double Prestamo;
    private double VTasado;
    private String cNomFamilia;
    private String cNomArticulo;
    private String cNomMarca;
    private String cNomLinea;
    private String cNomModelo;

    public Garantia (){}

    public int getIdGarantia() {
        return IdGarantia;
    }

    public void setIdGarantia(int idGarantia) {
        IdGarantia = idGarantia;
    }

    public int getFamilia() {
        return Familia;
    }

    public void setFamilia(int familia) {
        Familia = familia;
    }

    public int getArticulo() {
        return Articulo;
    }

    public void setArticulo(int articulo) {
        Articulo = articulo;
    }

    public int getMarca() {
        return Marca;
    }

    public void setMarca(int marca) {
        Marca = marca;
    }

    public int getLinea() {
        return Linea;
    }

    public void setLinea(int linea) {
        Linea = linea;
    }

    public int getModelo() {
        return Modelo;
    }

    public void setModelo(int modelo) {
        Modelo = modelo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getPrestamo() {
        return Prestamo;
    }

    public void setPrestamo(double prestamo) {
        Prestamo = prestamo;
    }

    public String getDescripcionGarantia() {
        return DescripcionGarantia;
    }

    public void setDescripcionGarantia(String descripcionGarantia) {
        DescripcionGarantia = descripcionGarantia;
    }

    public double getVTasado() {
        return VTasado;
    }

    public void setVTasado(double VTasado) {
        this.VTasado = VTasado;
    }

    public String getcNomFamilia() {
        return cNomFamilia;
    }

    public void setcNomFamilia(String cNomFamilia) {
        this.cNomFamilia = cNomFamilia;
    }

    public String getcNomArticulo() {
        return cNomArticulo;
    }

    public void setcNomArticulo(String cNomArticulo) {
        this.cNomArticulo = cNomArticulo;
    }

    public String getcNomMarca() {
        return cNomMarca;
    }

    public void setcNomMarca(String cNomMarca) {
        this.cNomMarca = cNomMarca;
    }

    public String getcNomLinea() {
        return cNomLinea;
    }

    public void setcNomLinea(String cNomLinea) {
        this.cNomLinea = cNomLinea;
    }

    public String getcNomModelo() {
        return cNomModelo;
    }

    public void setcNomModelo(String cNomModelo) {
        this.cNomModelo = cNomModelo;
    }

    protected Garantia(Parcel in) {
        IdGarantia = in.readInt();
        Familia = in.readInt();
        Articulo = in.readInt();
        Marca = in.readInt();
        Linea = in.readInt();
        Modelo = in.readInt();
        Descripcion = in.readString();
        DescripcionGarantia = in.readString();
        Prestamo = in.readDouble();
        VTasado = in.readDouble();
        cNomFamilia = in.readString();
        cNomArticulo = in.readString();
        cNomMarca = in.readString();
        cNomLinea = in.readString();
        cNomModelo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdGarantia);
        dest.writeInt(Familia);
        dest.writeInt(Articulo);
        dest.writeInt(Marca);
        dest.writeInt(Linea);
        dest.writeInt(Modelo);
        dest.writeString(Descripcion);
        dest.writeString(DescripcionGarantia);
        dest.writeDouble(Prestamo);
        dest.writeDouble(VTasado);
        dest.writeString(cNomFamilia);
        dest.writeString(cNomArticulo);
        dest.writeString(cNomMarca);
        dest.writeString(cNomLinea);
        dest.writeString(cNomModelo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Garantia> CREATOR = new Parcelable.Creator<Garantia>() {
        @Override
        public Garantia createFromParcel(Parcel in) {
            return new Garantia(in);
        }

        @Override
        public Garantia[] newArray(int size) {
            return new Garantia[size];
        }
    };
}