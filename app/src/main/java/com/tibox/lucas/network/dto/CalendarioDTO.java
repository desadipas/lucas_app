package com.tibox.lucas.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by desa02 on 23/06/2017.
 */

public class CalendarioDTO implements Parcelable {
    private int nNrosubCuota;
    private double nCapital;
    private double nIntComp;
    private String dFecVenc;
    private String dFecPago;
    private String dFecCob;
    private double nSeguro;

    public CalendarioDTO(  ){

    }

    public int getnNrosubCuota() {
        return nNrosubCuota;
    }

    public void setnNrosubCuota(int nNrosubCuota) {
        this.nNrosubCuota = nNrosubCuota;
    }

    public double getnCapital() {
        return nCapital;
    }

    public void setnCapital(double nCapital) {
        this.nCapital = nCapital;
    }

    public double getnIntComp() {
        return nIntComp;
    }

    public void setnIntComp(double nIntComp) {
        this.nIntComp = nIntComp;
    }

    public String getdFecVenc() {
        return dFecVenc;
    }

    public void setdFecVenc(String dFecVenc) {
        this.dFecVenc = dFecVenc;
    }

    public String getdFecPago() {
        return dFecPago;
    }

    public void setdFecPago(String dFecPago) {
        this.dFecPago = dFecPago;
    }

    public String getdFecCob() {
        return dFecCob;
    }

    public void setdFecCob(String dFecCob) {
        this.dFecCob = dFecCob;
    }

    public double getnSeguro() {
        return nSeguro;
    }

    public void setnSeguro(double nSeguro) {
        this.nSeguro = nSeguro;
    }

    protected CalendarioDTO(Parcel in) {
        nNrosubCuota = in.readInt();
        nCapital = in.readDouble();
        nIntComp = in.readDouble();
        dFecVenc = in.readString();
        dFecPago = in.readString();
        dFecCob = in.readString();
        nSeguro = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nNrosubCuota);
        dest.writeDouble(nCapital);
        dest.writeDouble(nIntComp);
        dest.writeString(dFecVenc);
        dest.writeString(dFecPago);
        dest.writeString(dFecCob);
        dest.writeDouble(nSeguro);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CalendarioDTO> CREATOR = new Parcelable.Creator<CalendarioDTO>() {
        @Override
        public CalendarioDTO createFromParcel(Parcel in) {
            return new CalendarioDTO(in);
        }

        @Override
        public CalendarioDTO[] newArray(int size) {
            return new CalendarioDTO[size];
        }
    };


}