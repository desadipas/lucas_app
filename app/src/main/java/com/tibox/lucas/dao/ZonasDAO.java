package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.tibox.lucas.entidad.Zonas;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 13/03/2017.
 */

public class ZonasDAO {

    public int insertar( Zonas zonas ){
        long resultado = -1;
        try
        {
            ContentValues data = new ContentValues();
            data.put( "cDescripcion", zonas.getcDescripcion() );
            data.put( "nCodigo", zonas.getnCodigo() );
            data.put( "nNivel", zonas.getnNivel() );
            data.put( "nTipo", zonas.getnTipo() );
            data.put( "nSubTipo", zonas.getnSubTipo() );

            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_ZONAS, null, data );
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public void rellenaSpinner( Spinner spinner, int nNivel, Context m_context, int nTipo ) {
        try {
            Cursor cursor = null;
            String[] args = new String[2];
            args[0] = String.valueOf( nNivel );
            args[1] = String.valueOf( nTipo );

            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nCodigo _id, cDescripcion FROM " + Constantes.TABLE_ZONAS + " WHERE nNivel = ? and nTipo = ? " , args );

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

    public void rellenaSpinnerDistrito( Spinner spinner, int nDepartamento, Context m_context, int nProvincia ) {
        try {
            Cursor cursor = null;
            String[] args = new String[2];
            args[0] = String.valueOf( nDepartamento );
            args[1] = String.valueOf( nProvincia );

            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nCodigo _id, cDescripcion FROM " + Constantes.TABLE_ZONAS + " WHERE nTipo = ? and nSubTipo = ? " , args );

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

    public void LimpiarZonas(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_ZONAS, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarZonasXnivel( int nNivel, int nTipo ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_ZONAS, "nNivel = ? and nTipo = ? ", new String[]{ String.valueOf( nNivel ), String.valueOf( nTipo ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<Zonas> listaZonas( ) {
        ArrayList<Zonas> listaCreditos = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_ZONAS + " ", null );
            if (cursor.moveToFirst()) {
                do {
                    Zonas creditos = new Zonas();
                    creditos.setcDescripcion(cursor.isNull(cursor.getColumnIndex("cDescripcion")) ? "" : cursor.getString(cursor.getColumnIndex("cDescripcion")));
                    creditos.setnCodigo(cursor.isNull(cursor.getColumnIndex("nCodigo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodigo")));
                    creditos.setnNivel(cursor.isNull(cursor.getColumnIndex("nNivel")) ? 0 : cursor.getInt(cursor.getColumnIndex("nNivel")));
                    creditos.setnTipo(cursor.isNull(cursor.getColumnIndex("nTipo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nTipo")));
                    listaCreditos.add( creditos );

                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return listaCreditos;
    }

    public ArrayList<Zonas> listaZonasNivelTipo( int nNivel, int nSubNivel ) {
        ArrayList<Zonas> listaCreditos = new ArrayList<>();

        String[] args = new String[2];
        args[0] = String.valueOf( nNivel );
        args[1] = String.valueOf( nSubNivel );

        Cursor cursor = null;

        try {
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_ZONAS + " WHERE nNivel = ? and nTipo = ? " , args );
            if (cursor.moveToFirst()) {
                do {
                    Zonas creditos = new Zonas();
                    creditos.setcDescripcion(cursor.isNull(cursor.getColumnIndex("cDescripcion")) ? "" : cursor.getString(cursor.getColumnIndex("cDescripcion")));
                    creditos.setnCodigo(cursor.isNull(cursor.getColumnIndex("nCodigo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodigo")));
                    creditos.setnNivel(cursor.isNull(cursor.getColumnIndex("nNivel")) ? 0 : cursor.getInt(cursor.getColumnIndex("nNivel")));
                    creditos.setnTipo(cursor.isNull(cursor.getColumnIndex("nTipo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nTipo")));
                    listaCreditos.add( creditos );

                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return listaCreditos;
    }
}
