package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.MontoCuota;
import com.tibox.lucas.network.dto.DatosSalida.Calendario;
import com.tibox.lucas.network.dto.DatosSalida.Credito;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.dto.Varnegocio;
import com.tibox.lucas.network.response.CalendarioCreditoResponse;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.VarnegocioResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivitySimulador extends AppCompatActivity {
    TextView tvTasainteres, tvPrestamoC, tvMontoCuota, tvTea, tvTcea;
    Button btnSolicitarCredito, btnCalcular;
    private double _montoMinimoD;
    private double _montoMaximoD;
    private double _montoTasaInteres;
    private int _nroCuota;
    private double _montoSeleccionado;
    private double _montoCuota;
    private SeekBar seekBar, seekBarCuotas;
    private int _nPlazo;
    private int _nPlazoSeleccionado;

    public final static int SIMULADOR_REQUEST_CODE = 1;
    public final static int CREDITO_REQUEST_CODE = 2;

    protected Usuario m_Sesion;
    private int m_nIdFlujoMaestro = 0;
    private boolean m_Solicitud = false;
    private boolean m_Calcular = false;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private FlujoMaestro m_flujoSolicitud;
    private FlujoMaestro m_flujo;
    private double m_cuotaMinima;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulador);

        tvTasainteres = (TextView) findViewById( R.id.tvTasainteres );
        tvPrestamoC = (TextView) findViewById( R.id.tvPrestamoC );
        tvMontoCuota = (TextView) findViewById( R.id.tvMontoCuota );

        tvTea = (TextView) findViewById( R.id.tv_tea );
        tvTcea = (TextView) findViewById( R.id.tv_tcea );

        btnSolicitarCredito = (Button) findViewById( R.id.btnSolicitarCredito );
        btnCalcular = (Button) findViewById( R.id.btnCalcular );
        seekBar = (SeekBar) findViewById( R.id.seekBar );
        seekBarCuotas = (SeekBar) findViewById( R.id.seekBarCuotas );

        String[] meses = {"1","2","3","4","5","6","7","8","9","10","11","12"};

        btnSolicitarCredito.setOnClickListener( btnSolicitarCreditosetOnClickListener );
        btnCalcular.setOnClickListener( btnCalcularsetOnClickListener );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        setTitle( "Obten tu crédito" );
        //toolbar.setSubtitle( R.string.title_activity_activity_simulador );
        toolbar.setTitleTextColor( getResources().getColor( R.color.White ) );
        toolbar.setSubtitleTextColor( getResources().getColor( R.color.White ));
        toolbar.setLogo( R.drawable.logo_lucas );

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("Solicitud") != null) {
                m_Solicitud = true; // MODO SOLICITUD.
                m_nIdFlujoMaestro = intent.getExtras().getInt( "IdFlujoMaestro" );
            }
        }

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.CUATRO ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_CUATRO );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //


        if ( !m_Solicitud ){ //SOLICITUD FALSE A CREAR

            //ObtenerVarnegocioAsync obtenerVarnegocioAsync = new ObtenerVarnegocioAsync( ActivitySimulador.this, Constantes.UNO );
            //obtenerVarnegocioAsync.execute();

        }
        else { //SOLICITUD TRUE YA ENVIADA

            ObtenerFlujoSolicitudAsyns obtenerFlujoSolicitudAsyns
                    = new ObtenerFlujoSolicitudAsyns( ActivitySimulador.this, m_nIdFlujoMaestro );
            obtenerFlujoSolicitudAsyns.execute();

        }

    }

    private void CargarSimuladorCuotas() {

        final int montoMIN = Constantes.TRES;
        int montoMAX = _nPlazo;
        final int incremento = 1;

        seekBarCuotas.setProgress( 0 );
        seekBarCuotas.incrementProgressBy( incremento );
        seekBarCuotas.setMax( montoMAX - montoMIN );
        final TextView seekBarValue = (TextView) findViewById( R.id.seekbarvalueCuotas );
        seekBarValue.setText( ""+( montoMIN ) );

        final TextView min = (TextView) findViewById( R.id.tvMinCuotas );
        final TextView max = (TextView) findViewById( R.id.tvMaxCuotas );

        min.setText( String.valueOf( montoMIN ) );
        max.setText( String.valueOf( montoMAX ) );

        seekBarCuotas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress = progress / incremento;
                progress = progress * incremento;
                seekBarValue.setText( String.valueOf( montoMIN + progress ) );

                _nPlazoSeleccionado = Integer.parseInt( seekBarValue.getText().toString() );

                if ( _nPlazoSeleccionado >= montoMIN ) {

                    tvMontoCuota.setText( "S/ 0.00" );
                    tvPrestamoC.setText( "S/ 0.00" );
                    tvTcea.setText( "0.0%" );
                    m_Calcular = false;

                    //Eliminanos el simulador en caliente
                    /*
                    ObtenerMontoCuotaAsync obtenerMontoCuotaAsync = new ObtenerMontoCuotaAsync(ActivitySimulador.this, _montoSeleccionado,
                            _nPlazoSeleccionado, _montoTasaInteres );
                    obtenerMontoCuotaAsync.execute();
                    */

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });
    }

    View.OnClickListener btnCalcularsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.CUATRO ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_CUATRO );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnCalcular.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            if ( _montoSeleccionado <= Constantes.CERO ){
                //etPrestamoReq.requestFocus();
                _montoMaximoD = 0;
                tvPrestamoC.setText( Utilidades.FormatoMonedaSoles( _montoMaximoD ) );
                Toast.makeText( ActivitySimulador.this,
                        "¡Ingresar correctamente el monto de prestamo!", Toast.LENGTH_SHORT).show();
                return;
            }
            else if ( _nPlazoSeleccionado <= 0 ){
                //etPrestamoReq.requestFocus();
                _montoMaximoD = 0;
                tvPrestamoC.setText( Utilidades.FormatoMonedaSoles( _montoMaximoD ) );
                Toast.makeText( ActivitySimulador.this,
                        "¡Ingresar correctamente el numero de meses del prestamo!", Toast.LENGTH_SHORT).show();
                return;
            }


            _montoMaximoD = _montoSeleccionado;
            tvPrestamoC.setText( Utilidades.FormatoMonedaSoles( _montoMaximoD ) );
            _nroCuota = _nPlazoSeleccionado;

            if ( _montoMaximoD > 0 ) {

                m_Calcular = true;

                ObtenerMontoCuotaAsync obtenerMontoCuotaAsync = new ObtenerMontoCuotaAsync( ActivitySimulador.this, _montoMaximoD,
                        _nroCuota, _montoTasaInteres );
                obtenerMontoCuotaAsync.execute();

            }


        }
    };

    View.OnClickListener btnSolicitarCreditosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.CUATRO ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_CUATRO );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnSolicitarCredito.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            //VALIDACION
            if ( !m_Calcular ){
                Toast.makeText( ActivitySimulador.this, "¡Calcular el monto de pago correctamente!", Toast.LENGTH_SHORT).show();
                return;
            }

            // obtenemos la cuota minima para pasar a la solicitud.
            ObtenerVarnegocioAsync obtenerVarnegocioAsync =
                    new ObtenerVarnegocioAsync( ActivitySimulador.this, Constantes.VARNEGOCIO_CUOTAMINIMA );
            obtenerVarnegocioAsync.execute();


            /*
            RegistrarSolicitudAsync registrarSolicitudAsync = new RegistrarSolicitudAsync( ActivitySimulador.this);
            registrarSolicitudAsync.execute();
            */


        }
    };

    public void CargarSimulador(){

        final int montoMIN = (int) _montoMinimoD;
        int montoMAX = (int) _montoMaximoD;
        final int incremento = 50;

        seekBar.setProgress( 0 );
        seekBar.incrementProgressBy( incremento );
        seekBar.setMax( montoMAX - montoMIN );
        final TextView seekBarValue = (TextView) findViewById( R.id.seekbarvalue );
        seekBarValue.setText( ""+( montoMIN ) );

        final TextView min = (TextView) findViewById( R.id.tvMin);
        final TextView max = (TextView) findViewById( R.id.tvMax);

        min.setText( String.valueOf( montoMIN ) );
        max.setText( String.valueOf( montoMAX ) );

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    //progress = ( progress / 50 ) == 1 ? 0 : ( progress / 50 )  ;
                    progress = progress / incremento;
                    progress = progress * incremento;

                    seekBarValue.setText( String.valueOf( montoMIN + progress ) );  //seekBarValue.setText(  String.valueOf( progress ) );

                    _montoSeleccionado = Double.parseDouble( seekBarValue.getText().toString() );
                    tvPrestamoC.setText( Utilidades.FormatoMonedaSoles( _montoSeleccionado ) );

                    if ( _montoSeleccionado >= montoMIN ) {

                        tvMontoCuota.setText( "S/ 0.00" );
                        tvPrestamoC.setText( "S/ 0.00" );
                        tvTcea.setText( "0.0%" );
                        m_Calcular = false;

                        //Eliminanos el simulador en caliente
                        /*
                        ObtenerMontoCuotaAsync obtenerMontoCuotaAsync = new ObtenerMontoCuotaAsync(ActivitySimulador.this, _montoSeleccionado,
                                _nPlazoSeleccionado, _montoTasaInteres);
                        obtenerMontoCuotaAsync.execute();
                        */
                    }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        /*
        mBubbleSeekBar0.correctOffsetWhenContainerOnScrolling();
        mBubbleSeekBar0.setProgress(0);
        mBubbleSeekBar0.setProgressRange( 0, 0 );
        mBubbleSeekBar0.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(int progress) {

                progress = progress / 50;
                progress = progress * 50;

                mStringBuilder.delete(0, mStringBuilder.length());
                mStringBuilder.append("").append( 0 + progress );


                _montoSeleccionado = Integer.parseInt( mStringBuilder.toString() );

                tvPrestamoC.setText( "S/. " + String.valueOf( _montoSeleccionado ) );
                _nroCuota = spMeses.getSelectedItemPosition() + 1;

                if ( _montoSeleccionado > _montoMinimoD ) {

                    ObtenerMontoCuotaAsync obtenerMontoCuotaAsync = new ObtenerMontoCuotaAsync(ActivitySimulador.this, _montoSeleccionado,
                            _nroCuota, _montoTasaInteres);
                    obtenerMontoCuotaAsync.execute();

                }
            }
        });
        */


    }

    public class ObtenerVarnegocioAsync extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerVarnegocioAsync( Context context, int nCodVar ){

            m_codvar = nCodVar;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo cuota minima...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                Varnegocio varnegocioDTO = new Varnegocio();
                //varnegocioDTO = m_webApi.obtenerValorNegocio( m_codvar, m_Sesion.getToken() );

                m_cuotaMinima = Constantes.CERO;
                m_VarnegocioResponse = m_webApi.obtenerValorNegocio( m_codvar, m_Sesion.getToken() );
                if( !m_VarnegocioResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_VarnegocioResponse.getM_data() != null ){
                        if ( m_VarnegocioResponse.getM_data().getcValorVar().length() > Constantes.CERO ){
                            m_cuotaMinima = Double.parseDouble( m_VarnegocioResponse.getM_data().getcValorVar() );
                            resp = RESULT_OK;
                        }
                        else
                            resp = RESULT_FALSE;
                    }
                }


                /*
                m_cuotaMinima = Constantes.CERO;
                if ( varnegocioDTO != null ){
                    if ( varnegocioDTO.getcValorVar().length() > Constantes.CERO ){
                        m_cuotaMinima = Double.parseDouble( varnegocioDTO.getcValorVar() );
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    return RESULT_FALSE;
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

                if ( _montoCuota < m_cuotaMinima ) {
                    Toast.makeText( ActivitySimulador.this, "El monto de cuota debe ser mayor que "
                            + Utilidades.FormatoMonedaSoles( m_cuotaMinima ) + ".", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegistrarSolicitudAsync registrarSolicitudAsync = new RegistrarSolicitudAsync( m_context );
                    registrarSolicitudAsync.execute();
                }

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
                //Toast.makeText(m_context, "Error de cuota minima.", Toast.LENGTH_SHORT).show();

                if ( m_VarnegocioResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
                pd.dismiss();
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
            }
        }

        private int m_codvar;
        private List<Varnegocio> m_listdto;
        private Context m_context;
        private ProgressDialog pd;
        private String m_mensajeAlerta;
        private IAppCreditosWebApi m_webApi;
        private VarnegocioResponse m_VarnegocioResponse;
    }

    public class ObtenerDatoTEaAsyncs extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerDatoTEaAsyncs( Context context, double Tasa ){
            m_Tasa = Tasa;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo TEA...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                m_TEA = m_webApi.ObtenerTEA( m_Tasa );
                if ( m_TEA > 0 ){
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
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();
                //tvTea.setText( String.valueOf( m_TEA ) + " %" );

                if ( !m_Solicitud )
                    CargarSimulador();

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();
                Toast.makeText( m_context, "Error de obtener TEA", Toast.LENGTH_SHORT ).show();
                //tvTea.setText( String.valueOf( 0 ) + " %" );
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                //tvTea.setText( String.valueOf( 0 ) + " %" );
                pd.dismiss();
            }
        }

        private double m_TEA;
        private double m_Tasa;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

    }

    public class ObtenerMontoCuotaAsync extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerMontoCuotaAsync( Context context, double prestamo, int cuota, double tasainteres ){
            m_prestamo = prestamo;
            m_cuota = cuota;
            m_tasainteres = tasainteres;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo monto cuota...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                MontoCuota datos = new MontoCuota();
                //datos.setdFechaSistema( new VarnegocioDAO().ObtenerVarnegocioxId( Constantes.VARNEGOCIO_FECHASISTEMA ).getcValorVar() );
                datos.setdFechaSistema( m_flujoSolicitud.getdFechaSistema() );
                datos.setnNroCuotas( m_cuota );
                datos.setnPrestamo( m_prestamo );
                datos.setnPeriodo( 30 );
                datos.setnTasa( m_flujoSolicitud.getnTasa() );
                datos.setnSeguro( m_flujoSolicitud.getnSeguroDesgravamen() );

                m_montocuota = 0.;
                m_CalendarioCreditoResponse =  m_webApi.ObtenerCalendarioMontoCuota( datos, m_Sesion.getToken() );
                if( !m_CalendarioCreditoResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    m_montocuota = Double.parseDouble( m_CalendarioCreditoResponse.getM_listdata().getMontoCuota() );
                    m_Tcea = m_webApi.ObtenerTCEA( datos, m_Sesion.getToken() );
                    if ( m_Tcea < 0 )
                        m_Tcea = 0;

                    _montoCuota = m_montocuota;
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
                tvMontoCuota.setText(  Utilidades.FormatoMonedaSoles( m_montocuota ) );
                tvTcea.setText( String.valueOf( m_Tcea ) + "%" ); // mdipas pruebas. borrar solo el comentario
                pd.dismiss();
            }
            else if (mensaje.equals(RESULT_FALSE)) {
                m_Calcular = false;
                tvMontoCuota.setText( Utilidades.FormatoMonedaSoles( m_montocuota ) );
                tvTcea.setText( "0.0%" );
                pd.dismiss();

                if ( m_CalendarioCreditoResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();
                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                tvTcea.setText( "0.0%" );
                m_Calcular = false;
                pd.dismiss();
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
            }
        }

        private double m_prestamo;
        private int m_cuota;
        private double m_tasainteres;
        private double m_Tcea;
        private double m_montocuota;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private CalendarioCreditoResponse m_CalendarioCreditoResponse;
    }

    public class RegistrarSolicitudAsync extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarSolicitudAsync( Context context ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Solicitando credito...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                Credito credito = new Credito();
                ResponseSolicitudDTO respuesta = new ResponseSolicitudDTO();

                credito.setnCodUsu( m_Sesion.getUsuario() );
                credito.setcUsuReg( m_Sesion.getUsuario() );
                credito.setnTasaComp( _montoTasaInteres );
                credito.setnTasa( _montoTasaInteres );
                credito.setnCodAge( m_Sesion.getAgencia() );
                credito.setnCodPers(  m_Sesion.getCodPers() );
                credito.setnCodPersReg( m_Sesion.getCodPers() );
                credito.setnPrestamo( _montoSeleccionado );
                credito.setnMontoCuota( _montoCuota );
                credito.setnNroCuotas( _nPlazoSeleccionado );
                credito.setnPeriodo( 30 );
                credito.setcUsuReg( m_Sesion.getUsuario() );

                credito.setcFormulario( m_flujo.getcNomform() );
                credito.setnIdFlujo( m_flujo.getnIdFlujo() );
                credito.setnIdFlujoMaestro( m_flujo.getnIdFlujoMaestro() );
                credito.setnOrdenFlujo( m_flujo.getnOrdenFlujo() );
                credito.setnProd( m_flujo.getnProd() );
                credito.setnSubProd( m_flujo.getnSubProd() );

                credito.setnSeguro( m_flujoSolicitud.getnSeguroDesgravamen() );
                credito.setdFechaSistema( m_flujoSolicitud.getdFechaSistema() );


                respuesta = m_webApi.RegistrarSolicitudCreditoGarantiaFlujo( credito, m_Sesion.getToken() );
                if ( respuesta != null ){
                     if( respuesta.getnValorRetorno() > 0 ){
                         m_nCodCred = respuesta.getnCodCred();
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
                Toast.makeText( m_context, "Solicitud Enviada", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                Intent solConcretada = new Intent( m_context, ActivityDatosOperacion.class );
                solConcretada.putExtra( "PrestamoSolicitado", _montoSeleccionado );
                solConcretada.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                startActivity( solConcretada );
                finish();

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();
                Toast.makeText( m_context, "Solicitud no procesada", Toast.LENGTH_SHORT ).show();
            }
            else{
                pd.dismiss();
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
            }
        }
        private int m_nCodCred;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if ( requestCode == SIMULADOR_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {

            }
        }
        else if ( requestCode == CREDITO_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                /*
                _montoMinimo = 0;
                _montoMaximo = 0;
                _montoTasaInteres = 0;
                */
            }
        }
        else {

        }
    }

    public class ObtenerFlujoSolicitudAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_iD;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private FlujoMaestro m_Respuesta;
        private FlujoMaestroResponse m_FlujoMaestroResponse;

        public ObtenerFlujoSolicitudAsyns( Context context, int iD ){
            m_iD = iD;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo flujo ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new FlujoMaestro();
                m_flujoSolicitud = new FlujoMaestro();
                m_flujo = new FlujoMaestro();

                //m_flujo = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );
                m_FlujoMaestroResponse = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );
                if( !m_FlujoMaestroResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    if ( m_FlujoMaestroResponse.getM_data() != null  ){
                        m_flujo = m_FlujoMaestroResponse.getM_data();

                        m_flujoSolicitud = m_webApi.obtenerFlujoSolicitud( m_iD, m_Sesion.getToken() );

                        if( m_flujoSolicitud != null ){
                            resp = RESULT_OK;
                            m_Respuesta = m_flujoSolicitud;


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


                btnSolicitarCredito.setText("LO QUIERO");
                _montoTasaInteres =  m_Respuesta.getnTasa() ;
                _montoMaximoD = m_Respuesta.getnPrestamoMax();
                _montoMinimoD = m_Respuesta.getnPrestamoMinimo();
                _nPlazo = (int) m_Respuesta.getnPlazo();


                _nPlazoSeleccionado = Constantes.TRES;
                _montoSeleccionado = _montoMinimoD;

                tvPrestamoC.setText( Utilidades.FormatoMonedaSoles( _montoMinimoD ) );
                //tvTasainteres.setText( String.valueOf( _montoTasaInteres ) + "%" );
                tvTasainteres.setText( Utilidades.FormatoMoneda( _montoTasaInteres ) + "%" );
                tvTea.setText( String.valueOf( obtenerTeaCalculado( _montoTasaInteres ) ) + "%" );

                CargarSimulador();
                CargarSimuladorCuotas();

                if ( _montoMinimoD > Constantes.CERO ) {

                    m_Calcular = true;
                    ObtenerMontoCuotaAsync obtenerMontoCuotaAsync = new ObtenerMontoCuotaAsync( ActivitySimulador.this, _montoMinimoD,
                            _nPlazoSeleccionado, _montoTasaInteres );
                    obtenerMontoCuotaAsync.execute();
                }

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                //Toast.makeText(m_context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_FlujoMaestroResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();
                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }







    public void Calcu( double nPrestamo, int nCuotas, int nPeriodicidad, double nTasa, String dFechaSistema, double nSeguro ){
        //double montocuota = Double.parseDouble( GeneraCalendario( nPrestamo, nCuotas, nPeriodicidad, nTasa, dFechaSistema, nSeguro ).get(0).getnMontoCuota() );
    }

    public final List<Calendario> GeneraCalendario(double nPrestamo, int nCuotas, int nPeriodicidad, double nTasa, String dFechaSistema, double nSeguro) {

        boolean valor = false;
        double nGastoSegDeg = nSeguro;
        double nMonto = nPrestamo;
        double nMontoCuotaBK = 0;
        double nValorInc = 0;
        double nIGV = 0;
        int pnFinCuotaGracia = 0;
        int pnTipoGracia = 1;
        boolean bOK = false;

        Object MatFechas = new Object();
        String[][] matDatos = new String[nCuotas + 1][8];
        Object[] MatFechas1 = new Object[nCuotas];
        String[][] MatFechas2 = new String[nCuotas + 1][2];

        DateFormat formater ;
        formater = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaDesem = null;

        try {
            fechaDesem = formater.parse( dFechaSistema );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Calendario> calendarioList = new ArrayList<>();
        Date dFechaPrueba = fechaDesem;

        if (!valor) {
            int nNumSub = 1;
            MatFechas = CalendarioFechasCuotaFija(nCuotas, dFechaPrueba, nPeriodicidad, nNumSub);
        }

        /*
        int cont = 0;
        double nInteres = 0;
        double nTasaCom = nTasa;
        double nMontoCuota = Utilidades.TruncateDecimal( CuotaPeriodoFijo( nTasaCom, 0, nGastoSegDeg, nCuotas, nPrestamo, 30) );

        List<Double> lstInteres = new ArrayList<>();

        Date dFecha = fechaDesem;
        double MontoCuotaReturn = 0;
        double nMontoNegativo = 0;
        double nMontoDiferenciaNeto = 0;
        double nPendIntComp = 0;
        double nPendComision = 0;
        double nMontoComisionCalculado = 0;
        double nMontoNetoICCOM = 0;
        double pnMontoComision = 0;

        do {
            nMonto = nPrestamo;
            nMontoCuotaBK = nMontoCuota;
            dFecha = dFechaPrueba;

            for ( int k = 1; (k <= nCuotas); k++ ) {
                nMontoCuota = nMontoCuotaBK;
                String[][] MatFe = ((String[][])(MatFechas));

                Date dFechaVenc = null;
                try {
                    dFechaVenc = formater.parse( MatFe[k][1] );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                matDatos[k][0] = MatFe[k][1];

                int dias = 0;
                try {
                    dias = Utilidades.diferenciaEnDias2( formater.parse( dFecha.toString() ) ,  formater.parse( dFechaVenc.toString() ) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                matDatos[k][1] = String.valueOf( k );

                nInteres = (Math.pow((1 + (nTasaCom / 100)), (dias / 30)) - 1);

                nInteres = Utilidades.TruncateDecimal( (nInteres * nMonto) );

                matDatos[k][4] = String.valueOf( nInteres );

                nMontoComisionCalculado = 0;
                if (((pnTipoGracia != 4)
                        || (k > pnFinCuotaGracia))) {
                    if ((pnMontoComision > 0)) {

                        try {
                            nMontoComisionCalculado = Utilidades.TruncateDecimal(((nMonto * Math.pow((1
                                    + (pnMontoComision / 100)), ( Utilidades.diferenciaEnDias2( formater.parse( dFecha.toString() ), formater.parse( MatFe[k][1] ) ) / 30) ) )
                            - 1 ) );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
                else {
                }

                nMontoNegativo = ( nMontoCuota - Double.parseDouble( matDatos[k][4] ) );

                if ((nMontoNegativo < 0)) {

                    nMontoDiferenciaNeto = ( Utilidades.TruncateDecimal( (Math.abs(nMontoNegativo) / (1 + (nIGV + 100)) ) ) );

                    if ( ( Double.parseDouble( matDatos[k][4] ) > nMontoDiferenciaNeto) ) {
                        nPendIntComp = ( Utilidades.TruncateDecimal(  ( nPendIntComp ) ) + nMontoDiferenciaNeto);
                        matDatos[k][4] = String.valueOf( Utilidades.TruncateDecimal( ( Double.parseDouble( matDatos[k][4] ) - nMontoDiferenciaNeto) ) );
                    }
                    else if ( ( nMontoComisionCalculado > nMontoDiferenciaNeto ) ) {
                        nPendComision = (nPendComision + nMontoDiferenciaNeto);
                        nMontoComisionCalculado = Utilidades.TruncateDecimal( (nMontoComisionCalculado - nMontoDiferenciaNeto) );
                    }
                    else {
                        nPendIntComp = (nPendIntComp + Double.parseDouble( matDatos[k][4] ) );
                        matDatos[k][4] = "0.00";
                        nPendComision = (nPendComision + (nMontoDiferenciaNeto - nPendIntComp));
                        nMontoComisionCalculado = Utilidades.TruncateDecimal( ( nMontoComisionCalculado - ( nMontoDiferenciaNeto - nPendIntComp ) ) );
                    }

                }
                else {
                    nMontoDiferenciaNeto = ( Utilidades.TruncateDecimal((nMontoNegativo / (1 + (nIGV / 100))) ));
                    if ((nPendIntComp > 0)) {
                        if ((nPendIntComp > nMontoDiferenciaNeto)) {
                            nMontoNetoICCOM = nMontoDiferenciaNeto;
                            matDatos[k][4] = String.valueOf( ( Double.parseDouble( matDatos[k][4] ) + nMontoNetoICCOM ) );
                            nPendIntComp = (nPendIntComp - nMontoNetoICCOM);
                            nMontoNegativo = 0;
                        }
                        else {
                            nMontoNetoICCOM = nPendIntComp;
                            nMontoNegativo = (nMontoNegativo - nPendIntComp);
                            matDatos[k][4] = String.valueOf( ( Double.parseDouble( matDatos[k][4] ) + nMontoNetoICCOM ) );
                            nPendIntComp = 0;
                        }

                    }

                    if ((nPendComision > 0)) {
                        if ((nPendComision > nMontoDiferenciaNeto)) {
                            nMontoNetoICCOM = nMontoDiferenciaNeto;
                            nMontoComisionCalculado = ( nMontoComisionCalculado + nMontoNetoICCOM );
                            nPendComision = (nPendComision - nMontoNetoICCOM);
                        }
                        else {
                            nMontoNetoICCOM = nPendComision;
                            nMontoComisionCalculado = ( nMontoComisionCalculado + nMontoNetoICCOM );
                            nPendComision = 0;
                        }

                    }

                }

                if ((k == 1)) {
                    matDatos[k][7] = String.valueOf( Utilidades.TruncateDecimal( ( nPrestamo * (nGastoSegDeg / 100) ) ) );
                }
                else {
                    matDatos[k][7] = String.valueOf( Utilidades.TruncateDecimal( ( Double.parseDouble( matDatos[ k - 1 ][6] ) * (nGastoSegDeg / 100) ) ) );
                }

                if ( ( ( pnTipoGracia != 4 )
                        || ( k > pnFinCuotaGracia ) ) ) {
                    if ( ( k != nCuotas ) ) {
                        matDatos[k][3] = String.valueOf( Utilidades.TruncateDecimal( (nMontoCuota - ( Double.parseDouble( matDatos[k][4] ) + Double.parseDouble( matDatos[k][7] ) ) ) ) );

                        if ( ( ( Double.parseDouble( matDatos[k][3] ) > 0)
                                    && ( Double.parseDouble( matDatos[k][3] ) <= 0.05))) {
                            matDatos[k][3] = "0.00";
                        }

                        if ( ( ( Double.parseDouble(matDatos[k][3] ) >= -0.05)
                                    && ( Double.parseDouble( matDatos[k][3] ) < 0))) {
                            matDatos[k][3] = "0.00";
                        }

                    }
                    else {
                        if ((k == 1)) {
                            matDatos[k][3] = String.valueOf( Utilidades.TruncateDecimal(nMonto) );
                        }
                        else {
                            matDatos[k][3] = String.valueOf( Utilidades.TruncateDecimal( Double.parseDouble( matDatos[k-1][6] ) ) );
                        }

                        if (((nPendComision > 0)
                                || (nPendIntComp > 0))) {
                            matDatos[k][4] = String.valueOf( Utilidades.TruncateDecimal( ( Double.parseDouble( matDatos[k][4] ) + nPendIntComp ) ) );
                        }

                    }

                }
                else {
                    matDatos[k][3] = "0.00";
                }

                matDatos[k][2] = String.valueOf( Utilidades.TruncateDecimal( ( Double.parseDouble(matDatos[k][3] ) + ( Double.parseDouble(matDatos[k][4] ) + Double.parseDouble(matDatos[k][7] ))) ) );
                nMonto = Utilidades.TruncateDecimal( (nMonto - Double.parseDouble(matDatos[k][3] ) ) );
                matDatos[k][6] = String.valueOf( Utilidades.TruncateDecimal(nMonto) );

                try {
                    dFecha = formater.parse( MatFe[k][1] );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            int ultimaFila = 0;

            for ( int l = 0; ( l < matDatos.length ); l++ ) {
                ultimaFila = Integer.parseInt( matDatos[l][1] );//  GetUpperBound(l);
                break;
            }


            ultimaFila = matDatos.length;

            if ( ( ultimaFila ) > 1 ) {
                if ( ( ( ( Double.parseDouble( matDatos[ultimaFila][2] ) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ) )
                            >= -0.01)
                            && ( ( Double.parseDouble( matDatos[ultimaFila][2] ) - Double.parseDouble(matDatos[ ultimaFila - 1 ][2] ) )
                            <= 0))) {
                    MontoCuotaReturn = Double.parseDouble(matDatos[ ultimaFila - 1 ][3] );
                    bOK = true;
                }
                else {
                    if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ) )
                                >= 250)) {
                        nValorInc = 0.3;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ) )
                                >= 210)) {
                        nValorInc = 0.28;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 180)) {
                        nValorInc = 0.27;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 120)) {
                        nValorInc = 0.26;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 100)) {
                        nValorInc = 0.25;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 90)) {
                        nValorInc = 0.24;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 80)) {
                        nValorInc = 0.23;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 75)) {
                        nValorInc = 0.22;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 70)) {
                        nValorInc = 0.21;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 69)) {
                        nValorInc = 0.2;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 68)) {
                        nValorInc = 0.19;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 67)) {
                        nValorInc = 0.18;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 65)) {
                        nValorInc = 0.17;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 60)) {
                        nValorInc = 0.16;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 50)) {
                        nValorInc = 0.15;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 40)) {
                        nValorInc = 0.14;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 30)) {
                        nValorInc = 0.13;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 20)) {
                        nValorInc = 0.12;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 10)) {
                        nValorInc = 0.11;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 5)) {
                        nValorInc = 0.1;
                    }
                    else if (((Math.abs(Double.parseDouble( matDatos[ultimaFila][2] )) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                >= 1)) {
                        nValorInc = 0.01;
                    }
                    else {
                        nValorInc = 0.01;
                    }

                    if (((Double.parseDouble( matDatos[ultimaFila][2] ) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                <= -0.01)) {
                        nMontoCuota = Utilidades.TruncateDecimal( ( ( nMontoCuota - nValorInc)) );
                    }
                    else if (((Double.parseDouble( matDatos[ultimaFila][2] ) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                > 0)) {
                        nMontoCuota = Utilidades.TruncateDecimal( ((nMontoCuota + nValorInc)) );
                    }

                    cont++;
                    if ((cont > 2500)) {
                        if ((((Double.parseDouble( matDatos[ultimaFila][2] ) - Double.parseDouble( matDatos[ ultimaFila - 1 ][2] ))
                                    < 0)
                                    || (cont == 2502))) {
                            MontoCuotaReturn = Double.parseDouble( matDatos[ ultimaFila - 1 ][3] );
                            bOK = true;
                        }

                    }

                }

            }
            else {
                MontoCuotaReturn = Double.parseDouble(matDatos[1][2]);
                bOK = true;
            }

            int cantidad1 = ( matDatos.length - 1 );
        } while (!bOK);

        double nMontoRef = 0;
        for (int m = 0; (m
                < (nCuotas * 1)); m++) {
            nMontoRef = ( nMontoRef + Utilidades.TruncateDecimal( Double.parseDouble(matDatos[m][2] ) ) );
        }

        for (int y = 1; (y <= nCuotas); y++) {
            Calendario obj = new Calendario();

            try {
                obj.setDfechPago( String.valueOf( formater.parse( matDatos[y][0] ) ) );
                obj.setdFechaPago( formater.parse( matDatos[y][0] ) );
            } catch (ParseException e) {
                e.printStackTrace();
            }

            obj.setnCuota( Integer.parseInt( matDatos[y][1] ) );
            obj.setnMontoCuota( String.valueOf( matDatos[y][2] ) );

            double datoCapital = ( ( matDatos[y][3] == null) || (matDatos[y][3] == "null") ? 0.0 : Double.parseDouble( ( matDatos[y][3] ) ) );


            obj.setnCapital( Utilidades.TruncateDecimal( datoCapital ) );
            obj.setnInteres( Double.parseDouble( matDatos[y][4] ) );
            obj.setnSaldos( Double.parseDouble( matDatos[y][6] ) );
            calendarioList.add(obj);
        }
        */

        return calendarioList;
    }

    private final Object CalendarioFechasCuotaFija(int nCuotas, Date fechaDesem, int nPeriodicidad, int nNumSub) {
        Date pdFecha;
        String[][] MatFechas = new String[nCuotas + 1][2];
        String[][] MatFechasEsp = new String[(nCuotas * nNumSub) + 1][3];

        Date dFechaDesembTempo;
        dFechaDesembTempo = fechaDesem;
        MatFechas = new String[ nCuotas + 1 ][3];

        pdFecha = dFechaDesembTempo;

        Date myDate = dFechaDesembTempo;
        myDate = Utilidades.addDays( myDate, 1 );

        for (int i = 1; (i <= nCuotas); i++) {
            if (( Weekday( pdFecha, Constantes.SEIS ) == 6 ) ) {
                pdFecha = Utilidades.addDays( pdFecha, 1 );
            }

            if ( ( Weekday( pdFecha, Constantes.CERO ) == 7)) {
                pdFecha = Utilidades.addDays( pdFecha, 1 );
            }

            pdFecha = Utilidades.addDays( pdFecha, nPeriodicidad );
            MatFechas[i][1] = pdFecha.toString();
        }

        return MatFechas;
    }

    public static int Weekday( Date date, int startOfWeek) {

        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        return ( ( ( cal.get( Calendar.DAY_OF_WEEK ) - startOfWeek ) + 7 ) % 7 );
    }

    public final double CuotaPeriodoFijo(double pTasaComp, double pTasaComision, double pnTasaSeguro, double pnCuotas, double vMonto, double pnPlazo) {
        double CuotaPeriodoFijo = 0;
        double nTasaComiTmp = 0;
        double Pot1 = 0;
        double nTasaTmp = 0;
        nTasaTmp = ( Math.pow((1 + (pTasaComp / 100)), (pnPlazo / 30)) - 1);
        nTasaComiTmp = ( Math.pow( (1 + (pTasaComision / 100)), (pnPlazo / 30)) - 1);
        nTasaComiTmp = Double.parseDouble( String.format("{0:0.##}", nTasaComiTmp));
        nTasaTmp = (nTasaTmp + nTasaComiTmp);
        nTasaTmp = (nTasaTmp * (1 + (pnTasaSeguro / 100)));
        Pot1 = Math.pow((1 + nTasaTmp), pnCuotas);
        CuotaPeriodoFijo = (((Pot1 * nTasaTmp) / (Pot1 - 1)) * vMonto);
        CuotaPeriodoFijo = Utilidades.TruncateDecimal( CuotaPeriodoFijo );
        return CuotaPeriodoFijo;
    }

    public void CalcularCalendario(){
        Object MatFechas = new Object();

        String[] parts = m_flujoSolicitud.getdFechaSistema().split( "/" );
        Date mydate = new Date( parts[1] + "/" + parts[0] + "/" + parts[2] );
        boolean valor = false;

        Date dFechaDesembolso = mydate;
        Date dFechaParaCalen = dFechaDesembolso;
        //dFechaParaCalen = getDateFormat( dFechaParaCalen );

    }

    public double obtenerTeaCalculado( double nTasaInteres ){
        double dTea = 0;
        dTea =  Math.round( ( ( Math.pow( ( 1 + nTasaInteres / 100.00 ) , ( 12 / 1 ) ) - 1 ) * 100.00 ) * 100.00 ) / 100.00;
        return dTea;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent solicitudFinal = new Intent( ActivitySimulador.this, ActivityBandejaCreditos.class );
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
