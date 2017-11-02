package com.tibox.lucas.network.connections;

import android.os.Environment;

/**
 * Created by desa02 on 01/03/2017.
 */

public class AppConfig {
    private AppConfig(){
    }
    public static enum Localidad{
        PERU
    }
    public static enum CompileType {
        PRODUCCION, DESARROLLO
    }
    public static Localidad localidad = Localidad.PERU;
    public static CompileType tipoCompilacion = CompileType.PRODUCCION;
    public static final ConnectionInfo connection = new ConexionLocal();

    public final static String directorioImagenes = Environment
            .getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ) + "/lucasPictures/";

    public final static String directorioPdf = Environment
            .getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS ) + "/lucasPDF/";

    public final static String directorioImagenes_api19menor = Environment.
            getExternalStorageDirectory() + "/Pictures/lucasPictures/";

}
