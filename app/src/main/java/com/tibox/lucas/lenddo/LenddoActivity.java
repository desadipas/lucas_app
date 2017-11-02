package com.tibox.lucas.lenddo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lenddo.data.AndroidData;
import com.lenddo.data.models.ClientOptions;
import com.lenddo.nativeonboarding.GoogleSignInHelper;
import com.tibox.lucas.R;
import com.tibox.lucas.activity.ActivityBandejaCreditos;
import com.tibox.lucas.activity.ActivityLogin;
import com.tibox.lucas.activity.ActivityRechazoSolicitud;
import com.tibox.lucas.activity.ActivityRegistroDocumento;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.BandejaCreditos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.BandParamDTO;
import com.tibox.lucas.network.dto.ClienteDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringFinalResultado;
import com.tibox.lucas.network.dto.DatosSalida.EvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.response.PersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import com.lenddo.sdk.core.LenddoConstants;
import com.lenddo.sdk.core.LenddoEventListener;
import com.lenddo.sdk.models.AuthorizationStatus;
import com.lenddo.sdk.models.FormDataCollector;
import com.lenddo.sdk.utils.UIHelper;
import com.lenddo.sdk.widget.LenddoButton;

import java.io.IOException;

public class LenddoActivity extends AppCompatActivity implements LenddoEventListener {
    private TextView tvApellidos, tvNombres, tvNumeroMovil, tvCorreoElectronico;

    private PersonaCreditoDTO m_PersonaDatos;
    protected Usuario m_Sesion;
    private int m_OrdenFlujo = 0;
    private int m_nIdFlujoMaestro = 0;
    private ELScoringFinalResultado m_ElScoringFinalResultado;
    private String m_ApplicationId = "";

    private static final String TAG = LenddoActivity.class.getName();

    private SharedPreferences permissionStatus;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    String[] permissionsRequired = new String[]{ Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALENDAR };


    private static final int MY_PERMISSIONS_READ_PHONE_STATE = 102;
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 103;
    private static final int MY_PERMISSIONS_READ_SMS = 104;
    private static final int MY_PERMISSIONS_READ_CONTACTS = 105;
    private static final int MY_PERMISSIONS_READ_CALENDAR = 106;

