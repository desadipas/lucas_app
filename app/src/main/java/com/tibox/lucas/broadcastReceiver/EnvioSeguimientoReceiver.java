package com.tibox.lucas.broadcastReceiver;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.location.LocationListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by desa02 on 11/04/2017.
 */

public class EnvioSeguimientoReceiver extends BroadcastReceiver implements LocationListener {

    private Context m_Context;
    private String m_cImei = "";
    protected LocationManager m_locationManager;
    protected LocationListener m_listener;
    private double LATITUD;
    private double LONGITUD;

    @Override
    public void onReceive(Context context, Intent intent) {
        m_Context = context;

        int permissionCheck = ContextCompat.checkSelfPermission(m_Context, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            m_cImei = Common.obtenerImei(m_Context);
        }


        PendingIntent locationIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.autentia.intent.GPS_LOCATION_CHANGED"),
                PendingIntent.FLAG_UPDATE_CURRENT);
        int permissionCheckUBI = ContextCompat.checkSelfPermission(m_Context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheckUBI == PackageManager.PERMISSION_GRANTED) {
        }
        /*
        if ( !m_locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            m_Context.sendBroadcast(intent);
        }
        */


        m_locationManager = (LocationManager) m_Context.getSystemService(Context.LOCATION_SERVICE);
        //Minimo tiempo para updates en Milisegundos
        long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 60 * 20; // 1 minuto
        //Minima distancia para updates en metros.
        long MIN_CAMBIO_DISTANCIA_PARA_UPDATES = (long) 1.5; // 1.5 metros
        //m_locationManager.removeUpdates( m_listener );
        //m_listener = new EnvioSeguimientoReceiver();

        m_locationManager.removeUpdates(this);
        m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES,
                MIN_CAMBIO_DISTANCIA_PARA_UPDATES, this);
        //if (m_locationManager != null)
        //m_locationManager.removeUpdates(this);

    }

    public void eliminarGPS() {
    }

    @Override
    public void onLocationChanged(Location location) {
        LATITUD = location.getLatitude();
        LONGITUD = location.getLongitude();

        if ( m_locationManager != null )
            if (ActivityCompat.checkSelfPermission( m_Context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission( m_Context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                return;
            }m_locationManager.removeUpdates(this);

        //registro de GPS
        RegistrarSeguimientoAsync registrarSeguimientoAsync  = new RegistrarSeguimientoAsync( m_Context, LATITUD, LONGITUD );
        registrarSeguimientoAsync.execute();
        //
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class Seguimiento {
         private String cImei;
         private String cLatitud;
         private String cLongitud;
         private String cUsuReg;
         private int nCodPersReg;
         private int nCodAge;


        public String getcImei() {
            return cImei;
        }

        public void setcImei(String cImei) {
            this.cImei = cImei;
        }

        public String getcLatitud() {
            return cLatitud;
        }

        public void setcLatitud(String cLatitud) {
            this.cLatitud = cLatitud;
        }

        public String getcLongitud() {
            return cLongitud;
        }

        public void setcLongitud(String cLongitud) {
            this.cLongitud = cLongitud;
        }

        public String getcUsuReg() {
            return cUsuReg;
        }

        public void setcUsuReg(String cUsuReg) {
            this.cUsuReg = cUsuReg;
        }

        public int getnCodPersReg() {
            return nCodPersReg;
        }

        public void setnCodPersReg(int nCodPersReg) {
            this.nCodPersReg = nCodPersReg;
        }

        public int getnCodAge() {
            return nCodAge;
        }

        public void setnCodAge(int nCodAge) {
            this.nCodAge = nCodAge;
        }
    }

    public class RegistrarSeguimientoAsync extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarSeguimientoAsync( Context context, double latitud, double longitud ){
            m_latitud = latitud;
            m_longitud = longitud;
            m_Contexto = context;
            m_webApi = new AppCreditoswebApi( m_Contexto );
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String Respuesta = "" ;
                if ( !Common.isNetworkConnected( m_Context ) ) {
                    return "No se encuentra conectado a internet.";
                }

                /*
                SeguimientoDTO seguimiento = new SeguimientoDTO();
                int resp = 0;
                seguimiento = new SeguimientoDTO();

                seguimiento.setcImei( m_cImei );
                seguimiento.setcLatitud( String.valueOf( m_latitud ) );
                seguimiento.setcLongitud( String.valueOf( m_longitud  ) );
                seguimiento.setnCodAge(m_Sesion.getAgencia());
                seguimiento.setnCodPersReg(m_Sesion.getCodPers());
                seguimiento.setcUsuReg(m_Sesion.getUsuario());
                */

                Seguimiento seguimiento = new Seguimiento();
                seguimiento.setcImei( m_cImei );
                seguimiento.setcLatitud( String.valueOf( m_latitud ) );
                seguimiento.setcLongitud( String.valueOf( m_longitud  ) );
                seguimiento.setnCodAge(99);
                seguimiento.setnCodPersReg(1);
                seguimiento.setcUsuReg("GPS");

                HttpParams httpParameters = new BasicHttpParams();
                HttpClient httpClient = new DefaultHttpClient(httpParameters);

                String serverName = "http://10.19.150.20:4080";
                //String serverName = "http://200.60.88.244:4080";

                String path = "/api/PostRegistraSeguimientoDatos";
                HttpPost post = new HttpPost(serverName + path);
                post.setHeader("content-type", "application/json");
                Gson gson = new GsonBuilder().serializeNulls().create();
                String dataJson = gson.toJson(seguimiento);
                int nRespuesta = 0;
                try {
                    post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
                    HttpResponse httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                        throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
                    }
                    nRespuesta = Integer.parseInt( EntityUtils.toString( httpResponse.getEntity()) );
                }
                catch (Exception ex) {
                    Log.e("ServicioRest", "Error!", ex);
                }

                if ( nRespuesta > 0 )
                    Respuesta = RESULT_OK;
                else
                    Respuesta = RESULT_FALSE;

                return Respuesta;
            }
            catch (Exception ex) {
                return "Ocurrió un error al consultar el servicio.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
        private double m_latitud;
        private double m_longitud;
        private Context m_Contexto;
        private IAppCreditosWebApi m_webApi;
    }


}
