package com.tibox.lucas.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 09/03/2017.
 */

public class DatosSimulacion implements Parcelable {
    private double nPrestamo;
    private int nCuota;
    private double nTasaInteres;
    private double nMontoCuota;
    private int nEstado;

    public DatosSimulacion(){}

    public double getnPrestamo() {
        return nPrestamo;
    }

    public void setnPrestamo(double nPrestamo) {
        this.nPrestamo = nPrestamo;
    }

    public int getnCuota() {
        return nCuota;
    }

    public void setnCuota(int nCuota) {
        this.nCuota = nCuota;
    }

    public double getnTasaInteres() {
        return nTasaInteres;
    }

    public void setnTasaInteres(double nTasaInteres) {
        this.nTasaInteres = nTasaInteres;
    }

    public double getnMontoCuota() {
        return nMontoCuota;
    }

    public void setnMontoCuota(double nMontoCuota) {
        this.nMontoCuota = nMontoCuota;
    }

    public int getnEstado() {
        return nEstado;
    }

    public void setnEstado(int nEstado) {
        this.nEstado = nEstado;
    }

    protected DatosSimulacion(Parcel in) {
        nPrestamo = in.readDouble();
        nCuota = in.readInt();
        nTasaInteres = in.readDouble();
        nMontoCuota = in.readDouble();
        nEstado = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(nPrestamo);
        dest.writeInt(nCuota);
        dest.writeDouble(nTasaInteres);
        dest.writeDouble(nMontoCuota);
        dest.writeInt(nEstado);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DatosSimulacion> CREATOR = new Parcelable.Creator<DatosSimulacion>() {
        @Override
        public DatosSimulacion createFromParcel(Parcel in) {
            return new DatosSimulacion(in);
        }

        @Override
        public DatosSimulacion[] newArray(int size) {
            return new DatosSimulacion[size];
        }
    };
}