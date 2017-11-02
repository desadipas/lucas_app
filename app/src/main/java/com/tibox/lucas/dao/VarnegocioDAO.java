package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tibox.lucas.network.dto.Varnegocio;
import com.tibox.lucas.utilidades.Constantes;

import java.util.List;

/**
 * Created by desa02 on 28/08/2017.
 */

public class VarnegocioDAO {

    public int insertar( Varnegocio value ) {
        long resultado = -1;

        try {
            ContentValues valores = new ContentValues();
            valores.put( "nCodVar", value.getnCodVar() );
            valores.put( "cNomVar", value.getcNomVar() );
            valores.put( "cValorVar", value.getcValorVar() );
            valores.put( "nTipoVar", value.getnTipoVar() );

            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_VARNEGOCIO, null, valores );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public Varnegocio ObtenerVarnegocioxId( int IdnCodVar ){
        Cursor cursor = null;
        Varnegocio varnegocio = new Varnegocio();
        try {
            String[] args = new String[1];
            args[0] = String.valueOf( IdnCodVar );

            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+ Constantes.TABLE_VARNEGOCIO +" Where nCodVar = ?", args );

            if( cursor.moveToFirst() ) {
                do {
                    varnegocio.setnCodVar( IdnCodVar );
                    varnegocio.setcNomVar( cursor.isNull(cursor.getColumnIndex("cNomVar")) ? "" : cursor.getString(cursor.getColumnIndex("cNomVar")) );
                    varnegocio.setcValorVar( cursor.isNull(cursor.getColumnIndex("cValorVar")) ? "" : cursor.getString(cursor.getColumnIndex("cValorVar")) );
                    varnegocio.setnTipoVar( cursor.isNull(cursor.getColumnIndex("nTipoVar")) ? 0 : cursor.getInt(cursor.getColumnIndex("nTipoVar")) );
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
        return varnegocio;
    }

    public void LimpiarTabla(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_VARNEGOCIO, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void EliminarVarnegocioXid( int IdnCodVar ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_VARNEGOCIO, "nCodVar = ? ", new String[]{ String.valueOf( IdnCodVar ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

}
