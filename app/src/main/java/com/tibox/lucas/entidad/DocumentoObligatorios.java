package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 23/03/2017.
 */

public class DocumentoObligatorios implements Parcelable {
    private int nCodigo;
    private String cNombre;
    private int nObligatorio;

    public DocumentoObligatorios(){
    }

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }

    public String getcNombre() {
        return cNombre;
    }

    public void setcNombre(String cNombre) {
        this.cNombre = cNombre;
    }

    public int getnObligatorio() {
        return nObligatorio;
    }

    public void setnObligatorio(int nObligatorio) {
        this.nObligatorio = nObligatorio;
    }

    protected DocumentoObligatorios(Parcel in) {
        nCodigo = in.readInt();
        cNombre = in.readString();
        nObligatorio = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nCodigo);
        dest.writeString(cNombre);
        dest.writeInt(nObligatorio);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DocumentoObligatorios> CREATOR = new Parcelable.Creator<DocumentoObligatorios>() {
        @Override
        public DocumentoObligatorios createFromParcel(Parcel in) {
            return new DocumentoObligatorios(in);
        }

        @Override
        public DocumentoObligatorios[] newArray(int size) {
            return new DocumentoObligatorios[size];
        }
    };
}