package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tibox.lucas.entidad.FotoDocumento;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 23/03/2017.
 */

public class FotoDocumentoDAO {

    public int insertar( FotoDocumento fotoDocumento ){
        long resultado = -1;
        try {
            ContentValues data = new ContentValues();
            data.put( "Descripcion", fotoDocumento.getDescripcion() );
            data.put( "TipoDocumento", fotoDocumento.getTipoDocumento() );
            data.put( "RutaFotoDocumento", fotoDocumento.getRutaFotoDocumento() );
            data.put( "Estado", fotoDocumento.getEstado() );
            data.put( "IdDocumentoLista", fotoDocumento.getIdDocumentoLista() );
            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_FOTODOCUMENTO, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public ArrayList<FotoDocumento> ListarFotoDocumento (int estado ){
        Cursor cursor = null;
        ArrayList<FotoDocumento> lvFotoDocumento = new ArrayList<>();

        try {

            String[] args = new String[1];
            args[0] = String.valueOf( estado );

            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+Constantes.TABLE_FOTODOCUMENTO +" Where Estado = ?", args );

            if(cursor.moveToFirst()){
                do{
                    FotoDocumento fotoDocumento = new FotoDocumento();

                    fotoDocumento.setIdFotoDocumento(cursor.isNull(cursor.getColumnIndex("IdFotoDocumento")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdFotoDocumento")));
                    fotoDocumento.setTipoDocumento(cursor.isNull(cursor.getColumnIndex("TipoDocumento")) ? 0 : cursor.getInt(cursor.getColumnIndex("TipoDocumento")));
                    fotoDocumento.setRutaFotoDocumento(cursor.isNull(cursor.getColumnIndex("RutaFotoDocumento")) ? "" : cursor.getString(cursor.getColumnIndex("RutaFotoDocumento")));
                    fotoDocumento.setDescripcion(cursor.isNull(cursor.getColumnIndex("Descripcion")) ? "" : cursor.getString(cursor.getColumnIndex("Descripcion")));
                    fotoDocumento.setEstado(cursor.isNull(cursor.getColumnIndex("Estado")) ? 0 : cursor.getInt(cursor.getColumnIndex("Estado")));
                    fotoDocumento.setIdDocumentoLista( cursor.isNull(cursor.getColumnIndex("IdDocumentoLista")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdDocumentoLista")));

                    lvFotoDocumento.add( fotoDocumento );
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if(cursor != null)
                cursor.close();
        }
        return lvFotoDocumento;
    }

    public void ActualizarIdDocumentoListaxTipoDoc ( int IdDocumentoLista, int TipoDocumento, int Estado, String Descripcion  ){
        try {
            ContentValues data = new ContentValues();
            data.put( "IdDocumentoLista", IdDocumentoLista );
            data.put( "Estado", Estado );
            data.put( "Descripcion", Descripcion );
            DataBaseHelper.myDataBase.update( Constantes.TABLE_FOTODOCUMENTO, data, "TipoDocumento = ?", new String[]{String.valueOf( TipoDocumento )});

        }catch ( Exception ex ){
            ex.printStackTrace();
        }
    }

    public void LimpiarFotoDocumento(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTODOCUMENTO, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarFotosxTipoDocumento( int TipoDocumento ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTODOCUMENTO, "TipoDocumento = ? ", new String[]{ String.valueOf( TipoDocumento ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarFotosxIdDocumentoLista( int IdDocumentoLista ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTODOCUMENTO, "IdDocumentoLista = ? ", new String[]{ String.valueOf( IdDocumentoLista ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarFotosxIdFotoDocumento( int IdFotoDocumento ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTODOCUMENTO, "IdFotoDocumento = ? ", new String[]{ String.valueOf( IdFotoDocumento ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean BuscarTipoDocumento( int TipoDocumento ){
        Boolean exist = false;
        try {
            Cursor mCursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_FOTODOCUMENTO + " WHERE TipoDocumento = "+ TipoDocumento +"", null);

            if ( mCursor != null ) {
                if ( mCursor.getCount() > 0 ) {
                    mCursor.close();
                    exist = true;
                }
                else {
                    mCursor.close();
                    exist = false;
                }
            }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        return exist;
    }

    public boolean BuscarExisteFotoPorgregar( int Estado, int TipoDocumento ){
        Boolean exist = false;

        String[] args = new String[2];
        args[0] = String.valueOf( Estado );
        args[1] = String.valueOf( TipoDocumento );

        try {
            Cursor mCursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM "
                    + Constantes.TABLE_FOTODOCUMENTO + " WHERE Estado = ? and TipoDocumento = ?", args );

            if ( mCursor != null ) {
                if ( mCursor.getCount() > 0 ) {
                    mCursor.close();
                    exist = true;
                }
                else {
                    mCursor.close();
                    exist = false;
                }
            }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        return exist;
    }

    public boolean BuscarExisteFotoPorgregarView( int Estado ){
        Boolean exist = false;

        String[] args = new String[1];
        args[0] = String.valueOf( Estado );

        try {
            Cursor mCursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM "
                    + Constantes.TABLE_FOTODOCUMENTO + " WHERE Estado = ? ", args );

            if ( mCursor != null ) {
                if ( mCursor.getCount() > 0 ) {
                    mCursor.close();
                    exist = true;
                }
                else {
                    mCursor.close();
                    exist = false;
                }
            }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        return exist;
    }

}
