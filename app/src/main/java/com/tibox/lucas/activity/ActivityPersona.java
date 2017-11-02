package com.tibox.lucas.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.tibox.lucas.R;
import com.tibox.lucas.broadcastReceiver.EnvioSeguimientoReceiver;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.dao.ZonasDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.entidad.Zonas;
import com.tibox.lucas.lenddo.LenddoActivity;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.Autenticacion;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.ClienteDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.ELDocumento;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoCliente;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoRCCDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreLenddoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringDemograficoDatos;
import com.tibox.lucas.network.dto.DatosPersona;
import com.tibox.lucas.network.dto.DatosSalida.ELIngresoPredecidoDemograficoResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELIngresoPredecidoRCCResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELIngresoPredecidoResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELMoraComercialResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoreBuroResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoreDemograficoResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoreLenddoResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringBuroCuotaUtilizadaResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringBuroResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringDemograficoResultado;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringFinalResultado;
import com.tibox.lucas.network.dto.DatosSalida.EvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.DatosSalida.PreEvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.TokenItems;
import com.tibox.lucas.network.dto.DatosSimulacion;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.PersonaRespDTO;
import com.tibox.lucas.network.dto.UsuarioDTO;
import com.tibox.lucas.network.dto.ValidadorDTO;
import com.tibox.lucas.network.dto.ZonasDTO;
import com.tibox.lucas.network.response.PersonaRespResponse;
import com.tibox.lucas.network.response.PersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.ZonasResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ActivityPersona extends AppCompatActivity {

    Spinner spProfesion, spDepartamento, spProvincia, spDistrito, spDireccion, spResidencia, spSexo, spEstadoCivil, spActEconomica,
            spSitLaboral, spEmpleo
            ,spMz,spLt,spCargoPublico;
    EditText etDocumento,etNombres,etPrimerApellido,etSegundoApellido,etCelular,etCorreo,etDireccion,etTelefonofijo,etRuc,
            etTelefonoEmpleo,etIngresoDeclarado,etDireccionEmpleo,etFechaNac,etFechaIngLaboral,etCodigoVerificador
            ,etMz,etLt,etCorreoConfirmar;
    ImageButton ibtnFechaNac, ibtnFechaLaboral;
    private FloatingActionButton fabGrabarPersona;
    private Button btnBuscarCliente, btnCodigoVerificador, btnContinuar;
    private TextView tvLinkTerminos;
    private LinearLayout lyCodigoVerificador, lyDatosVerificacion, lyConfirmarCorreo ;
    private CheckBox cbxAutorizoDatos, cbxAutorizoEnvio;

    private int m_IdspDepartamento = 0;
    private int m_IdspProvincia = 0;
    private int m_IdspDistrito = 0;

    private int day, month ,year;
    private int dayL, monthL ,yearL;

    protected Usuario m_Sesion;
    private int m_nCodPers = 0;
    private int m_nIdFlujoMaestro = 0;

    Button btnpruebas;
    LocationManager locationManager;

    private boolean m_EditarDatos = false;
    private boolean m_VerificadorDoc = false;
    private String m_DocumentoValidacion = "";
    private String m_Access_token = "";

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText etPINVerificacion;
    private Button btnSolicitarCodigo;
    private int m_nCodVerificacionMovil = 0;
    private LinearLayout lyCodigoVerificadorNumero;
    private LinearLayout lyConyuge;
    private EditText etDocumentoConyuge,etNombresConyuge,etApellidosConyuge, etCentroLaboral;
    Spinner spDepartamentoEmpleo, spProvinciaEmpleo, spDistritoEmpleo, spDireccionEmpleo, spMzEmpleo,spLtEmpleo;
    EditText etMzEmpleo,etLtEmpleo;
    private int m_IdspDepartamentoEmpleo = 0;
    private int m_IdspProvinciaEmpleo = 0;
    private int m_IdspDistritoEmpleo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_persona );

        spActEconomica = (Spinner) findViewById( R.id.spActEconomica );
        spProfesion = (Spinner) findViewById( R.id.spProfesion );
        spDepartamento = (Spinner) findViewById( R.id.spDepartamento );
        spProvincia = (Spinner) findViewById( R.id.spProvincia );
        spDistrito = (Spinner) findViewById( R.id.spDistrito );
        spDireccion = (Spinner) findViewById( R.id.spDireccion );
        spResidencia = (Spinner) findViewById( R.id.spResidencia );
        spSexo = (Spinner) findViewById( R.id.spSexo );
        spEstadoCivil = (Spinner) findViewById( R.id.spEstadoCivil );
        spSitLaboral = (Spinner) findViewById( R.id.spSitLaboral );
        spEmpleo = (Spinner) findViewById( R.id.spEmpleo );
        ibtnFechaNac = (ImageButton ) findViewById( R.id.ibtnFechaNac );
        ibtnFechaLaboral = ( ImageButton ) findViewById( R.id.ibtnFechaLaboral );
        etFechaNac = ( EditText ) findViewById( R.id.etFechaNac );
        etFechaIngLaboral = ( EditText ) findViewById( R.id.etFechaIngLaboral );
        fabGrabarPersona = (FloatingActionButton) findViewById( R.id.fabGrabarPersona );
        btnBuscarCliente = (Button) findViewById( R.id.btnBuscarCliente );
        btnpruebas = (Button) findViewById( R.id.btnpruebas );
        tvLinkTerminos = (TextView) findViewById( R.id.tvLinkTerminos );
        btnCodigoVerificador = (Button) findViewById( R.id.btnCodigoVerificador );
        etCodigoVerificador = (EditText)findViewById( R.id.etCodigoVerificador );
        etDocumento = (EditText) findViewById( R.id.etDocumento );
        etNombres = (EditText) findViewById( R.id.etNombres );
        etPrimerApellido = (EditText) findViewById( R.id.etPrimerApellido );
        etSegundoApellido = (EditText) findViewById( R.id.etSegundoApellido );
        etCelular = ( EditText ) findViewById( R.id.etCelularT );
        etCorreo = (EditText) findViewById( R.id.etCorreo );
        etCorreoConfirmar = (EditText) findViewById( R.id.etCorreoConfirmar );
        etDireccion = (EditText) findViewById( R.id.etDireccion );
        etTelefonofijo = (EditText) findViewById( R.id.etTelefonofijo );
        etRuc = (EditText) findViewById( R.id.etRuc );
        etTelefonoEmpleo = (EditText) findViewById( R.id.etTelefonoEmpleo );
        etIngresoDeclarado = (EditText) findViewById( R.id.etIngresoDeclarado );
        etDireccionEmpleo = (EditText) findViewById( R.id.etDireccionEmpleo );
        etFechaNac = (EditText) findViewById( R.id.etFechaNac );
        etFechaIngLaboral = (EditText) findViewById( R.id.etFechaIngLaboral );
        lyCodigoVerificador = (LinearLayout) findViewById( R.id.lyCodigoVerificador );
        lyDatosVerificacion = (LinearLayout) findViewById( R.id.lyDatosVerificacion );
        lyConfirmarCorreo = (LinearLayout) findViewById( R.id.ly_confirmar_correo );
        cbxAutorizoEnvio = (CheckBox) findViewById( R.id.cbxAutorizoEnvio );
        cbxAutorizoDatos = (CheckBox) findViewById( R.id.cbxAutorizoDatos );
        spMz = (Spinner) findViewById( R.id.spMz );
        spLt = (Spinner) findViewById( R.id.spLt );
        spCargoPublico = (Spinner) findViewById( R.id.spCargoPublico );
        etMz = (EditText) findViewById( R.id.etMz );
        etLt = (EditText) findViewById( R.id.etLt );
        btnContinuar = (Button) findViewById( R.id.btn_continuar );
        btnSolicitarCodigo = (Button) findViewById( R.id.btnSolicitarCodigo );
        etPINVerificacion = (EditText) findViewById( R.id.etPINVerificacion );

        btnSolicitarCodigo.setOnClickListener( btnSolicitarCodigosetOnClickListener );

        lyCodigoVerificadorNumero = (LinearLayout) findViewById( R.id.lyCodigoVerificadorNumero );
        lyConyuge = (LinearLayout) findViewById( R.id.lyConyuge );
        etDocumentoConyuge = (EditText) findViewById( R.id.etDocumentoConyuge );
        etNombresConyuge = (EditText) findViewById( R.id.etNombresConyuge );
        etApellidosConyuge = (EditText) findViewById( R.id.etApellidosConyuge );
        etCentroLaboral = (EditText) findViewById( R.id.etCentroLaboral );

        spDepartamentoEmpleo = (Spinner) findViewById( R.id.spDepartamentoEmpleo );
        spProvinciaEmpleo = (Spinner) findViewById( R.id.spProvinciaEmpleo );
        spDistritoEmpleo = (Spinner) findViewById( R.id.spDistritoEmpleo );
        spDireccionEmpleo = (Spinner) findViewById( R.id.spDireccionEmpleo );
        spMzEmpleo = (Spinner) findViewById( R.id.spMzEmpleo );
        spLtEmpleo = (Spinner) findViewById( R.id.spLtEmpleo );
        etMzEmpleo = (EditText) findViewById( R.id.etMzEmpleo );
        etLtEmpleo = (EditText) findViewById( R.id.etLtEmpleo );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer icono
        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        Bundle intent = getIntent().getExtras();
        if ( intent != null) {

            if ( intent.getString("Documento" ) != null ){
                m_EditarDatos = true;
                etDocumento.setText( intent.getString("Documento") );
                lyCodigoVerificador.setVisibility( View.GONE );
                lyConfirmarCorreo.setVisibility( View.GONE );
                m_Access_token = m_Sesion.getToken(); //agregado para token 14.08.2017
                lyCodigoVerificadorNumero.setVisibility( View.VISIBLE );
            }
        }

        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get( Calendar.MONTH );
        day = calendar.get( Calendar.DAY_OF_MONTH );
        yearL = calendar.get( Calendar.YEAR );
        monthL = calendar.get( Calendar.MONTH );
        dayL = calendar.get( Calendar.DAY_OF_MONTH );

        spDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspDepartamento = (int) spDepartamento.getSelectedItemId();
                ActualizarZonas( m_IdspDepartamento, Constantes.CERO, Constantes.DOS, spProvincia, m_IdspProvincia, Constantes.UNO );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspProvincia = (int) spProvincia.getSelectedItemId();
                ActualizarZonas( m_IdspDepartamento, m_IdspProvincia, Constantes.TRES, spDistrito, m_IdspDistrito, Constantes.UNO );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spEstadoCivil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( (int) spEstadoCivil.getSelectedItemId() == Constantes.DOS )
                    lyConyuge.setVisibility( View.VISIBLE );
                else
                    lyConyuge.setVisibility( View.GONE );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDepartamentoEmpleo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspDepartamentoEmpleo = (int) spDepartamentoEmpleo.getSelectedItemId();
                ActualizarZonas( m_IdspDepartamentoEmpleo, Constantes.CERO, Constantes.DOS, spProvinciaEmpleo, m_IdspProvinciaEmpleo, Constantes.DOS );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spProvinciaEmpleo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspProvinciaEmpleo = (int) spProvinciaEmpleo.getSelectedItemId();
                ActualizarZonas( m_IdspDepartamentoEmpleo, m_IdspProvinciaEmpleo, Constantes.TRES, spDistritoEmpleo, m_IdspDistritoEmpleo, Constantes.DOS );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ibtnFechaNac.setOnClickListener( ibtnFechaNacsetOnClickListener );
        ibtnFechaLaboral.setOnClickListener( ibtnFechaLaboralsetOnClickListener );
        btnContinuar.setOnClickListener( fabGrabarPersonasetOnClickListener );
        btnBuscarCliente.setOnClickListener( btnBuscarClientesetOnClickListener );
        btnCodigoVerificador.setOnClickListener( btnCodigoVerificadorsetOnClickListener );

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.NUEVE ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_NUEVE );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //


        //mdipas 14.08.2017
        if ( m_EditarDatos ){

            new ZonasDAO().LimpiarZonas();

            ActualizarCatalogoZonasAsync actualizarCatalogoZonasAsync =
                        new ActualizarCatalogoZonasAsync( ActivityPersona.this, Constantes.CERO, Constantes.CERO,
                            Constantes.UNO, spDepartamento, m_IdspDepartamento, Constantes.UNO );
            actualizarCatalogoZonasAsync.execute();

        }
        else{

            UsuarioDTO usuarioTibox = new UsuarioDTO();
            usuarioTibox.setUsername( "tibox@tibox.com.pe" );
            usuarioTibox.setPassword( "TiboxWebApi" );

            ObtenerTokenAsync obtenerTokenAsync = new ObtenerTokenAsync( ActivityPersona.this, usuarioTibox  );
            obtenerTokenAsync.execute();
        }
        //end


        tvLinkTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse( Constantes.URL_TERMINOS_CONDICIONES );
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity( intent );
            }
        });

    }

    View.OnClickListener btnSolicitarCodigosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick( View view ) {

            if ( m_nCodVerificacionMovil == Constantes.CERO ){
                ObtenerVerificacion();
            }
            else {

                AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityPersona.this );
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
    };

    View.OnClickListener btnCodigoVerificadorsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            LayoutInflater factory = LayoutInflater.from(ActivityPersona.this);
            final View viewI = factory.inflate( R.layout.image_dni, null);

            AlertDialog.Builder builder = new AlertDialog.Builder( ActivityPersona.this );
            builder.setTitle("Código verificador")
                    .setView( viewI )
                    .setCancelable(false)
                    .setNeutralButton("Cerrar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }
    };

    View.OnClickListener btnBuscarClientesetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if ( etDocumento.getText().toString().trim().equals( "" ) || etDocumento.getText().length() < Constantes.OCHO )
            {
                etDocumento.requestFocus();
                Toast.makeText( ActivityPersona.this, "¡Ingresar correctamente el numero de documento!", Toast.LENGTH_SHORT).show();
                return;
            }

            ConsultaDocumentoAsync consultaDocumentoAsync = new ConsultaDocumentoAsync( ActivityPersona.this, etDocumento.getText().toString() );
            consultaDocumentoAsync.execute();

        }
    };

    protected ValidadorDTO ValidarRegistroDatos(){

        ValidadorDTO validar = new ValidadorDTO();
        validar.setbValor( false );
        validar.setcMensaje( "" );

        if ( etDocumento.getText().toString().trim().equals( "" ) || etDocumento.getText().length() < Constantes.OCHO )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Documento" );
            return validar;
        }

        if ( etNombres.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Nombres" );
            return validar;
        }

        if ( etPrimerApellido.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Primer Apellido" );
            return validar;
        }

        if ( etSegundoApellido.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Segundo Apellido" );
            return validar;
        }

        if ( etCelular.getText().toString().trim().equals( "" ) || etCelular.getText().length() < Constantes.NUEVE )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Celular" );
            return validar;
        }

        // validar que empieze 9--
        String nroCelularPrimero = etCelular.getText().toString().substring(0, 1);
        if ( !nroCelularPrimero.trim().equals( "9" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Celular" );
            return validar;
        }
        //

        if ( !Utilidades.isValidEmail( etCorreo.getText().toString() ) ){
            validar.setbValor( true );
            validar.setcMensaje( "Correo" );
            return validar;
        }


        if ( !etCorreoConfirmar.getText().toString().trim().equals( etCorreo.getText().toString() )
                && !m_EditarDatos )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Correo Confirmar" );
            return validar;
        }

        if ( (int) spDistrito.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Distrito" );
            return validar;
        }

        if ( (int) spDireccion.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Dirección" );
            return validar;
        }

        if ( etDireccion.getText().toString().trim().equals( "" ) || etDireccion.getText().length() < Constantes.TRES )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Dirección" );
            return validar;
        }

        if ( (int) spMz.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Nro o Mz" );
            return validar;
        }

        if ( etMz.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Nro o Mz"  );
            return validar;
        }

        if ( (int) spLt.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Lote" );
            return validar;
        }

        if ( etLt.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Lote" );
            return validar;
        }

        if ( (int) spResidencia.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Residencia" );
            return validar;
        }

        if ( (int) spSexo.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Sexo" );
            return validar;
        }

        if ( etFechaNac.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Fecha Nac." );
            return validar;
        }

        if ( (int) spEstadoCivil.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Estado Civil" );
            return validar;
        }

        if( (int) spEstadoCivil.getSelectedItemId() == Constantes.DOS ){
            if ( etDocumentoConyuge.getText().toString().trim().equals( "" ) || etDocumentoConyuge.getText().length() < Constantes.OCHO )
            {
                validar.setbValor( true );
                validar.setcMensaje( "Documento Conyuge" );
                return validar;
            }

            if ( etNombresConyuge.getText().toString().trim().equals( "" ) )
            {
                validar.setbValor( true );
                validar.setcMensaje( "Nombres Conyuge" );
                return validar;
            }

            if ( etApellidosConyuge.getText().toString().trim().equals( "" ) )
            {
                validar.setbValor( true );
                validar.setcMensaje( "Apellidos Conyuge" );
                return validar;
            }
        }



        if ( (int) spCargoPublico.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Cargo Publico" );
            return validar;
        }

        if ( (int) spActEconomica.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Act. Economica" );
            return validar;
        }

        if ( (int) spSitLaboral.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Sit. Laboral" );
            return validar;
        }

        if ( (int) spProfesion.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Profesión" );
            return validar;
        }

        if ( (int) spEmpleo.getSelectedItemId() == 0 )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Empleo" );
            return validar;
        }

        /*
        if ( etRuc.getText().toString().trim().equals( "" ) || etRuc.getText().length() < Constantes.ONCE )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Ruc" );
            return validar;
        }
        */

        /*
        if ( etCentroLaboral.getText().toString().trim().equals( "" ) || etCentroLaboral.getText().length() < Constantes.TRES )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Centro Laboral" );
            return validar;
        }
        */

        if ( etFechaIngLaboral.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Fecha Ing. Laboral" );
            return validar;
        }

        if ( etTelefonoEmpleo.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Telefono Empleo" );
            return validar;
        }

        if ( etIngresoDeclarado.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Ingreso Declarado" );
            return validar;
        }

        /*
        if ( etDireccionEmpleo.getText().toString().trim().equals( "" ) )
        {
            validar.setbValor( true );
            validar.setcMensaje( "Dirección Empleo" );
            return validar;
        }
        */

        return validar;
    }

    View.OnClickListener fabGrabarPersonasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.NUEVE ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_NUEVE );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, !m_EditarDatos ? btnContinuar.getText().toString()+ "-Registrar" : btnContinuar.getText().toString() + "-Actualizar" );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            ValidadorDTO validar = new ValidadorDTO();
            validar = ValidarRegistroDatos();

            if ( validar.isbValor() ){
                Toast.makeText( ActivityPersona.this, "" + validar.getcMensaje() + " incompleto", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            int yearActual = calendar.get( Calendar.YEAR );
            String Fecha = etFechaNac.getText().toString();
            String[] cadena = Fecha.split("/");
            int AnioCliente = 1999;

            for (int i = 0; i < cadena.length; i++) {
                if ( i == Constantes.DOS )
                    AnioCliente = Integer.parseInt(cadena[i]);
            }
            if( ( yearActual - AnioCliente ) < Constantes.DIECIOCHO )
            {
                Toast.makeText( ActivityPersona.this, "Es Menor de edad", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( !cbxAutorizoDatos.isChecked() ){
                Toast.makeText( ActivityPersona.this, "¡Autorizar uso de datos personales!", Toast.LENGTH_SHORT).show();
                return;
            }
            if ( !cbxAutorizoEnvio.isChecked() ){
                Toast.makeText( ActivityPersona.this, "¡Autorizar envios de información!", Toast.LENGTH_SHORT).show();
                return;
            }

            // validando codigo de verificador de DNI
            if ( !m_EditarDatos ){

                if ( ( m_VerificadorDoc && !etDocumento.getText().toString().trim().equals( m_DocumentoValidacion ) ) || ( !m_VerificadorDoc ) ) {

                    if (etCodigoVerificador.getText().toString().trim().equals("") || etCodigoVerificador.getText().length() < Constantes.CERO) {
                        etCodigoVerificador.requestFocus();
                        Toast.makeText(ActivityPersona.this, "¡Verificar el codigo del documento!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    m_VerificadorDoc = validaDocumento( etDocumento.getText().toString(), String.valueOf( etCodigoVerificador.getText().toString() ) );
                    m_DocumentoValidacion = etDocumento.getText().toString();
                }

                if ( m_VerificadorDoc )
                    Toast.makeText( ActivityPersona.this, "¡Validación correcta!", Toast.LENGTH_SHORT);
                else {
                    Toast.makeText(ActivityPersona.this, "¡Codigo de verificación de dni incorrecto", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else {
                int nCodigoVerifiacion = 0;
                if ( etPINVerificacion.getText().toString().trim().equals( "" )
                        || etPINVerificacion.getText().length() < Constantes.CUATRO ){
                    Toast.makeText( ActivityPersona.this, "¡Ingresar el codigo correctamente!", Toast.LENGTH_SHORT ).show();
                    return;
                }

                nCodigoVerifiacion = Integer.parseInt( etPINVerificacion.getText().toString() );

                if ( nCodigoVerifiacion != m_nCodVerificacionMovil ){
                    Toast.makeText( ActivityPersona.this, "¡Código errado!", Toast.LENGTH_SHORT ).show();
                    return;
                }
            }
            // End


            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityPersona.this );
            dialog.setTitle("Aviso");
            if ( m_EditarDatos )
                dialog.setMessage("¿Esta seguro de actualizar sus datos?");
            else
                dialog.setMessage("¿Esta seguro de registrar los datos?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    DatosPersona datosPersona = new DatosPersona();
                    datosPersona = DatosRegistroPersona();

                    Persona persona = new Persona();

                    persona.setnDirTipo2( String.valueOf( (int) spMz.getSelectedItemId() ) );
                    persona.setnDirTipo3( String.valueOf( (int) spLt.getSelectedItemId() ) );
                    persona.setcDirValor2( etMz.getText().toString() );
                    persona.setcDirValor3( etLt.getText().toString() );

                    if ( m_EditarDatos ) {
                        persona.setnCodAge( m_Sesion.getAgencia() );
                        persona.setnCodPers( m_Sesion.getCodPers() );
                        persona.setcTextoSms( "" );
                        persona.setcTextoEmail( "" );
                        persona.setcTituloEmail( "" );
                    }
                    else{
                        persona.setnCodAge( Constantes.CINCO );
                        persona.setnCodPers( Constantes.CERO );

                    }
                    persona.setnTipoDoc( Constantes.UNO );
                    persona.setnProd( Constantes.PRODUCTO );
                    persona.setnSubProd( Constantes.SUB_PRODUCTO );
                    persona.setcNombres( etNombres.getText().toString() );
                    persona.setcApePat( etPrimerApellido.getText().toString() );
                    persona.setcApeMat( etSegundoApellido.getText().toString() );
                    persona.setcCelular( etCelular.getText().toString() );

                    persona.setcEmail( etCorreo.getText().toString() );//
                    persona.setcConfirmaEmail( etCorreo.getText().toString() );

                    String cCodZona = ( datosPersona.getnIdDepartamento() + datosPersona.getnIdProvincia() + datosPersona.getnIdDistrito() + "000000" );
                    persona.setcCodZona( cCodZona );

                    persona.setnCUUI( String.valueOf( (int) spActEconomica.getSelectedItemId() ) );
                    persona.setcNomConyuge( "" );
                    persona.setcApeConyuge( "" );
                    persona.setcDirValor1( etDireccion.getText().toString() );
                    persona.setcDniConyuge( "" );
                    persona.setcRuc( etRuc.getText().toString() );
                    persona.setcTelefono( etTelefonofijo.getText().toString() );
                    persona.setcTelfEmpleo( etTelefonoEmpleo.getText().toString() );
                    persona.setdFechaNacimiento( etFechaNac.getText().toString() );
                    persona.setdFecIngrLab( etFechaIngLaboral.getText().toString() );
                    persona.setnSexo( String.valueOf( (int) spSexo.getSelectedItemId() ) );
                    persona.setnEstadoCivil( String.valueOf( (int) spEstadoCivil.getSelectedItemId() ) );
                    persona.setnProfes( String.valueOf( (int) spProfesion.getSelectedItemId() ) );
                    persona.setnIngresoDeclado( Double.parseDouble( etIngresoDeclarado.getText().toString() ) );
                    persona.setnNroDoc( etDocumento.getText().toString() );
                    persona.setnTipoResidencia( String.valueOf( (int) spResidencia.getSelectedItemId() ) );
                    persona.setnTipoEmp( String.valueOf( (int) spEmpleo.getSelectedItemId() ) );
                    persona.setnSitLab( String.valueOf( (int) spSitLaboral.getSelectedItemId() ) );
                    persona.setnDirTipo1( String.valueOf( (int) spDireccion.getSelectedItemId() ) );
                    persona.setnCodigoVerificador( Integer.parseInt( etCodigoVerificador.getText().toString() ) );
                    persona.setcProfesionOtros( "" );
                    persona.setcDepartamento( datosPersona.getnIdDepartamento() );
                    persona.setcProvincia( datosPersona.getnIdProvincia()  );
                    persona.setcDistrito( datosPersona.getnIdDistrito() );
                    persona.setbCargoPublico( (int) spCargoPublico.getSelectedItemId() == Constantes.DOS ? "True" : "False" );
                    persona.setcNomEmpresa( etCentroLaboral.getText().toString() );
                    persona.setcDniConyuge( etDocumentoConyuge.getText().toString() );
                    persona.setcNomConyuge( etNombresConyuge.getText().toString() );
                    persona.setcApeConyuge( etApellidosConyuge.getText().toString() );

                    persona.setcDirEmpleo( etDireccionEmpleo.getText().toString() );
                    persona.setcDirValor1Empleo( etDireccionEmpleo.getText().toString() );
                    persona.setcDirValor2Empleo( etMzEmpleo.getText().toString() );
                    persona.setcDirValor3Empleo( etLtEmpleo.getText().toString() );
                    persona.setnDirTipo2Empleo( String.valueOf( (int) spMzEmpleo.getSelectedItemId() ) );
                    persona.setnDirTipo3Empleo( String.valueOf( (int) spLtEmpleo.getSelectedItemId() ) );
                    persona.setnDirTipo1Empleo( String.valueOf( (int) spDireccionEmpleo.getSelectedItemId() ) );

                    if ( (int) spDepartamentoEmpleo.getSelectedItemId() < Constantes.DIEZ  )
                        persona.setcDepartamentoEmpleo( "0" + String.valueOf ( (int) spDepartamentoEmpleo.getSelectedItemId() ) );
                    else
                        persona.setcDepartamentoEmpleo( String.valueOf ( (int) spDepartamentoEmpleo.getSelectedItemId() ) );
                    if ( (int) spProvinciaEmpleo.getSelectedItemId() < Constantes.DIEZ  )
                        persona.setcProvinciaEmpleo( "0" + String.valueOf ( (int) spProvinciaEmpleo.getSelectedItemId() ) );
                    else
                        persona.setcProvinciaEmpleo( String.valueOf ( (int) spProvinciaEmpleo.getSelectedItemId() ) );
                    if ( (int) spDistritoEmpleo.getSelectedItemId() < Constantes.DIEZ  )
                        persona.setcDistritoEmpleo( "0" + String.valueOf ( (int) spDistritoEmpleo.getSelectedItemId() ) );
                    else
                        persona.setcDistritoEmpleo( String.valueOf ( (int) spDistritoEmpleo.getSelectedItemId() ) );

                    String cCodZonaEmpleo = ( persona.getcDepartamentoEmpleo() + persona.getcProvinciaEmpleo() + persona.getcDistritoEmpleo() + "000000" );
                    persona.setcCodZonaEmpleo( cCodZonaEmpleo );

                    if ( m_EditarDatos ) {
                        ActualizarPersonaAsyns actualizarPersonaAsyns = new ActualizarPersonaAsyns( ActivityPersona.this, persona);
                        actualizarPersonaAsyns.execute();
                    }
                    else {

                        persona.setcTextoSms( Constantes.Mensaje_Texto_Sms );
                        persona.setcTituloEmail( "Bienvenido a SoyLucas " + persona.getcNombres() );
                        persona.setcTextoEmail( emailCuerpo( "BIENVENIDO (A)", persona.getcNombres(), "¡Gracias por registrarte!", "Para acceder al portal de SoyLucas, ingresa con las siguientes credenciales:", "Usuario: "
                                + persona.getcEmail(), "Contraseña: " + persona.getnNroDoc(), "Nota: Por seguridad debes cambiar tu contraseña." ) );

                        RegistrarPersonaAsyns registrarPersonaAsyns = new RegistrarPersonaAsyns( ActivityPersona.this, persona);
                        registrarPersonaAsyns.execute();
                    }


                }
            });
            dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialog.show();

        }
    };

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

    private DatosPersona DatosRegistroPersona(){

        DatosPersona persona = new DatosPersona();

        String telefono =  String.valueOf( this.etCelular.getText().toString() );
        String telefijo = String.valueOf( this.etTelefonofijo.getText().toString() );
        String ruc = String.valueOf( this.etRuc.getText().toString() );
        String teleempleo = String.valueOf( this.etTelefonoEmpleo.getText().toString() );
        double ingresodeclarado = Double.parseDouble( this.etIngresoDeclarado.getText().toString() );

        persona.setcNombres( etNombres.getText().toString() );
        persona.setcPrimerApellido( etPrimerApellido.getText().toString() );
        persona.setcSegundoApellido( etSegundoApellido.getText().toString() );
        persona.setcCelular( telefono );
        persona.setcCorreo( etCorreo.getText().toString() );

        if ( (int) spDepartamento.getSelectedItemId() < Constantes.DIEZ  )
            persona.setnIdDepartamento( "0" + String.valueOf ( (int) spDepartamento.getSelectedItemId() ) );
        else
            persona.setnIdDepartamento( String.valueOf ( (int) spDepartamento.getSelectedItemId() ) );
        if ( (int) spProvincia.getSelectedItemId() < Constantes.DIEZ  )
            persona.setnIdProvincia( "0" + String.valueOf ( (int) spProvincia.getSelectedItemId() ) );
        else
            persona.setnIdProvincia( String.valueOf ( (int) spProvincia.getSelectedItemId() ) );
        if ( (int) spDistrito.getSelectedItemId() < Constantes.DIEZ  )
            persona.setnIdDistrito( "0" + String.valueOf ( (int) spDistrito.getSelectedItemId() ) );
        else
            persona.setnIdDistrito( String.valueOf ( (int) spDistrito.getSelectedItemId() ) );

        persona.setnTipoDireccion( (int) spDireccion.getSelectedItemId() );
        persona.setcDireccion( etDireccion.getText().toString() );
        persona.setnTipoResidencia( (int) spResidencia.getSelectedItemId() );
        persona.setnIdTipoSexo( (int) spSexo.getSelectedItemId() );
        persona.setcTelefonoFijo( telefijo );
        persona.setcFechaNac( etFechaNac.getText().toString() );
        persona.setnIdEstadoCivil( (int) spEstadoCivil.getSelectedItemId() );
        persona.setnIdActEco( (int) spActEconomica.getSelectedItemId() );
        persona.setnIdSituLab( (int) spSitLaboral.getSelectedItemId() );
        persona.setnIdProfesion( (int) spProfesion.getSelectedItemId() );
        persona.setnIdTipoEmpleo( (int) spEmpleo.getSelectedItemId() );
        persona.setcRuc( ruc );
        persona.setcFechaIngLaboral( etFechaIngLaboral.getText().toString() );
        persona.setcTelefonoEmpleo( teleempleo );
        persona.setnIngresoDeclarado( ingresodeclarado );
        persona.setcDireccionEmpleo( etDireccionEmpleo.getText().toString() );

        return persona;
    }

    View.OnClickListener ibtnFechaNacsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            DatePickerDialog fecha = new DatePickerDialog( ActivityPersona.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int Year, int monthOfYear, int dayOfMonth) {
                    etFechaNac.setText( dayOfMonth + "/" + ( monthOfYear + 1 ) + "/" + Year );
                    year = Year;
                    month = monthOfYear;
                    day = dayOfMonth;
                }
            }, year, month, day );
            fecha.getDatePicker().setMaxDate( new Date().getTime() );
            fecha.show();

        }
    };

    View.OnClickListener ibtnFechaLaboralsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            DatePickerDialog fecha = new DatePickerDialog( ActivityPersona.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int Year, int monthOfYear, int dayOfMonth) {
                    etFechaIngLaboral.setText( dayOfMonth + "/" + ( monthOfYear + 1 ) + "/" + Year );
                    yearL = Year;
                    monthL = monthOfYear;
                    dayL = dayOfMonth;
                }
            }, yearL, monthL, dayL );
            fecha.getDatePicker().setMaxDate( new Date().getTime() );
            fecha.show();

        }
    };

    public class ActualizarCatalogoZonasAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_IdSpinner;
        private Spinner m_Spinner;
        private int m_Nivel;
        private int m_CodigoInferior;
        private int m_CodigoSuperior;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private int m_Tipo;
        private ZonasResponse m_ZonasResponse;

        public ActualizarCatalogoZonasAsync( Context context, int CodigoSuperior, int CodigoInferior, int nNivel, Spinner sp, int IdSpinner, int nTipo ){
            m_IdSpinner = IdSpinner;
            m_Spinner = sp;
            m_Nivel = nNivel;
            m_CodigoInferior = CodigoInferior;
            m_CodigoSuperior = CodigoSuperior;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_Tipo = nTipo;
        }
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando Zonas ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                String CodigoSuperior = "";
                String CodigoInferior = "";

                CodigoSuperior = String.valueOf( m_CodigoSuperior );
                CodigoInferior = String.valueOf( m_CodigoInferior );

                if ( m_CodigoInferior < Constantes.DIEZ )
                    CodigoInferior = "0" + CodigoInferior;
                if ( m_CodigoSuperior < Constantes.DIEZ )
                    CodigoSuperior = "0" + CodigoSuperior;

                List<ZonasDTO> ListaZonas = new ArrayList<>();
                //ListaZonas = m_webApi.obtenerZonas( CodigoInferior, CodigoSuperior, m_Nivel, m_Access_token );

                m_ZonasResponse = m_webApi.obtenerZonas( CodigoInferior, CodigoSuperior, m_Nivel, m_Access_token );
                if ( !m_ZonasResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_ZonasResponse.getM_listdata() != null ){
                        if ( m_ZonasResponse.getM_listdata().size() > Constantes.CERO ){
                            for ( ZonasDTO dto : m_ZonasResponse.getM_listdata() ){

                                Zonas zonas = new Zonas();
                                zonas.setnCodigo(Integer.parseInt(dto.getCODIGO()));
                                zonas.setcDescripcion(dto.getDESCRIPCION());
                                zonas.setnNivel(m_Nivel);
                                zonas.setnTipo( m_Tipo );

                                new ZonasDAO().insertar(zonas);
                                resp = RESULT_OK;
                            }
                        }
                        else
                            resp = RESULT_FALSE;
                    }
                    else
                        resp = RESULT_FALSE;

                }

                /*
                if ( ListaZonas != null ){
                    if ( ListaZonas.size() > Constantes.CERO ){
                        for ( ZonasDTO dto : ListaZonas ){

                                Zonas zonas = new Zonas();
                                zonas.setnCodigo(Integer.parseInt(dto.getCODIGO()));
                                zonas.setcDescripcion(dto.getDESCRIPCION());
                                zonas.setnNivel(m_Nivel);

                                zonas.setnTipo( m_Tipo );

                                new ZonasDAO().insertar(zonas);
                            resp = RESULT_OK;
                        }
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
        protected void onPostExecute( String mensaje ) {
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();

                new ZonasDAO().rellenaSpinner( m_Spinner, m_Nivel, m_context, m_Tipo );
                m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdSpinner ) );

                if ( m_Nivel == Constantes.TRES && (int) spDireccion.getSelectedItemId() <= 0 ){
                    // Cargar Datos Catalogos
                    //ConsultaCatalogoAsync consultaCatalogoAsync = new ConsultaCatalogoAsync( m_context, 0 );
                    //consultaCatalogoAsync.execute();

                    ActualizarCatalogoZonasEmpleoAsync actualizarCatalogoZonasAsync =
                            new ActualizarCatalogoZonasEmpleoAsync( m_context, Constantes.CERO, Constantes.CERO,
                                    Constantes.UNO, spDepartamentoEmpleo, m_IdspDepartamentoEmpleo, Constantes.DOS );
                    actualizarCatalogoZonasAsync.execute();

                }

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();
                //Toast.makeText( m_context, "¡No se actualizo data interna!", Toast.LENGTH_SHORT ).show();
                new ZonasDAO().rellenaSpinner( m_Spinner, m_Nivel, m_context, m_Tipo );

                if ( m_ZonasResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }

        }

    }

    public class ActualizarCatalogoZonasEmpleoAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_IdSpinner;
        private Spinner m_Spinner;
        private int m_Nivel;
        private int m_CodigoInferior;
        private int m_CodigoSuperior;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private int m_Tipo;
        private ZonasResponse m_ZonasResponse;

        public ActualizarCatalogoZonasEmpleoAsync( Context context, int CodigoSuperior, int CodigoInferior, int nNivel, Spinner sp, int IdSpinner, int nTipo ){
            m_IdSpinner = IdSpinner;
            m_Spinner = sp;
            m_Nivel = nNivel;
            m_CodigoInferior = CodigoInferior;
            m_CodigoSuperior = CodigoSuperior;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_Tipo = nTipo;
        }
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando Zonas Empleo ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                String CodigoSuperior = "";
                String CodigoInferior = "";

                CodigoSuperior = String.valueOf( m_CodigoSuperior );
                CodigoInferior = String.valueOf( m_CodigoInferior );

                if ( m_CodigoInferior < Constantes.DIEZ )
                    CodigoInferior = "0" + CodigoInferior;
                if ( m_CodigoSuperior < Constantes.DIEZ )
                    CodigoSuperior = "0" + CodigoSuperior;

                List<ZonasDTO> ListaZonas = new ArrayList<>();
                //ListaZonas = m_webApi.obtenerZonas( CodigoInferior, CodigoSuperior, m_Nivel, m_Access_token );

                m_ZonasResponse = m_webApi.obtenerZonas( CodigoInferior, CodigoSuperior, m_Nivel, m_Access_token );
                if ( !m_ZonasResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_ZonasResponse.getM_listdata() != null ){
                        if ( m_ZonasResponse.getM_listdata().size() > Constantes.CERO ){
                            for ( ZonasDTO dto : m_ZonasResponse.getM_listdata() ){

                                Zonas zonas = new Zonas();
                                zonas.setnCodigo(Integer.parseInt(dto.getCODIGO()));
                                zonas.setcDescripcion(dto.getDESCRIPCION());
                                zonas.setnNivel(m_Nivel);

                                zonas.setnTipo( Constantes.DOS );

                                new ZonasDAO().insertar(zonas);
                                resp = RESULT_OK;
                            }
                        }
                        else
                            resp = RESULT_FALSE;
                    }
                    else
                        resp = RESULT_FALSE;
                }

                /*
                if ( ListaZonas != null ){
                    if ( ListaZonas.size() > Constantes.CERO ){
                        for ( ZonasDTO dto : ListaZonas ){

                            Zonas zonas = new Zonas();
                            zonas.setnCodigo(Integer.parseInt(dto.getCODIGO()));
                            zonas.setcDescripcion(dto.getDESCRIPCION());
                            zonas.setnNivel(m_Nivel);

                            zonas.setnTipo( Constantes.DOS );

                            new ZonasDAO().insertar(zonas);
                            resp = RESULT_OK;
                        }
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
        protected void onPostExecute( String mensaje ) {
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();

                new ZonasDAO().rellenaSpinner( m_Spinner, m_Nivel , m_context, Constantes.DOS );
                m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdSpinner ) );

                if ( m_Nivel == Constantes.TRES && (int) spDireccion.getSelectedItemId() <= 0 ){
                    // Cargar Datos Catalogos
                    ConsultaCatalogoAsync consultaCatalogoAsync = new ConsultaCatalogoAsync( m_context, Constantes.CERO );
                    consultaCatalogoAsync.execute();
                }

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();
                //Toast.makeText( m_context, "¡No se actualizo data interna!", Toast.LENGTH_SHORT ).show();
                new ZonasDAO().rellenaSpinner( m_Spinner, m_Nivel, m_context, Constantes.DOS );

                if ( m_ZonasResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }

        }

    }

    public class ConsultaDocumentoAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private String m_mensajeAlerta;
        private String m_documento;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Persona m_Persona;
        private PersonaResponse m_PersonaResponse;

        public ConsultaDocumentoAsync( Context context, String Documento ){
            m_documento = Documento;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Consultando documento...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_Persona = new Persona();
                Persona datos = new Persona();
                datos.setnCodPers( m_Sesion.getCodPers() );
                datos.setnNroDoc( m_Sesion.getRolDescripcion() ); //Documento
                datos.setcEmail( m_Sesion.getUsuario() );

                //m_Persona = m_webApi.obtenerPersonaDatos( datos, m_Sesion.getToken() );
                m_PersonaResponse = m_webApi.obtenerPersonaDatos( datos, m_Sesion.getToken() );
                if( !m_PersonaResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    m_Persona = m_PersonaResponse.getM_data();
                }

                /*
                if ( m_Persona != null ){
                    if( m_Persona.getnCodPers() > Constantes.CERO ){
                        resp = RESULT_OK;
                    }
                    else{
                        m_mensajeAlerta = "No se obtuvo datos del Cliente";
                        resp = RESULT_FALSE;
                    }
                }
                else{
                    m_mensajeAlerta = "No se obtuvo datos del Cliente";
                    resp = RESULT_FALSE;
                }
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
                CargarDatos( m_Persona );
                pd.dismiss();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_context, m_mensajeAlerta, Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_PersonaResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
                Toast.makeText( m_context, m_mensajeAlerta, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
    }

    protected void CargarDatos( Persona datos ){

        etNombres.setText( datos.getcNombres() );
        etPrimerApellido.setText( datos.getcApePat() );
        etSegundoApellido.setText( datos.getcApeMat() );
        etCelular.setText( datos.getcCelular() );
        etCorreo.setText( datos.getcEmail() );
        etDireccion.setText( datos.getcDirValor1() );//datos.getcDireccion() );
        etMz.setText( datos.getcDirValor2() );
        etLt.setText( datos.getcDirValor3() );
        etTelefonofijo.setText( datos.getcTelefono() );
        etFechaNac.setText( datos.getdFechaNacimiento() );
        etRuc.setText( datos.getcRuc() );
        etCentroLaboral.setText( datos.getcNomEmpresa() );//
        etFechaIngLaboral.setText( datos.getdFecIngrLab() );
        etTelefonoEmpleo.setText( datos.getcTelfEmpleo() );
        etIngresoDeclarado.setText( String.valueOf( datos.getnIngresoDeclado() ) );
        m_nCodPers = datos.getnCodPers();
        etDocumento.setEnabled( false );
        etNombres.setEnabled( false );
        etPrimerApellido.setEnabled( false );
        etSegundoApellido.setEnabled( false );
        etCelular.requestFocus();

        //Cargar Zonas
        m_IdspDepartamento = Integer.valueOf( datos.getcDepartamento() );
        m_IdspProvincia = Integer.valueOf( datos.getcProvincia() );
        m_IdspDistrito = Integer.valueOf( datos.getcDistrito() );

        if ( m_IdspDepartamento > 0 )
            spDepartamento.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );
        if ( m_IdspProvincia > 0 )
            spProvincia.setSelection( Utilidades.getCursorSpinnerPositionById( spProvincia, m_IdspProvincia ) );
        if ( m_IdspDistrito > 0 )
            spDistrito.setSelection( Utilidades.getCursorSpinnerPositionById( spDistrito, m_IdspDistrito ) );

        if ( Integer.valueOf( datos.getnDirTipo1() ) > 0 )
            spDireccion.setSelection( Utilidades.getCursorSpinnerPositionById( spDireccion, Integer.valueOf( datos.getnDirTipo1() ) ) );
        if ( datos.getnCUUI() != null )
            if ( Integer.parseInt( datos.getnCUUI() ) > 0 )
                spActEconomica.setSelection( Utilidades.getCursorSpinnerPositionById( spActEconomica, Integer.parseInt( datos.getnCUUI() ) ) );
        if ( Integer.valueOf( datos.getnSexo() ) > 0 )
            spSexo.setSelection( Utilidades.getCursorSpinnerPositionById( spSexo, Integer.valueOf( datos.getnSexo() ) ) );
        if ( Integer.valueOf( datos.getnEstadoCivil() ) > 0 )
            spEstadoCivil.setSelection( Utilidades.getCursorSpinnerPositionById( spEstadoCivil, Integer.valueOf( datos.getnEstadoCivil() ) ) );
        if ( Integer.valueOf( datos.getnSitLab() ) > 0 )
            spSitLaboral.setSelection( Utilidades.getCursorSpinnerPositionById( spSitLaboral, Integer.valueOf( datos.getnSitLab() ) ) );
        if ( Integer.valueOf( datos.getnProfes() ) > 0 )
            spProfesion.setSelection( Utilidades.getCursorSpinnerPositionById( spProfesion, Integer.valueOf( datos.getnProfes() ) ) );
        if ( Integer.valueOf( datos.getnTipoEmp() ) > 0 )
            spEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spEmpleo, Integer.valueOf( datos.getnTipoEmp() ) ) );
        if ( Integer.valueOf( datos.getnTipoResidencia() ) > 0 )
            spResidencia.setSelection( Utilidades.getCursorSpinnerPositionById( spResidencia, Integer.valueOf( datos.getnTipoResidencia() ) ) );

        if ( Boolean.parseBoolean( datos.getbCargoPublico() ) )
            spCargoPublico.setSelection( Utilidades.getCursorSpinnerPositionById( spCargoPublico, Constantes.DOS ) );

        if ( Integer.valueOf( datos.getnDirTipo2() ) > 0 )
            spMz.setSelection( Utilidades.getCursorSpinnerPositionById( spMz, Integer.valueOf( datos.getnDirTipo2() ) ) );
        if ( Integer.valueOf( datos.getnDirTipo3() ) > 0 )
            spLt.setSelection( Utilidades.getCursorSpinnerPositionById( spLt, Integer.valueOf( datos.getnDirTipo3() ) ) );

        etDocumentoConyuge.setText( datos.getcDniConyuge() );
        etNombresConyuge.setText( datos.getcNomConyuge() );
        etApellidosConyuge.setText( datos.getcApeConyuge() );

        //direccion de empleo mdipas 01.09.2017
        m_IdspDepartamentoEmpleo = Integer.valueOf( datos.getcDepartamentoEmpleo() );
        m_IdspProvinciaEmpleo = Integer.valueOf( datos.getcProvinciaEmpleo() );
        m_IdspDistritoEmpleo = Integer.valueOf( datos.getcDistritoEmpleo() );

        etDireccionEmpleo.setText( datos.getcDirValor1Empleo() );
        etMzEmpleo.setText( datos.getcDirValor2Empleo() );
        etLtEmpleo.setText( datos.getcDirValor3Empleo() );

        if ( m_IdspDepartamentoEmpleo > 0 )
            spDepartamentoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamentoEmpleo, m_IdspDepartamentoEmpleo ) );
        if ( m_IdspProvinciaEmpleo > 0 )
            spProvincia.setSelection( Utilidades.getCursorSpinnerPositionById( spProvincia, m_IdspProvinciaEmpleo ) );
        if ( m_IdspDistritoEmpleo > 0 )
            spDistritoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDistritoEmpleo, m_IdspDistritoEmpleo ) );
        if ( Integer.valueOf( datos.getnDirTipo2Empleo() ) > 0 )
            spMzEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spMzEmpleo, Integer.valueOf( datos.getnDirTipo2Empleo() ) ) );
        if ( Integer.valueOf( datos.getnDirTipo3Empleo() ) > 0 )
            spLtEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spLtEmpleo, Integer.valueOf( datos.getnDirTipo3Empleo() ) ) );
        if ( Integer.valueOf( datos.getnDirTipo1Empleo() ) > 0 )
            spDireccionEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDireccionEmpleo, Integer.valueOf( datos.getnDirTipo1Empleo() ) ) );

        if ( m_EditarDatos ){
            boolean estado = !m_EditarDatos;
            etDocumento.setEnabled( estado );
            etPrimerApellido.setEnabled( estado );
            etSegundoApellido.setEnabled( estado );
            etNombres.setEnabled( estado );
            etCorreo.setEnabled( estado );
        }

    }

    public class ConsultaCatalogoAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ConsultaCatalogoAsync( Context context, int Codigo ){
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

                for ( int i = 0; i <= Constantes.SIETE; i ++ ){

                    m_listcatalogo = new ArrayList<>();
                    int Codigo = 0;

                    if (i == 0 )
                        Codigo = Constantes.TIPO_ACT_ECO;
                    else if( i == 1 )
                        Codigo = Constantes.TIPO_DIRECCION;
                    else if( i == 2 )
                        Codigo = Constantes.TIPO_SEXO;
                    else if( i == 3 )
                        Codigo = Constantes.TIPO_ESTADO_CIVIL;
                    else if( i == 4 )
                        Codigo = Constantes.TIPO_EST_LABORAL;
                    else if( i == 5 )
                        Codigo = Constantes.TIPO_PROFESION;
                    else if( i == 6 )
                        Codigo = Constantes.TIPO_EMPLEO;
                    else
                        Codigo = Constantes.TIPO_RESIDENCIA;

                    m_listcatalogo = m_webApi.obtenerCatalago( Codigo, m_Access_token );
                    if ( m_listcatalogo != null ) {
                        if ( m_listcatalogo.size() > 0 ) {
                            new CatalagoCodigosDAO().LimpiarCatalogoCodigosxID( Codigo );
                            for ( CatalagoCodigosDTO dto : m_listcatalogo ) {
                                CatalagoCodigos catalagoCodigos = new CatalagoCodigos();
                                catalagoCodigos.setcNomCod( dto.getcNomCod() );
                                catalagoCodigos.setnCodigo( Codigo );
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
                ActualizarCatalogoCodigos();
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

        private String m_mensajeAlerta;
        private List<CatalagoCodigosDTO> m_listcatalogo;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

    }

    protected void ActualizarZonas( int idCodigoSuperior, int idCodigoInferior, int nNivel, Spinner spinner, int idSpinner, int nTipo ){
        try {

            new ZonasDAO().LimpiarZonasXnivel( nNivel, nTipo );

            if ( Constantes.UNO == nTipo ) {

                ActualizarCatalogoZonasAsync actualizarCatalogoZonasAsync =
                        new ActualizarCatalogoZonasAsync( ActivityPersona.this, idCodigoSuperior, idCodigoInferior, nNivel, spinner, idSpinner, nTipo );
                actualizarCatalogoZonasAsync.execute();
            }
            else{
                ActualizarCatalogoZonasEmpleoAsync actualizarCatalogoZonasAsync =
                        new ActualizarCatalogoZonasEmpleoAsync( ActivityPersona.this, idCodigoSuperior, idCodigoInferior, nNivel, spinner, idSpinner, nTipo );
                actualizarCatalogoZonasAsync.execute();
            }

        }
        catch ( Exception ex )
        {
            Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }

    protected void ActualizarCatalogoCodigos(){
        try {

            new CatalagoCodigosDAO().rellenaSpinner( spDireccion, Constantes.TIPO_DIRECCION, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spActEconomica, Constantes.TIPO_ACT_ECO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spSexo, Constantes.TIPO_SEXO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spEstadoCivil, Constantes.TIPO_ESTADO_CIVIL, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spSitLaboral, Constantes.TIPO_EST_LABORAL, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spProfesion, Constantes.TIPO_PROFESION, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spEmpleo, Constantes.TIPO_EMPLEO, ActivityPersona.this );

            new CatalagoCodigosDAO().rellenaSpinnerOrden( spResidencia, Constantes.TIPO_RESIDENCIA, ActivityPersona.this, Constantes.TRES );

            new CatalagoCodigosDAO().rellenaSpinner( spMz, Constantes.TIPO_NRO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spLt, Constantes.TIPO_DPTO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spCargoPublico, Constantes.TIPO_CARGO_PUBLICO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spMzEmpleo, Constantes.TIPO_NRO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spLtEmpleo, Constantes.TIPO_DPTO, ActivityPersona.this );
            new CatalagoCodigosDAO().rellenaSpinner( spDireccionEmpleo, Constantes.TIPO_DIRECCION, ActivityPersona.this );

            if ( m_EditarDatos ){

                ConsultaDocumentoAsync consultaDocumentoAsync =
                        new ConsultaDocumentoAsync( ActivityPersona.this, etDocumento.getText().toString() );
                consultaDocumentoAsync.execute();

                boolean estado = !m_EditarDatos;
                etDocumento.setEnabled( estado );
                etPrimerApellido.setEnabled( estado );
                etSegundoApellido.setEnabled( estado );
                etNombres.setEnabled( estado );
                etCorreo.setEnabled( estado );
                lyCodigoVerificador.setVisibility( View.GONE );
                lyConfirmarCorreo.setVisibility( View.GONE );
            }

        }
        catch ( Exception ex )
        {
            Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }

    public class RegistrarPersonaAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private PersonaRespResponse m_PersonaRespResponse;

        public RegistrarPersonaAsyns(Context context, Persona persona ){
            m_PersonaCreditoDTO = persona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando persona ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                PersonaRespDTO dto = new PersonaRespDTO();

                //dto = m_webApi.registrarPersonaCredito( m_PersonaCreditoDTO, m_Access_token );
                m_PersonaRespResponse = m_webApi.registrarPersonaCredito( m_PersonaCreditoDTO, m_Access_token );
                if( !m_PersonaRespResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_PersonaRespResponse.getM_data() != null ){
                        if ( m_PersonaRespResponse.getM_data().getnCodPers() > Constantes.CERO ){ //Solo registra persona o edita persona.
                            m_nCodPers = m_PersonaRespResponse.getM_data().getnCodPers();
                            resp = RESULT_OK;
                        }
                        else {
                            m_mensajeAlerta = m_PersonaRespResponse.getM_data().getcMensaje();
                            resp = RESULT_FALSE;
                        }

                    }
                    else {
                        m_mensajeAlerta = "¡Error en el proceso!";
                        resp = RESULT_FALSE;
                    }
                }


                /*
                if ( dto != null ){
                    if ( dto.getnCodPers() > Constantes.CERO ){ //Solo registra persona o edita persona.
                        m_nCodPers = dto.getnCodPers();
                        resp = RESULT_OK;
                    }
                    else {
                        m_mensajeAlerta = dto.getcMensaje();
                        resp = RESULT_FALSE;
                    }

                }
                else {
                    m_mensajeAlerta = "¡Error en el proceso!";
                    resp = RESULT_FALSE;
                }
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

                Toast.makeText(m_context, "¡Se realizo el proceso de datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                // REGISTRO NUEVA PERSONA
                //Agregar sesion creada por la nueva persona

                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setUsername( m_PersonaCreditoDTO.getcEmail() );
                usuarioDTO.setPassword( m_PersonaCreditoDTO.getnNroDoc() );

                ObtenerTokenNuevoAsync obtenerTokenAsync =
                new ObtenerTokenNuevoAsync( m_context, usuarioDTO, m_PersonaCreditoDTO );
                obtenerTokenAsync.execute();


            }
            else if (mensaje.equals(RESULT_FALSE)) {
                //Toast.makeText(m_context, m_mensajeAlerta, Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_PersonaRespResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private String m_mensajeAlerta;
        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    public class ObtenerAutenticacionAsync extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerAutenticacionAsync ( Context context, UsuarioDTO usuarioDTO, Persona persona ){

            m_PersonaCreditoDTO = persona;
            m_UsuarioDTO = usuarioDTO;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {

            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Autenticando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                Persona dto = new Persona();
                dto = m_webApi.obtenerAutenticacion( m_UsuarioDTO );

                if ( dto != null ) {
                    //if ( dto.getnCodUsu() > Constantes.CERO ) {
                    if ( dto.getnCodPers() > Constantes.CERO ) {

                        m_Usuario = new Usuario();

                        m_Usuario.setUsuario( m_UsuarioDTO.getUsername() );
                        m_Usuario.setClave( m_UsuarioDTO.getPassword() );
                        m_Usuario.setRolDescripcion( dto.getnNroDoc() ); //DOCUMENTO
                        m_Usuario.setRol( 0 );
                        m_Usuario.setAgencia( Constantes.CINCO );
                        m_Usuario.setAgenciaNombre( "" );
                        m_Usuario.setCodDepAge( 0 );
                        m_Usuario.setCodUsu( 0 );
                        m_Usuario.setNombreUsuario( dto.getcNombres() );
                        m_Usuario.setCodPers( dto.getnCodPers() );
                        m_Usuario.setToken( m_UsuarioDTO.getPassword() );
                        m_Usuario.setTipo( "" );
                        m_Usuario.setIdPais(0 );
                        m_Usuario.setIdEmpresa( 0 );
                        m_Usuario.setIdTipoEmpresa( 0 );
                        m_Usuario.setMoneda( "" );

                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    return RESULT_FALSE;

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
        protected void onPostExecute( String mensaje ) {
            if (mensaje.equals(RESULT_OK)) {
                RegistrarAutenticacion( m_Usuario, pd, m_PersonaCreditoDTO );
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText(m_context, "¡No se pudo realizar el acceso al Usuario!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private Usuario m_Usuario;
        private UsuarioDTO m_UsuarioDTO;
        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

    }

    public void RegistrarAutenticacion( Usuario usuario, ProgressDialog pd, Persona oPersonaCreditoDTO ){

        pd.dismiss();

        int ingresoAutenticacion = 0;
        ingresoAutenticacion = new UsuarioDAO().ingresarAutenticacion( usuario );

        //Guardando en modo privado la sesion nueva
        SharedPreferences.Editor spe = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
        Gson gson = new Gson();
        String json = gson.toJson( usuario );
        spe.putString( ActivityLogin.ARG_AUTENTICACION_JSON, json );
        spe.commit();
        //end

        if ( ingresoAutenticacion > Constantes.CERO ) {
            new UsuarioDAO().iniciarSesion( usuario );

            // PreEvaluacion
            ObtenerPreEvaluacionAsyns preEvaluacionAsyns =
                    new ObtenerPreEvaluacionAsyns( ActivityPersona.this, oPersonaCreditoDTO );
            preEvaluacionAsyns.execute();
            //

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    protected boolean validaDocumento( String pnDocumento, String pnCodChequeo ){
        int addition;
        String[] HashNumbers = new String[11];
        boolean OK;
        addition = 0;
        Integer[] Hash = new Integer[10];
        String identificationComponent;
        identificationComponent = pnDocumento;
        int identificationComponentLength;
        identificationComponentLength = identificationComponent.length();
        Hash[0] = 5;
        Hash[1] = 4;
        Hash[2] = 3;
        Hash[3] = 2;
        Hash[4] = 7;
        Hash[5] = 6;
        Hash[6] = 5;
        Hash[7] = 4;
        Hash[8] = 3;
        Hash[9] = 2;
        int diff;
        diff = (10 - identificationComponentLength);
        int i;
        i = identificationComponentLength;
        while ( ( i >= 1 ) ) {
            addition = ( addition + ( Integer.parseInt( identificationComponent.substring( i-1, i ) ) - 0 ) * Hash[( (i - 1) + diff)] );
            i = ( i - 1);
        }

        addition = ( 11 - ( addition % 11 ) );

        if ( (addition == 11 ) ) {
            addition = 0;
        }

        if ( Utilidades.IsNumeric( pnCodChequeo ) ) {
            HashNumbers[0] = "6";
            HashNumbers[1] = "7";
            HashNumbers[2] = "8";
            HashNumbers[3] = "9";
            HashNumbers[4] = "0";
            HashNumbers[5] = "1";
            HashNumbers[6] = "1";
            HashNumbers[7] = "2";
            HashNumbers[8] = "3";
            HashNumbers[9] = "4";
            HashNumbers[10] = "5";

            if ( pnCodChequeo.equals( HashNumbers[addition] ) ) {
                OK = true;
            }
            else {
                OK = false;
            }

        }
        else {
            HashNumbers[0] = "K";
            HashNumbers[1] = "A";
            HashNumbers[2] = "B";
            HashNumbers[3] = "C";
            HashNumbers[4] = "D";
            HashNumbers[5] = "E";
            HashNumbers[6] = "F";
            HashNumbers[7] = "G";
            HashNumbers[8] = "H";
            HashNumbers[9] = "I";
            HashNumbers[10] = "J";
            if ( pnCodChequeo.equals( HashNumbers[addition] ) ) {
                OK = true;
            }
            else {
                OK = false;
            }
        }
        return OK;
    }

    protected String GenerarCodigoVerificadorParaLenddo(){
        String appIDCompleto = "";
        int codigo = 0;
        String appID = Constantes.APPLICATION_ID;
        int randomPIN = (int)( Math.random()  * 899999 ) + 100000;
        codigo = randomPIN;

        Calendar calendar = Calendar.getInstance();
        int anio = calendar.get( Calendar.YEAR );
        int mes = calendar.get( Calendar.MONTH );
        int dia = calendar.get( Calendar.DATE );
        int hora = calendar.get( Calendar.HOUR_OF_DAY );
        int minuto = calendar.get( Calendar.MINUTE );
        int segundo = calendar.get( Calendar.SECOND );

        String fecha = String.valueOf( anio + mes + dia );
        String horario = String.valueOf( hora + minuto + segundo );

        appIDCompleto = appID + fecha + horario + String.valueOf( codigo );

        /*
        var f = new Date();
        var fecha = f.getDate() + "" + (f.getMonth() + 1) + "" + f.getFullYear();
        var time = f.getHours() + "" + f.getMinutes() + "" + f.getSeconds();
        var application_id = Constantes.APPLICATION_ID + fecha + (new Date().getTime()  Math.round(Math.random()  ((899999) + 100000)));
        */

        return appIDCompleto;
    }

    public class ObtenerTokenAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private TokenItems m_TokenItems;
        private UsuarioDTO m_UsuarioDTO;

        public ObtenerTokenAsync( Context context, UsuarioDTO usuario ){
            m_UsuarioDTO = usuario;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Token...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_TokenItems = new TokenItems();
                m_TokenItems = m_webApi.ObtenerToken( m_UsuarioDTO );

                if ( m_TokenItems != null )
                    resp = RESULT_OK;
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
                pd.dismiss();

                m_Access_token = m_TokenItems.getAccess_token();

                ActualizarCatalogoZonasAsync actualizarCatalogoZonasAsync = new ActualizarCatalogoZonasAsync( ActivityPersona.this, 0,0,
                        Constantes.UNO, spDepartamento, m_IdspDepartamento, Constantes.UNO );
                actualizarCatalogoZonasAsync.execute();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ObtenerTokenNuevoAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private TokenItems m_TokenItems;
        private UsuarioDTO m_UsuarioDTO;
        private Persona m_Persona;

        public ObtenerTokenNuevoAsync( Context context, UsuarioDTO usuario, Persona persona ){
            m_Persona = persona;
            m_UsuarioDTO = usuario;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Token...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_TokenItems = new TokenItems();
                m_TokenItems = m_webApi.ObtenerToken( m_UsuarioDTO );

                if ( m_TokenItems != null )
                    resp = RESULT_OK;
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
                pd.dismiss();
                m_Access_token = m_TokenItems.getAccess_token();
                m_UsuarioDTO.setPassword( m_Access_token ); // cambio el password por el token y recuperar el usuario con el email.

                ObtenerAutenticacionAsync obtenerAutenticacionAsync =
                        new ObtenerAutenticacionAsync( m_context, m_UsuarioDTO, m_Persona );
                obtenerAutenticacionAsync.execute();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ObtenerPreEvaluacionAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private PreEvaluacionResp m_Respuesta;

        public ObtenerPreEvaluacionAsyns( Context context, Persona oPersona ){

            m_PersonaCreditoDTO = oPersona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Evaluando datos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new PreEvaluacionResp();
                m_PersonaCreditoDTO.setnProducto( Constantes.PRODUCTO_SCORING );
                m_PersonaCreditoDTO.setnTipoDoc( Constantes.UNO );
                m_Respuesta = m_webApi.obtenerPreEvaluacion( m_PersonaCreditoDTO, m_Access_token );

                if ( m_Respuesta != null  ){
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

                String validar = "";
                String[] cadena = m_Respuesta.getcResultado().split("|");
                for ( int i = 0; i < cadena.length; i++ ) {
                    if ( i == Constantes.DOS )
                        validar =  (cadena[i]);
                }

                if ( validar.equals( "BANCARIZADO" ) ){

                    ObtenerEvaluacionAsyns obtenerEvaluacionAsyns =
                            new ObtenerEvaluacionAsyns( m_context, m_PersonaCreditoDTO );
                    obtenerEvaluacionAsyns.execute();

                }
                else {

                    PersonaCreditoDTO datosPersona = new PersonaCreditoDTO();

                    datosPersona.setcNombres( m_PersonaCreditoDTO.getcNombres() );
                    datosPersona.setnNroDoc( m_PersonaCreditoDTO.getnNroDoc() );
                    datosPersona.setcApePat( m_PersonaCreditoDTO.getcApePat() );
                    datosPersona.setcApeMat( m_PersonaCreditoDTO.getcApeMat() );
                    datosPersona.setcEmail( m_PersonaCreditoDTO.getcEmail() );
                    datosPersona.setdFechaNacimiento( m_PersonaCreditoDTO.getdFechaNacimiento() );
                    datosPersona.setcDirValor1( m_PersonaCreditoDTO.getcDirValor1() );
                    datosPersona.setcTelefono( m_PersonaCreditoDTO.getcTelefono() );
                    datosPersona.setcCelular( m_PersonaCreditoDTO.getcCelular() );
                    datosPersona.setnCodPers( m_PersonaCreditoDTO.getnCodPers() );
                    datosPersona.setcClienteLenddo( GenerarCodigoVerificadorParaLenddo() );

                    //LENDDO
                    Intent lenddo = new Intent( m_context, LenddoActivity.class );
                    lenddo.putExtra( "DatosPersona", datosPersona );
                    lenddo.putExtra( "OrdenFlujo", Constantes.DOS );
                    startActivity( lenddo );
                    finish();

                }


            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText(m_context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ObtenerEvaluacionAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private EvaluacionResp m_Respuesta;

        public ObtenerEvaluacionAsyns( Context context, Persona oPersona ){

            m_PersonaCreditoDTO = oPersona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Evaluando datos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new EvaluacionResp();
                m_PersonaCreditoDTO.setnProd( Constantes.PRODUCTO );
                m_PersonaCreditoDTO.setnSubProd( Constantes.SUB_PRODUCTO );
                m_PersonaCreditoDTO.setnTipoDoc( Constantes.UNO );
                m_Respuesta = m_webApi.obtenerEvaluacion( m_PersonaCreditoDTO, m_Access_token );

                if ( m_Respuesta != null  ){
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

                String mensajeSms = "";
                if ( m_Respuesta.getnRechazado() == Constantes.UNO ) {

                    mensajeSms = Constantes.Mensaje_Rechazado;
                    if ( m_Respuesta.getnPEP() == Constantes.UNO )
                        mensajeSms = Constantes.Mensaje_Rechazado_PEP;

                    Intent rechazo = new Intent( m_context, ActivityRechazoSolicitud.class );
                    rechazo.putExtra( "Telefono", m_PersonaCreditoDTO.getcCelular().toString() );
                    rechazo.putExtra( "Mensaje", mensajeSms );
                    startActivity( rechazo );

                    //BLOQUEADO
                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder( m_context );
                    builder.setMessage("Lamentamos informarle que no podemos aprobar su solicitud, gracias por contactarnos")
                            .setTitle("Lucas!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            String mensajeSms = "";
                                            mensajeSms = Constantes.Mensaje_Rechazado;
                                            if ( m_Respuesta.getnPEP() == Constantes.UNO )
                                                mensajeSms = Constantes.Mensaje_Rechazado_PEP;

                                            Alerta alerta = new Alerta();
                                            alerta.setcMovil( m_PersonaCreditoDTO.getcCelular().toString() );
                                            alerta.setcTexto( mensajeSms );

                                            EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( m_context, alerta, mensajeSms, true );
                                            envioSMSAsyns.execute();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */
                }
                else{
                    // enviar a solicitud
                    Intent solicitudFinal = new Intent( m_context, ActivitySimulador.class );
                    solicitudFinal.putExtra( "Solicitud" , "OK" );
                    solicitudFinal.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                    startActivity( solicitudFinal );
                    finish();
                }

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText(m_context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ActualizarPersonaAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private PersonaRespResponse m_PersonaRespResponse;

        public ActualizarPersonaAsyns( Context context, Persona persona ){
            m_PersonaCreditoDTO = persona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando datos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                PersonaRespDTO dto = new PersonaRespDTO();
                //dto = m_webApi.actualizarPersonaCredito( m_PersonaCreditoDTO, m_Access_token );

                m_PersonaRespResponse = m_webApi.actualizarPersonaCredito( m_PersonaCreditoDTO, m_Access_token );
                if( !m_PersonaRespResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_PersonaRespResponse.getM_data() != null ){
                        if ( m_PersonaRespResponse.getM_data().getnCodPers() > Constantes.CERO ){ //Solo registra persona o edita persona.
                            m_nCodPers = m_PersonaRespResponse.getM_data().getnCodPers();
                            resp = RESULT_OK;
                        }
                        else {
                            m_mensajeAlerta = m_PersonaRespResponse.getM_data().getcMensaje();
                            resp = RESULT_FALSE;
                        }

                    }
                    else {
                        m_mensajeAlerta = "¡Error en el proceso!";
                        resp = RESULT_FALSE;
                    }
                }


                /*
                if ( dto != null ){
                    if ( dto.getnCodPers() > Constantes.CERO ){
                        m_nCodPers = dto.getnCodPers();
                        resp = RESULT_OK;
                    }
                    else {
                        m_mensajeAlerta = dto.getcMensaje();
                        resp = RESULT_FALSE;
                    }
                }
                else {
                    m_mensajeAlerta = "¡Error en el proceso!";
                    resp = RESULT_FALSE;
                }
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

                Toast.makeText(m_context, "¡Se realizo el proceso de datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                Intent intent = new Intent (m_context, ActivityBandejaCreditos.class );
                startActivity(intent);
                finish();

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                //Toast.makeText(m_context, m_mensajeAlerta, Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_PersonaRespResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private String m_mensajeAlerta;
        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    protected void ObtenerVerificacion(){

        m_nCodVerificacionMovil = GenerarCodigoVerificador();

        Alerta alerta = new Alerta();
        alerta.setcMovil( etCelular.getText().toString() );
        alerta.setcTexto( "Hola SoyLucas, tu código es " + m_nCodVerificacionMovil );
        alerta.setcEmail( "" );
        alerta.setcTitulo( "Lucas" );

        EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( ActivityPersona.this, alerta, "", false );
        envioSMSAsyns.execute();
    }

    protected int GenerarCodigoVerificador(){
        int codigo = 0;
        int randomPIN = (int)( Math.random()  * 9000 ) + 1000;
        codigo = randomPIN;
        String val = ""+randomPIN;
        return codigo;
    }

    public class EnvioSMSAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Alerta m_Alerta;
        private String m_MensajeEnvio;
        private boolean m_bEnvioEmail;
        private RedResponse m_RedResponse;

        public EnvioSMSAsyns( Context context, Alerta alerta, String mensajeEnvio, boolean bEnvioEmail ){
            m_Alerta = alerta;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_MensajeEnvio = mensajeEnvio;
            m_bEnvioEmail = bEnvioEmail;
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

                if( m_bEnvioEmail ){

                    Alerta alerta = new Alerta();
                    alerta.setcTexto( emailCuerpo("MALAS NOTICIAS", m_Sesion.getNombreUsuario(), "Te hemos rechazado", m_MensajeEnvio, "", "", "") );
                    alerta.setcEmail( m_Sesion.getUsuario() );
                    alerta.setcTitulo( "Hola SoyLucas, tenemos malas noticias." );

                    EnvioEmailAsyns envioEmailAsyns = new EnvioEmailAsyns( m_Context, alerta );
                    envioEmailAsyns.execute();
                }

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

                Intent bandeja = new Intent( m_Context, ActivityBandejaCreditos.class);
                startActivity( bandeja );
                finish();
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
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if ( m_EditarDatos ){
                    setResult( RESULT_CANCELED );
                    finish();
                }
                else {
                    /*
                    Intent solicitudFinal = new Intent( ActivityPersona.this, ActivityBandejaCreditos.class );
                    startActivity( solicitudFinal );
                    finish();
                    */

                    setResult( RESULT_CANCELED );
                    finish();
                }

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
