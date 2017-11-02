package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.DatoFirmaCuentaDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Reporte;
import com.tibox.lucas.network.dto.DatosSalida.Credito;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.DatosSalida.ResultadoEnvio;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;
import com.tibox.lucas.network.dto.FlujoLucasDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.SolicitudResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDatosOperacion extends AppCompatActivity {

    private Spinner spBancos;
    private EditText etCuentaBancaria;
    private Button btnRegistroTransferencia;
    protected Usuario m_Sesion;
    private TextInputLayout inputlayoutCuentaBancaria;

    private int m_nIdFlujoMaestro = 0;
    private int m_nCodCred = 0;
    private String m_cCelular = "";
    private int m_nCodVerificacion = 0;
    private int m_OrdenFlujo = 0;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private FlujoMaestro m_flujo;
    private double m_nPrestamoSolicitado;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_operacion);

        spBancos = (Spinner) findViewById( R.id.spBancos );
        etCuentaBancaria = (EditText) findViewById( R.id.etCuentaBancaria );
        btnRegistroTransferencia = (Button) findViewById( R.id.btnRegistroTransferencia );
        inputlayoutCuentaBancaria = (TextInputLayout) findViewById( R.id.input_layout_cuenta_bancaria );

        btnRegistroTransferencia.setOnClickListener( btnRegistroTransferenciasetOnClickListener );


        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

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

        etCuentaBancaria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutCuentaBancaria.setError(null); // hide error
            }
        });

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SEIS ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SEIS );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

        ObtenerFlujoAsyns obtenerFlujoAsyns
                = new ObtenerFlujoAsyns( ActivityDatosOperacion.this, m_nIdFlujoMaestro );
        obtenerFlujoAsyns.execute();

        /*
        ConsultaCatalogoAsync consultaCatalogoAsync =
                new ConsultaCatalogoAsync( ActivityDatosOperacion.this, Constantes.TIPO_BANCOS );
        consultaCatalogoAsync.execute();
        */
    }

    View.OnClickListener btnRegistroTransferenciasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.SEIS ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_SEIS );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnRegistroTransferencia.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            if( etCuentaBancaria.getText().toString().trim().equals( "" ) ){
                Toast.makeText( ActivityDatosOperacion.this, "Falta completar datos.", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( (int) spBancos.getSelectedItemId() == Constantes.CERO ){
                Toast.makeText( ActivityDatosOperacion.this, "¡Ingresar el banco correctamente!", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( (int) spBancos.getSelectedItemId() == Constantes.UNO ) {
                if ( etCuentaBancaria.getText().length() < Constantes.TRECE ) {
                    inputlayoutCuentaBancaria.setError( "La cuenta bancaria debe de tener 13 a 14 dígitos, BCP." );
                    return;
                }
                else if ( etCuentaBancaria.getText().length() > Constantes.CATORCE ) {
                    inputlayoutCuentaBancaria.setError( "La cuenta bancaria debe de tener 13 a 14 dígitos, BCP." );
                    return;
                }
            }
            else if ( (int) spBancos.getSelectedItemId() == Constantes.DOS ) {
                if ( etCuentaBancaria.getText().length() != Constantes.VEINTE ) {
                    inputlayoutCuentaBancaria.setError( "La cuenta bancaria debe de tener 20 dígitos, BBVA." );
                    return;
                }
            } else if ( (int) spBancos.getSelectedItemId() == Constantes.TRES ) {
                if ( etCuentaBancaria.getText().length() != Constantes.TRECE ) {
                    inputlayoutCuentaBancaria.setError( "La cuenta bancaria debe de tener 13 dígitos, Interbank." );
                    return;
                }
            }

            DatoFirmaCuentaDTO param = new DatoFirmaCuentaDTO();

            param.setnCodCred( m_nCodCred );
            param.setnCodAge( m_Sesion.getAgencia() );
            param.setnBanco( (int) spBancos.getSelectedItemId() );
            param.setcCuentaBancaria( etCuentaBancaria.getText().toString() );
            param.setnIdFlujoMaestro( m_nIdFlujoMaestro );
            param.setnProd( Constantes.PRODUCTO );
            param.setnSubProd( Constantes.SUB_PRODUCTO );
            param.setcNomForm( "/StateInformacion" );
            param.setcUsuReg( m_Sesion.getUsuario() );
            param.setnCodPersReg( m_Sesion.getCodPers() );
            param.setnIdFlujo( 1032 );
            param.setnCodPers( m_Sesion.getCodPers() );
            param.setnOrdenFlujo( m_OrdenFlujo );

            //REGISTRAR TRANSFERENCIA
            RegistrarTransferenciaAsync async = new RegistrarTransferenciaAsync( ActivityDatosOperacion.this, param);
            async.execute();

        }
    };

    public class RegistrarTransferenciaAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private DatoFirmaCuentaDTO m_param;
        private String m_Mensaje;
        private SolicitudResponse m_SolicitudResponse;

        public RegistrarTransferenciaAsync( Context context, DatoFirmaCuentaDTO parame ){
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
                Credito modalidad = new Credito();


                modalidad.setnCodAge( m_Sesion.getAgencia() );
                modalidad.setnCodPers( m_Sesion.getCodPers() );
                modalidad.setnBanco( m_param.getnBanco() );
                modalidad.setcNroCuenta( m_param.getcCuentaBancaria() );
                modalidad.setcUsuReg( m_Sesion.getUsuario() );
                modalidad.setnCodPersReg( m_Sesion.getCodPers() );
                modalidad.setnIdFlujoMaestro( m_flujo.getnIdFlujoMaestro() );
                modalidad.setnProd( m_flujo.getnProd() );
                modalidad.setnSubProd( m_flujo.getnSubProd() );
                modalidad.setcFormulario( m_flujo.getcNomform() );
                modalidad.setnIdFlujo( m_flujo.getnIdFlujo() );
                modalidad.setnCodCred( m_flujo.getnCodCred() );
                modalidad.setnOrdenFlujo( m_flujo.getnOrdenFlujo() );

                //respuesta = m_webApi.RegistrarNumeroCuenta( modalidad, m_Sesion.getToken() );
                m_SolicitudResponse = m_webApi.RegistrarNumeroCuenta( modalidad, m_Sesion.getToken() );
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

                /*
                Reporte datos = new Reporte();
                datos.setnCodAge( m_Sesion.getAgencia() );
                datos.setnCodCred( m_flujo.getnCodCred() );
                //datos.setcEmail( m_Sesion.getUsuario() );
                datos.setcEmail( "manuel.dipas@tibox.com.pe" ); // mdipas 19.08.2017 pruebas
                datos.setcNombres( m_Sesion.getNombreUsuario() );
                datos.setnPrestamo( m_nPrestamoSolicitado );

                ReporteEnvioAsyns reporteEnvioAsyns = new ReporteEnvioAsyns( m_context, datos );
                reporteEnvioAsyns.execute();
                */

                GenerarReportesAsyns generarReportesAsyns = new GenerarReportesAsyns( m_context, m_flujo );
                generarReportesAsyns.execute();


            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_context, "¡Registro no procesada", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_SolicitudResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
                ActualizarCatalogoCodigos();
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

    protected void ActualizarCatalogoCodigos(){
        try{
            new CatalagoCodigosDAO().rellenaSpinner( spBancos, Constantes.TIPO_BANCOS, ActivityDatosOperacion.this );
        }
        catch ( Exception ex )
        {
            Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
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
                //m_Respuesta = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );

                m_FlujoMaestroResponse = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );
                if( !m_FlujoMaestroResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_FlujoMaestroResponse.getM_data() != null  ) {
                        if (m_FlujoMaestroResponse.getM_data().getnIdFlujo() > Constantes.CERO){
                            m_flujo = m_FlujoMaestroResponse.getM_data();


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
                }


                /*
                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.getnIdFlujo() > Constantes.CERO ){
                        m_flujo = m_Respuesta;
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

                ConsultaCatalogoAsync consultaCatalogoAsync =
                        new ConsultaCatalogoAsync( ActivityDatosOperacion.this, Constantes.TIPO_BANCOS );
                consultaCatalogoAsync.execute();

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
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Generando documentos ...");
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

                Intent continuar = new Intent( m_Context, ActivityDatosContrato.class );
                continuar.putExtra( "IdFlujoMaestro", m_flujo.getnIdFlujoMaestro() );
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

    public class GenerarReportesAsyns extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private FlujoMaestro m_Datos;
        private ResultadoEnvio m_Respuesta;

        public GenerarReportesAsyns( Context context, FlujoMaestro datos ){
            m_Datos = datos;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Generando documentos ...");
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
                m_Respuesta = m_webApi.GenerandoReporte( m_Datos, m_Sesion.getToken() );

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

                Intent continuar = new Intent( m_Context, ActivityDatosContrato.class );
                continuar.putExtra( "IdFlujoMaestro", m_flujo.getnIdFlujoMaestro() );
                continuar.putExtra( "PrestamoSolicitado", m_nPrestamoSolicitado );
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
                Intent solicitudFinal = new Intent( ActivityDatosOperacion.this, ActivityBandejaCreditos.class );
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
