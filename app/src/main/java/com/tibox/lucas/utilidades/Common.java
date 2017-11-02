package com.tibox.lucas.utilidades;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by apple on 14/09/16.
 */
public class Common {
    private static final int MAX_IMAGE_SIZE = 960;


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

    public static boolean ExistFile(String uri){
        boolean result=false;

        File file= new File(uri);

        if(file.exists()){
            result=true;
        }

        return result;
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

    public static Bitmap getPreview( String uri ) throws FileNotFoundException {
        final int THUMBNAIL_SIZE = 200;

        FileInputStream fis = new FileInputStream( uri );
        Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

        imageBitmap = Bitmap.createScaledBitmap( imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress( Bitmap.CompressFormat.JPEG, 50, baos );

        byte[] array = baos.toByteArray();
        return BitmapFactory.decodeByteArray( array, 0, array.length );
    }

    public static Float TruncateDecimal( double number ) {
        return Math.round( number * 100 ) / 100.f;
    }

    public static int getDPI( int size, DisplayMetrics metrics ) {
        return ( size * metrics.densityDpi ) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @SuppressLint( "SimpleDateFormat" )
    public static Date getParsedDateTime( String strDate ) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
        return sdf.parse( strDate );
    }

    public static Date getParsedDate( String strDate ) throws ParseException {
        return getParsedDate(strDate, "dd/MM/yyyy");
    }

    @SuppressLint( "SimpleDateFormat" )
    public static Date getParsedDate( String strDate, String format ) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat( format );
        return sdf.parse(strDate);
    }

    public static String getFormattedDate( Date date, String format ) {
        return ( String ) DateFormat.format(format, date);
    }

    public static String formatStringDate( String strDate, String inFormat, String outFormat ) {
        String outDate;
        try {
            Date date = getParsedDate( strDate, inFormat );
            outDate = getFormattedDate( date, outFormat );
        }
        catch ( ParseException ex ) {
            outDate = "<Date Parse Error>";
        }
        return outDate;
    }

    public static int parseInt( String strInt, int defaultValue ) {
        try {
            return Integer.parseInt( strInt );
        }
        catch ( NumberFormatException ex ) {
            return defaultValue;
        }
    }

    public static float parseFloat( String strFloat, float defaultValue ) {
        try {
            return Float.parseFloat(strFloat);
        }
        catch ( NumberFormatException ex ) {
            return defaultValue;
        }
    }

    public static double parseDouble( String strDouble, double defaultValue ) {
        try {
            return Double.parseDouble(strDouble);
        }
        catch ( NumberFormatException ex ) {
            return defaultValue;
        }
    }

    public static int getIntFromEditText( EditText editText, int defaultValue ) {
        try {
            if ( editText.getText().toString().trim().equals( "" ) ) {
                return defaultValue;
            }
            return Integer.parseInt(editText.getText().toString().trim());
        }
        catch ( NumberFormatException ex ) {
            return defaultValue;
        }
    }

    public static float getFloatFromEditText( EditText editText, float defaultValue ) {
        try {
            if ( editText.getText().toString().trim().equals( "" ) ) {
                return defaultValue;
            }
            return Float.parseFloat(editText.getText().toString().trim());
        }
        catch ( NumberFormatException ex ) {
            return defaultValue;
        }
    }

    public static double getDoubleFromEditText( EditText editText, double defaultValue ) {
        try {
            if ( editText.getText().toString().trim().equals( "" ) ) {
                return defaultValue;
            }
            return Double.parseDouble(editText.getText().toString().trim());
        }
        catch ( NumberFormatException ex ) {
            return defaultValue;
        }
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

    public static <T> void setSpinnerArrayAdapter( Spinner sp, List<T> itemList ) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>( sp.getContext(), android.R.layout.simple_spinner_item,
                itemList );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        sp.setAdapter( adapter );
//		sp.setSelection( 0 );
    }

    public static void setSpinnerArrayAdapter( Spinner sp, int resArrayId ) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( sp.getContext(), resArrayId,
                android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        sp.setAdapter( adapter );
//		sp.setSelection( 0 );
    }

    public static boolean validaStringNoVacio( String str ) {
        if ( str != null ) {
            return !str.trim().isEmpty();
        }
        else {
            return false;
        }
    }

    public static boolean compareString( String str1, String str2 ) {
        if ( ( str1 == null ) && ( str2 == null ) ) {
            return true;
        }
        else if ( ( str1 != null ) && ( str2 != null ) ) {
            return str1.equals( str2 );
        }
        else {
            return false;
        }
    }

    public static boolean ExisteCarpetaMovil( String strRuta ) {
        File file = new File( strRuta );
        try {
            Thread.sleep( 3000 );
        }
        catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        if ( !file.isFile() ) {
            return false;
        }
        else {
            return true;
        }
    }

    public static String createStringFromDictionary( Hashtable<String, String> dictionary ) {
        String str = "";

        Enumeration<String> e = dictionary.keys();

        for ( String key : Collections.list(e) ) {
            if ( str.length() > 0 ) {
                str += ";";
            }
            str += key + ":" + dictionary.get( key );
        }

        return str;
    }

    public static String getRealPathFromUri( Activity activity, Uri contentUri ) {
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressWarnings( "deprecation" )
            Cursor cursor = activity.managedQuery( contentUri, proj, null, null, null );
            int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            cursor.moveToFirst();

            return cursor.getString( column_index );
        }
        catch ( IllegalArgumentException e ) {
            return String.valueOf( contentUri );
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null) && networkInfo.isConnected();
    }

    public static String obtenerImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imeiNumber = telephonyManager.getDeviceId();
        if (imeiNumber == null) {
            imeiNumber = "";
        }
        return imeiNumber;
    }
}
