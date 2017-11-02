package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.activity.dialogfragment.MyDialogFragConfirmacionNumero;
import com.tibox.lucas.activity.interfaces.OnClickMyDialogConfirmacionNumero;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.User;
import com.tibox.lucas.network.dto.DatosSalida.CodPersona;
import com.tibox.lucas.network.dto.DatosSalida.Texto;
import com.tibox.lucas.network.dto.RespuestaDTO;
import com.tibox.lucas.network.dto.UsuarioLucasDTO;
import com.tibox.lucas.network.dto.ValidadorDTO;
import com.tibox.lucas.network.response.CodPersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.UserResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityCambioContrasenia extends AppCompatActivity {
    private EditText etContraseniaActual, etContraseniaNueva, etContraseniaNuevaRep;
    private Button btnCambiar, btnCancelar;

    protected Usuario m_Sesion;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private User m_UserForm;
    private String m_PasswordDesencriptado;

    private EditText etPINVerificacion;
    private Button btnSolicitarCodigo;
    private int m_nCodVerificacionMovil = 0;
    private LinearLayout lyCodigoVerificadorNumero;

    protected TextInputLayout m_input_layout_password_actual;
    protected TextInputLayout m_input_layout_password_nueva;
    protected TextInputLayout m_input_layout_password_repetir;

    private Toolbar toolbar;
    private String m_Numero_Celular = "";
    private boolean m_Urgente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasenia);

        etContraseniaActual = (EditText) findViewById( R.id.et_contrasenia_actual );
        etContraseniaNueva = (EditText) findViewById( R.id.et_contrasenia_nueva );
        etContraseniaNuevaRep = (EditText) findViewById( R.id.et_contrasenia_nueva_rep );

        btnCambiar = (Button) findViewById( R.id.btn_cambiar );
        btnCancelar = (Button) findViewById( R.id.btn_cancelar );

        btnSolicitarCodigo = (Button) findViewById( R.id.btnSolicitarCodigo );
        etPINVerificacion = (EditText) findViewById( R.id.etPINVerificacion );
        btnSolicitarCodigo.setOnClickListener( btnSolicitarCodigosetOnClickListener );
        lyCodigoVerificadorNumero = (LinearLayout) findViewById( R.id.lyCodigoVerificadorNumero );

        m_input_layout_password_actual = (TextInputLayout) findViewById(R.id.input_layout_password_actual);
        m_input_layout_password_nueva = (TextInputLayout) findViewById(R.id.input_layout_password_nueva);
        m_input_layout_password_repetir = (TextInputLayout) findViewById(R.id.input_layout_password_repetir);

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion; // mdipas por mientras

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer icono

        btnCambiar.setOnClickListener( btnCambiarsetOnClickListener );
        btnCancelar.setOnClickListener( btnCancelarsetOnClickListener );

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DIEZ ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DIEZ );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

        User datos = new User();
        datos.setEmail( m_Sesion.getUsuario() );

        VerificarEmailAsyns verificarEmailAsyns =
                new VerificarEmailAsyns( ActivityCambioContrasenia.this, datos );
        verificarEmailAsyns.execute();

        etContraseniaActual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                m_input_layout_password_actual.setError(null); // hide error
            }
        });

        etContraseniaNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                m_input_layout_password_nueva.setError(null); // hide error
            }
        });

        etContraseniaNuevaRep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                m_input_layout_password_repetir.setError(null); // hide error
            }
        });


        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            m_Urgente = intent.getBoolean( "Urgente" );
        }

    }

    View.OnClickListener btnSolicitarCodigosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Bundle bundleNumero = new Bundle();
            bundleNumero.putString("NumeroObtenido", m_UserForm.getcMovil() );
            FragmentManager fm = getSupportFragmentManager();
            MyDialogFragConfirmacionNumero myDialogFrag = new MyDialogFragConfirmacionNumero();
            myDialogFrag.setArguments( bundleNumero );
            myDialogFrag.setCancelable( true );
            myDialogFrag.show(fm,"Diag");
            myDialogFrag.setOnClickEnviarListener(new OnClickMyDialogConfirmacionNumero() {
                @Override
                public void onClickEnviarSms( String numeroConfirmado ) {
                    m_Numero_Celular = numeroConfirmado;

                    if ( m_nCodVerificacionMovil == Constantes.CERO ){
                        ObtenerVerificacion();
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityCambioContrasenia.this );
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

        }
    };

    public ValidadorDTO ValidarContrasenia( String newPassword ){
        ValidadorDTO validar = new ValidadorDTO();
        validar.setbValor( false );
        validar.setcMensaje( "" );

        if( newPassword.equals( "" ) ){
            validar.setbValor( true );
            validar.setcMensaje( "Ingresar su contraseña." );
            return validar;
        }
        else if ( newPassword.length() < Constantes.SIETE  || newPassword.length() > Constantes.QUINCE  ) {
            validar.setbValor( true );
            validar.setcMensaje("Su contraseña debe de tener de 7 a 15 carácteres.");
            return validar;
        }
        /*else ( !Utilidades.isValidPasswordSpecialCharacters( newPassword.toString() ) ){
            validar.setbValor( true );
            validar.setcMensaje( "La contraseña nueva debe contener letra mayúscula, numero y/o caracter especial (@#$%^&+=!) " );
            return validar;
        }
        */

        return validar;
    }

    View.OnClickListener btnCambiarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DIEZ ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DIEZ );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnCambiar.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            ValidadorDTO validar = new ValidadorDTO();
            validar = ValidarContrasenia( etContraseniaNueva.getText().toString() );

            if ( etContraseniaActual.getText().toString().trim().equals( "" ) || etContraseniaActual.getText().length() == Constantes.CERO ){
                //Toast.makeText( ActivityCambioContrasenia.this, "Ingresar contraseña actual", Toast.LENGTH_SHORT).show();
                m_input_layout_password_actual.setError("Ingresar contraseña actual");
                return;
            }
            else if( !etContraseniaActual.getText().toString().equals( m_PasswordDesencriptado ) ){
                //Toast.makeText( ActivityCambioContrasenia.this, "Su contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
                m_input_layout_password_actual.setError("Su contraseña actual es incorrecta");
                return;
            }
            else if ( etContraseniaNueva.getText().toString().equals( m_PasswordDesencriptado ) ){
                //Toast.makeText( ActivityCambioContrasenia.this, "La contraseña nueva no puede ser la misma que una contraseña utilizada previamente.", Toast.LENGTH_SHORT).show();
                m_input_layout_password_nueva.setError("La contraseña nueva no puede ser la misma que una contraseña utilizada previamente.");
                return;
            }
            else if ( validar.isbValor() ){
                //Toast.makeText( ActivityCambioContrasenia.this, validar.getcMensaje(), Toast.LENGTH_SHORT).show();
                m_input_layout_password_nueva.setError( validar.getcMensaje() );
                return;
            }
            else if ( !etContraseniaNuevaRep.getText().toString().equals( etContraseniaNueva.getText().toString() ) ){
                //Toast.makeText( ActivityCambioContrasenia.this, "No coincide la contraseña repetida", Toast.LENGTH_SHORT).show();
                m_input_layout_password_repetir.setError("No coincide la contraseña repetida");
                return;
            }

            // validacion codigo de movil
            int nCodigoVerifiacion = 0;
            if ( etPINVerificacion.getText().toString().trim().equals( "" )
                    || etPINVerificacion.getText().length() < Constantes.CUATRO ){
                Toast.makeText( ActivityCambioContrasenia.this, "Ingresar el codigo correctamente.", Toast.LENGTH_SHORT ).show();
                return;
            }

            nCodigoVerifiacion = Integer.parseInt( etPINVerificacion.getText().toString() );

            if ( nCodigoVerifiacion != m_nCodVerificacionMovil ){
                Toast.makeText( ActivityCambioContrasenia.this, "Código errado.", Toast.LENGTH_SHORT ).show();
                return;
            }
            //


            User userdatos = new User();
            userdatos.setEmail( m_Sesion.getUsuario() );
            userdatos.setPassword( etContraseniaNueva.getText().toString() );

            //CAMBIAR CONTRASEÑA
            ActualizarContraseniaAsyns contraseniaAsyns =
                    new ActualizarContraseniaAsyns( ActivityCambioContrasenia.this, userdatos );
            contraseniaAsyns.execute();


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

        public ActualizarContraseniaAsyns(Context context, User datos ){
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
                //respuesta = m_webApi.CambioPassword( m_datos, m_Sesion.getToken() );

                m_CodPersonaResponse = m_webApi.CambioPassword( m_datos, m_Sesion.getToken() );
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

                /*
                if ( respuesta != null ){
                    if ( respuesta.getnCodPers() > Constantes.CERO ){
                        resp = RESULT_OK;
                    }
                    else{
                        resp = RESULT_FALSE;
                    }
                }
                else{
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
            if( mensaje.equals( RESULT_OK ) ){
                pd.dismiss();

                Alerta alerta = new Alerta();
                alerta.setcMovil( m_UserForm.getcMovil() );
                //alerta.setcMovil( "962956971" ); //mdipas pruebas de movil
                alerta.setcTexto( "Hola SoyLucas, tu contraseña ya fue cambiada!" );
                alerta.setcEmail( "" );
                alerta.setcTitulo( "Lucas" );

                EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( m_context, alerta, true );
                envioSMSAsyns.execute();



            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                //Toast.makeText( m_context, "Error de envio", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_CodPersonaResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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

    View.OnClickListener btnCancelarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.DIEZ ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_DIEZ );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnCancelar.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            if ( m_Urgente ){
                //Eliminamos la Preferencia agregada
                SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                //end

                Intent login = new Intent( ActivityCambioContrasenia.this, ActivityLogin.class );
                startActivity( login );
                finish();
            }
            else
            {
                setResult( RESULT_CANCELED );
                finish();
            }

        }
    };

    public class VerificarEmailAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private User m_User;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private UserResponse m_UserResponse;

        public VerificarEmailAsyns( Context context, User user ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_User = user;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Verificando email ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;
                if (!Common.isNetworkConnected( m_context ) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_UserResponse = m_webApi.VerificarEmail( m_User, m_Sesion.getToken() );
                if( !m_UserResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_UserResponse.getM_data() != null ){
                        m_UserForm = m_UserResponse.getM_data();
                        if ( m_UserResponse.getM_data().getId() > Constantes.CERO ){
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


                /*
                if ( m_UserForm != null ){
                    if ( m_UserForm.getId() > Constantes.CERO ){
                        resp = RESULT_OK;
                    }
                    else{
                        resp = RESULT_FALSE;
                    }
                }
                else{
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
            if( mensaje.equals( RESULT_OK ) ){
                pd.dismiss();

                DesencriptarAsyns desencriptarAsyns =
                        new DesencriptarAsyns( m_context, m_UserForm );
                desencriptarAsyns.execute();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                //Toast.makeText( m_context, "Error de verificación", Toast.LENGTH_SHORT ).show();
                pd.dismiss();

                if ( m_UserResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
    }

    public class DesencriptarAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private User m_User;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

        public DesencriptarAsyns( Context context, User user ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_User = user;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Desencriptar pass ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;
                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                Texto texto = new Texto();
                texto = m_webApi.Desencriptar( m_User, m_Sesion.getToken() );

                if ( texto != null ){
                    if ( !texto.getcTexto().toString().equals( "" ) ){
                        resp = RESULT_OK;
                        m_PasswordDesencriptado = texto.getcTexto().toString();
                    }
                    else{
                        resp = RESULT_FALSE;
                    }
                }
                else{
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
            if( mensaje.equals( RESULT_OK ) ){
                pd.dismiss();
            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText( m_context, "Error de verificación", Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
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
        private boolean m_TipoEnvio;
        private RedResponse m_RedResponse;

        public EnvioSMSAsyns( Context context, Alerta alerta, boolean tipoEnvio ){
            m_TipoEnvio = tipoEnvio;
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
                //Toast.makeText( m_Context, "Su contraseña fue cambiada!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_TipoEnvio ) {
                    //Toast.makeText( m_Context, "Su contraseña fue cambiada.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent( m_Context, ActivityBandejaCreditos.class );
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText( m_Context, "Mensaje enviado.", Toast.LENGTH_SHORT).show();

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

    protected void ObtenerVerificacion(){

        m_nCodVerificacionMovil = GenerarCodigoVerificador();

        Alerta alerta = new Alerta();
        alerta.setcMovil( m_Numero_Celular );
        alerta.setcTexto( "Hola SoyLucas, tu código es " + m_nCodVerificacionMovil );
        alerta.setcEmail( "" );
        alerta.setcTitulo( "Lucas" );

        EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( ActivityCambioContrasenia.this, alerta, false );
        envioSMSAsyns.execute();
    }

    protected int GenerarCodigoVerificador(){
        int codigo = 0;
        int randomPIN = (int)( Math.random()  * 9000 ) + 1000;
        codigo = randomPIN;
        String val = ""+randomPIN;
        return codigo;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if ( m_Urgente ){
                    //Eliminamos la Preferencia agregada
                    SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                    settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                    //end

                    Intent login = new Intent( ActivityCambioContrasenia.this, ActivityLogin.class );
                    startActivity( login );
                    finish();
                }
                else{
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
