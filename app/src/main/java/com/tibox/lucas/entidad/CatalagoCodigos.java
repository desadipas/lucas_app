package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 10/03/2017.
 */

public class CatalagoCodigos implements Parcelable {
    private String cNomCod;
    private int nCodigo;
    private int nValor;

    public CatalagoCodigos(){

    }


    public String getcNomCod() {
        return cNomCod;
    }

    public void setcNomCod(String cNomCod) {
        this.cNomCod = cNomCod;
    }

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }

    public int getnValor() {
        return nValor;
    }

    public void setnValor(int nValor) {
        this.nValor = nValor;
    }

    protected CatalagoCodigos(Parcel in) {
        cNomCod = in.readString();
        nCodigo = in.readInt();
        nValor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cNomCod);
        dest.writeInt(nCodigo);
        dest.writeInt(nValor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CatalagoCodigos> CREATOR = new Parcelable.Creator<CatalagoCodigos>() {
        @Override
        public CatalagoCodigos createFromParcel(Parcel in) {
            return new CatalagoCodigos(in);
        }

        @Override
        public CatalagoCodigos[] newArray(int size) {
            return new CatalagoCodigos[size];
        }
    };
}
