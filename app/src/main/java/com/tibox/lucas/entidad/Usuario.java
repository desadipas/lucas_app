package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 28/02/2017.
 */

public class Usuario implements Parcelable {

    private int IdUsuario;
    private String NombreUsuario;
    private String Usuario;
    private String Clave;
    private int Agencia;
    private String AgenciaNombre;
    private int CodPers;
    private int CodUsu;
    private int Rol;
    private String RolDescripcion;
    private int CodDepAge;
    private String UrlFotoUsuario;
    private String Moneda;
    private String Tipo;
    private String Token; // como DNI
    private int idTipoEmpresa;
    private int idEmpresa;
    private int idPais;

    public Usuario() {
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public int getAgencia() {
        return Agencia;
    }

    public void setAgencia(int agencia) {
        Agencia = agencia;
    }

    public String getAgenciaNombre() {
        return AgenciaNombre;
    }

    public void setAgenciaNombre(String agenciaNombre) {
        AgenciaNombre = agenciaNombre;
    }

    public int getCodPers() {
        return CodPers;
    }

    public void setCodPers(int codPers) {
        CodPers = codPers;
    }

    public int getCodUsu() {
        return CodUsu;
    }

    public void setCodUsu(int codUsu) {
        CodUsu = codUsu;
    }

    public int getRol() {
        return Rol;
    }

    public void setRol(int rol) {
        Rol = rol;
    }

    public String getRolDescripcion() {
        return RolDescripcion;
    }

    public void setRolDescripcion(String rolDescripcion) {
        RolDescripcion = rolDescripcion;
    }

    public int getCodDepAge() {
        return CodDepAge;
    }

    public void setCodDepAge(int codDepAge) {
        CodDepAge = codDepAge;
    }

    public String getUrlFotoUsuario() {
        return UrlFotoUsuario;
    }

    public void setUrlFotoUsuario(String urlFotoUsuario) {
        UrlFotoUsuario = urlFotoUsuario;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String moneda) {
        Moneda = moneda;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getIdTipoEmpresa() {
        return idTipoEmpresa;
    }

    public void setIdTipoEmpresa(int idTipoEmpresa) {
        this.idTipoEmpresa = idTipoEmpresa;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    protected Usuario(Parcel in) {
        IdUsuario = in.readInt();
        NombreUsuario = in.readString();
        Usuario = in.readString();
        Clave = in.readString();
        Agencia = in.readInt();
        AgenciaNombre = in.readString();
        CodPers = in.readInt();
        CodUsu = in.readInt();
        Rol = in.readInt();
        RolDescripcion = in.readString();
        CodDepAge = in.readInt();
        UrlFotoUsuario = in.readString();
        Moneda = in.readString();
        Tipo = in.readString();
        Token = in.readString();
        idTipoEmpresa = in.readInt();
        idEmpresa = in.readInt();
        idPais = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdUsuario);
        dest.writeString(NombreUsuario);
        dest.writeString(Usuario);
        dest.writeString(Clave);
        dest.writeInt(Agencia);
        dest.writeString(AgenciaNombre);
        dest.writeInt(CodPers);
        dest.writeInt(CodUsu);
        dest.writeInt(Rol);
        dest.writeString(RolDescripcion);
        dest.writeInt(CodDepAge);
        dest.writeString(UrlFotoUsuario);
        dest.writeString(Moneda);
        dest.writeString(Tipo);
        dest.writeString(Token);
        dest.writeInt(idTipoEmpresa);
        dest.writeInt(idEmpresa);
        dest.writeInt(idPais);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
