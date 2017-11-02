package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.tibox.lucas.entidad.DocumentoObligatorios;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desa02 on 23/03/2017.
 */

public class DocumentoObligatoriosDAO {
    public int insertar( DocumentoObligatorios obligatorios ){
        long resultado = -1;
        try {
            ContentValues data = new ContentValues();
            data.put( "nCodigo", obligatorios.getnCodigo() );
            data.put( "cNombre", obligatorios.getcNombre() );
            data.put( "nObligatorio", obligatorios.getnObligatorio() );
            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_DOCUMENTO_OBLIGATORIOS, null, data );
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public ArrayList<DocumentoObligatorios> ListarDocumentoObligatorios (int estado ){
        Cursor cursor = null;
        ArrayList<DocumentoObligatorios> lvLista = new ArrayList<>();
        try {
            String[] args = new String[1];
            args[0] = String.valueOf( estado );
            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+Constantes.TABLE_DOCUMENTO_OBLIGATORIOS +"", null );
            if(cursor.moveToFirst()){
                do{
                    DocumentoObligatorios obligatorios = new DocumentoObligatorios();
                    obligatorios.setnCodigo(cursor.isNull(cursor.getColumnIndex("nCodigo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodigo")));
                    obligatorios.setnObligatorio(cursor.isNull(cursor.getColumnIndex("nObligatorio")) ? 0 : cursor.getInt(cursor.getColumnIndex("nObligatorio")));
                    obligatorios.setcNombre(cursor.isNull(cursor.getColumnIndex("cNombre")) ? "" : cursor.getString(cursor.getColumnIndex("cNombre")));
                    lvLista.add( obligatorios );
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
        return lvLista;
    }

    public void rellenaSpinner(Spinner spinner, Context m_context ) {
        try {
            Cursor cursor = null;
            String[] args = new String[1];

            cursor = DataBaseHelper.myDataBase.rawQuery("SELECT nCodigo _id, cNombre FROM " + Constantes.TABLE_DOCUMENTO_OBLIGATORIOS + "", null);

            @SuppressWarnings( "deprecation" )
            SimpleCursorAdapter spinnerCodigo = new SimpleCursorAdapter( m_context,
                    android.R.layout.simple_spinner_item,
                    cursor, new String[] { "cNombre" },
                    new int[] { android.R.id.text1 } );

            spinnerCodigo.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            spinner.setAdapter( spinnerCodigo );
            spinner.setSelection( 0 );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    public DocumentoObligatorios ObtenerDatosTipoDoc( int nIdCodigo ){
        ArrayList<DocumentoObligatorios> list = new ArrayList<>();
        Cursor cursor = null;
        DocumentoObligatorios obligatorios = new DocumentoObligatorios();
        obligatorios.setcNombre( "" );
        try {
            String[] args = new String[1];
            args[0] = String.valueOf( nIdCodigo );
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT nCodigo _id, cNombre, nObligatorio FROM " + Constantes.TABLE_DOCUMENTO_OBLIGATORIOS + " WHERE nCodigo = ?" , args);

            if (cursor.moveToFirst()) {
                do {
                    obligatorios.setnCodigo( cursor.isNull(cursor.getColumnIndex("nCodigo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodigo")));
                    obligatorios.setcNombre( cursor.isNull(cursor.getColumnIndex("cNombre")) ? "" : cursor.getString(cursor.getColumnIndex("cNombre")));
                    obligatorios.setnObligatorio( cursor.isNull(cursor.getColumnIndex("nObligatorio")) ? 0 : cursor.getInt(cursor.getColumnIndex("nObligatorio")));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return obligatorios;
    }

    public List<DocumentoObligatorios> ObtenerDocumentosObligatorios( int nObligatorio ){
        ArrayList<DocumentoObligatorios> list = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] args = new String[1];
            args[0] = String.valueOf( Constantes.UNO );
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_DOCUMENTO_OBLIGATORIOS + " WHERE nObligatorio = ?" , args);

            if (cursor.moveToFirst()) {
                do {
                    DocumentoObligatorios obligatorios = new DocumentoObligatorios();
                    obligatorios.setnCodigo( cursor.isNull(cursor.getColumnIndex("nCodigo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodigo")));
                    obligatorios.setcNombre( cursor.isNull(cursor.getColumnIndex("cNombre")) ? "" : cursor.getString(cursor.getColumnIndex("cNombre")));
                    obligatorios.setnObligatorio( cursor.isNull(cursor.getColumnIndex("nObligatorio")) ? 0 : cursor.getInt(cursor.getColumnIndex("nObligatorio")));
                    list.add( obligatorios );
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void LimpiarDocumentoObligatorios(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_DOCUMENTO_OBLIGATORIOS, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }
}
