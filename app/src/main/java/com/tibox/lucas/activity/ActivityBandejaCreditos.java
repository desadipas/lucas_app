package com.tibox.lucas.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lenddo.data.AndroidData;
import com.tibox.lucas.R;
import com.tibox.lucas.adaptadores.listview.AdaptadorLvCreditos;
import com.tibox.lucas.adaptadores.recyclerview.AdaptadorRvSolicitudes;
import com.tibox.lucas.adaptadores.recyclerview.RecyclerAdapter;
import com.tibox.lucas.dao.BandejaCreditosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.BandejaCreditos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.entidad.expandablegroup.SubTitle;
import com.tibox.lucas.entidad.expandablegroup.Title;
import com.tibox.lucas.lenddo.LenddoActivity;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.AnularDTO;
import com.tibox.lucas.network.dto.BandParamDTO;
import com.tibox.lucas.network.dto.CalendarioDTO;
import com.tibox.lucas.network.dto.ClienteDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosSalida.Credito;
import com.tibox.lucas.network.dto.DatosSalida.EvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.DatosSalida.PreEvaluacionResp;
import com.tibox.lucas.network.dto.OperacionesDTO;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.response.CalendarioResponse;
import com.tibox.lucas.network.response.CreditoResponse;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.PersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ActivityBandejaCreditos extends AppCompatActivity implements RecyclerAdapter.RVListener {

    private ListView lvCreditos;
    private AdaptadorLvCreditos m_AdaptadorLvCreditos;
    private ArrayList<BandejaCreditos> arrayListCreditos;
    protected Usuario m_Sesion;
    protected BandejaCreditos m_SelectCredito;
    private Button btnBuscarCredito, btnNuevoCredito;

    public final static int ACTUALIZAR_DATOS_REQUEST_CODE = 200;
    public final static int CAMBIO_CONTRASENIA_REQUEST_CODE = 201;
    public final static int CONSULTA_CREDITO_REQUEST_CODE = 202;
    private static final int REQUEST_CALL_PHONE = 203;

    private static long back_pressed;
    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private FlujoMaestro m_Flujo;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView rvSolicitudes;
    private AdaptadorRvSolicitudes arvListaSolicitudes;
    private boolean m_bPermisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja_creditos);

        lvCreditos = (ListView) findViewById(R.id.lvCreditos);
        btnBuscarCredito = (Button) findViewById(R.id.btnBuscarCredito);
        btnNuevoCredito = (Button) findViewById(R.id.btnNuevoCredito);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        rvSolicitudes = (RecyclerView) findViewById(R.id.rvSolicitudes);


        if ( savedInstanceState != null ) {
            m_Sesion = new Usuario();
            m_Sesion = savedInstanceState.getParcelable("key");
        }
        else{
            m_Sesion = new Usuario();
            m_Sesion = UsuarioDAO.instanciaSesion;
        }

        //m_Sesion = new Usuario();
        //m_Sesion = UsuarioDAO.instanciaSesion;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(m_Sesion.getNombreUsuario());
        toolbar.setTitleTextColor(getResources().getColor(R.color.White));
        toolbar.setLogo(R.drawable.logo_lucas); //Establecer Logo

        btnBuscarCredito.setOnClickListener( btnBuscarCreditosetOnClickListener );
        btnNuevoCredito.setOnClickListener( btnNuevoCreditosetOnClickListener );
        lvCreditos.setOnItemLongClickListener( lvCreditossetOnItemLongClickListener );
        lvCreditos.setOnItemClickListener( lvCreditossetOnItemClickListener );

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Constantes.DOS));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString(FirebaseAnalytics.Param.ORIGIN, "OpenActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //

        BandParamDTO paramDTO = new BandParamDTO();
        paramDTO.setcUsername( "" );
        paramDTO.setcTitular( "" );
        paramDTO.setnCodAge( m_Sesion.getAgencia() );
        paramDTO.setnTamPagina( 100 );
        paramDTO.setnCodPers( m_Sesion.getCodPers() );
        paramDTO.setcNro( m_Sesion.getToken() );

        ConsultaCreditosAsync consultaCreditosAsync = new ConsultaCreditosAsync( ActivityBandejaCreditos.this, paramDTO);
        consultaCreditosAsync.execute();

        setupNavigationView();

        //start
        //Validacion de permisos
        if ( Build.VERSION.SDK_INT < 23 ) {
            //No necesita chequear permisos.
        }
        else
        {
            int permission = ContextCompat.checkSelfPermission( this, Manifest.permission.CALL_PHONE );
            if ( permission != PackageManager.PERMISSION_GRANTED )
            {
                if ( ActivityCompat.shouldShowRequestPermissionRationale( this, android.Manifest.permission.CALL_PHONE ) )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Se requiere permiso para que inicie una llamada telefónica para esta aplicación.")
                            .setTitle("SoyLucas!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ObtenerPermisosParaElMovil();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    Toast.makeText(this, "Se requiere permiso para que inicie una llamada telefónica para esta aplicación.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            else
            {
                m_bPermisos = true;
            }
        }


        //end

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("key", m_Sesion);

    }

    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }

    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.action_buscar:

                //Send Analytics
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Constantes.DOS));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                bundle.putString(FirebaseAnalytics.Param.ORIGIN, btnBuscarCredito.getText().toString());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //

                BandParamDTO paramDTO = new BandParamDTO();
                paramDTO.setcUsername("");
                paramDTO.setcTitular("");
                paramDTO.setnCodAge(m_Sesion.getAgencia());
                paramDTO.setnTamPagina(100);
                paramDTO.setnCodPers(m_Sesion.getCodPers());
                paramDTO.setcNro(m_Sesion.getToken());

                ConsultaCreditosAsync consultaCreditosAsync = new ConsultaCreditosAsync(ActivityBandejaCreditos.this, paramDTO);
                consultaCreditosAsync.execute();

                break;
            case R.id.action_solicitar:

                //Send Analytics
                Bundle bundle2 = new Bundle();
                bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(Constantes.DOS));
                bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS);
                bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                bundle2.putString(FirebaseAnalytics.Param.ORIGIN, btnNuevoCredito.getText().toString());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2);
                //

                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityBandejaCreditos.this);
                dialog.setTitle("Aviso");
                dialog.setMessage("¿Esta seguro de realizar un nuevo Crédito?");
                dialog.setCancelable(false);
                dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        m_SelectCredito = new BandejaCreditos();

                        ConsultaDocumentoAsync consultaDocumentoAsync = new ConsultaDocumentoAsync(ActivityBandejaCreditos.this,
                                m_SelectCredito, true);
                        consultaDocumentoAsync.execute();


                    }
                });
                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                break;
            case R.id.action_llamar:
                // Action to perform when Account Menu item is selected.

                if ( Build.VERSION.SDK_INT < 23 ) {
                    //No necesita chequear permisos.
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    String TelefonoLucas = "tel:" + getString( R.string.numero_soylucas );
                    callIntent.setData( Uri.parse( TelefonoLucas ) );
                    startActivity(callIntent);
                }
                else
                {
                    if ( m_bPermisos ) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String TelefonoLucas = "tel:" + getString(R.string.numero_soylucas);
                        callIntent.setData( Uri.parse( TelefonoLucas ) );
                        if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED ) {
                            return;
                        }
                        startActivity(callIntent);
                    }
                    else
                        ObtenerPermisosParaElMovil();
                }
                break;

        }
    }


    AdapterView.OnItemClickListener lvCreditossetOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            m_SelectCredito = (BandejaCreditos) adapterView.getAdapter().getItem( position );
            registerForContextMenu( adapterView );
            openContextMenu( adapterView );
        }
    };

    View.OnClickListener btnNuevoCreditosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DOS ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnNuevoCredito.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            AlertDialog.Builder dialog
                    = new AlertDialog.Builder( new ContextThemeWrapper( ActivityBandejaCreditos.this, R.style.MyAlertDialogTheme ) );
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Esta seguro de realizar un nuevo Crédito?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    m_SelectCredito = new BandejaCreditos();

                    ConsultaDocumentoAsync consultaDocumentoAsync = new ConsultaDocumentoAsync( ActivityBandejaCreditos.this,
                            m_SelectCredito, true );
                    consultaDocumentoAsync.execute();


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

    View.OnClickListener btnBuscarCreditosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DOS ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnBuscarCredito.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            BandParamDTO paramDTO = new BandParamDTO();
            paramDTO.setcUsername( "" );
            paramDTO.setcTitular( "" );
            paramDTO.setnCodAge( m_Sesion.getAgencia() );
            paramDTO.setnTamPagina( 100 );
            paramDTO.setnCodPers( m_Sesion.getCodPers() );
            paramDTO.setcNro( m_Sesion.getToken() );

            ConsultaCreditosAsync consultaCreditosAsync= new ConsultaCreditosAsync( ActivityBandejaCreditos.this, paramDTO );
            consultaCreditosAsync.execute();

        }
    };

    AdapterView.OnItemLongClickListener lvCreditossetOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

            LayoutInflater factory = LayoutInflater.from( ActivityBandejaCreditos.this );
            final View viewI = factory.inflate(R.layout.image_logo, null);

            AlertDialog.Builder builder = new AlertDialog.Builder( ActivityBandejaCreditos.this );
            builder.setView( viewI )
                    .setCancelable(true)
                    .setNeutralButton("Cerrar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle( "-> " + m_SelectCredito.getcNombreProceso() );
        inflater.inflate( R.menu.menu_action_selected, menu );

        MenuItem detalle = menu.findItem( R.id.item_detalle );
        MenuItem anular = menu.findItem( R.id.item_anular );
        MenuItem continuar = menu.findItem( R.id.item_continuar );
        MenuItem eliminar = menu.findItem( R.id.item_eliminar );
        MenuItem editar = menu.findItem( R.id.item_editar );

        eliminar.setVisible( false );
        editar.setVisible( false );

        if ( m_SelectCredito.getnEstado() == 30 ){
            anular.setVisible( false );
            continuar.setVisible( false );
        }
        else{
            detalle.setVisible( false );
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_continuar:
                CargarFlujo( m_SelectCredito );
                return true;
            case R.id.item_anular:
                AnularCredito( m_SelectCredito );
                return true;
            case R.id.item_cancelar:
                return true;
            case R.id.item_detalle:

                ObtenerDetalleCreditoAsync detalle =
                        new ObtenerDetalleCreditoAsync( ActivityBandejaCreditos.this );
                detalle.execute();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void iAnularFlujo( final BandejaCreditos solicitud, int position ) {


        AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityBandejaCreditos.this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro de anular el Crédito?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                AnularCredito( solicitud );


            }
        });
        dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog.show();


    }

    @Override
    public void iContinuarFlujo( BandejaCreditos solicitud, int position ) {
        //Toast.makeText( this, "continuar ACT RV..."+ solicitud.getcFechaReg(), Toast.LENGTH_SHORT ).show();

        if ( solicitud.getnEstado() >= Constantes.TREINTA )
        {
            m_SelectCredito = new BandejaCreditos();
            m_SelectCredito.setcCliente( solicitud.getcCliente() );
            m_SelectCredito.setnCodAge( solicitud.getnCodAge() );
            m_SelectCredito.setnCodCred( solicitud.getnCodCred() );
            m_SelectCredito.setnIdFlujoMaestro( solicitud.getnIdFlujoMaestro() );

            ObtenerDetalleCreditoAsync detalle =
                    new ObtenerDetalleCreditoAsync( ActivityBandejaCreditos.this );
            detalle.execute();
        }
        else
            CargarFlujo( solicitud );

    }

    public class ObtenerDetalleCreditoAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private List<OperacionesDTO> m_ListaOperaciones;
        private List<CalendarioDTO> m_ListaCalend;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private CalendarioResponse m_CalendarioResponse;

        public ObtenerDetalleCreditoAsync( Context context ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Consultando credito...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_ListaCalend = new ArrayList<CalendarioDTO>();
                m_ListaOperaciones = new ArrayList<OperacionesDTO>();


                //m_ListaCalend = m_webApi.ObtenerCalendarioCreditoLucas( m_SelectCredito.getnCodCred(), m_Sesion.getAgencia(), m_Sesion.getToken() );
                m_CalendarioResponse = m_webApi.ObtenerCalendarioCreditoLucas( m_SelectCredito.getnCodCred(), m_Sesion.getAgencia(), m_Sesion.getToken() );
                if( !m_CalendarioResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;

                    if ( m_CalendarioResponse.getM_listdata() != null ){
                        m_ListaCalend = m_CalendarioResponse.getM_listdata();
                        m_ListaOperaciones = m_webApi.ObtenerOperacionesCreditoLucas( m_SelectCredito.getnCodCred(), m_Sesion.getAgencia(), m_Sesion.getToken() );
                        if ( m_ListaOperaciones != null ){

                            FlujoMaestro tasaInteres = new FlujoMaestro();
                            tasaInteres = m_webApi.obtenerFlujoSolicitud( m_SelectCredito.getnIdFlujoMaestro(), m_Sesion.getToken() );
                            m_SelectCredito.setnPrestamo( tasaInteres.getnTasa() ); //Tasa Interes

                            resp = RESULT_OK;
                        }
                        else
                            resp = RESULT_FALSE;
                    }
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

            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();

                if ( m_ListaCalend.size() > Constantes.CERO )
                {
                    Intent intent = new Intent( m_context, ActivityConsultaCredito.class );
                    intent.putExtra( "ListCalendario", (Serializable) m_ListaCalend ); //lista de calendario
                    intent.putExtra( "ListOperaciones", (Serializable) m_ListaOperaciones ); //lista de operaciones
                    intent.putExtra( "datosClienteContrato", m_SelectCredito.getcCliente().toString() );
                    intent.putExtra( "nCodAge", m_SelectCredito.getnCodAge() );
                    intent.putExtra( "nCodCred", m_SelectCredito.getnCodCred() );
                    intent.putExtra( "nTasa", m_SelectCredito.getnPrestamo() ); //Tasa Interes

                    startActivityForResult(intent, CONSULTA_CREDITO_REQUEST_CODE);
                }
                else{
                    Toast.makeText( m_context, "No hay detalle a mostrar.", Toast.LENGTH_SHORT ).show();
                    pd.dismiss();
                }

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_context, "Hubo un error en anular el credito.", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_CalendarioResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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

                                            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                                            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();

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
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }

        }
    }

    private void  AnularCredito( BandejaCreditos credito ){

        AnularDTO anular = new AnularDTO();
        anular.setnIdFlujoMaestro( credito.getnIdFlujoMaestro() );
        anular.setcComentario( "ANULACION DE CREDITO DESDE APP_MOVIL" );
        anular.setcUser( m_Sesion.getUsuario() );

        AnularCreditoAsync anularCreditoAsync = new AnularCreditoAsync( this, anular );
        anularCreditoAsync.execute();

    }

    public class AnularCreditoAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private AnularDTO m_creditoAnular;
        private String m_mensajeAlerta;
        private FlujoMaestroResponse m_FlujoMaestroResponse;

        public AnularCreditoAsync( Context context, AnularDTO creditoAnular ){
            m_creditoAnular = creditoAnular;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Anulando credito...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }
                int nRespuesta = Constantes.CERO;
                FlujoMaestro flujo_anular = new FlujoMaestro();

                //flujo_anular = m_webApi.obtenerFlujo( m_creditoAnular.getnIdFlujoMaestro(), m_Sesion.getToken() );
                m_FlujoMaestroResponse = m_webApi.obtenerFlujo( m_creditoAnular.getnIdFlujoMaestro(), m_Sesion.getToken() );

                if( !m_FlujoMaestroResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_FlujoMaestroResponse.getM_data() != null ){
                        if ( m_FlujoMaestroResponse.getM_data().getnIdFlujoMaestro() > Constantes.CERO )
                            nRespuesta = m_webApi.AnularCreditoLucas( m_FlujoMaestroResponse.getM_data(), m_Sesion.getToken() );
                        else
                            nRespuesta = Constantes.CERO;

                        if ( nRespuesta > Constantes.CERO )
                            resp = RESULT_OK;
                        else
                            resp = RESULT_FALSE;
                    }
                    /*
                    if ( nRespuesta > Constantes.CERO )
                        resp = RESULT_OK;
                    else
                        resp = RESULT_FALSE;
                    */
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
            if ( mensaje.equals( RESULT_OK ) ) {

                pd.dismiss();
                new BandejaCreditosDAO().eliminarXidFlujoMaestro( m_creditoAnular.getnIdFlujoMaestro() );
                CargarLista();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_context, "Hubo un error en anular el credito.", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_FlujoMaestroResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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


                                            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                                            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();


                                            Intent solicitudFinal = new Intent( ActivityBandejaCreditos.this, ActivityLogin.class );
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
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
    }

    private void CargarFlujo( BandejaCreditos creditos ){

        ObtenerFlujoAsyns obtenerFlujoAsyns =
                new ObtenerFlujoAsyns( ActivityBandejaCreditos.this, creditos.getnIdFlujoMaestro() );
        obtenerFlujoAsyns.execute();

    }

    public class ConsultaDocumentoAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private boolean m_tipoConsulta;
        private PersonaCreditoDTO m_PersonaCreditoDTO;
        private BandejaCreditos m_BandejaCredito;
        private String m_mensajeAlerta;
        private ClienteDTO m_ClienteDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Persona m_Persona;
        private PersonaResponse m_PersonaResponse;

        public ConsultaDocumentoAsync( Context context, BandejaCreditos credito, boolean tipoConsulta ){
            m_tipoConsulta = tipoConsulta;
            m_BandejaCredito = credito;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Consultando datos...");
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
                datos.setnNroDoc( m_Sesion.getRolDescripcion() );
                datos.setcEmail( m_Sesion.getUsuario() );

                m_PersonaResponse =  m_webApi.obtenerPersonaDatos( datos, m_Sesion.getToken() );

                if( !m_PersonaResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    m_Persona = m_PersonaResponse.getM_data();
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
            if ( mensaje.equals(RESULT_OK )) {

                pd.dismiss();

                //PERTENECE A UN CARGO PUBLICO
                if ( m_Persona.getbCargoPublico().equals( "True" ) )
                    Toast.makeText( m_context, "No podemos otorgarte un prestamo por ser persona con cargo público.", Toast.LENGTH_SHORT ).show();
                else{
                    //VALIDACION DE N INTENTOS RECHAZADOS
                    ObtenerRechazoxDiaAsyns obtenerRechazoxDiaAsyns =
                            new ObtenerRechazoxDiaAsyns( m_context, m_Persona );
                    obtenerRechazoxDiaAsyns.execute();
                    //
                }
                //



                //VALIDACION DE N INTENTOS RECHAZADOS
                /*
                ObtenerRechazoxDiaAsyns obtenerRechazoxDiaAsyns =
                        new ObtenerRechazoxDiaAsyns( m_context, m_Persona );
                obtenerRechazoxDiaAsyns.execute();
                */
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
                if ( m_PersonaResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();
                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
    }

    public class ConsultaCreditosAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private CreditoResponse m_CreditoResponse;
        private List<Credito> m_creditos;
        private BandParamDTO m_baBandParamDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

        public ConsultaCreditosAsync( Context context, BandParamDTO paramDTO ){
            m_baBandParamDTO = paramDTO;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Consultando creditos...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;
                if ( !Common.isNetworkConnected( m_context ) ) {
                    return "No se encuentra conectado a internet.";
                }
                //m_creditos = new ArrayList<>();
                m_CreditoResponse = m_webApi.ConsultaCreditos( m_baBandParamDTO, m_Sesion.getToken() );
                if ( !m_CreditoResponse.isM_success() ) {
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_CreditoResponse.getM_listdata() != null ){
                        new BandejaCreditosDAO().LimpiarTabla();
                        if ( m_CreditoResponse.getM_listdata().size() > Constantes.CERO ){
                            for ( Credito creditoDTO : m_CreditoResponse.getM_listdata() ){
                                BandejaCreditos bandeja = new BandejaCreditos();
                                bandeja.setnCodAge( creditoDTO.getnCodAge() );
                                bandeja.setnCodCred( creditoDTO.getnCodCred() );
                                bandeja.setnCodPersTitular( m_Sesion.getCodPers() );
                                bandeja.setnOrdenFlujo( creditoDTO.getnOrdenFlujo() );
                                bandeja.setnIdFlujo( creditoDTO.getnIdFlujo() );
                                bandeja.setnIdFlujoMaestro( creditoDTO.getnIdFlujoMaestro() );
                                bandeja.setnSubProd( creditoDTO.getnSubProd() );
                                bandeja.setnProd( creditoDTO.getnProd() );
                                bandeja.setnPrestamo( creditoDTO.getnPrestamo() );
                                bandeja.setnActibo( creditoDTO.getbActivo() );
                                bandeja.setcCliente( creditoDTO.getcNumeroContrato() + "/" + creditoDTO.getnPrestamo()
                                        + "/" + creditoDTO.getnNroCuotas() + "/" + creditoDTO.getnMontoCuota()
                                        + "/" + creditoDTO.getnTasaComp() ); //DATOS DEL CONTRATO
                                bandeja.setcDocumento( "" );
                                bandeja.setcFechaReg( creditoDTO.getdFechaRegistro() );
                                bandeja.setcEstado( creditoDTO.getcEstado() );
                                String s = creditoDTO.getcFormulario();
                                String[] split = s.split("State");
                                String firstSubString = split[1];
                                bandeja.setcNombreProceso( firstSubString );
                                bandeja.setcNomAge( "" );
                                bandeja.setcSubProducto( creditoDTO.getcSubProd() );
                                bandeja.setcMoneda( creditoDTO.getcMoneda() );
                                bandeja.setnEstado( creditoDTO.getnEstado() );

                                int nresult = 0;
                                nresult = new BandejaCreditosDAO().insertar( bandeja );
                            }
                        }
                    }
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
                CargarLista();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();

                if ( m_CreditoResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }

        }
    }

    protected void CargarLista(){

        arrayListCreditos = new BandejaCreditosDAO().listBandejaCreditos();
        m_AdaptadorLvCreditos = new AdaptadorLvCreditos( this, R.layout.activity_bandeja_creditos, arrayListCreditos );
        lvCreditos.setAdapter( m_AdaptadorLvCreditos );

        //RecyclerView
        /*
        rvSolicitudes.setHasFixedSize(true);
        rvSolicitudes.setLayoutManager( new LinearLayoutManager( ActivityBandejaCreditos.this ) );
        arvListaSolicitudes = new AdaptadorRvSolicitudes( ActivityBandejaCreditos.this ,arrayListCreditos );
        rvSolicitudes.setAdapter( arvListaSolicitudes );
        */
        //

        //Expandable RecyclerView
        List<Title> list = getList( arrayListCreditos );
        RecyclerAdapter adapter = new RecyclerAdapter( this, arrayListCreditos, list, this );
        rvSolicitudes.setLayoutManager( new LinearLayoutManager(this) );
        rvSolicitudes.setAdapter( adapter );
        rvSolicitudes.addItemDecoration( new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) );
        rvSolicitudes.setAdapter( adapter );
        //

    }

    private List<Title> getList( ArrayList<BandejaCreditos> datos ) {
        List<Title> list = new ArrayList<>();

        for (int i = Constantes.CERO; i < datos.size(); i++) {
            List<SubTitle> subTitles = new ArrayList<>();

            for ( int j = Constantes.CERO; j< Constantes.CUATRO; j++ ){

                SubTitle subTitle = new SubTitle("","", null);

                String detalle = datos.get( i ).getcCliente().toString();
                String[] cadena = detalle.split("/");
                String cuotas = "";
                String montocuota = "";

                if ( cadena.length == Constantes.CINCO ){
                    cuotas = cadena[2];
                    montocuota = "S/ " + cadena[3];
                }
                else{
                    cuotas = "0";
                    montocuota = "S/ 0.00";
                }

                if ( j == Constantes.CERO )
                    subTitle = new SubTitle( "Fecha registro", datos.get( i ).getcFechaReg(),
                            datos.get( i ).getnEstado() == Constantes.TREINTA ? getResources().getDrawable(R.drawable.ic_logofecha_verde )
                                    : getResources().getDrawable(R.drawable.ic_logofecha ) );
                if ( j == Constantes.UNO )
                    subTitle = new SubTitle( "Cuotas", cuotas,
                            datos.get( i ).getnEstado() == Constantes.TREINTA ? getResources().getDrawable(R.drawable.ic_logocuota_verde )
                                    : getResources().getDrawable(R.drawable.ic_logocuota ) );
                if ( j == Constantes.DOS )
                    subTitle = new SubTitle( "Monto por cuota", montocuota,
                            datos.get( i ).getnEstado() == Constantes.TREINTA ? getResources().getDrawable(R.drawable.ic_logomonto_verde )
                                    : getResources().getDrawable(R.drawable.ic_logomonto ) );
                if ( j == Constantes.TRES )
                    subTitle = new SubTitle( "Proceso", datos.get( i ).getcNombreProceso(),
                            datos.get( i ).getnEstado() == Constantes.TREINTA ? getResources().getDrawable(R.drawable.ic_logoproceso_verde )
                                    : getResources().getDrawable(R.drawable.ic_logoproceso ) );
                subTitles.add(subTitle);
            }

            String nameFlujo = "continuar";
            if ( datos.get( i ).getnEstado() == Constantes.TREINTA )
                nameFlujo = "detalles";

            Title model = new Title( datos.get( i ).getcEstado(), subTitles, null, nameFlujo,
                    String.valueOf( Utilidades.FormatoMoneda( datos.get( i ).getnPrestamo() ) ) );
            list.add( model );
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bandeja, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_editar_datos:

                //Send Analytics
                Bundle bundle = new Bundle();
                bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DOS ) );
                bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS );
                bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "MenuItem");
                bundle.putString( FirebaseAnalytics.Param.ORIGIN, "Actualización datos" );
                mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
                //

                //mdipas cambiando de actividad
                //Intent editarDatos = new Intent( ActivityBandejaCreditos.this, ActivityPersona.class );
                Intent editarDatos = new Intent( ActivityBandejaCreditos.this, ActivityDatosPersona.class );
                editarDatos.putExtra( "Documento", m_Sesion.getRolDescripcion() ); //Documento
                startActivityForResult( editarDatos, ACTUALIZAR_DATOS_REQUEST_CODE );
                return true;
            case R.id.item_editar_contrasena:

                //Send Analytics
                Bundle bundle1 = new Bundle();
                bundle1.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DOS ) );
                bundle1.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS );
                bundle1.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "MenuItem");
                bundle1.putString( FirebaseAnalytics.Param.ORIGIN, "Cambiar contraseña" );
                mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle1 );
                //

                Intent cambioContrasenia = new Intent( ActivityBandejaCreditos.this, ActivityCambioContrasenia.class );
                cambioContrasenia.putExtra( "Urgente", false ); //Documento_pruebas
                startActivityForResult( cambioContrasenia, CAMBIO_CONTRASENIA_REQUEST_CODE );
                return true;

            case R.id.item_salir_sesion:

                //Send Analytics
                Bundle bundle2 = new Bundle();
                bundle2.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DOS ) );
                bundle2.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DOS );
                bundle2.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "MenuItem");
                bundle2.putString( FirebaseAnalytics.Param.ORIGIN, "Cerrar sesion" );
                mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle2 );
                //

                //detener el servicio de recopilacion de datos.
                AndroidData.clear( this );
                //

                //Eliminamos la Preferencia agregada
                SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                //end

                Intent solicitudFinal = new Intent( ActivityBandejaCreditos.this, ActivityLogin.class );
                startActivity( solicitudFinal );
                finish();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

    public class ObtenerPreEvaluacionAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Persona m_PersonaCreditoDTO;
        private String m_mensajeAlerta;
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
                m_PersonaCreditoDTO.setnCodAge( m_Sesion.getAgencia() );
                m_Respuesta = m_webApi.obtenerPreEvaluacion( m_PersonaCreditoDTO, m_Sesion.getToken() );

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
                String Bancarizado = m_Respuesta.getcResultado().toString();
                String[] cadena = StringUtils.split( Bancarizado,"|" );
                for ( int i = 0; i < cadena.length; i++ ) {
                    if ( i == Constantes.UNO )
                        validar =  ( cadena[i] );
                }

                if ( validar.equals( "BANCARIZADO" ) ){

                    ObtenerEvaluacionAsyns obtenerEvaluacionAsyns =
                            new ObtenerEvaluacionAsyns( m_context, m_PersonaCreditoDTO, false );
                    obtenerEvaluacionAsyns.execute();

                }
                else {

                    if ( m_PersonaCreditoDTO.getnTipoEmp().toString().trim().equals( "1" )
                            && m_PersonaCreditoDTO.getnSitLab().toString().trim().equals( "1" ) ){

                        ObtenerEvaluacionAsyns obtenerEvaluacionAsyns =
                                new ObtenerEvaluacionAsyns( m_context, m_PersonaCreditoDTO, true );
                        obtenerEvaluacionAsyns.execute();
                    }
                    else{

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
        private String m_mensajeAlerta;
        private int m_nIdFlujoMaestro;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private EvaluacionResp m_Respuesta;
        private boolean m_bancarizado;

        public ObtenerEvaluacionAsyns( Context context, Persona oPersona, boolean bBancarizado ){

            m_PersonaCreditoDTO = oPersona;
            m_context = context;
            m_bancarizado = bBancarizado;
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
                m_PersonaCreditoDTO.setnCodAge( m_Sesion.getAgencia() );

                m_Respuesta = m_webApi.obtenerEvaluacion( m_PersonaCreditoDTO, m_Sesion.getToken() );

                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.getnIdFlujoMaestro() > Constantes.CERO ){
                        m_nIdFlujoMaestro = m_Respuesta.getnIdFlujoMaestro();
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
                // RECHAZADO
                pd.dismiss();
                if ( m_Respuesta.getnRechazado() == Constantes.UNO ) {
                    String mensajeSms = "";
                    mensajeSms = Constantes.Mensaje_Rechazado;
                    if ( m_Respuesta.getnPEP() == Constantes.UNO )
                        mensajeSms = Constantes.Mensaje_Rechazado_PEP;

                    Intent rechazo = new Intent( m_context, ActivityRechazoSolicitud.class );
                    rechazo.putExtra( "Telefono", m_PersonaCreditoDTO.getcCelular().toString() );
                    rechazo.putExtra( "Mensaje", mensajeSms );
                    startActivity( rechazo );
                }
                else{
                    if ( !m_bancarizado ) {
                        // enviar a solicitud
                        Intent solicitudFinal = new Intent( m_context, ActivitySimulador.class);
                        solicitudFinal.putExtra( "Solicitud", "OK");
                        solicitudFinal.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                        startActivity(solicitudFinal);
                        finish();
                    }
                    else {
                        //NO bancarizado
                        Intent flujo = new Intent( m_context, ActivityRegistroDocumento.class );
                        flujo.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                        flujo.putExtra( "OrdenFlujo", Constantes.DOS);
                        startActivity(flujo);
                        finish();
                    }
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
                    if ( m_FlujoMaestroResponse.getM_data() != null  ){
                        m_Respuesta = m_FlujoMaestroResponse.getM_data();
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
                //DIRECCIONAR FLUJO
                pd.dismiss();

                if ( m_Respuesta.getnIdFlujo() == 1029 ) {
                    Intent flujo = new Intent( m_Context, ActivityRegistroDocumento.class );
                    flujo.putExtra( "IdFlujoMaestro", m_Respuesta.getnIdFlujoMaestro() );
                    flujo.putExtra( "OrdenFlujo", m_Respuesta.getnOrdenFlujo() );
                    startActivity( flujo );
                    finish();
                }
                else if ( m_Respuesta.getnIdFlujo() == 1030 ){

                    Intent flujo = new Intent( m_Context, ActivitySimulador.class );
                    flujo.putExtra( "Solicitud" , "OK" );
                    flujo.putExtra( "IdFlujoMaestro", m_Respuesta.getnIdFlujoMaestro() );
                    startActivity( flujo );
                    finish();

                }
                else if ( m_Respuesta.getnIdFlujo() == 1032 ){

                    ObtenerDatosPrestamoAsyns prestamoAsyns =
                            new ObtenerDatosPrestamoAsyns( m_Context, m_Sesion.getAgencia(), m_Respuesta.getnCodCred() );
                    prestamoAsyns.execute();

                }
                else if ( m_Respuesta.getnIdFlujo() == 1031 ){

                    Intent flujo = new Intent( m_Context, ActivityDatosContrato.class ); //solicitud
                    flujo.putExtra( "IdFlujoMaestro", m_Respuesta.getnIdFlujoMaestro() );
                    startActivity( flujo );
                    finish();
                }
                else if ( m_Respuesta.getnIdFlujo() == 1033 ){
                    Toast.makeText( m_Context, "¡Crédito Pre - Aprobado!", Toast.LENGTH_SHORT ).show();

                    //Cargamos la bandeja
                    BandParamDTO paramDTO = new BandParamDTO();
                    paramDTO.setcUsername( "" );
                    paramDTO.setcTitular( "" );
                    paramDTO.setnCodAge( m_Sesion.getAgencia() );
                    paramDTO.setnTamPagina( 100 );
                    paramDTO.setnCodPers( m_Sesion.getCodPers() );
                    paramDTO.setcNro( m_Sesion.getToken() );

                    ConsultaCreditosAsync consultaCreditosAsync= new ConsultaCreditosAsync( ActivityBandejaCreditos.this, paramDTO );
                    consultaCreditosAsync.execute();
                    //end


                }
                else {
                    Toast.makeText( m_Context, "No tiene proceso correcto", Toast.LENGTH_SHORT ).show();
                }


            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
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

    public class ObtenerFlujoSolicitudAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_iD;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private FlujoMaestro m_Respuesta;

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
                m_Respuesta = m_webApi.obtenerFlujoSolicitud( m_iD, m_Sesion.getToken() );

                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.getnIdFlujoMaestro() > Constantes.CERO ){
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

                Intent flujo = new Intent( m_Context, ActivityDatosOperacion.class );
                flujo.putExtra( "IdFlujoMaestro", m_Flujo.getnIdFlujoMaestro() );
                flujo.putExtra( "PrestamoSolicitado", m_Respuesta.getnPrestamo() );
                startActivity( flujo );
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

                                            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                                            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();


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

    public class ObtenerRechazoxDiaAsyns extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private int m_respuesta;
        private Persona m_Persona;

        public ObtenerRechazoxDiaAsyns( Context context, Persona oPersona ){
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_Persona = oPersona;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Obteniendo intentos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }

                m_respuesta = m_webApi.obtenerRechazoxDia( m_Persona.getnNroDoc(), m_Sesion.getToken() );
                if ( m_respuesta == Constantes.UNO ){
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
                pd.dismiss();

                //VALIDAR CREDITOS EN PROCESO
                ObtenerCreditoxFlujoAsyns creditoxFlujoAsyns =
                        new ObtenerCreditoxFlujoAsyns( m_Context, m_Persona );
                creditoxFlujoAsyns.execute();


            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText( m_Context, "Te hemos evaluado y has sido rechazado, por favor intentalo nuevamente mañana.", Toast.LENGTH_LONG ).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ObtenerCreditoxFlujoAsyns extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private int m_respuesta;
        private Persona m_Persona;

        public ObtenerCreditoxFlujoAsyns( Context context, Persona oPersona ){
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_Persona = oPersona;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Obteniendo creditos en proceso ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }

                m_respuesta = m_webApi.obtenerCreditoxFlujo( m_Persona.getnNroDoc(), m_Sesion.getToken() );
                if ( m_respuesta == Constantes.UNO ){
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
                pd.dismiss();

                //NUEVO CREDITO
                ObtenerPreEvaluacionAsyns obtenerPreEvaluacionAsyns =
                        new ObtenerPreEvaluacionAsyns( m_Context, m_Persona );
                obtenerPreEvaluacionAsyns.execute();


            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText( m_Context, "Ya tienes un préstamo en proceso!", Toast.LENGTH_LONG ).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    private boolean ObtenerPermisosParaElMovil() {
        m_bPermisos = false;
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.CALL_PHONE )
                != PackageManager.PERMISSION_GRANTED )
        {

            ActivityCompat.requestPermissions( this,new String[] {
                            Manifest.permission.CALL_PHONE }, //Usar el telefono
                    REQUEST_CALL_PHONE );
        }
        else
            m_bPermisos = true;
        return m_bPermisos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == ACTUALIZAR_DATOS_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                Toast.makeText( ActivityBandejaCreditos.this,
                        "¡OK!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText( ActivityBandejaCreditos.this,
                        "¡CANCELADO actualización!", Toast.LENGTH_SHORT).show();
            }
        }
        if ( requestCode == CAMBIO_CONTRASENIA_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                Toast.makeText( ActivityBandejaCreditos.this,
                        "¡OK!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText( ActivityBandejaCreditos.this,
                        "¡CANCELADO cambio!", Toast.LENGTH_SHORT).show();
            }
        }
        if ( requestCode == CONSULTA_CREDITO_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                Toast.makeText( ActivityBandejaCreditos.this,
                        "¡OK!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText( ActivityBandejaCreditos.this,
                        "¡CANCELADO consulta!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if ( back_pressed + 2000 > System.currentTimeMillis() ) {
            finish();
        }
        else
            Toast.makeText(getBaseContext(), "¡Presione una vez más para salir!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();

    }


}
