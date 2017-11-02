package com.tibox.lucas.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.activity.dialogfragment.MyDialogFragConfirmacionNumero;
import com.tibox.lucas.activity.interfaces.OnClickMyDialogConfirmacionNumero;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.Reporte;
import com.tibox.lucas.network.dto.DatosSalida.Credito;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.DatosSalida.ResultadoEnvio;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;
import com.tibox.lucas.network.dto.FlujoLucasDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.response.FileResponse;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.SolicitudResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityDatosContrato extends AppCompatActivity {

    private TextView tvPrestamoContrato,tvCuotaContrato,tvMontoContrato,tvTeaContrato;
    private EditText etPINVerificacion;
    private Button btnSolicitud, btnHR1, btnHR2, btnContrato, btnSolicitarCodigo, btnContinuar, btnSeguro;
    private CheckBox cbxCondicionesContrato;
    private FloatingActionButton fabContinuarDatos;
    private WebView webView;;

    private int m_nIdFlujoMaestro = 0;
    private int m_nCodCred = 0;
    private String m_cCelular = "";
    protected Usuario m_Sesion;
    private int m_nCodVerificacion = 0;
    private String mio_Claro = "962956971";
    private int m_OrdenFlujo = 0;

    private static final int REQUEST_WRITE_STORAGE = 700;
    private boolean bPermisos;
    private static String TAG = ActivityDatosContrato.class.getName();

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private FlujoMaestro m_Flujo;
    private double m_nPrestamoSolicitado;
    private Toolbar toolbar;
    private String m_Numero_Celular = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_contrato);

        tvPrestamoContrato = (TextView) findViewById( R.id.tvPrestamoContrato );
        tvCuotaContrato = (TextView) findViewById( R.id.tvCuotaContrato );
        tvMontoContrato = (TextView) findViewById( R.id.tvMontoContrato );
        tvTeaContrato = (TextView) findViewById( R.id.tvTeaContrato );
        etPINVerificacion = (EditText) findViewById( R.id.etPINVerificacion );

        btnSolicitud = (Button) findViewById( R.id.btnSolicitud );
        btnHR1 = (Button) findViewById( R.id.btnHR1 );
        btnHR2 = (Button) findViewById( R.id.btnHR2 );
        btnContrato = (Button) findViewById( R.id.btnContrato );
        btnSolicitarCodigo = (Button) findViewById( R.id.btnSolicitarCodigo );
        //fabContinuarDatos = (FloatingActionButton) findViewById( R.id.fabContinuarDatos );
        btnContinuar = ( Button ) findViewById( R.id.btnContinuar );
        btnSeguro = (Button) findViewById( R.id.btn_seguro );

        cbxCondicionesContrato = (CheckBox) findViewById( R.id.cbxCondicionesContrato );
        //webView = (WebView) findViewById( R.id.webView );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        btnSolicitud.setOnClickListener( btnSolicitudsetOnClickListener );
        btnHR1.setOnClickListener( btnHR1setOnClickListener );
        btnHR2.setOnClickListener( btnHR2setOnClickListener );
        btnContrato.setOnClickListener( btnContratosetOnClickListener );
        btnSolicitarCodigo.setOnClickListener( btnSolicitarCodigosetOnClickListener );
        btnContinuar.setOnClickListener( fabContinuarDatossetOnClickListener );
        btnSeguro.setOnClickListener( btnSegurosetOnClickListener );

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        setTitle( "Obten tu crédito" );
        toolbar.setTitleTextColor( getResources().getColor( R.color.White ) );
        toolbar.setSubtitleTextColor( getResources().getColor( R.color.White ));
        toolbar.setLogo( R.drawable.logo_lucas );

        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            m_nIdFlujoMaestro = intent.getInt( "IdFlujoMaestro" );
            m_nPrestamoSolicitado = intent.getDouble( "PrestamoSolicitado" );
        }

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

        /*
        ObtenerFlujoLucas obtenerFlujoLucas =
                new ObtenerFlujoLucas( ActivityDatosContrato.this, m_nIdFlujoMaestro );
        obtenerFlujoLucas.execute();
        */

        ObtenerFlujoAsyns obtenerFlujoAsyns =
                new ObtenerFlujoAsyns(  ActivityDatosContrato.this, m_nIdFlujoMaestro );
        obtenerFlujoAsyns.execute();


        int permission = ContextCompat.checkSelfPermission( this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if ( permission != PackageManager.PERMISSION_GRANTED ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale( this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se requiere permiso para acceder a la tarjeta SD para esta aplicación.")
                        .setTitle("SoyLucas!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ObtenerPermisosParaElMovil();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(this,"Se requiere permiso para acceder a la tarjeta SD para esta aplicación.",Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else{

            bPermisos = true;
        }


    }

    View.OnClickListener btnSegurosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if ( bPermisos ) {

                DocumentosPdfDTO param = new DocumentosPdfDTO();
                param.setnAgencia( m_Sesion.getAgencia() );
                param.setnCredito( m_nCodCred );
                param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_SEGURO );

                ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( ActivityDatosContrato.this, param,
                        btnSolicitud.getText().toString());
                pdfAsync.execute();

            }
            else
                ObtenerPermisosParaElMovil();

        }
    };

    View.OnClickListener btnSolicitudsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnSolicitud.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            if ( bPermisos ) {

                DocumentosPdfDTO param = new DocumentosPdfDTO();
                param.setnAgencia( m_Sesion.getAgencia() );
                param.setnCredito( m_nCodCred );
                param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_SOLICITUD );

                ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( ActivityDatosContrato.this, param,
                        btnSolicitud.getText().toString());
                pdfAsync.execute();

            }
            else
                ObtenerPermisosParaElMovil();

        }
    };

    View.OnClickListener btnHR1setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnHR1.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            if ( bPermisos ) {

                DocumentosPdfDTO param = new DocumentosPdfDTO();
                param.setnAgencia(m_Sesion.getAgencia());
                param.setnCredito(m_nCodCred);
                param.setnTipoDoc(Constantes.DOC_CRED_ONLINE_HOJA_RESUMEN1);

                ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync(ActivityDatosContrato.this, param,
                        btnHR1.getText().toString());
                pdfAsync.execute();

            }
            else
                ObtenerPermisosParaElMovil();
        }
    };

    View.OnClickListener btnHR2setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnHR2.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //


            if ( bPermisos ) {

                DocumentosPdfDTO param = new DocumentosPdfDTO();
                param.setnAgencia( m_Sesion.getAgencia() );
                param.setnCredito( m_nCodCred );
                param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_HOJA_RESUMEN2 );

                ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( ActivityDatosContrato.this, param ,
                        btnHR2.getText().toString() );
                pdfAsync.execute();

            }
            else
                ObtenerPermisosParaElMovil();
        }
    };

    View.OnClickListener btnContratosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnContrato.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //


            if ( bPermisos ) {

                DocumentosPdfDTO param = new DocumentosPdfDTO();
                param.setnAgencia( m_Sesion.getAgencia() );
                param.setnCredito( m_nCodCred );
                param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_CONTRATO );

                ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( ActivityDatosContrato.this, param ,
                        btnContrato.getText().toString() );
                pdfAsync.execute();

            }
            else
                ObtenerPermisosParaElMovil();
        }
    };

    View.OnClickListener btnSolicitarCodigosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnSolicitarCodigo.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //



            Bundle bundleNumero = new Bundle();
            bundleNumero.putString("NumeroObtenido", m_Flujo.getcMovil() );
            FragmentManager fm = getSupportFragmentManager();
            MyDialogFragConfirmacionNumero myDialogFrag = new MyDialogFragConfirmacionNumero();
            myDialogFrag.setArguments( bundleNumero );
            myDialogFrag.setCancelable( true );
            myDialogFrag.show(fm,"Diag");
            myDialogFrag.setOnClickEnviarListener(new OnClickMyDialogConfirmacionNumero() {
                @Override
                public void onClickEnviarSms( String numeroConfirmado ) {
                    m_Numero_Celular = numeroConfirmado;

                    if ( m_nCodVerificacion == Constantes.CERO ){
                        ObtenerVerificacion();
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityDatosContrato.this );
                        dialog.setTitle("Aviso");
                        dialog.setMessage("¿Esta seguro que desea generar nuevo código?");
                        dialog.setCancelable(false);
                        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ObtenerVerificacion();
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
            });





            /*
            if ( m_nCodVerificacion == Constantes.CERO ){
                ObtenerVerificacion();
            }
            else {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityDatosContrato.this);
                dialog.setTitle("Aviso");
                dialog.setMessage("¿Esta seguro que desea generar nuevo código?");
                dialog.setCancelable(false);
                dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ObtenerVerificacion();
                    }
                });
                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
            */


        }
    };

    View.OnClickListener fabContinuarDatossetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SIETE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SIETE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnContinuar.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            int nCodigoVerifiacion = 0;
            if ( etPINVerificacion.getText().toString().trim().equals( "" )
                    || etPINVerificacion.getText().length() < Constantes.CUATRO ){
                Toast.makeText( ActivityDatosContrato.this, "¡Ingresar el codigo correctamente!", Toast.LENGTH_SHORT ).show();
                return;
            }

            nCodigoVerifiacion = Integer.parseInt( etPINVerificacion.getText().toString() );

            if ( nCodigoVerifiacion != m_nCodVerificacion ){
                Toast.makeText( ActivityDatosContrato.this, "¡Código errado!", Toast.LENGTH_SHORT ).show();
                return;
            }
            if ( !cbxCondicionesContrato.isChecked() ){
                Toast.makeText( ActivityDatosContrato.this, "¡Activar las condiciones del contrato!", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( nCodigoVerifiacion == m_nCodVerificacion ){

                Credito credito = new Credito();
                credito.setnCodAge( m_Sesion.getAgencia() );
                credito.setcUsuReg( m_Sesion.getUsuario() );
                credito.setnCodPersReg( m_Sesion.getCodPers() );
                credito.setnIdFlujoMaestro( m_nIdFlujoMaestro );
                credito.setcMovil( m_Flujo.getcMovil() );
                credito.setnFirma( m_nCodVerificacion );
                credito.setnProd( m_Flujo.getnProd() );
                credito.setnSubProd( m_Flujo.getnSubProd() );
                credito.setcFormulario( m_Flujo.getcNomform() );
                credito.setnCodCred( m_Flujo.getnCodCred() );
                credito.setnIdFlujo( m_Flujo.getnIdFlujo() );
                credito.setnOrdenFlujo( m_Flujo.getnOrdenFlujo() );

                RegistrarFirmaAsync firmaAsync =
                        new RegistrarFirmaAsync( ActivityDatosContrato.this, credito );
                firmaAsync.execute();


            }

        }
    };

    public class RegistrarFirmaAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Credito m_param;
        private String m_Mensaje;
        private SolicitudResponse m_SolicitudResponse;

        public RegistrarFirmaAsync( Context context, Credito parame ){
            m_context = context;
            m_param = parame;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                ResponseSolicitudDTO respuesta = new ResponseSolicitudDTO();
                //respuesta = m_webApi.RegistrarFirmaDigital( m_param, m_Sesion.getToken() );

                m_SolicitudResponse = m_webApi.RegistrarFirmaDigital( m_param, m_Sesion.getToken() );
                if( !m_SolicitudResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_SolicitudResponse.getM_data() != null ){
                        if ( m_SolicitudResponse.getM_data().getnValorRetorno() == Constantes.UNO ){
                            resp = RESULT_OK;
                        }
                        else
                            resp = RESULT_FALSE;
                    }
                    else
                        resp = RESULT_FALSE;
                }

                /*
                if ( respuesta != null ){
                    if ( respuesta.getnValorRetorno() == Constantes.UNO ){
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
            if ( mensaje.equals( RESULT_OK ) ){
                pd.dismiss();

                Reporte datos = new Reporte();
                datos.setnCodAge( m_Sesion.getAgencia() );
                datos.setnCodCred( m_Flujo.getnCodCred() );
                datos.setcEmail( m_Sesion.getUsuario() );
                //datos.setcEmail( "manuel.dipas@tibox.com.pe" ); // mdipas 19.08.2017 pruebas
                datos.setcNombres( m_Sesion.getNombreUsuario() );
                datos.setnPrestamo( m_nPrestamoSolicitado );

                ReporteEnvioAsyns reporteEnvioAsyns = new ReporteEnvioAsyns( m_context, datos );
                reporteEnvioAsyns.execute();


                /*
                Intent continuar = new Intent( m_context, ActivityGestionSolicitud.class );
                continuar.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                startActivity( continuar );
                finish();
                */

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_context, "¡Registro no procesada", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_SolicitudResponse.getM_response().equals( Constantes.No_Autorizado ) )
                {

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder( m_context );
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

                                            Intent solicitudFinal = new Intent( m_context, ActivityLogin.class );
                                            startActivity( solicitudFinal );
                                            finish();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

        }
    }

    public class ConsultaCatalogoAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ConsultaCatalogoAsync(Context context, int Codigo ){
            m_nCodigo = Codigo;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Consultando catalago...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }
                    m_listcatalogo = new ArrayList<>();
                    m_listcatalogo = m_webApi.obtenerCatalago( m_nCodigo, m_Sesion.getToken() );

                    if ( m_listcatalogo != null ) {
                        if ( m_listcatalogo.size() > 0 ) {
                            new CatalagoCodigosDAO().LimpiarCatalogoCodigosxID( m_nCodigo );
                            for ( CatalagoCodigosDTO dto : m_listcatalogo ) {
                                CatalagoCodigos catalagoCodigos = new CatalagoCodigos();
                                catalagoCodigos.setcNomCod( dto.getcNomCod() );
                                catalagoCodigos.setnCodigo( m_nCodigo );
                                catalagoCodigos.setnValor( Integer.valueOf( dto.getnValor() ) );
                                new CatalagoCodigosDAO().insertar( catalagoCodigos );
                            }
                            resp = RESULT_OK;
                        }
                        else
                            resp = RESULT_FALSE;
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
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
        private int m_nCodigo;
        private List<CatalagoCodigosDTO> m_listcatalogo;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    protected void ObtenerVerificacion(){

        m_nCodVerificacion = GenerarCodigoVerificador();

        Alerta alerta = new Alerta();
        alerta.setcMovil( m_Numero_Celular );
        //alerta.setcMovil( m_Flujo.getcMovil() );
        //alerta.setcMovil( "962956971" ); //mdipas pruebas de movil
        alerta.setcTexto( "Hola SoyLucas, tu código es " + m_nCodVerificacion );
        alerta.setcEmail( "" );
        alerta.setcTitulo( "Lucas" );

        EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( ActivityDatosContrato.this, alerta );
        envioSMSAsyns.execute();

    }

    protected int GenerarCodigoVerificador(){
        int codigo = 0;
        int randomPIN = (int)( Math.random()  * 9000 ) + 1000;
        codigo = randomPIN;
        String val = ""+randomPIN;
        return codigo;
    }

    public void showPdf( String namePdf ){
        File file = new File(  AppConfig.directorioPdf, namePdf + ".pdf");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf"); startActivity(intent);
    }

    public class ObtenerPDFAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private File m_File = null;
        private String m_namePdf;
        private DocumentosPdfDTO m_param;
        private String m_Mensaje;
        private FileResponse m_FileResponse;

        public ObtenerPDFAsync( Context context, DocumentosPdfDTO param ,String namePdf ){
            m_param = param;
            m_namePdf = namePdf;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "obteniendo PDF...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                int nResp = 0;

                m_namePdf = m_namePdf + "_" + m_param.getnAgencia() + "-" + m_param.getnCredito();
                //m_File = m_webApi.ObtenerImagePDF( m_param, m_namePdf, m_Sesion.getToken() );
                m_FileResponse = m_webApi.ObtenerImagePDF( m_param, m_namePdf, m_Sesion.getToken() );
                if ( !m_FileResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                }

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
                showPdf( m_namePdf );
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText(m_context, m_Mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_FileResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();
                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    private boolean ObtenerPermisosParaElMovil() {
        bPermisos = false;
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions( this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, //Escribir en memoria del telefono
                    REQUEST_WRITE_STORAGE );
        }
        else
            bPermisos = true;

        return bPermisos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( requestCode == REQUEST_WRITE_STORAGE ) {
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                bPermisos = true;
            }
        }

    }

    public class ObtenerFlujoAsyns extends AsyncTask<Void, Void,String>{
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
                m_Flujo = new FlujoMaestro();
                //m_Respuesta = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );

                m_FlujoMaestroResponse = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );
                if( !m_FlujoMaestroResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_FlujoMaestroResponse.getM_data() != null  ) {
                        if (m_FlujoMaestroResponse.getM_data().getnIdFlujo() > Constantes.CERO ) {
                            m_nCodCred = m_FlujoMaestroResponse.getM_data().getnCodCred();
                            m_Flujo = m_FlujoMaestroResponse.getM_data();

                            //OBTENGO EL PASO DEL FLUJOO
                            m_FlujoMaestroResponse = m_webApi.obtenerPasosFlujo(  m_iD, m_Sesion.getToken() );
                            if( !m_FlujoMaestroResponse.isM_success() ){
                                resp = RESULT_FALSE;
                            }
                            else {
                                if ( m_FlujoMaestroResponse.getM_data() != null) {
                                    resp = RESULT_OK;
                                }
                            }
                            //END

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
                        m_nCodCred = m_Respuesta.getnCodCred();
                        m_Flujo = m_Respuesta;
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

                // agrego el nombre y paso del flujo
                toolbar.setSubtitle( m_FlujoMaestroResponse.getM_data().getcComentario() );
                //end

                ObtenerDatosPrestamoAsyns obtenerDatosPrestamoAsyns =
                        new ObtenerDatosPrestamoAsyns( m_Context, m_Sesion.getAgencia(), m_Flujo.getnCodCred() );
                obtenerDatosPrestamoAsyns.execute();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                //Toast.makeText( m_Context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_FlujoMaestroResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_Context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

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

    public class ObtenerDatosPrestamoAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_nCodAge;
        private int m_nCodCred;
        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Credito m_Respuesta;

        public ObtenerDatosPrestamoAsyns( Context context, int nCodAge, int nCodCred ){
            m_nCodAge = nCodAge;
            m_nCodCred = nCodCred;
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
                m_Respuesta = new Credito();
                m_Respuesta = m_webApi.obtenerDatosPrestamo( m_nCodAge, m_nCodCred, m_Sesion.getToken() );

                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.getnPrestamo() > Constantes.CERO ){
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
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
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();

                tvPrestamoContrato.setText(  "" + Utilidades.FormatoMoneda( m_Respuesta.getnPrestamo() )  );
                tvCuotaContrato.setText( "" + m_Respuesta.getnNroCuotas() );
                tvMontoContrato.setText( "" + m_Respuesta.getnMontoCuota() );
                tvTeaContrato.setText( "" + m_Respuesta.getnTasaComp() + "%" );


                ConsultaCatalogoAsync consultaCatalogoAsync =
                        new ConsultaCatalogoAsync( m_Context, Constantes.TIPO_DOC_CRED_ONLINE );
                consultaCatalogoAsync.execute();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText( m_Context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class EnvioSMSAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Alerta m_Alerta;
        private RedResponse m_RedResponse;

        public EnvioSMSAsyns( Context context, Alerta alerta ){
            m_Alerta = alerta;
            m_Context = context;
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

    public class ReporteEnvioAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Reporte m_reporte;
        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private ResultadoEnvio m_Respuesta;

        public ReporteEnvioAsyns( Context context, Reporte reporte ){
            m_reporte = reporte;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Enviando documentos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new ResultadoEnvio();
                m_Respuesta = m_webApi.ReporteEnvio( m_reporte, m_Sesion.getToken() );

                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.isBresultado() == true ){
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
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
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();

                Intent continuar = new Intent( m_Context, ActivityGestionSolicitud.class );
                continuar.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                startActivity( continuar );
                finish();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText( m_Context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
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
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent solicitudFinal = new Intent( ActivityDatosContrato.this, ActivityBandejaCreditos.class );
                startActivity( solicitudFinal );
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
