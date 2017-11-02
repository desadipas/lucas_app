package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.IOException;

public class ActivityRechazoSolicitud extends AppCompatActivity {

    private Button btnContinuar;

    protected Usuario m_Sesion;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FlujoMaestro m_Flujo;
    private FlujoMaestro m_FlujoSolicitud;
    private int m_nIdFlujoMaestro = 0;
    private String m_cMensajeEnvio = "";
    private String m_cTelefono = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechazo_solicitud);

        btnContinuar = ( Button ) findViewById( R.id.btnContinuar );
        btnContinuar.setOnClickListener( fabContinuarsetOnClickListener );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer icono

        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            m_nIdFlujoMaestro = intent.getInt( "IdFlujoMaestro" );
            m_cTelefono = intent.getString( "Telefono" );
            m_cMensajeEnvio = intent.getString( "Mensaje" );
        }


        Alerta alerta = new Alerta();
        alerta.setcMovil( m_cTelefono );
        //alerta.setcMovil( "962956971" ); // mdipas para pruebas
        alerta.setcTexto( m_cMensajeEnvio );

        EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( ActivityRechazoSolicitud.this, alerta, m_cMensajeEnvio );
        envioSMSAsyns.execute();

    }

    View.OnClickListener fabContinuarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent rechazo = new Intent( ActivityRechazoSolicitud.this, ActivityBandejaCreditos.class );
            startActivity( rechazo );
            finish();

        }
    };

    public class ObtenerFlujoAsyns extends AsyncTask<Void, Void,String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_iD;
        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private FlujoMaestro m_Respuesta;
        private FlujoMaestroResponse m_FlujoMaestroResponse;

        public ObtenerFlujoAsyns( Context context, int iD ){
            m_iD = iD;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Obteniendo flujo ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new FlujoMaestro();
                //m_Respuesta = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );

                m_FlujoMaestroResponse = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );
                if( !m_FlujoMaestroResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_FlujoMaestroResponse.getM_data() != null  ){
                        if ( m_FlujoMaestroResponse.getM_data().getnIdFlujo() > Constantes.CERO ){
                            m_Flujo = m_FlujoMaestroResponse.getM_data();
                            resp = RESULT_OK;
                        }
                        else
                            resp = RESULT_FALSE;
                    }
                    else
                        resp = RESULT_FALSE;
                }

                /*
                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.getnIdFlujo() > Constantes.CERO ){
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    resp = RESULT_FALSE;
                */

                return resp;
            }
            catch (IOException ex) {
                return "Ocurrió un error de conexión con el servicio.";
            }
            catch (Exception ex) {
                return "Ocurrió un error al consultar el servicio.";
            }
        }

        @Override
        protected void onPostExecute(String mensaje) {
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                //Toast.makeText( m_Context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_FlujoMaestroResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_Context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder( m_Context );
                    builder.setMessage( Constantes.Mensaje_Sesion_Caducado )
                            .setTitle("Lucas!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            //Eliminamos la Preferencia agregada
                                            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                                            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                                            //end

                                            Intent solicitudFinal = new Intent( m_Context, ActivityLogin.class );
                                            startActivity( solicitudFinal );
                                            finish();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */

                }
                else
                    Toast.makeText( m_Context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();


            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    private String emailCuerpo( String titulo, String nombre, String resumen, String descripcion1, String texto1, String texto2, String descripcion2 ){
        String body = "";

        body = "<table border='0' style='width: 100%; background: #f1f1f1; font-family: verdana'>" + " \n" +
                "<tr>" + " \n" +
                "<tr>" + " \n" +
                "<table cellspacing='0' cellpadding='0' border='0' style='margin: 0 auto; width: 85%'>" + " \n" +
                "<tr style='background: #FD293F'>" + "\n" +
                "<td colspan='3' style='padding: 30px'>" + " \n" +
                "<h1 style='text-transform: uppercase; text-align: center; color: white; margin: 0 auto; font-family: verdana'>" + titulo + " </h1> " + " \n" +
                "</td>" + " \n" +
                "</tr>" + " \n" +
                "<tr style='background: white'>" + " \n" +
                "<td colspan='3' style='padding: 15px;'>" + " \n" +
                "<h2 style='background: white; text-transform: uppercase; text-align: center; font-family: verdana'>hola " + nombre + "</h2>" + " \n" +
                "<h4 style='font-weight: 500; text-transform: uppercase; text-align: center; font-family: verdana'><i>" + resumen + "</i></h4>" + "\n" +
                "<p style='text-align: justify; padding: 15px; font-family: verdana'>" + descripcion1 + "</p>";

        if ( !texto1.equals( "" )  || !texto2.equals( "" ) ) {
            body = body + "<ul>";
            if ( !texto1.equals( "" ) ) {
                body = body + "<li><b>" + texto1 + "</b></li>";
            }
            if ( !texto2.equals( "" ) ) {
                body = body + "<li><b>" + texto2 + "</b></li>";
            }
            body = body + "<ul>";
        }

        if ( !descripcion2.equals( "" ) ) {
            body = body + "<p>" + descripcion2 + "</p>";
        }

        body = body + "</td>" + " \n" +
                "</tr>" + " \n" +
                "<tr style='background: #454544; text-transform: uppercase; text-align: center'>" + "\n" +
                "<td style='padding: 25px'>" +"\n" +
                "<button style='text-align: center; color: #fff; font-size: 14px; font-weight: 500;  border:none; padding: 10px 20px; background-color: #FFB700; font-size: 25px'>" +"\n" +
                "<span style='color: #454544; text-transform: uppercase; font-weight: bold; font-size: 25px'>Alo Lucas:</span> 01 615-7030</button>" +"\n" +
                "</td>" +"\n" +
                "</tr>" +"\n" +
                "</table>" +"\n" +
                "</td>" +"\n" +
                "</tr>" +"\n" +
                "</table>";

        return body;
    }

    public class EnvioSMSAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Alerta m_Alerta;
        private String m_MensajeEnvio;
        private RedResponse m_RedResponse;

        public EnvioSMSAsyns( Context context, Alerta alerta, String mensajeEnvio ){
            m_Alerta = alerta;
            m_Context = context;
            m_MensajeEnvio = mensajeEnvio;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Enviando SMS ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                boolean m_Respuesta = false;
                //m_Respuesta = m_webApi.EnvioSms( m_Alerta, m_Sesion.getToken() );

                m_RedResponse = m_webApi.EnvioSms( m_Alerta, m_Sesion.getToken() );
                if( !m_RedResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_RedResponse.getM_data().iscRed() ){
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }


                /*
                if ( m_Respuesta == true ){
                    resp = RESULT_OK;
                }
                else
                    resp = RESULT_FALSE;
                */

                return resp;
            }
            catch (IOException ex) {
                return "Ocurrió un error de conexión con el servicio.";
            }
            catch (Exception ex) {
                return "Ocurrió un error al consultar el servicio.";
            }
        }

        @Override
        protected void onPostExecute(String mensaje) {
            if ( mensaje.equals( RESULT_OK ) ) {
                Toast.makeText( m_Context, "Mensaje enviado.", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                Alerta alerta = new Alerta();
                alerta.setcTexto( emailCuerpo("MALAS NOTICIAS", m_Sesion.getNombreUsuario(), "Te hemos rechazado", m_MensajeEnvio, "", "", "") );
                alerta.setcEmail( m_Sesion.getUsuario() );
                //alerta.setcEmail( "manuel.dipas@tibox.com.pe" ); // mdipas por pruebas de email
                alerta.setcTitulo( "Hola SoyLucas, tenemos malas noticias." );

                EnvioEmailAsyns envioEmailAsyns = new EnvioEmailAsyns( m_Context, alerta );
                envioEmailAsyns.execute();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_Context, "¡Error de envío!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_RedResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_Context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder( m_Context );
                    builder.setMessage( Constantes.Mensaje_Sesion_Caducado )
                            .setTitle("Lucas!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            //Eliminamos la Preferencia agregada
                                            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                                            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                                            //end

                                            Intent solicitudFinal = new Intent( m_Context, ActivityLogin.class );
                                            startActivity( solicitudFinal );
                                            finish();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */

                }
                else
                    Toast.makeText( m_Context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class EnvioEmailAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Alerta m_Alerta;

        public EnvioEmailAsyns( Context context, Alerta alerta ){
            m_Alerta = alerta;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Enviando email ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                boolean m_Respuesta = false;
                m_Respuesta = m_webApi.EnvioEmail( m_Alerta, m_Sesion.getToken() );
                if ( m_Respuesta == true ){
                    resp = RESULT_OK;
                }
                else
                    resp = RESULT_FALSE;

                return resp;
            }
            catch (IOException ex) {
                return "Ocurrió un error de conexión con el servicio.";
            }
            catch (Exception ex) {
                return "Ocurrió un error al consultar el servicio.";
            }
        }

        @Override
        protected void onPostExecute(String mensaje) {
            if ( mensaje.equals( RESULT_OK ) ) {
                Toast.makeText( m_Context, "Email enviado.", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText( m_Context, "¡Error de envío!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent salir = new Intent( ActivityRechazoSolicitud.this, ActivityBandejaCreditos.class );
                startActivity( salir );
                finish();
            }
        });
        dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}
