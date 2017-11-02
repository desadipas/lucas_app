package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desa02 on 01/03/2017.
 */

public class UsuarioDAO {
    public static Usuario instanciaSesion;

    public int ingresarAutenticacion( Usuario usuario ){

        long resultado = -1;
        if ( existeAutenticacion( usuario ) )
            actualizarAutenticacion( usuario );
        else
        {
            try {
                ContentValues data = new ContentValues();
                data.put( "NombreUsuario", usuario.getNombreUsuario());
                data.put( "Clave", usuario.getClave());
                data.put( "Agencia", usuario.getAgencia());
                data.put( "AgenciaNombre", usuario.getAgenciaNombre());
                data.put( "CodDepAge", usuario.getCodDepAge());
                data.put( "CodUsu", usuario.getCodUsu());
                data.put( "Rol", usuario.getRol());
                data.put( "RolDescripcion", usuario.getRolDescripcion());
                data.put( "Usuario", usuario.getUsuario());
                data.put( "Tipo", usuario.getTipo() );
                data.put( "Token", usuario.getToken() );
                data.put( "idEmpresa", usuario.getIdEmpresa() );
                data.put( "idPais", usuario.getIdPais() );
                data.put( "idTipoEmpresa", usuario.getIdTipoEmpresa() );
                data.put( "Moneda", usuario.getMoneda() );
                data.put( "CodPers", usuario.getCodPers() );
                resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_USUARIO, null, data );

            }catch ( Exception ex ) {
                ex.printStackTrace();
            }

        }
        return ((int)resultado);
    }

    private boolean existeAutenticacion( Usuario usuario ){

        Boolean resp = false;
        try {
            Cursor mCursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_USUARIO + " WHERE Usuario = '" + usuario.getUsuario() +"' and Clave = '" + usuario.getClave() + "' ", null);

            if ( mCursor != null ) {
                if ( mCursor.getCount() > 0 ) {
                    mCursor.close();
                    resp = true;
                    /* record exist */
                }
                else {
                    mCursor.close();
                    resp = false;
                    /* record not exist */
                }
            }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        return resp;
    }

    public int actualizarAutenticacion( Usuario usuario){

        try {
            ContentValues data = new ContentValues();

            data.put( "NombreUsuario", usuario.getNombreUsuario());
            data.put( "Clave", usuario.getClave());
            data.put( "Agencia", usuario.getAgencia());
            data.put( "AgenciaNombre", usuario.getAgenciaNombre());
            data.put( "CodDepAge", usuario.getCodDepAge());
            data.put( "CodUsu", usuario.getCodUsu());
            data.put( "Rol", usuario.getRol());
            data.put( "RolDescripcion", usuario.getRolDescripcion());
            data.put( "Usuario", usuario.getUsuario());
            data.put( "Tipo", usuario.getTipo() );
            data.put( "Token", usuario.getToken() );
            data.put( "idEmpresa", usuario.getIdEmpresa() );
            data.put( "idPais", usuario.getIdPais() );
            data.put( "idTipoEmpresa", usuario.getIdTipoEmpresa() );
            data.put( "Moneda", usuario.getMoneda() );
            data.put( "CodPers", usuario.getCodPers() );
            DataBaseHelper.myDataBase.update( Constantes.TABLE_USUARIO, data, "IdUsuario = ?", new String[]{String.valueOf( usuario.getIdUsuario() )});

        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        return 1;
    }

    public void cerrarSesion() {
        UsuarioDAO.instanciaSesion = null;
        limpiarAutenticacion();
    }

    public void limpiarAutenticacion(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_USUARIO, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void iniciarSesion( Usuario sesion ) {
        instanciaSesion = sesion;
        limpiarAutenticacion();
        ingresarAutenticacion( sesion );
    }

    public Usuario ObtenerSesion (){

        Cursor cursor = null;
        List<Usuario> list = new ArrayList<>();
        try {

            if ( instanciaSesion != null ) {
                return instanciaSesion;
            }
            else {
                cursor = DataBaseHelper.myDataBase.query( Constantes.TABLE_USUARIO, null , null, null , null, null, null);

                if (cursor.moveToFirst()) {
                    do {
                        Usuario user = new Usuario();
                        user.setNombreUsuario(cursor.isNull(cursor.getColumnIndex("NombreUsuario")) ? "" : cursor.getString(cursor.getColumnIndex("NombreUsuario")));
                        user.setIdUsuario(cursor.isNull(cursor.getColumnIndex("IdUsuario")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdUsuario")));
                        user.setClave(cursor.isNull(cursor.getColumnIndex("Clave")) ? "" : cursor.getString(cursor.getColumnIndex("Clave")));
                        user.setAgencia(cursor.isNull(cursor.getColumnIndex("Agencia")) ? 0 : cursor.getInt(cursor.getColumnIndex("Agencia")));
                        user.setAgenciaNombre(cursor.isNull(cursor.getColumnIndex("AgenciaNombre")) ? "" : cursor.getString(cursor.getColumnIndex("AgenciaNombre")));
                        user.setCodDepAge(cursor.isNull(cursor.getColumnIndex("CodDepAge")) ? 0 : cursor.getInt(cursor.getColumnIndex("CodDepAge")));
                        user.setCodUsu(cursor.isNull(cursor.getColumnIndex("CodUsu")) ? 0 : cursor.getInt(cursor.getColumnIndex("CodUsu")));
                        user.setNombreUsuario(cursor.isNull(cursor.getColumnIndex("NombreUsuario")) ? "" : cursor.getString(cursor.getColumnIndex("NombreUsuario")));
                        user.setRol(cursor.isNull(cursor.getColumnIndex("Rol")) ? 0 : cursor.getInt(cursor.getColumnIndex("Rol")));
                        user.setRolDescripcion(cursor.isNull(cursor.getColumnIndex("RolDescripcion")) ? "" : cursor.getString(cursor.getColumnIndex("RolDescripcion")));
                        user.setUsuario(cursor.isNull(cursor.getColumnIndex("Usuario")) ? "" : cursor.getString(cursor.getColumnIndex("Usuario")));
                        user.setUrlFotoUsuario(cursor.isNull(cursor.getColumnIndex("UrlFotoUsuario")) ? "" : cursor.getString(cursor.getColumnIndex("UrlFotoUsuario")));

                        user.setTipo(cursor.isNull(cursor.getColumnIndex("Tipo")) ? "" : cursor.getString(cursor.getColumnIndex("Tipo")));
                        user.setToken(cursor.isNull(cursor.getColumnIndex("Token")) ? "" : cursor.getString(cursor.getColumnIndex("Token")));
                        user.setIdEmpresa(cursor.isNull(cursor.getColumnIndex("idEmpresa")) ? 0 : cursor.getInt(cursor.getColumnIndex("idEmpresa")));
                        user.setIdPais(cursor.isNull(cursor.getColumnIndex("idPais")) ? 0 : cursor.getInt(cursor.getColumnIndex("idPais")));
                        user.setIdTipoEmpresa(cursor.isNull(cursor.getColumnIndex("idTipoEmpresa")) ? 0 : cursor.getInt(cursor.getColumnIndex("idTipoEmpresa")));
                        user.setMoneda(cursor.isNull(cursor.getColumnIndex("Moneda")) ? "" : cursor.getString(cursor.getColumnIndex("Moneda")));
                        user.setCodPers(cursor.isNull(cursor.getColumnIndex("CodPers")) ? 0 : cursor.getInt(cursor.getColumnIndex("CodPers")));
                        list.add( user );

                    } while (cursor.moveToNext());
                }
                if ( list.size() > 0 ){
                    return  list.get( 0 );
                }
                else
                    return null;
            }
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<Usuario> ListOneUser( Usuario usuario ) {
        ArrayList<Usuario> ListOneUser = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = DataBaseHelper.myDataBase.query( Constantes.TABLE_USUARIO, new String[]{"NombreUsuario","IdUsuario"}, "Usuario=? and Clave=?",
                    new String[]{String.valueOf(usuario.getUsuario()),String.valueOf(usuario.getClave())}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Usuario user = new Usuario();

                    user.setNombreUsuario(cursor.isNull(cursor.getColumnIndex("NombreUsuario")) ? "" : cursor.getString(cursor.getColumnIndex("NombreUsuario")));
                    user.setIdUsuario(cursor.isNull(cursor.getColumnIndex("IdUsuario")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdUsuario")));
                    user.setClave(cursor.isNull(cursor.getColumnIndex("Clave")) ? "" : cursor.getString(cursor.getColumnIndex("Clave")));
                    user.setAgencia(cursor.isNull(cursor.getColumnIndex("Agencia")) ? 0 : cursor.getInt(cursor.getColumnIndex("Agencia")));
                    user.setAgenciaNombre(cursor.isNull(cursor.getColumnIndex("AgenciaNombre")) ? "" : cursor.getString(cursor.getColumnIndex("AgenciaNombre")));
                    user.setCodDepAge(cursor.isNull(cursor.getColumnIndex("CodDepAge")) ? 0 : cursor.getInt(cursor.getColumnIndex("CodDepAge")));
                    user.setCodUsu(cursor.isNull(cursor.getColumnIndex("CodUsu")) ? 0 : cursor.getInt(cursor.getColumnIndex("CodUsu")));
                    user.setNombreUsuario(cursor.isNull(cursor.getColumnIndex("NombreUsuario")) ? "" : cursor.getString(cursor.getColumnIndex("NombreUsuario")));
                    user.setRol(cursor.isNull(cursor.getColumnIndex("Rol")) ? 0 : cursor.getInt(cursor.getColumnIndex("Rol")));
                    user.setRolDescripcion(cursor.isNull(cursor.getColumnIndex("RolDescripcion")) ? "" : cursor.getString(cursor.getColumnIndex("RolDescripcion")));
                    user.setUsuario(cursor.isNull(cursor.getColumnIndex("Usuario")) ? "" : cursor.getString(cursor.getColumnIndex("Usuario")));
                    user.setUrlFotoUsuario(cursor.isNull(cursor.getColumnIndex("UrlFotoUsuario")) ? "" : cursor.getString(cursor.getColumnIndex("UrlFotoUsuario")));

                    user.setTipo(cursor.isNull(cursor.getColumnIndex("Tipo")) ? "" : cursor.getString(cursor.getColumnIndex("Tipo")));
                    user.setToken(cursor.isNull(cursor.getColumnIndex("Token")) ? "" : cursor.getString(cursor.getColumnIndex("Token")));
                    user.setIdEmpresa(cursor.isNull(cursor.getColumnIndex("idEmpresa")) ? 0 : cursor.getInt(cursor.getColumnIndex("idEmpresa")));
                    user.setIdPais(cursor.isNull(cursor.getColumnIndex("idPais")) ? 0 : cursor.getInt(cursor.getColumnIndex("idPais")));
                    user.setIdTipoEmpresa(cursor.isNull(cursor.getColumnIndex("idTipoEmpresa")) ? 0 : cursor.getInt(cursor.getColumnIndex("idTipoEmpresa")));
                    user.setMoneda(cursor.isNull(cursor.getColumnIndex("Moneda")) ? "" : cursor.getString(cursor.getColumnIndex("Moneda")));
                    user.setCodPers(cursor.isNull(cursor.getColumnIndex("CodPers")) ? 0 : cursor.getInt(cursor.getColumnIndex("CodPers")));
                    ListOneUser.add(user);

                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return ListOneUser;
    }

}
