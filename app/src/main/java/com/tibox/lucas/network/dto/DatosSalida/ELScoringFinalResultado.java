package com.tibox.lucas.network.dto.DatosSalida;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 04/04/2017.
 */

public class ELScoringFinalResultado implements Parcelable {
    private int nDecicion;
    private double nCuotaMaxima;
    private double nCuotaUtilizada;
    private double nCuotaDisponible;
    private double nPrestamo1;
    private double nPrestamo2;
    private double nPrestamo3;
    private double nPrestamo4;
    private double nTasa;
    private int nPlazo;
    private double nPrestamoMinimo;
    private double nPrestamoMaximo;
    private double nPorcGarantiaAvaluo;
    private double nRCI;
    private double nRMA;
    private int nCategoria;
    private String sDescripcionRechazo;
    private double nPorcentajeInicial;
    private boolean bError;
    private String sMensajeError;

    public ELScoringFinalResultado(){

    }

    public int getnDecicion() {
        return nDecicion;
    }

    public void setnDecicion(int nDecicion) {
        this.nDecicion = nDecicion;
    }

    public double getnCuotaMaxima() {
        return nCuotaMaxima;
    }

    public void setnCuotaMaxima(double nCuotaMaxima) {
        this.nCuotaMaxima = nCuotaMaxima;
    }

    public double getnCuotaUtilizada() {
        return nCuotaUtilizada;
    }

    public void setnCuotaUtilizada(double nCuotaUtilizada) {
        this.nCuotaUtilizada = nCuotaUtilizada;
    }

    public double getnCuotaDisponible() {
        return nCuotaDisponible;
    }

    public void setnCuotaDisponible(double nCuotaDisponible) {
        this.nCuotaDisponible = nCuotaDisponible;
    }

    public double getnPrestamo1() {
        return nPrestamo1;
    }

    public void setnPrestamo1(double nPrestamo1) {
        this.nPrestamo1 = nPrestamo1;
    }

    public double getnPrestamo2() {
        return nPrestamo2;
    }

    public void setnPrestamo2(double nPrestamo2) {
        this.nPrestamo2 = nPrestamo2;
    }

    public double getnPrestamo3() {
        return nPrestamo3;
    }

    public void setnPrestamo3(double nPrestamo3) {
        this.nPrestamo3 = nPrestamo3;
    }

    public double getnPrestamo4() {
        return nPrestamo4;
    }

    public void setnPrestamo4(double nPrestamo4) {
        this.nPrestamo4 = nPrestamo4;
    }

    public double getnTasa() {
        return nTasa;
    }

    public void setnTasa(double nTasa) {
        this.nTasa = nTasa;
    }

    public int getnPlazo() {
        return nPlazo;
    }

    public void setnPlazo(int nPlazo) {
        this.nPlazo = nPlazo;
    }

    public double getnPrestamoMinimo() {
        return nPrestamoMinimo;
    }

    public void setnPrestamoMinimo(double nPrestamoMinimo) {
        this.nPrestamoMinimo = nPrestamoMinimo;
    }

    public double getnPrestamoMaximo() {
        return nPrestamoMaximo;
    }

    public void setnPrestamoMaximo(double nPrestamoMaximo) {
        this.nPrestamoMaximo = nPrestamoMaximo;
    }

    public double getnPorcGarantiaAvaluo() {
        return nPorcGarantiaAvaluo;
    }

    public void setnPorcGarantiaAvaluo(double nPorcGarantiaAvaluo) {
        this.nPorcGarantiaAvaluo = nPorcGarantiaAvaluo;
    }

    public double getnRCI() {
        return nRCI;
    }

    public void setnRCI(double nRCI) {
        this.nRCI = nRCI;
    }

    public double getnRMA() {
        return nRMA;
    }

    public void setnRMA(double nRMA) {
        this.nRMA = nRMA;
    }

    public int getnCategoria() {
        return nCategoria;
    }

    public void setnCategoria(int nCategoria) {
        this.nCategoria = nCategoria;
    }

    public String getsDescripcionRechazo() {
        return sDescripcionRechazo;
    }

    public void setsDescripcionRechazo(String sDescripcionRechazo) {
        this.sDescripcionRechazo = sDescripcionRechazo;
    }

    public double getnPorcentajeInicial() {
        return nPorcentajeInicial;
    }

    public void setnPorcentajeInicial(double nPorcentajeInicial) {
        this.nPorcentajeInicial = nPorcentajeInicial;
    }

    public boolean isbError() {
        return bError;
    }

    public void setbError(boolean bError) {
        this.bError = bError;
    }

    public String getsMensajeError() {
        return sMensajeError;
    }

    public void setsMensajeError(String sMensajeError) {
        this.sMensajeError = sMensajeError;
    }

    protected ELScoringFinalResultado(Parcel in) {
        nDecicion = in.readInt();
        nCuotaMaxima = in.readDouble();
        nCuotaUtilizada = in.readDouble();
        nCuotaDisponible = in.readDouble();
        nPrestamo1 = in.readDouble();
        nPrestamo2 = in.readDouble();
        nPrestamo3 = in.readDouble();
        nPrestamo4 = in.readDouble();
        nTasa = in.readDouble();
        nPlazo = in.readInt();
        nPrestamoMinimo = in.readDouble();
        nPrestamoMaximo = in.readDouble();
        nPorcGarantiaAvaluo = in.readDouble();
        nRCI = in.readDouble();
        nRMA = in.readDouble();
        nCategoria = in.readInt();
        sDescripcionRechazo = in.readString();
        nPorcentajeInicial = in.readDouble();
        bError = in.readByte() != 0x00;
        sMensajeError = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nDecicion);
        dest.writeDouble(nCuotaMaxima);
        dest.writeDouble(nCuotaUtilizada);
        dest.writeDouble(nCuotaDisponible);
        dest.writeDouble(nPrestamo1);
        dest.writeDouble(nPrestamo2);
        dest.writeDouble(nPrestamo3);
        dest.writeDouble(nPrestamo4);
        dest.writeDouble(nTasa);
        dest.writeInt(nPlazo);
        dest.writeDouble(nPrestamoMinimo);
        dest.writeDouble(nPrestamoMaximo);
        dest.writeDouble(nPorcGarantiaAvaluo);
        dest.writeDouble(nRCI);
        dest.writeDouble(nRMA);
        dest.writeInt(nCategoria);
        dest.writeString(sDescripcionRechazo);
        dest.writeDouble(nPorcentajeInicial);
        dest.writeByte((byte) (bError ? 0x01 : 0x00));
        dest.writeString(sMensajeError);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ELScoringFinalResultado> CREATOR = new Parcelable.Creator<ELScoringFinalResultado>() {
        @Override
        public ELScoringFinalResultado createFromParcel(Parcel in) {
            return new ELScoringFinalResultado(in);
        }

        @Override
        public ELScoringFinalResultado[] newArray(int size) {
            return new ELScoringFinalResultado[size];
        }
    };
}
