package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 27/06/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class OperacionesDTO implements Parcelable {
    private double nMontoNuevoSaldo;
    private double nIGV;
    private int nCuotaPagada;
    private double nIntComp;
    private double nCapital;
    private String dFecha;

    public OperacionesDTO(){}

    public double getnMontoNuevoSaldo() {
        return nMontoNuevoSaldo;
    }

    public void setnMontoNuevoSaldo(double nMontoNuevoSaldo) {
        this.nMontoNuevoSaldo = nMontoNuevoSaldo;
    }

    public double getnIGV() {
        return nIGV;
    }

    public void setnIGV(double nIGV) {
        this.nIGV = nIGV;
    }

    public int getnCuotaPagada() {
        return nCuotaPagada;
    }

    public void setnCuotaPagada(int nCuotaPagada) {
        this.nCuotaPagada = nCuotaPagada;
    }

    public double getnIntComp() {
        return nIntComp;
    }

    public void setnIntComp(double nIntComp) {
        this.nIntComp = nIntComp;
    }

    public double getnCapital() {
        return nCapital;
    }

    public void setnCapital(double nCapital) {
        this.nCapital = nCapital;
    }

    public String getdFecha() {
        return dFecha;
    }

    public void setdFecha(String dFecha) {
        this.dFecha = dFecha;
    }

    protected OperacionesDTO(Parcel in) {
        nMontoNuevoSaldo = in.readDouble();
        nIGV = in.readDouble();
        nCuotaPagada = in.readInt();
        nIntComp = in.readDouble();
        nCapital = in.readDouble();
        dFecha = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(nMontoNuevoSaldo);
        dest.writeDouble(nIGV);
        dest.writeInt(nCuotaPagada);
        dest.writeDouble(nIntComp);
        dest.writeDouble(nCapital);
        dest.writeString(dFecha);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OperacionesDTO> CREATOR = new Parcelable.Creator<OperacionesDTO>() {
        @Override
        public OperacionesDTO createFromParcel(Parcel in) {
            return new OperacionesDTO(in);
        }

        @Override
        public OperacionesDTO[] newArray(int size) {
            return new OperacionesDTO[size];
        }
    };
}
