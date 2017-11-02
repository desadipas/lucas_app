package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tibox.lucas.entidad.Garantia;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 21/03/2017.
 */

public class GarantiaDAO {
    public int insertar( Garantia garantia ){
        long resultado = -1;
        try {
            ContentValues data = new ContentValues();
            data.put( "Familia", garantia.getFamilia() );
            data.put( "Articulo", garantia.getArticulo() );
            data.put( "Marca", garantia.getMarca() );
            data.put( "Linea", garantia.getLinea() );
            data.put( "Modelo", garantia.getModelo() );
            data.put( "Descripcion", garantia.getDescripcion() );
            data.put( "DescripcionGarantia", garantia.getDescripcionGarantia() );
            data.put( "Prestamo", garantia.getPrestamo() );
            data.put( "VTasado", garantia.getVTasado() );
            data.put( "cNomFamilia", garantia.getcNomFamilia() );
            data.put( "cNomArticulo", garantia.getcNomArticulo() );
            data.put( "cNomMarca", garantia.getcNomMarca() );
            data.put( "cNomLinea", garantia.getcNomLinea() );
            data.put( "cNomModelo", garantia.getcNomModelo() );

            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_GARANTIA, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }

        return ((int)resultado);
    }

    public Garantia ObtenerGarantiaxId( int IdGarantia ){
        Cursor cursor = null;
        Garantia garantia = new Garantia();
        try {
            String[] args = new String[1];
            args[0] = String.valueOf( IdGarantia );

            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+ Constantes.TABLE_GARANTIA +" Where IdGarantia = ?", args );

            if( cursor.moveToFirst() ) {
                do {
                    garantia.setIdGarantia(cursor.isNull(cursor.getColumnIndex("IdGarantia")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdGarantia")));
                    garantia.setDescripcionGarantia(cursor.isNull(cursor.getColumnIndex("DescripcionGarantia")) ? "" : cursor.getString(cursor.getColumnIndex("DescripcionGarantia")));
                    garantia.setFamilia(cursor.isNull(cursor.getColumnIndex("Familia")) ? 0 : cursor.getInt(cursor.getColumnIndex("Familia")));
                    garantia.setArticulo(cursor.isNull(cursor.getColumnIndex("Articulo")) ? 0 : cursor.getInt(cursor.getColumnIndex("Articulo")));
                    garantia.setMarca(cursor.isNull(cursor.getColumnIndex("Marca")) ? 0 : cursor.getInt(cursor.getColumnIndex("Marca")));
                    garantia.setLinea(cursor.isNull(cursor.getColumnIndex("Linea")) ? 0 : cursor.getInt(cursor.getColumnIndex("Linea")));
                    garantia.setModelo(cursor.isNull(cursor.getColumnIndex("Modelo")) ? 0 : cursor.getInt(cursor.getColumnIndex("Modelo")));
                    garantia.setPrestamo(cursor.isNull(cursor.getColumnIndex("Prestamo")) ? 0.0 : cursor.getDouble(cursor.getColumnIndex("Prestamo")));
                    garantia.setDescripcion(cursor.isNull(cursor.getColumnIndex("Descripcion")) ? "" : cursor.getString(cursor.getColumnIndex("Descripcion")));
                    garantia.setVTasado(cursor.isNull(cursor.getColumnIndex("VTasado")) ? 0.0 : cursor.getDouble(cursor.getColumnIndex("VTasado")));

                    garantia.setcNomFamilia(cursor.isNull(cursor.getColumnIndex("cNomFamilia")) ? "" : cursor.getString(cursor.getColumnIndex("cNomFamilia")));
                    garantia.setcNomArticulo(cursor.isNull(cursor.getColumnIndex("cNomArticulo")) ? "" : cursor.getString(cursor.getColumnIndex("cNomArticulo")));
                    garantia.setcNomMarca(cursor.isNull(cursor.getColumnIndex("cNomMarca")) ? "" : cursor.getString(cursor.getColumnIndex("cNomMarca")));
                    garantia.setcNomLinea(cursor.isNull(cursor.getColumnIndex("cNomLinea")) ? "" : cursor.getString(cursor.getColumnIndex("cNomLinea")));
                    garantia.setcNomModelo(cursor.isNull(cursor.getColumnIndex("cNomModelo")) ? "" : cursor.getString(cursor.getColumnIndex("cNomModelo")));

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
        return garantia;
    }

    public ArrayList<Garantia> ListarGarantias(){
        Cursor cursor = null;
        ArrayList<Garantia> lvGarantia = new ArrayList<>();
        try {

            cursor =  DataBaseHelper.myDataBase.rawQuery(" select * from "+ Constantes.TABLE_GARANTIA +"", null );

            if(cursor.moveToFirst()){
                do{
                    Garantia garantia = new Garantia();

                    garantia.setIdGarantia(cursor.isNull(cursor.getColumnIndex("IdGarantia")) ? 0 : cursor.getInt(cursor.getColumnIndex("IdGarantia")));
                    garantia.setDescripcionGarantia(cursor.isNull(cursor.getColumnIndex("DescripcionGarantia")) ? "" : cursor.getString(cursor.getColumnIndex("DescripcionGarantia")));
                    garantia.setFamilia(cursor.isNull(cursor.getColumnIndex("Familia")) ? 0 : cursor.getInt(cursor.getColumnIndex("Familia")));
                    garantia.setArticulo(cursor.isNull(cursor.getColumnIndex("Articulo")) ? 0 : cursor.getInt(cursor.getColumnIndex("Articulo")));
                    garantia.setMarca(cursor.isNull(cursor.getColumnIndex("Marca")) ? 0 : cursor.getInt(cursor.getColumnIndex("Marca")));
                    garantia.setLinea(cursor.isNull(cursor.getColumnIndex("Linea")) ? 0 : cursor.getInt(cursor.getColumnIndex("Linea")));
                    garantia.setModelo(cursor.isNull(cursor.getColumnIndex("Modelo")) ? 0 : cursor.getInt(cursor.getColumnIndex("Modelo")));
                    garantia.setPrestamo(cursor.isNull(cursor.getColumnIndex("Prestamo")) ? 0.0 : cursor.getDouble(cursor.getColumnIndex("Prestamo")));
                    garantia.setDescripcion(cursor.isNull(cursor.getColumnIndex("Descripcion")) ? "" : cursor.getString(cursor.getColumnIndex("Descripcion")));
                    garantia.setVTasado(cursor.isNull(cursor.getColumnIndex("VTasado")) ? 0.0 : cursor.getDouble(cursor.getColumnIndex("VTasado")));

                    garantia.setcNomFamilia(cursor.isNull(cursor.getColumnIndex("cNomFamilia")) ? "" : cursor.getString(cursor.getColumnIndex("cNomFamilia")));
                    garantia.setcNomArticulo(cursor.isNull(cursor.getColumnIndex("cNomArticulo")) ? "" : cursor.getString(cursor.getColumnIndex("cNomArticulo")));
                    garantia.setcNomMarca(cursor.isNull(cursor.getColumnIndex("cNomMarca")) ? "" : cursor.getString(cursor.getColumnIndex("cNomMarca")));
                    garantia.setcNomLinea(cursor.isNull(cursor.getColumnIndex("cNomLinea")) ? "" : cursor.getString(cursor.getColumnIndex("cNomLinea")));
                    garantia.setcNomModelo(cursor.isNull(cursor.getColumnIndex("cNomModelo")) ? "" : cursor.getString(cursor.getColumnIndex("cNomModelo")));

                    lvGarantia.add( garantia );
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
        return lvGarantia;
    }

    public boolean ActualizarGarantia( Garantia garantia, int IdGarantia ){
        boolean resp = false;
        try {
            ContentValues data = new ContentValues();
            data.put( "Familia", garantia.getFamilia() );
            data.put( "Articulo", garantia.getArticulo() );
            data.put( "Marca", garantia.getMarca() );
            data.put( "Linea", garantia.getLinea() );
            data.put( "Modelo", garantia.getModelo() );
            data.put( "Descripcion", garantia.getDescripcion() );
            data.put( "DescripcionGarantia", garantia.getDescripcionGarantia() );
            data.put( "Prestamo", garantia.getPrestamo() );
            data.put( "VTasado", garantia.getVTasado() );

            data.put( "cNomFamilia", garantia.getcNomFamilia() );
            data.put( "cNomArticulo", garantia.getcNomArticulo() );
            data.put( "cNomMarca", garantia.getcNomMarca() );
            data.put( "cNomLinea", garantia.getcNomLinea() );
            data.put( "cNomModelo", garantia.getcNomModelo() );

            DataBaseHelper.myDataBase.update( Constantes.TABLE_GARANTIA, data, "IdGarantia = ?", new String[]{String.valueOf( IdGarantia )});
            resp = true;
        }catch ( Exception ex ){
            ex.printStackTrace();
            resp = false;
        }
        return resp;
    }

    public void EliminarGarantia(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_GARANTIA, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void EliminarGarantiaXid( int IdGarantia ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_GARANTIA, "IdGarantia = ? ", new String[]{ String.valueOf( IdGarantia ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }


}
