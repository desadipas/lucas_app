package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 16/03/2017.
 */

public class FamiliaGarantia implements Parcelable {
    private int nIdCodigo;
    private int nNivel;
    private String cDescripcion;
    private int nIdCodigoSuperior;
    private double nPorValorTasado;
    private double nValorTasado;
    private double nPrestamoTotal;

    public FamiliaGarantia(){}

    public int getnIdCodigo() {
        return nIdCodigo;
    }

    public void setnIdCodigo(int nIdCodigo) {
        this.nIdCodigo = nIdCodigo;
    }

    public int getnNivel() {
        return nNivel;
    }

    public void setnNivel(int nNivel) {
        this.nNivel = nNivel;
    }

    public String getcDescripcion() {
        return cDescripcion;
    }

    public void setcDescripcion(String cDescripcion) {
        this.cDescripcion = cDescripcion;
    }

    public int getnIdCodigoSuperior() {
        return nIdCodigoSuperior;
    }

    public void setnIdCodigoSuperior(int nIdCodigoSuperior) {
        this.nIdCodigoSuperior = nIdCodigoSuperior;
    }

    public double getnPorValorTasado() {
        return nPorValorTasado;
    }

    public void setnPorValorTasado(double nPorValorTasado) {
        this.nPorValorTasado = nPorValorTasado;
    }

    public double getnValorTasado() {
        return nValorTasado;
    }

    public void setnValorTasado(double nValorTasado) {
        this.nValorTasado = nValorTasado;
    }

    public double getnPrestamoTotal() {
        return nPrestamoTotal;
    }

    public void setnPrestamoTotal(double nPrestamoTotal) {
        this.nPrestamoTotal = nPrestamoTotal;
    }

    protected FamiliaGarantia(Parcel in) {
        nIdCodigo = in.readInt();
        nNivel = in.readInt();
        cDescripcion = in.readString();
        nIdCodigoSuperior = in.readInt();
        nPorValorTasado = in.readDouble();
        nValorTasado = in.readDouble();
        nPrestamoTotal = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nIdCodigo);
        dest.writeInt(nNivel);
        dest.writeString(cDescripcion);
        dest.writeInt(nIdCodigoSuperior);
        dest.writeDouble(nPorValorTasado);
        dest.writeDouble(nValorTasado);
        dest.writeDouble(nPrestamoTotal);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FamiliaGarantia> CREATOR = new Parcelable.Creator<FamiliaGarantia>() {
        @Override
        public FamiliaGarantia createFromParcel(Parcel in) {
            return new FamiliaGarantia(in);
        }

        @Override
        public FamiliaGarantia[] newArray(int size) {
            return new FamiliaGarantia[size];
        }
    };
}