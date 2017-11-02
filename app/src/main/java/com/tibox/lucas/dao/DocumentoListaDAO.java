package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tibox.lucas.entidad.DocumentoLista;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 23/03/2017.
 */

public class DocumentoListaDAO {
    public int insertar( DocumentoLista documentoLista ){
        long resultado = -1;
        try {
            ContentValues data = new ContentValues();
            data.put( "Descripcion", documentoLista.getDescripcion() );
            data.put( "TipoDocumento", documentoLista.getTipoDocumento() );
            data.put( "Obligatorio", documentoLista.getObligatorio() );
            data.put( "Estado", documentoLista.getEstado() );
            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_DOCUMENTOLISTA, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public ArrayList<DocumentoLista> ListarDocumentoLista (int estado ){
        Cursor cursor = null;
        ArrayList<DocumentoLista> lvDocumentoLista = new ArrayList<>();

        try {

            String[] args = new String[1];
            args[0] = String.valueOf( estado );

            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+Constantes.TABLE_DOCUMENTOLISTA +"", null );

            if(cursor.moveToFirst()){
                do{
                    DocumentoLista documentoLista = new DocumentoLista();

                    documentoLista.setTipoDocumento(cursor.isNull(cursor.getColumnIndex("TipoDocumento")) ? 0 : cursor.getInt(cursor.getColumnIndex("TipoDocumento")));
                    documentoLista.setObligatorio(cursor.isNull(cursor.getColumnIndex("Obligatorio")) ? 0 : cursor.getInt(cursor.getColumnIndex("Obligatorio")));
                    documentoLista.setDescripcion(cursor.isNull(cursor.getColumnIndex("Descripcion")) ? "" : cursor.getString(cursor.getColumnIndex("Descripcion")));
                    documentoLista.setEstado(cursor.isNull(cursor.getColumnIndex("Estado")) ? 0 : cursor.getInt(cursor.getColumnIndex("Estado")));
                    documentoLista.setIdDocumentoLista( cursor.isNull(cursor.getColumnIndex("IdDocumentoLista")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdDocumentoLista")));

                    lvDocumentoLista.add( documentoLista );
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
        return lvDocumentoLista;
    }

    public boolean BuscarTipoDocumento( int TipoDocumento ){
        Boolean exist = false;
        try {
            Cursor mCursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_DOCUMENTOLISTA + " WHERE TipoDocumento = "+ TipoDocumento +"", null);

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

    public void LimpiarDocumentoLista(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_DOCUMENTOLISTA, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarListaxIdDocumentoLista( int IdDocumentoLista ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_DOCUMENTOLISTA, "IdDocumentoLista = ? ", new String[]{ String.valueOf( IdDocumentoLista ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

}
