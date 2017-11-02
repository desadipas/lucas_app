package com.tibox.lucas.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tibox.lucas.entidad.BandejaCreditos;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 18/04/2017.
 */

public class BandejaCreditosDAO {

    public int insertar(BandejaCreditos bandejaCreditos){
        long resultado = -1;
        try
        {
            ContentValues data = new ContentValues();
            data.put( "nIdFlujoMaestro", bandejaCreditos.getnIdFlujoMaestro() );
            data.put( "nIdFlujo", bandejaCreditos.getnIdFlujo() );
            data.put( "nCodCred", bandejaCreditos.getnCodCred() );
            data.put( "nCodAge", bandejaCreditos.getnCodAge() );
            data.put( "nCodPersTitular", bandejaCreditos.getnCodPersTitular() );
            data.put( "nPrestamo", bandejaCreditos.getnPrestamo() );
            data.put( "nEstado", bandejaCreditos.getnEstado() );
            data.put( "nProd", bandejaCreditos.getnProd() );
            data.put( "nSubProd", bandejaCreditos.getnSubProd() );
            data.put( "nOrdenFlujo", bandejaCreditos.getnOrdenFlujo() );
            data.put( "nNecesario", bandejaCreditos.getnNecesario() );
            data.put( "nActibo", bandejaCreditos.getnActibo() );
            data.put( "cNomAge", bandejaCreditos.getcNomAge() );
            data.put( "cCliente", bandejaCreditos.getcCliente() );
            data.put( "cSubProducto", bandejaCreditos.getcSubProducto() );
            data.put( "cEstado", bandejaCreditos.getcEstado() );
            data.put( "cNombreProceso", bandejaCreditos.getcNombreProceso() );
            data.put( "cDocumento", bandejaCreditos.getcDocumento() );
            data.put( "cFechaReg", bandejaCreditos.getcFechaReg() );
            data.put( "cMoneda", bandejaCreditos.getcMoneda() );

            resultado = DataBaseHelper.myDataBase.insert( Constantes.TABLE_CREDITOS, null, data );

        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return ((int)resultado);
    }

    public ArrayList<BandejaCreditos> listBandejaCreditos( ) {
        ArrayList<BandejaCreditos> listaCreditos = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = DataBaseHelper.myDataBase.rawQuery( "SELECT * FROM " + Constantes.TABLE_CREDITOS + " ", null );
            if (cursor.moveToFirst()) {
                do {
                    BandejaCreditos creditos = new BandejaCreditos();
                    creditos.setnIdFlujoMaestro(cursor.isNull(cursor.getColumnIndex("nIdFlujoMaestro")) ? 0 : cursor.getInt(cursor.getColumnIndex("nIdFlujoMaestro")));
                    creditos.setnIdFlujo(cursor.isNull(cursor.getColumnIndex("nIdFlujo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nIdFlujo")));
                    creditos.setnPrestamo(cursor.isNull(cursor.getColumnIndex("nPrestamo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nPrestamo")));
                    creditos.setnOrdenFlujo(cursor.isNull(cursor.getColumnIndex("nOrdenFlujo")) ? 0 : cursor.getInt(cursor.getColumnIndex("nOrdenFlujo")));
                    creditos.setnCodPersTitular(cursor.isNull(cursor.getColumnIndex("nCodPersTitular")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodPersTitular")));
                    creditos.setnCodAge(cursor.isNull(cursor.getColumnIndex("nCodAge")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodAge")));
                    creditos.setnCodCred(cursor.isNull(cursor.getColumnIndex("nCodCred")) ? 0 : cursor.getInt(cursor.getColumnIndex("nCodCred")));

                    creditos.setcSubProducto(cursor.isNull(cursor.getColumnIndex("cSubProducto")) ? "" : cursor.getString(cursor.getColumnIndex("cSubProducto")));
                    creditos.setcMoneda(cursor.isNull(cursor.getColumnIndex("cMoneda")) ? "" : cursor.getString(cursor.getColumnIndex("cMoneda")));
                    creditos.setcNombreProceso(cursor.isNull(cursor.getColumnIndex("cNombreProceso")) ? "" : cursor.getString(cursor.getColumnIndex("cNombreProceso")));
                    creditos.setcEstado(cursor.isNull(cursor.getColumnIndex("cEstado")) ? "" : cursor.getString(cursor.getColumnIndex("cEstado")));
                    creditos.setcCliente(cursor.isNull(cursor.getColumnIndex("cCliente")) ? "" : cursor.getString(cursor.getColumnIndex("cCliente")));
                    creditos.setcFechaReg(cursor.isNull(cursor.getColumnIndex("cFechaReg")) ? "" : cursor.getString(cursor.getColumnIndex("cFechaReg")));
                    creditos.setcDocumento(cursor.isNull(cursor.getColumnIndex("cDocumento")) ? "" : cursor.getString(cursor.getColumnIndex("cDocumento")));

                    creditos.setnEstado( cursor.isNull(cursor.getColumnIndex("nEstado")) ? 0 : cursor.getInt(cursor.getColumnIndex("nEstado")) );

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

    public void eliminarXidFlujoMaestro( int nIdFlujoMaestro ){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_CREDITOS, "nIdFlujoMaestro = ? ", new String[]{ String.valueOf( nIdFlujoMaestro ) });
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public void LimpiarTabla(){
        try{
            DataBaseHelper.myDataBase.delete( Constantes.TABLE_CREDITOS, null, null );
        }
        catch ( Exception ex){
            ex.printStackTrace();
        }
    }


}
