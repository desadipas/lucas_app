package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tibox.lucas.entidad.FotoGarantia;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 16/03/2017.
 */

public class FotoGarantiaDAO {
    public int insertar( FotoGarantia fotoGarantia ){
        long resultado = -1;
        try {
            ContentValues data = new ContentValues();
            data.put( "IdGarantia", fotoGarantia.getIdGarantia() );
            data.put( "Descripcion", fotoGarantia.getDescripcion() );
            data.put( "RutaFotoArticulo", fotoGarantia.getRutaFotoArticulo() );
            data.put( "Estado", fotoGarantia.getEstado() );
            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_FOTOGARANTE, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }

        return ((int)resultado);

    }

    public void LimpiarFotoGarantia(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTOGARANTE, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarFotosxSeleccion( int IdFotoArticulo ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTOGARANTE, "IdFotoArticulo = ? ", new String[]{ String.valueOf( IdFotoArticulo ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarFotosxIdGarantia( int IdGarantia ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_FOTOGARANTE, "IdGarantia = ? ", new String[]{ String.valueOf( IdGarantia ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void ActualizarEstado ( int Estado, int IdFotoArticulo  ){
        try {
            ContentValues data = new ContentValues();
            data.put( "Estado", Estado );
            DataBaseHelper.myDataBase.update( Constantes.TABLE_FOTOGARANTE, data, "IdFotoArticulo = ?", new String[]{String.valueOf( IdFotoArticulo )});

        }catch ( Exception ex ){
            ex.printStackTrace();
        }
    }

    public void ActualizarIdGarantia ( int IdGarantia, int Estado  ){
        try {
            ContentValues data = new ContentValues();
            data.put( "IdGarantia", IdGarantia );
            data.put( "Estado", Estado );
            DataBaseHelper.myDataBase.update( Constantes.TABLE_FOTOGARANTE, data, "Estado = ?", new String[]{String.valueOf( Constantes.ESTADO_PORAGREGAR )});

        }catch ( Exception ex ){
            ex.printStackTrace();
        }
    }

    public ArrayList<FotoGarantia> ListarFotoGarantia ( int estado, int nCargar, int IdGarantia ){
        Cursor cursor = null;
        ArrayList<FotoGarantia> lvFotoGarantia = new ArrayList<>();

        try {

            String[] args = new String[1];
            args[0] = String.valueOf( IdGarantia );

            if ( nCargar == Constantes.UNO )
                cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+Constantes.TABLE_FOTOGARANTE +" Where Estado = 1 and IdGarantia = ? ", args );
            else
                cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+Constantes.TABLE_FOTOGARANTE +" Where Estado = 0 ", null );

            if(cursor.moveToFirst()){
                do{
                    FotoGarantia fotoGarantia = new FotoGarantia();

                    fotoGarantia.setIdFotoArticulo(cursor.isNull(cursor.getColumnIndex("IdFotoArticulo")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdFotoArticulo")));
                    fotoGarantia.setIdGarantia(cursor.isNull(cursor.getColumnIndex("IdGarantia")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdGarantia")));
                    fotoGarantia.setRutaFotoArticulo(cursor.isNull(cursor.getColumnIndex("RutaFotoArticulo")) ? "" : cursor.getString(cursor.getColumnIndex("RutaFotoArticulo")));
                    fotoGarantia.setDescripcion(cursor.isNull(cursor.getColumnIndex("Descripcion")) ? "" : cursor.getString(cursor.getColumnIndex("Descripcion")));
                    fotoGarantia.setEstado(cursor.isNull(cursor.getColumnIndex("Estado")) ? 0 : cursor.getInt(cursor.getColumnIndex("Estado")));

                    lvFotoGarantia.add( fotoGarantia );
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
        return lvFotoGarantia;
    }

    public ArrayList<FotoGarantia> ListarFotoxIdGarantia ( int IdGarantia ){
        Cursor cursor = null;
        ArrayList<FotoGarantia> lvFotoGarantia = new ArrayList<>();

        try {

            String[] args = new String[1];
            args[0] = String.valueOf( IdGarantia );

            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+Constantes.TABLE_FOTOGARANTE +" Where IdGarantia = ? ", args );


            if(cursor.moveToFirst()){
                do{
                    FotoGarantia fotoGarantia = new FotoGarantia();

                    fotoGarantia.setIdFotoArticulo(cursor.isNull(cursor.getColumnIndex("IdFotoArticulo")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdFotoArticulo")));
                    fotoGarantia.setIdGarantia(cursor.isNull(cursor.getColumnIndex("IdGarantia")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdGarantia")));
                    fotoGarantia.setRutaFotoArticulo(cursor.isNull(cursor.getColumnIndex("RutaFotoArticulo")) ? "" : cursor.getString(cursor.getColumnIndex("RutaFotoArticulo")));
                    fotoGarantia.setDescripcion(cursor.isNull(cursor.getColumnIndex("Descripcion")) ? "" : cursor.getString(cursor.getColumnIndex("Descripcion")));
                    fotoGarantia.setEstado(cursor.isNull(cursor.getColumnIndex("Estado")) ? 0 : cursor.getInt(cursor.getColumnIndex("Estado")));

                    lvFotoGarantia.add( fotoGarantia );
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
        return lvFotoGarantia;
    }

    public Boolean consultarFotoGarantiaActualizar( int IdGarantia ){
        Boolean resp = false;
        String[] args = new String[1];
        args[0] = String.valueOf( IdGarantia );
        try {
            Cursor mCursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_FOTOGARANTE + " Where IdGarantia = ? ", args );

            if ( mCursor != null ) {
                if ( mCursor.getCount() > 0 ) {
                    mCursor.close();
                    resp = true;
                }
                else {
                    mCursor.close();
                    resp = false;
                }
            }
        }catch ( Exception ex ){
            ex.printStackTrace();
        }
        return resp;
    }
}
