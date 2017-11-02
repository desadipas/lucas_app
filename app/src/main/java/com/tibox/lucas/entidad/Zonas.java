package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 13/03/2017.
 */

public class Zonas implements Parcelable {
    private int nCodigo;
    private String cDescripcion;
    private int nNivel;
    private int nTipo;
    private int nSubTipo;

    public Zonas(){

    }

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }

    public String getcDescripcion() {
        return cDescripcion;
    }

    public void setcDescripcion(String cDescripcion) {
        this.cDescripcion = cDescripcion;
    }

    public int getnNivel() {
        return nNivel;
    }

    public void setnNivel(int nNivel) {
        this.nNivel = nNivel;
    }

    protected Zonas(Parcel in) {
        nCodigo = in.readInt();
        cDescripcion = in.readString();
        nNivel = in.readInt();
        nTipo = in.readInt();
        nSubTipo = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nCodigo);
        dest.writeString(cDescripcion);
        dest.writeInt(nNivel);
        dest.writeInt(nTipo);
        dest.writeInt(nSubTipo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Zonas> CREATOR = new Parcelable.Creator<Zonas>() {
        @Override
        public Zonas createFromParcel(Parcel in) {
            return new Zonas(in);
        }

        @Override
        public Zonas[] newArray(int size) {
            return new Zonas[size];
        }
    };

    public int getnTipo() {
        return nTipo;
    }

    public void setnTipo(int nTipo) {
        this.nTipo = nTipo;
    }

    public int getnSubTipo() {
        return nSubTipo;
    }

    public void setnSubTipo(int nSubTipo) {
        this.nSubTipo = nSubTipo;
    }
}
