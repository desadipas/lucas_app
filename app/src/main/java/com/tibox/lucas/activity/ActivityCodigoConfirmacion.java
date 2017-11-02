package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.dao.ZonasDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.entidad.Zonas;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.User;
import com.tibox.lucas.network.dto.DatosSalida.CodPersona;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.UsuarioDTO;
import com.tibox.lucas.network.dto.Varnegocio;
import com.tibox.lucas.network.dto.ZonasDTO;
import com.tibox.lucas.network.response.CodPersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.VarnegocioResponse;
import com.tibox.lucas.network.response.ZonasResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityCodigoConfirmacion extends AppCompatActivity {

    private Button btnCambiarContrasenia, btnAtras;
    private EditText etCodigoConfirmacion;
    private TextInputLayout inputLayoutCodigoConfirmacion;

    private User m_UserForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_confirmacion);

        btnAtras = (Button) findViewById( R.id.btn_atras );
        btnCambiarContrasenia = (Button) findViewById( R.id.btn_cambiar_contrasenia );
        etCodigoConfirmacion = (EditText) findViewById( R.id.et_codigo_confirmacion );
        inputLayoutCodigoConfirmacion = (TextInputLayout) findViewById( R.id.input_layout_codigo_confirmacion);

        btnAtras.setOnClickListener( btnAtrassetOnClickListener );
        btnCambiarContrasenia.setOnClickListener( btnCambiarContraseniasetOnClickListener );

        Bundle intent = getIntent().getExtras();
        m_UserForm = new User();
        if ( intent != null) {
            m_UserForm = (User) intent.getSerializable( "Datos" );
        }

        etCodigoConfirmacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutCodigoConfirmacion.setError(null); // hide error
            }
        });

    }

    View.OnClickListener btnAtrassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setResult( RESULT_CANCELED );
            finish();
        }
    };

    View.OnClickListener btnCambiarContraseniasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // validacion codigo de movil
            int nCodigoVerifiacion = 0;
            if ( etCodigoConfirmacion.getText().toString().trim().equals( "" ) || etCodigoConfirmacion.getText().length() < Constantes.CUATRO ){
                //Toast.makeText( ActivityCodigoConfirmacion.this, "¡Ingresar el codigo correctamente!", Toast.LENGTH_SHORT ).show();
                inputLayoutCodigoConfirmacion.setError( "Ingresar el codigo correctamente." );
                return;
            }
            nCodigoVerifiacion = Integer.parseInt( etCodigoConfirmacion.getText().toString() );

            if ( nCodigoVerifiacion != m_UserForm.getnCodPers() ){
                //Toast.makeText( ActivityCodigoConfirmacion.this, "¡Código errado!", Toast.LENGTH_SHORT ).show();
                inputLayoutCodigoConfirmacion.setError( "Código errado." );
                return;
            }
            //


            User userdatos = new User();
            userdatos.setEmail( m_UserForm.getEmail() );
            userdatos.setPassword( m_UserForm.getPassword() );

            //CAMBIAR CONTRASEÑA
            ActualizarContraseniaAsyns contraseniaAsyns = new ActualizarContraseniaAsyns( ActivityCodigoConfirmacion.this, userdatos );
            contraseniaAsyns.execute();
            //

        }
    };

    public class ActualizarContraseniaAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private User m_datos;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private String m_mensajeAlerta;
        private CodPersonaResponse m_CodPersonaResponse;

        public ActualizarContraseniaAsyns( Context context, User datos ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_datos = datos;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando contraseña ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;
                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                CodPersona respuesta = new CodPersona();

                m_CodPersonaResponse = m_webApi.CambioPassword( m_datos, m_UserForm.getLastName() );
                if( !m_CodPersonaResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_CodPersonaResponse.getM_data() != null ){
                        if ( m_CodPersonaResponse.getM_data().getnCodPers() > Constantes.CERO ){
                            resp = RESULT_OK;
                        }
                        else{
                            resp = RESULT_FALSE;
                        }
                    }
                    else{
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
            if( mensaje.equals( RESULT_OK ) ){
                pd.dismiss();

                Alerta alerta = new Alerta();
                alerta.setcMovil( m_UserForm.getcMovil() );
                alerta.setcTexto( "Hola SoyLucas, tu contraseña ya fue cambiada!" );
                alerta.setcEmail( "" );
                alerta.setcTitulo( "Lucas" );

                EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( m_context, alerta );
                envioSMSAsyns.execute();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                pd.dismiss();
                if ( m_CodPersonaResponse.getM_response().equals( Constantes.No_Autorizado ) ){
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

    public class EnvioSMSAsyns extends AsyncTask<Void, Void,String> {
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

                m_RedResponse = m_webApi.EnvioSms( m_Alerta, m_UserForm.getLastName() );
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
                //Toast.makeText( m_Context, "Mensaje enviado.", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                //Vamos a la pantalla de registro de confirmacion
                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setUsername( String.valueOf( m_UserForm.getEmail() ));
                usuarioDTO.setPassword( String.valueOf( m_UserForm.getLastName() ));
                //usuarioDTO.setPassword( String.valueOf( m_UserForm.getPassword() ));

                ObtenerAutenticacionAsync obtenerAutenticacionAsync = new ObtenerAutenticacionAsync( m_Context, usuarioDTO );
                obtenerAutenticacionAsync.execute();
                //

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();

                if ( m_RedResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
                Persona dto = new Persona();
                dto = m_webApi.obtenerAutenticacion( m_UsuarioDTO );
                if ( dto != null ) {
                    if ( dto.getnCodPers() > Constantes.CERO ) {

                        m_Usuario = new Usuario();
                        m_Usuario.setUsuario( m_UsuarioDTO.getUsername() );
                        m_Usuario.setClave( m_UsuarioDTO.getPassword() );
                        m_Usuario.setRolDescripcion( dto.getnNroDoc() ); //DOCUMENTO
                        m_Usuario.setRol( 0 );
                        m_Usuario.setAgencia( Constantes.CINCO ); //dto.getnCodAge()
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
            if (mensaje.equals( RESULT_OK ) ) {
                RegistrarAutenticacion( m_Usuario, pd );
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText(m_context, "Datos incorrectos.", Toast.LENGTH_SHORT).show();
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

    }

    public void RegistrarAutenticacion( Usuario usuario, ProgressDialog pd ){
        int ingresoAutenticacion = 0;
        ingresoAutenticacion = new UsuarioDAO().ingresarAutenticacion( usuario );

        //Guardando en modo privado la sesion nueva
        SharedPreferences.Editor spe = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
        Gson gson = new Gson();
        String json = gson.toJson(usuario);
        spe.putString( ActivityLogin.ARG_AUTENTICACION_JSON, json );
        spe.commit();
        //end

        if ( ingresoAutenticacion > Constantes.CERO ){
            ActualizarCatalogoInterno( pd, Constantes.CERO, usuario );
        }


    }

    protected void ActualizarCatalogoInterno( ProgressDialog pd, int nuevaVersion, Usuario usuario){
        try
        {
            ActualizarCatalogoInternoAsync actualizarCatalogoInternoAsync =
                    new ActualizarCatalogoInternoAsync( ActivityCodigoConfirmacion.this, usuario, nuevaVersion, pd );
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
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando catalogo interno...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String resp = "" ;
                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
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

    protected void onInicioSesionFinalizado( Usuario usuario, ProgressDialog pd ) {
        pd.dismiss();
        new UsuarioDAO().iniciarSesion(usuario);
        Toast.makeText( this, "Bienvenido.", Toast.LENGTH_SHORT ).show();
        Intent i = new Intent( ActivityCodigoConfirmacion.this, ActivityBandejaCreditos.class);
        startActivity( i );
        finish();
    }

}
