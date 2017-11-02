package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 14/03/2017.
 */

public class FotoGarantia implements Parcelable {
    private int IdFotoArticulo;
    private int IdGarantia;
    private String Descripcion;
    private String RutaFotoArticulo;
    private int Estado;

    public FotoGarantia(){}

    public int getIdFotoArticulo() {
        return IdFotoArticulo;
    }

    public void setIdFotoArticulo(int idFotoArticulo) {
        IdFotoArticulo = idFotoArticulo;
    }

    public int getIdGarantia() {
        return IdGarantia;
    }

    public void setIdGarantia(int idGarantia) {
        IdGarantia = idGarantia;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getRutaFotoArticulo() {
        return RutaFotoArticulo;
    }

    public void setRutaFotoArticulo(String rutaFotoArticulo) {
        RutaFotoArticulo = rutaFotoArticulo;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    protected FotoGarantia(Parcel in) {
        IdFotoArticulo = in.readInt();
        IdGarantia = in.readInt();
        Descripcion = in.readString();
        RutaFotoArticulo = in.readString();
        Estado = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdFotoArticulo);
        dest.writeInt(IdGarantia);
        dest.writeString(Descripcion);
        dest.writeString(RutaFotoArticulo);
        dest.writeInt(Estado);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FotoGarantia> CREATOR = new Parcelable.Creator<FotoGarantia>() {
        @Override
        public FotoGarantia createFromParcel(Parcel in) {
            return new FotoGarantia(in);
        }

        @Override
        public FotoGarantia[] newArray(int size) {
            return new FotoGarantia[size];
        }
    };
}
