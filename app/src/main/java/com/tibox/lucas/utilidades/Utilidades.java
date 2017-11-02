package com.tibox.lucas.utilidades;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.tibox.lucas.network.connections.AppConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilidades {
    private static final int MAX_IMAGE_SIZE = 960;

    public static final HashMap<String, String> obtenerDataString( String mensaje ) {
        HashMap<String, String> dataMap = new HashMap<String, String>();
        try {
            String[] listaDatos = mensaje.split( ";" );
            for ( int k = 0; k < listaDatos.length; k++ ) {
                String dato = listaDatos[k];
                String[] separado = dato.split( ":" );
                if ( separado.length == 2 ) {
                    dataMap.put( separado[0], separado[1] );
                }
            }
        }
        catch (Exception ex) {
            return new HashMap<String, String>();
        }


        return dataMap;
    }

    public static final int parseInt( String strValue, int defaultValue ) {
        try {
            return Integer.parseInt( strValue );
        }
        catch ( Exception ex ) {
            return defaultValue;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaHoraActual(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String horaActual = dateFormat.format(new Date());

        return horaActual;
    }

    public static int obtenerMesActual(){
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int obtenerDiaActualSemana(){
        return Calendar.getInstance().get( Calendar.DAY_OF_WEEK );
    }

    public static Date addDays( Date date, int days) {
            Calendar cal = Calendar.getInstance();
            cal.setTime( date );
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
    }

    public static int diferenciaEnDias2( Date fechaMayor, Date fechaMenor ) {
        long diferenciaEn_ms = fechaMenor.getTime() - fechaMayor.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

    public static int hallaRestaFecha( Date fechaMayor, Date fechaMenor ) {
        long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

    public static String saveImagePhone(Bitmap bitmap, String NombreDato, String Directorio ) throws InterruptedException {
        if (bitmap != null) {
            File newdir = new File( Directorio );
            newdir.mkdirs();
            Thread.sleep(1000);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File( Directorio + NombreDato + "_" +  timeStamp + ".jpg" );
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bytes.toByteArray());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Directorio + NombreDato + "_" +  timeStamp + ".jpg";
        }
        return "";
    }

    public static boolean deleteDirectory( File path ) {
        if ( path.exists() ) {
            File[] files = path.listFiles();

            if ( files == null ) {
                return true;
            }
            for ( int i = 0; i < files.length; i++ ) {
                if ( files[i].isDirectory() ) {
                    deleteDirectory( files[i] );
                }
                else {
                    files[i].delete();
                }
            }
        }
        return ( path.delete() );
    }

    public static boolean existeCarpetaMovil(String strRuta) {
        File file = new File(strRuta);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }

    public static Bitmap resizeImage( Bitmap image ) {
        int width = image.getWidth();
        int height = image.getHeight();

        if ( width < MAX_IMAGE_SIZE && height < MAX_IMAGE_SIZE )
            return image;

        int WidthResult;
        int HeightResult;
        if ( width > height ) {
            WidthResult = MAX_IMAGE_SIZE;
            HeightResult = ( height * MAX_IMAGE_SIZE ) / width;
        }
        else {
            HeightResult = MAX_IMAGE_SIZE;
            WidthResult = ( width * MAX_IMAGE_SIZE ) / height;
        }

        return Bitmap.createScaledBitmap( image, WidthResult, HeightResult, true );
    }

    public static Double TruncateDecimal( double number ) {
        return Math.round( number * 100 ) / 100.0;
    }

    public static Double TruncateDecimalOcho( double number ) {
        return Math.round( number * 100000000 ) / 100000000.0;
    }

    public static int getCursorSpinnerPositionById( Spinner sp, int id ) {
        SimpleCursorAdapter myAdap = ( SimpleCursorAdapter ) sp.getAdapter();
        int spinnerPosition = 0;
        for ( int i = 0; i < myAdap.getCount(); i++ ) {
            if ( myAdap.getItemId( i ) == id ) {
                spinnerPosition = i;
                break;
            }
        }
        return spinnerPosition;
    }

    public static boolean IsNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static String FormatoMonedaSoles( double valor ){

        String valorInicial = "0.00";

        if ( Double.parseDouble( valorInicial ) == valor ) {
            return "S/ 0.00";
        }
        else{
            DecimalFormat format = new DecimalFormat( "#,###.00" );
            format.setDecimalFormatSymbols(new DecimalFormatSymbols( Locale.US ) );
            valorInicial = "S/ " + format.format( valor );
            return valorInicial;
        }
    }

    public static String FormatoMoneda( double valor ){

        String valorInicial = "0.00";

        if ( Double.parseDouble( valorInicial ) == valor ) {
            return "0.00";
        }
        else{
            DecimalFormat format = new DecimalFormat( "#,###.00" );
            format.setDecimalFormatSymbols(new DecimalFormatSymbols( Locale.US ) );
            valorInicial = "" + format.format( valor );
            return valorInicial;
        }
    }

    public static boolean isValidPasswordSpecialCharacters( final String password ) {
        Pattern pattern;
        Matcher matcher;
        //Debe tener al menos un dato del 0 al 9, Mayuscula, caracter especial y minimo de 7 y maximo 15 de longitud de caracteres.
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{7,15}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        //valida vacio, formato email
        return !(email == null || TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
