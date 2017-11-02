package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.tibox.lucas.R;
import com.tibox.lucas.adaptadores.spinner.NothingSelectedSpinnerAdapter;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.utilidades.Constantes;
import com.weiwangcn.betterspinner.library.BetterSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

/**
 * Created by desa02 on 10/03/2017.
 */

public class CatalagoCodigosDAO {

    public int insertar(CatalagoCodigos catalagoCodigos){
        long resultado = -1;
        try
        {
            ContentValues data = new ContentValues();
            data.put( "cNomCod", catalagoCodigos.getcNomCod() );
            data.put( "nCodigo", catalagoCodigos.getnCodigo() );
            data.put( "nValor", Integer.valueOf( catalagoCodigos.getnValor() ) );
            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_CATALAGOCODIGOS, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public ArrayList<CatalagoCodigos> listCatalagoCodigos( ) {
        ArrayList<CatalagoCodigos> listaMantFamilia = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nValor, cNomCod FROM " + Constantes.TABLE_CATALAGOCODIGOS + " ", null );
            if (cursor.moveToFirst()) {
                do {
                    CatalagoCodigos catalago = new CatalagoCodigos();
                    catalago.setnValor(cursor.isNull(cursor.getColumnIndex("nValor")) ? 0 : cursor.getInt(cursor.getColumnIndex("nValor")));
                    catalago.setcNomCod(cursor.isNull(cursor.getColumnIndex("cNomCod")) ? "" : cursor.getString(cursor.getColumnIndex("cNomCod")));

                    listaMantFamilia.add( catalago );
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return listaMantFamilia;
    }

    public void rellenaSpinner( Spinner spinner, int nCodigoVar, Context m_context ) {
        try {
            Cursor cursor = null;
            String[] args = new String[1];
            args[0] = String.valueOf( nCodigoVar );
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nValor _id, cNomCod FROM " + Constantes.TABLE_CATALAGOCODIGOS + " WHERE nCodigo = ? " , args);

            @SuppressWarnings( "deprecation" )
            SimpleCursorAdapter spinnerCodigo = new SimpleCursorAdapter( m_context,
                    android.R.layout.simple_spinner_item,
                    cursor, new String[] { "cNomCod" },
                    new int[] { android.R.id.text1 } );

            spinnerCodigo.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            spinner.setAdapter( spinnerCodigo );
            spinner.setSelection( 0 );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    public void rellenaBetterSpinner(MaterialBetterSpinner spinner, int nCodigoVar, Context m_context ) {
        try {
            Cursor cursor = null;
            String[] args = new String[1];
            args[0] = String.valueOf( nCodigoVar );
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nValor _id, cNomCod FROM " + Constantes.TABLE_CATALAGOCODIGOS + " WHERE nCodigo = ? " , args);

            @SuppressWarnings( "deprecation" )
            SimpleCursorAdapter spinnerCodigo = new SimpleCursorAdapter( m_context,
                    android.R.layout.simple_dropdown_item_1line,
                    cursor, new String[] { "cNomCod" },
                    new int[] { android.R.id.text1 } );

            spinnerCodigo.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

            //ArrayAdapter<String> adapter = new ArrayAdapter<String>( m_context, android.R.layout.simple_dropdown_item_1line, list);

            spinner.setAdapter( spinnerCodigo );
            spinner.setSelection( 0 );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    public void rellenaSpinnerOrden( Spinner spinner, int nCodigoVar, Context m_context, int nOrden ) {
        try {
            Cursor cursor = null;
            String[] args = new String[1];
            args[0] = String.valueOf( nCodigoVar );

            if ( nOrden == Constantes.UNO )
                cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nValor _id, cNomCod FROM " + Constantes.TABLE_CATALAGOCODIGOS + " WHERE nCodigo = ? " , args);
            else if ( nOrden == Constantes.DOS )
                cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nValor _id, cNomCod FROM " + Constantes.TABLE_CATALAGOCODIGOS + " WHERE nCodigo = ? order by nValor asc " , args);
            else
                cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nValor _id, cNomCod FROM " + Constantes.TABLE_CATALAGOCODIGOS + " WHERE nCodigo = ? order by nValor desc " , args);

            @SuppressWarnings( "deprecation" )
            SimpleCursorAdapter spinnerCodigo = new SimpleCursorAdapter( m_context,
                    android.R.layout.simple_spinner_item,
                    cursor, new String[] { "cNomCod" },
                    new int[] { android.R.id.text1 } );

            spinnerCodigo.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            spinner.setAdapter( spinnerCodigo );
            spinner.setSelection( 0 );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    public void LimpiarCatalogoCodigos(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_CATALAGOCODIGOS, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarCatalogoCodigosxID( int nCodigo ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_CATALAGOCODIGOS, "nCodigo = ? ", new String[]{ String.valueOf( nCodigo ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

}
