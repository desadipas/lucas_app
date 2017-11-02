package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 23/03/2017.
 */

public class FotoDocumento implements Parcelable {
    private int IdFotoDocumento;
    private int IdDocumentoLista;
    private String Descripcion;
    private int TipoDocumento;
    private String RutaFotoDocumento;
    private int Estado;

    public FotoDocumento(){
    }


    public int getIdFotoDocumento() {
        return IdFotoDocumento;
    }

    public void setIdFotoDocumento(int idFotoDocumento) {
        IdFotoDocumento = idFotoDocumento;
    }

    public int getIdDocumentoLista() {
        return IdDocumentoLista;
    }

    public void setIdDocumentoLista(int idDocumentoLista) {
        IdDocumentoLista = idDocumentoLista;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getRutaFotoDocumento() {
        return RutaFotoDocumento;
    }

    public void setRutaFotoDocumento(String rutaFotoDocumento) {
        RutaFotoDocumento = rutaFotoDocumento;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    protected FotoDocumento(Parcel in) {
        IdFotoDocumento = in.readInt();
        IdDocumentoLista = in.readInt();
        Descripcion = in.readString();
        TipoDocumento = in.readInt();
        RutaFotoDocumento = in.readString();
        Estado = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdFotoDocumento);
        dest.writeInt(IdDocumentoLista);
        dest.writeString(Descripcion);
        dest.writeInt(TipoDocumento);
        dest.writeString(RutaFotoDocumento);
        dest.writeInt(Estado);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FotoDocumento> CREATOR = new Parcelable.Creator<FotoDocumento>() {
        @Override
        public FotoDocumento createFromParcel(Parcel in) {
            return new FotoDocumento(in);
        }

        @Override
        public FotoDocumento[] newArray(int size) {
            return new FotoDocumento[size];
        }
    };
}