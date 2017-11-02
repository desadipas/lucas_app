package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 23/03/2017.
 */

public class DocumentoLista implements Parcelable {
    private int IdDocumentoLista;
    private String Descripcion;
    private int TipoDocumento;
    private int Obligatorio;
    private int Estado;

    public DocumentoLista(){
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

    public int getObligatorio() {
        return Obligatorio;
    }

    public void setObligatorio(int obligatorio) {
        Obligatorio = obligatorio;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    protected DocumentoLista(Parcel in) {
        IdDocumentoLista = in.readInt();
        Descripcion = in.readString();
        TipoDocumento = in.readInt();
        Obligatorio = in.readInt();
        Estado = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdDocumentoLista);
        dest.writeString(Descripcion);
        dest.writeInt(TipoDocumento);
        dest.writeInt(Obligatorio);
        dest.writeInt(Estado);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DocumentoLista> CREATOR = new Parcelable.Creator<DocumentoLista>() {
        @Override
        public DocumentoLista createFromParcel(Parcel in) {
            return new DocumentoLista(in);
        }

        @Override
        public DocumentoLista[] newArray(int size) {
            return new DocumentoLista[size];
        }
    };
}
