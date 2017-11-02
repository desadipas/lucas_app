package com.tibox.lucas.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.DataBaseHelper;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.dao.VarnegocioDAO;
import com.tibox.lucas.dao.ZonasDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.entidad.Zonas;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.CalendarioDTO;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.DatosSalida.TokenItems;
import com.tibox.lucas.network.dto.UsuarioDTO;
import com.tibox.lucas.network.dto.Varnegocio;
import com.tibox.lucas.network.dto.ZonasDTO;
import com.tibox.lucas.network.response.VarnegocioResponse;
import com.tibox.lucas.network.response.ZonasResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogin extends AppCompatActivity {
    private EditText etCorreo;
    private EditText etPassword;

    private Button btnAcceder,btnRegistro;
    private Switch swShowPassword;
    private DataBaseHelper dataBaseHelper;

    public final static int REGISTRAR_DATOS_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 101;
    public final static int CAMBIO_CONTRASENIA_URGENTE_REQUEST_CODE = 102;

    //SharedPreferences
    public final static String ARG_AUTENTICACION_JSON = "ARG_AUTENTICACION_JSON";
    String PS_ID;
    String SECRET;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    protected TextInputLayout inputLayoutCorreo;
    protected TextInputLayout inputLayoutPassword;
    private TextView tvRecuperarContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etCorreo = (EditText)findViewById( R.id.etUser );
        etPassword = (EditText)findViewById( R.id.etPassword );
        btnAcceder = (Button)findViewById( R.id.btnAcceder );
        swShowPassword = (Switch) findViewById( R.id.swShowPassword );
        btnRegistro = (Button)findViewById( R.id.btnRegistro );

        inputLayoutCorreo = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        tvRecuperarContraseña = (TextView) findViewById( R.id.tvRecuperarContraseña );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer icono

        btnAcceder.setOnClickListener( btnAccederOnClickListener );
        btnRegistro.setOnClickListener( btnRegistrosetOnClickListener );
        tvRecuperarContraseña.setOnClickListener( tvRecuperarContraseñasetOnClickListener );

        PS_ID = Constantes.PARNET_SCRIPT_ID_LENDDO;
        SECRET = Constantes.API_SECRET;

        swShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if ( !isChecked )
                {
                    etPassword.setTransformationMethod( PasswordTransformationMethod.getInstance() );
                    swShowPassword.setText("MOSTRAR ");
                }
                else
                {
                    etPassword.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
                    swShowPassword.setText("OCULTAR ");

                }
            }
        });

        etCorreo.setText("");

        if ( Build.VERSION.SDK_INT < 23 ) {
            //Do not need to check the permission
        }
        else {
            ObtenerPermisos();
        }

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.UNO ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_UNO );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

        etCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutCorreo.setError(null); // hide error
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutPassword.setError(null); // hide error
            }
        });
    }

    View.OnClickListener tvRecuperarContraseñasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent recuperar = new Intent( ActivityLogin.this, ActivityRecuperarContrasenia.class );
            startActivity( recuperar );

        }
    };

    private void ObtenerPermisos() {

        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int readPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if ( storagePermission != PackageManager.PERMISSION_GRANTED ) {
            listPermissionsNeeded.add( Manifest.permission.READ_EXTERNAL_STORAGE );
        }
        if ( readPhonePermission != PackageManager.PERMISSION_GRANTED ) {
            listPermissionsNeeded.add( Manifest.permission.READ_PHONE_STATE );
        }

        if ( !listPermissionsNeeded.isEmpty() ) {
            ActivityCompat.requestPermissions( this,
                    listPermissionsNeeded.toArray( new String[ listPermissionsNeeded.size() ] ), MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE );
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    View.OnClickListener btnRegistrosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.UNO ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_UNO );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnRegistro.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            Intent registrarDatos = new Intent( ActivityLogin.this, ActivityDatosPersona.class );
            startActivityForResult( registrarDatos, REGISTRAR_DATOS_REQUEST_CODE );

        }
    };

    View.OnClickListener btnAccederOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.UNO ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_UNO );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnAcceder.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            if ( !Utilidades.isValidEmail( etCorreo.getText().toString() ) ){
                etCorreo.requestFocus();
                inputLayoutCorreo.setError("Ingresar su email correctamente.");
                return;
            }

            if ( etPassword.getEditableText().toString().trim().equals("") ) {
                etPassword.requestFocus();
                inputLayoutPassword.setError("Ingresar su contraseña.");
                return;
            }

            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setUsername( String.valueOf( etCorreo.getText().toString() ) );
            usuarioDTO.setPassword( String.valueOf( etPassword.getText().toString() ) );

            ObtenerTokenAsync obtenerTokenAsync = new ObtenerTokenAsync( ActivityLogin.this, usuarioDTO );
            obtenerTokenAsync.execute();

        }
    };

    public class ObtenerAutenticacionAsync extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerAutenticacionAsync ( Context context, UsuarioDTO usuarioDTO ){
            m_UsuarioDTO = usuarioDTO;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Autenticando...");
            pd.setCancelable( false );
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                //Autenticacion dto  = new Autenticacion();
                Persona dto = new Persona();

                //Encriptando
                //m_UsuarioDTO.setPassword( m_webApi.ClaveEncriptada( m_UsuarioDTO.getPassword().toString() ) );
                //end

                dto = m_webApi.obtenerAutenticacion( m_UsuarioDTO );

                if ( dto != null ) {
                    //if ( dto.getnCodUsu() > Constantes.CERO ) {
                    if ( dto.getnCodPers() > Constantes.CERO ) {
                        m_Usuario = new Usuario();

                        m_Usuario.setUsuario( m_UsuarioDTO.getUsername() );
                        //m_Usuario.setClave( m_UsuarioDTO.getPassword() ); //token obtenido.
                        m_Usuario.setClave( m_UsuarioDTO.getPassword() ); //token obtenido.
                        m_Usuario.setRolDescripcion( dto.getnNroDoc() ); //documento del titular
                        m_Usuario.setRol( 0 );
                        m_Usuario.setAgencia( Constantes.CINCO ); //dto.getnCodAge()
                        m_Usuario.setAgenciaNombre( "" );
                        m_Usuario.setCodDepAge( 0 );
                        m_Usuario.setCodUsu( 0 );
                        m_Usuario.setNombreUsuario( dto.getcNombres() );
                        m_Usuario.setCodPers( dto.getnCodPers() );
                        m_Usuario.setToken( m_UsuarioDTO.getPassword() ); //token obtenido.
                        m_Usuario.setTipo( "" );
                        m_Usuario.setIdPais( 0 );
                        m_Usuario.setIdEmpresa( 0 );
                        m_Usuario.setIdTipoEmpresa( dto.getChangePass() ); // dato para cambio de contraseña 0 : cambiar 1:  no cambiar
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
            if (mensaje.equals( RESULT_OK ) ) {
                RegistrarAutenticacion( m_Usuario, pd );
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText(m_context, "Email o contraseña incorrectas.", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private Usuario m_Usuario;
        private UsuarioDTO m_UsuarioDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Persona m_Persona;

    }

    public void RegistrarAutenticacion( Usuario usuario, ProgressDialog pd ){
        int ingresoAutenticacion = 0;
        ingresoAutenticacion = new UsuarioDAO().ingresarAutenticacion( usuario );

        //Guardando en modo privado la sesion nueva
        SharedPreferences.Editor spe = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
        Gson gson = new Gson();
        String json = gson.toJson( usuario );
        spe.putString( ARG_AUTENTICACION_JSON, json );
        spe.commit();
        //end

        if ( ingresoAutenticacion > Constantes.CERO ){
            //onInicioSesionFinalizado( usuario , pd );
            ActualizarCatalogoInterno( pd, Constantes.CERO, usuario );
        }
    }

    protected void onInicioSesionFinalizado( Usuario usuario, ProgressDialog pd ) {
        pd.dismiss();
        etCorreo.setText("");
        etPassword.setText("");
        new UsuarioDAO().iniciarSesion( usuario );
        //Toast.makeText( this, "Inicio de sesión correcto", Toast.LENGTH_SHORT ).show();

        if ( usuario.getIdTipoEmpresa() == Constantes.UNO ){
            Toast.makeText( this, "Bienvenido.", Toast.LENGTH_SHORT ).show();
            Intent i = new Intent( ActivityLogin.this, ActivityBandejaCreditos.class);
            startActivity( i );
            finish();
        }
        else{
            Intent cambioContrasenia = new Intent( ActivityLogin.this, ActivityCambioContrasenia.class );
            cambioContrasenia.putExtra( "Urgente", true );
            startActivity( cambioContrasenia );
            finish();
            //startActivityForResult( cambioContrasenia, CAMBIO_CONTRASENIA_URGENTE_REQUEST_CODE );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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
            m_context = context;
            m_UsuarioDTO = usuario;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Autenticando...");
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

                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setUsername( String.valueOf( etCorreo.getText().toString() ) );
                usuarioDTO.setPassword( String.valueOf( m_TokenItems.getAccess_token() ) ); // token obtenido.

                ObtenerAutenticacionAsync obtenerAutenticacionAsync = new ObtenerAutenticacionAsync( m_context, usuarioDTO );
                obtenerAutenticacionAsync.execute();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText(m_context, "Usuario y/o contraseña invalidos.", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    protected void ActualizarCatalogoInterno( ProgressDialog pd, int nuevaVersion, Usuario usuario){
        try{

            ActualizarCatalogoInternoAsync actualizarCatalogoInternoAsync =
                    new ActualizarCatalogoInternoAsync( ActivityLogin.this, usuario, nuevaVersion, pd );
            actualizarCatalogoInternoAsync.execute();

        }
        catch ( Exception ex )
        {
            Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
        }

    }

    public class ActualizarCatalogoInternoAsync extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Usuario m_Usuario;
        private Context m_context;
        private ProgressDialog pd;
        private AppCreditoswebApi m_webApi;
        private VarnegocioResponse m_VarnegocioResponse;

        public ActualizarCatalogoInternoAsync ( Context context, Usuario usuario , int nuevaVersion, ProgressDialog pdT ){

            m_Usuario = usuario;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            pd = pdT;
        }

        @Override
        protected void onPreExecute() {

            //pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando catalogo interno...");
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Cargando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                //STARt

                Varnegocio varnegocioDTO = new Varnegocio();
                /*
                varnegocioDTO = m_webApi.obtenerValorNegocio( Constantes.VARNEGOCIO_RECHAZOXDIA, m_Usuario.getToken() );
                if ( varnegocioDTO != null ){
                    if ( varnegocioDTO.getcValorVar().length() > Constantes.CERO ){
                        new VarnegocioDAO().insertar( varnegocioDTO );
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    return RESULT_FALSE;
                */
                //END

                //STARt
                /*
                varnegocioDTO = new Varnegocio();
                varnegocioDTO = m_webApi.obtenerValorNegocio( Constantes.VARNEGOCIO_CREDITOSXCLIENTE_FLUJO, m_Usuario.getToken() );
                if ( varnegocioDTO != null ){
                    if ( varnegocioDTO.getcValorVar().length() > Constantes.CERO ){
                        new VarnegocioDAO().insertar( varnegocioDTO );
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    return RESULT_FALSE;
                */
                //END

                //STARt
                /*
                varnegocioDTO = new Varnegocio();
                varnegocioDTO = m_webApi.obtenerValorNegocio( Constantes.VARNEGOCIO_CUOTAMINIMA, m_Usuario.getToken() );
                if ( varnegocioDTO != null ){
                    if ( varnegocioDTO.getcValorVar().length() > Constantes.CERO ){
                        new VarnegocioDAO().insertar( varnegocioDTO );
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    return RESULT_FALSE;
                 */
                //END


                //STARt
                /*
                varnegocioDTO = new Varnegocio();
                //varnegocioDTO = m_webApi.obtenerValorNegocio( Constantes.VARNEGOCIO_FECHASISTEMA, m_Usuario.getToken() );
                m_VarnegocioResponse = m_webApi.obtenerValorNegocio( Constantes.VARNEGOCIO_FECHASISTEMA, m_Usuario.getToken() );
                if( !m_VarnegocioResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_VarnegocioResponse.getM_data() != null ){
                        if ( m_VarnegocioResponse.getM_data().getcValorVar().length() > Constantes.CERO ){
                            new VarnegocioDAO().insertar( m_VarnegocioResponse.getM_data() );
                            resp = RESULT_OK;
                        }
                        else
                            resp = RESULT_FALSE;
                    }
                    else
                        return RESULT_FALSE;
                }
                */
                //END


                //START
                ArrayList<CatalagoCodigos> catalagoSize = new ArrayList<>();
                catalagoSize = new CatalagoCodigosDAO().listCatalagoCodigos();
                if ( catalagoSize.size() <= Constantes.SEIS ) {

                    List<CatalagoCodigosDTO> m_listcatalogo = new ArrayList<>();
                    for (int i = 0; i <= Constantes.SIETE; i++) {
                        m_listcatalogo = new ArrayList<>();
                        int Codigo = 0;
                        if (i == 0)
                            Codigo = Constantes.TIPO_ACT_ECO;
                        else if (i == 1)
                            Codigo = Constantes.TIPO_DIRECCION;
                        else if (i == 2)
                            Codigo = Constantes.TIPO_SEXO;
                        else if (i == 3)
                            Codigo = Constantes.TIPO_ESTADO_CIVIL;
                        else if (i == 4)
                            Codigo = Constantes.TIPO_EST_LABORAL;
                        else if (i == 5)
                            Codigo = Constantes.TIPO_PROFESION;
                        else if (i == 6)
                            Codigo = Constantes.TIPO_EMPLEO;
                        else
                            Codigo = Constantes.TIPO_RESIDENCIA;

                        m_listcatalogo = m_webApi.obtenerCatalago(Codigo, m_Usuario.getToken());
                        if (m_listcatalogo != null) {
                            if (m_listcatalogo.size() > Constantes.CERO) {

                                new CatalagoCodigosDAO().LimpiarCatalogoCodigosxID(Codigo); // eliminando

                                for (CatalagoCodigosDTO dto : m_listcatalogo) {

                                    CatalagoCodigos catalagoCodigos = new CatalagoCodigos();
                                    catalagoCodigos.setcNomCod(dto.getcNomCod());
                                    catalagoCodigos.setnCodigo(Codigo);
                                    catalagoCodigos.setnValor(Integer.valueOf(dto.getnValor()));
                                    new CatalagoCodigosDAO().insertar(catalagoCodigos);
                                }
                                resp = RESULT_OK;
                            } else
                                resp = RESULT_FALSE;
                        } else
                            resp = RESULT_FALSE;
                    }
                }
                else
                    resp = RESULT_OK;
                //END

                //START
                ArrayList<Zonas> zonasSize = new ArrayList<>();
                zonasSize = new ZonasDAO().listaZonas();
                if ( zonasSize.size() == Constantes.CERO ) {

                    ZonasResponse m_ZonasResponse;
                    String CodigoSuperior = "";
                    String CodigoInferior = "";

                    //INSERTANDO PROVINCIAS
                    m_ZonasResponse = m_webApi.obtenerZonas(CodigoInferior, CodigoSuperior, Constantes.UNO, m_Usuario.getToken());
                    if (!m_ZonasResponse.isM_success()) {
                        resp = RESULT_FALSE;
                    } else {
                        resp = RESULT_OK;
                        if (m_ZonasResponse.getM_listdata() != null) {
                            if (m_ZonasResponse.getM_listdata().size() > Constantes.CERO) {

                                new ZonasDAO().LimpiarZonas(); //eliminado zonas

                                for (ZonasDTO departamento : m_ZonasResponse.getM_listdata()) {

                                    //INSERTANDO DEPARTAMENTOS

                                    Zonas zonas = new Zonas();
                                    zonas.setnCodigo(Integer.parseInt(departamento.getCODIGO()));
                                    zonas.setcDescripcion(departamento.getDESCRIPCION());
                                    zonas.setnNivel(Constantes.UNO);
                                    zonas.setnTipo(Constantes.UNO);
                                    new ZonasDAO().insertar(zonas);


                                    CodigoSuperior = String.valueOf(zonas.getnCodigo());
                                    if (zonas.getnCodigo() < Constantes.DIEZ)
                                        CodigoSuperior = "0" + CodigoSuperior;
                                    m_ZonasResponse = m_webApi.obtenerZonas(CodigoInferior, CodigoSuperior, Constantes.DOS, m_Usuario.getToken());

                                    if (m_ZonasResponse.getM_listdata() != null) {
                                        if (m_ZonasResponse.getM_listdata().size() > Constantes.CERO) {
                                            for (ZonasDTO provincia : m_ZonasResponse.getM_listdata()) {

                                                //INSERTANDO PROVINCIAS

                                                zonas = new Zonas();
                                                zonas.setnCodigo(Integer.parseInt(provincia.getCODIGO()));
                                                zonas.setcDescripcion(provincia.getDESCRIPCION());
                                                zonas.setnNivel(Constantes.DOS);
                                                //zonas.setnTipo( Constantes.DOS );
                                                zonas.setnTipo(Integer.parseInt(departamento.getCODIGO()));
                                                new ZonasDAO().insertar(zonas);


                                                CodigoInferior = String.valueOf(zonas.getnCodigo());
                                                if (zonas.getnCodigo() < Constantes.DIEZ)
                                                    CodigoInferior = "0" + CodigoInferior;
                                                m_ZonasResponse = m_webApi.obtenerZonas(CodigoInferior, CodigoSuperior, Constantes.TRES, m_Usuario.getToken());
                                                if (m_ZonasResponse.getM_listdata() != null) {
                                                    if (m_ZonasResponse.getM_listdata().size() > Constantes.CERO) {
                                                        for (ZonasDTO distrito : m_ZonasResponse.getM_listdata()) {

                                                            //INSERTANDO DISTRITOS

                                                            zonas = new Zonas();
                                                            zonas.setnCodigo(Integer.parseInt(distrito.getCODIGO()));
                                                            zonas.setcDescripcion(distrito.getDESCRIPCION());
                                                            zonas.setnNivel(Constantes.TRES);
                                                            //zonas.setnTipo( Constantes.TRES );
                                                            zonas.setnTipo(Integer.parseInt(departamento.getCODIGO()));
                                                            zonas.setnSubTipo(Integer.parseInt(provincia.getCODIGO()));
                                                            new ZonasDAO().insertar(zonas);

                                                        }
                                                    } else
                                                        resp = RESULT_FALSE;
                                                } else
                                                    resp = RESULT_FALSE;


                                            }
                                        } else
                                            resp = RESULT_FALSE;
                                    } else
                                        resp = RESULT_FALSE;


                                    resp = RESULT_OK;
                                }
                            } else
                                resp = RESULT_FALSE;
                        } else
                            resp = RESULT_FALSE;
                    }
                }
                else
                    resp = RESULT_OK;
                //END


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

                //ArrayList<Zonas> listaCreditos = new ArrayList<>();
                //listaCreditos = new ZonasDAO().listaZonas();
                //listaCreditos = new ZonasDAO().listaZonasNivelTipo( 1, 1 );

                onInicioSesionFinalizado( m_Usuario, pd);
            }
            else if (mensaje.equals(RESULT_FALSE)) {
                Toast.makeText(m_context, "No se actualizo data interna!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == REGISTRAR_DATOS_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                Toast.makeText( this, "SoyLucas!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText( this, "Operación Cancelada.", Toast.LENGTH_SHORT).show();
            }
        }
        else if( requestCode == CAMBIO_CONTRASENIA_URGENTE_REQUEST_CODE ){
            if ( resultCode == RESULT_OK ) {

            }
            else{
                Toast.makeText( this, "Operación Cancelada.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
