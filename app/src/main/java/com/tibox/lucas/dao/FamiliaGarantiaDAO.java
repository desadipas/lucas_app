package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.tibox.lucas.entidad.FamiliaGarantia;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 17/03/2017.
 */

public class FamiliaGarantiaDAO {
    public int insertar( FamiliaGarantia familiaGarantia ){
        long resultado = -1;
        try {
            ContentValues data = new ContentValues();
            data.put( "nIdCodigo", familiaGarantia.getnIdCodigo() );
            data.put( "cDescripcion", familiaGarantia.getcDescripcion() );
            data.put( "nNivel", familiaGarantia.getnNivel() );
            data.put( "nIdCodigoSuperior", familiaGarantia .getnIdCodigoSuperior() );
            data.put( "nPorValorTasado", familiaGarantia .getnPorValorTasado() );
            data.put( "nValorTasado", familiaGarantia .getnValorTasado() );
            data.put( "nPrestamoTotal", familiaGarantia .getnPrestamoTotal() );
            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_FAMILIAGARANTIA, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }

        return ((int)resultado);
    }

    public void LimpiarFamilia(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FAMILIAGARANTIA, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }
    public void LimpiarFamiliaxNivel( int Nivel ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FAMILIAGARANTIA, "nNivel = ? ", new String[]{ String.valueOf( Nivel ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public String ObtenerNombreFamilia( int nIdCodigo , int nNivel ){
        ArrayList<FamiliaGarantia> lstAnaquel = new ArrayList<>();
        Cursor cursor = null;
        FamiliaGarantia familiaGarantia = new FamiliaGarantia();
        familiaGarantia.setcDescripcion( "" );
        try {
            String[] args = new String[2];
            args[0] = String.valueOf( nIdCodigo );
            args[1] = String.valueOf( nNivel );
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nIdCodigo _id, cDescripcion FROM " + Constantes.TABLE_FAMILIAGARANTIA + " WHERE nIdCodigo = ? and nNivel = ?" , args);

            if (cursor.moveToFirst()) {
                do {
                    familiaGarantia.setnIdCodigoSuperior( cursor.isNull(cursor.getColumnIndex("nIdCodigo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nIdCodigo")));
                    familiaGarantia.setcDescripcion( cursor.isNull(cursor.getColumnIndex("cDescripcion")) ? "" : cursor.getString(cursor.getColumnIndex("cDescripcion")));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return familiaGarantia.getcDescripcion();
    }

    public void rellenaSpinner(Spinner spinner, int nIdCodigoSuperior, int nNivel, Context m_context ) {
        try {
            Cursor cursor = null;
            String[] args = new String[1];
            args[0] = String.valueOf( nNivel );

            if ( nNivel == Constantes.UNO ){
                cursor = DataBaseHelper.myDataBase.rawQuery("SELECT nIdCodigo _id, cDescripcion FROM " + Constantes.TABLE_FAMILIAGARANTIA + " WHERE nNivel = 1 ", null );
            }
            else
                cursor = DataBaseHelper.myDataBase.rawQuery("SELECT nIdCodigo _id, cDescripcion FROM " + Constantes.TABLE_FAMILIAGARANTIA + " WHERE nNivel = ? ", args);


            @SuppressWarnings( "deprecation" )
            SimpleCursorAdapter spinnerCodigo = new SimpleCursorAdapter( m_context,
                    android.R.layout.simple_spinner_item,
                    cursor, new String[] { "cDescripcion" },
                    new int[] { android.R.id.text1 } );

            spinnerCodigo.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            spinner.setAdapter( spinnerCodigo );
            spinner.setSelection( 0 );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