    //LENDDO
    private LenddoButton btnLenddoButton;
    private UIHelper helper;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lenddo);

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        //LENDDO
        btnLenddoButton = (LenddoButton) findViewById(R.id.verifyButton);
        tvApellidos = (TextView) findViewById( R.id.tv_apellidos );
        tvNombres = (TextView) findViewById( R.id.tv_nombres );
        tvNumeroMovil = (TextView) findViewById( R.id.tv_numero_movil );
        tvCorreoElectronico  = (TextView) findViewById( R.id.tv_correo_electronico );

        helper = new UIHelper(this, this);
        helper.addGoogleSignIn(new GoogleSignInHelper());
        helper.customizeBackPopup("Soy Lucas", "¿Está seguro de salir del proceso?", "Si", "No");
        btnLenddoButton.setUiHelper(helper);
        LenddoConstants.AUTHORIZE_DATA_ENDPOINT = Constantes.AUTHORIZE_DATA_ENDPOINT;

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer icono

        m_ElScoringFinalResultado = new ELScoringFinalResultado();
        m_PersonaDatos = new PersonaCreditoDTO();

        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            m_OrdenFlujo = intent.getInt("OrdenFlujo");
            m_PersonaDatos = intent.getParcelable("DatosPersona");
        }

        tvApellidos.setText( m_PersonaDatos.getcApePat().toString().toUpperCase() +" "+ m_PersonaDatos.getcApeMat().toString().toUpperCase() );
        tvNombres.setText( m_PersonaDatos.getcNombres().toString().toUpperCase() );
        tvNumeroMovil.setText( m_PersonaDatos.getcCelular().toString() );
        tvCorreoElectronico.setText( m_PersonaDatos.getcEmail().toString() );

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.TRES ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_TRES );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

    }

    @Override
    public boolean onButtonClicked(FormDataCollector formDataCollector) {
        /*
        String appIDCompleto = "";
        int numeroGenerado = 0;
        String appID = Constantes.APPLICATION_ID;
        numeroGenerado = GenerarCodigoVerificadorParaLenddo();
        appIDCompleto = appID + String.valueOf( numeroGenerado );
        */

        if (Build.VERSION.SDK_INT < 23) {
            //No necesita chequear permisos.
        }
        else {

            if (ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED
                    ) {

                if ( ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[0] )
                        || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[1] )
                        || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[2] )
                        || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[3] )
                        || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[4]) ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LenddoActivity.this);
                    builder.setTitle("Soy Lucas! Necesita permisos múltiples");
                    builder.setMessage(getString(R.string.mensaje_permisos_lenddo_data));
                    builder.setPositiveButton("Concede", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //Peticion de pedidos de permiso del app.
                            //ActivityCompat.requestPermissions( LenddoActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT );
                            ActivityCompat.requestPermissions( LenddoActivity.this, llenarPermisos(), PERMISSION_CALLBACK_CONSTANT );
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                } else if ( permissionStatus.getBoolean( permissionsRequired[0], false ) ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LenddoActivity.this);
                    builder.setTitle("Soy Lucas! Necesita permisos múltiples");
                    builder.setMessage(getString(R.string.mensaje_permisos_lenddo_data));
                    builder.setPositiveButton("Concede", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING );
                            Toast.makeText(getBaseContext(), "Ir a Permisos para conceder", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else {
                    //Peticion de pedidos de permiso del app.
                    ActivityCompat.requestPermissions( LenddoActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT );
                }

                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean( permissionsRequired[0] , true);
                editor.commit();

                return false;

            }
            else{
                //proceedAfterPermission();
            }
        }

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.TRES ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_TRES );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button Lenddo");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnLenddoButton.getText().toString() );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

        
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.enableLogDisplay(true);
        AndroidData.setup( getApplicationContext(), Constantes.PARNET_SCRIPT_ID_LENDDO,
                Constantes.API_SECRET, clientOptions );
        AndroidData.startAndroidData( LenddoActivity.this, m_PersonaDatos.getcClienteLenddo() );


        String partnerscript_id = Constantes.PARNET_SCRIPT_ID_LENDDO; //Pruebas App (PE)
        formDataCollector.collect( this, R.id.formContainer );
        formDataCollector.setApplicationId( m_PersonaDatos.getcClienteLenddo() );
        formDataCollector.setPartnerScriptId( partnerscript_id );
        formDataCollector.setLastName( m_PersonaDatos.getcApePat() );
        formDataCollector.setFirstName( m_PersonaDatos.getcNombres() );
        formDataCollector.setEmail( m_PersonaDatos.getcEmail() );
        formDataCollector.setMobilePhone( m_PersonaDatos.getcCelular() );
        formDataCollector.setDateOfBirth( m_PersonaDatos.getdFechaNacimiento() );
        formDataCollector.validate();
        return true;
    }

    public String[] llenarPermisos(){

        int numPermisos = 0;
        for ( int i = 0 ; i < permissionsRequired.length ; i++ ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[i] ) ) {
                numPermisos = numPermisos + 1;
            }
        }
        String[] permisosPorAutorizar = new String[numPermisos]; // 2

        for ( int i = 0 ; i < permissionsRequired.length ; i++ ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this, permissionsRequired[i] ) ) {

                /*
                for ( int j = 0 ; j < permisosPorAutorizar.length ; j++ ) {
                    permisosPorAutorizar[j] =  permissionsRequired[i];
                }
                */
                permisosPorAutorizar[ numPermisos - 1 ] =  permissionsRequired[i];
                numPermisos = numPermisos - 1;
            }
        }

         /*
        if ( ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                ) {
            permisosPorAutorizar[0] =  Manifest.permission.ACCESS_FINE_LOCATION;//new String[]{ Manifest.permission.ACCESS_FINE_LOCATION };
        }

        if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.READ_SMS ) != PackageManager.PERMISSION_GRANTED
                ) {
            permisosPorAutorizar[1] =  Manifest.permission.READ_SMS;
        }
        if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                ) {
            permisosPorAutorizar[2] =  Manifest.permission.ACCESS_FINE_LOCATION;
        }
        if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED
                ) {
            permisosPorAutorizar[3] =  Manifest.permission.READ_CONTACTS;
        }
        if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED
                ) {
            permisosPorAutorizar[4] =  Manifest.permission.READ_CALENDAR;
        }


        else if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.READ_CONTACTS ) == PackageManager.PERMISSION_GRANTED
                ) {
            permissionsRequired = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALENDAR };
        }
        else if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.READ_CALENDAR ) == PackageManager.PERMISSION_GRANTED
                ) {
            permissionsRequired = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE };
        }
        else if (ActivityCompat.checkSelfPermission( LenddoActivity.this,  Manifest.permission.READ_PHONE_STATE ) == PackageManager.PERMISSION_GRANTED
                ) {
            permissionsRequired = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALENDAR };
        }
        else{
            permissionsRequired = new String[]{ Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALENDAR };
        }
        */
        //end

        return permisosPorAutorizar;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == PERMISSION_CALLBACK_CONSTANT ){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if( grantResults[i] == PackageManager.PERMISSION_GRANTED ){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if( allgranted ){
                proceedAfterPermission();
            } else if( ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale( LenddoActivity.this,permissionsRequired[4])){

                //txtPermissions.setText("Permissions Required");
                AlertDialog.Builder builder = new AlertDialog.Builder( LenddoActivity.this);
                builder.setTitle("Soy Lucas! Necesita permisos múltiples");
                builder.setMessage( getString( R.string.mensaje_permisos_lenddo_data ) );
                builder.setPositiveButton("Concede", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        //Peticion de pedidos de permiso del app.
                        ActivityCompat.requestPermissions( LenddoActivity.this, llenarPermisos(), PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {
                Toast.makeText(getBaseContext(), "No se puede obtener permiso", Toast.LENGTH_SHORT ).show();
            }
        }

    }

    @Override
    public void onAuthorizeComplete(FormDataCollector formDataCollector) {

        PersonaDatosAsync personaDatosAsync =
                new PersonaDatosAsync( LenddoActivity.this );
        personaDatosAsync.execute();

    }

    @Override
    public void onAuthorizeCanceled(FormDataCollector formDataCollector) {

        Intent intent = new Intent( this, ActivityBandejaCreditos.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthorizeError(int statusCode, String rawResponse) {
        Toast.makeText( this, "Error! code: "+statusCode+" response:"+rawResponse, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizeFailure(Throwable throwable) {
        Toast.makeText( this, "Failure: "+throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected int GenerarCodigoVerificadorParaLenddo(){
        int codigo = 0;
        int randomPIN = (int)( Math.random()  * 9000 ) + 1000;
        codigo = randomPIN;
        String val = ""+randomPIN;
        return codigo;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called " + requestCode);
        helper.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == REQUEST_PERMISSION_SETTING ) {
            if ( ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[2]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[3]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[4]) == PackageManager.PERMISSION_GRANTED
                    ) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        //txtPermissions.setText("We've got all permissions");
        Toast.makeText(getBaseContext(), "Tenemos todos los permisos", Toast.LENGTH_SHORT ).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if ( ActivityCompat.checkSelfPermission( LenddoActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[2]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[3]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LenddoActivity.this, permissionsRequired[4]) == PackageManager.PERMISSION_GRANTED
                    ) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class PersonaDatosAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private PersonaResponse m_PersonaResponse;

        public PersonaDatosAsync( Context context ){
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
            if ( mensaje.equals(RESULT_OK )) {
                pd.dismiss();

                ObtenerEvaluacionAsyns obtenerEvaluacionAsyns =
                        new ObtenerEvaluacionAsyns( m_context, m_Persona );
                obtenerEvaluacionAsyns.execute();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_context, m_mensajeAlerta, Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_PersonaResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    AlertDialog.Builder builder = new AlertDialog.Builder( m_context );
                    //builder.setMessage("Se ha denegado la autorización para esta solicitud.")
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

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }

        private boolean m_tipoConsulta;
        private PersonaCreditoDTO m_PersonaCreditoDTO;
        private String m_mensajeAlerta;
        private ClienteDTO m_ClienteDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Persona m_Persona;
    }

    public class ObtenerEvaluacionAsyns extends AsyncTask<Void, Void,String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Persona m_PersonaCreditoDTO;
        private int m_nIdFlujoMaestro;
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
                m_PersonaCreditoDTO.setnCodAge( m_Sesion.getAgencia() );
                m_PersonaCreditoDTO.setnProducto( Constantes.PRODUCTO_SCORING );
                m_PersonaCreditoDTO.setcLenddo( m_PersonaDatos.getcClienteLenddo() );

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

                    Intent flujo = new Intent( m_context, ActivityRegistroDocumento.class );

                    flujo.putExtra("IdFlujoMaestro", m_nIdFlujoMaestro);
                    flujo.putExtra("OrdenFlujo", Constantes.DOS);
                    startActivity(flujo);
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
                alerta.setcTitulo( "Hola SoyLucas, tenemos malas noticias." );

                EnvioEmailAsyns envioEmailAsyns = new EnvioEmailAsyns( m_Context, alerta );
                envioEmailAsyns.execute();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText( m_Context, "¡Error de envío!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_RedResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    AlertDialog.Builder builder = new AlertDialog.Builder( m_Context );
                    //builder.setMessage("Se ha denegado la autorización para esta solicitud.")
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

    @Override
    public void onBackPressed() {
        /*
        if (helper.onBackPressed()) {
            super.onBackPressed();
        }
        */

        if (helper.onBackPressed()) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Esta seguro que desea salir del flujo?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent solicitudFinal = new Intent(LenddoActivity.this, ActivityBandejaCreditos.class);
                    startActivity(solicitudFinal);
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
}
