package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.activity.dialogfragment.MyDialogFragConfirmacionNumero;
import com.tibox.lucas.activity.interfaces.OnClickMyDialogConfirmacionNumero;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.User;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.DatosSalida.TokenItems;
import com.tibox.lucas.network.dto.UsuarioDTO;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.UserResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.io.IOException;
import java.io.Serializable;

public class ActivityRecuperarContrasenia extends AppCompatActivity {

    private EditText etCorreoRecuperar,etNuevaContraseniaRecuperar,etConfirmarContraseniaRecuperar, etPinSeguridad;
    private TextInputLayout inputLayoutCorreoRecuperar,inputLayoutNuevaContraseniaRecuperar,inputLayoutConfirmarContraseniaRecuperar;
    private Button btnContinuar, btnAtras, btnSolicitarCodigoPin;

    private FlujoMaestro m_Flujo;
    private Toolbar toolbar;
    private int m_nCodVerificacion = 0;
    private String m_Access_token = "";
    private String m_NumeroInternoTitular = "";
    private User m_UserForm;

    public final static int CONFIRMACION_CODIGO_REQUEST_CODE = 701;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenia);

        etCorreoRecuperar = (EditText) findViewById( R.id.et_correo_recuperar );
        etNuevaContraseniaRecuperar = (EditText) findViewById( R.id.et_nueva_contrasenia_recuperar );
        etConfirmarContraseniaRecuperar = (EditText) findViewById( R.id.et_confirmar_contrasenia_recuperar );

        inputLayoutCorreoRecuperar = (TextInputLayout) findViewById( R.id.input_layout_correo_recuperar );
        inputLayoutNuevaContraseniaRecuperar = (TextInputLayout) findViewById( R.id.input_layout_nueva_contrasenia_recuperar );
        inputLayoutConfirmarContraseniaRecuperar = (TextInputLayout) findViewById( R.id.input_layout_confirmar_contrasenia_recuperar );

        btnContinuar = (Button) findViewById( R.id.btn_continuar );
        btnAtras = (Button) findViewById( R.id.btn_atras );
        btnSolicitarCodigoPin = (Button) findViewById( R.id.btn_solicitar_codigo );
        etPinSeguridad = (EditText) findViewById( R.id.et_pin_seguridad );

        btnContinuar.setOnClickListener( btnContinuarsetOnClickListener );
        btnAtras.setOnClickListener( btnAtrassetOnClickListener );
        btnSolicitarCodigoPin.setOnClickListener( btnSolicitarCodigoPinsetOnClickListener );

        etCorreoRecuperar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutCorreoRecuperar.setError(null); // hide error
            }
        });
        etNuevaContraseniaRecuperar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutNuevaContraseniaRecuperar.setError(null); // hide error
            }
        });
        etConfirmarContraseniaRecuperar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputLayoutConfirmarContraseniaRecuperar.setError(null); // hide error
            }
        });

    }

    View.OnClickListener btnSolicitarCodigoPinsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

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
                    //m_Numero_Celular = numeroConfirmado;

                    if ( m_nCodVerificacion == Constantes.CERO ){
                        ObtenerVerificacion();
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityRecuperarContrasenia.this );
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

    View.OnClickListener btnContinuarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if ( !Utilidades.isValidEmail( etCorreoRecuperar.getText().toString() ) ){
                etCorreoRecuperar.requestFocus();
                inputLayoutCorreoRecuperar.setError("Ingresar su email correctamente.");
                return;
            }
            if ( etNuevaContraseniaRecuperar.getText().toString().trim().equals("") ) {
                etNuevaContraseniaRecuperar.requestFocus();
                inputLayoutNuevaContraseniaRecuperar.setError("Ingresar su contraseña.");
                return;
            }
            if ( etNuevaContraseniaRecuperar.getText().length() < Constantes.SIETE  || etNuevaContraseniaRecuperar.getText().length() > Constantes.QUINCE  ) {
                etNuevaContraseniaRecuperar.requestFocus();
                inputLayoutNuevaContraseniaRecuperar.setError("Su contraseña debe de tener de 7 a 15 carácteres.");
                return;
            }
            /*
            if (!Utilidades.isValidPasswordSpecialCharacters( etNuevaContraseniaRecuperar.getText().toString() )){
                etNuevaContraseniaRecuperar.requestFocus();
                inputLayoutNuevaContraseniaRecuperar.setError("La contraseña nueva debe contener letra mayúscula, numero y/o caracter especial (@#$%^&+=!) ");
                return;
            }
            */

            /*
            (
              (?=.*\d)		#   must contains one digit from 0-9
              (?=.*[a-z])		#   must contains one lowercase characters
              (?=.*[A-Z])		#   must contains one uppercase characters
              (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
                          .		#     match anything with previous condition checking
                            {6,20}	#        length at least 6 characters and maximum of 20
            )
            */


            if ( etConfirmarContraseniaRecuperar.getText().toString().trim().equals("") ) {
                etConfirmarContraseniaRecuperar.requestFocus();
                inputLayoutConfirmarContraseniaRecuperar.setError("Ingresar su contraseña confirmada.");
                return;
            }

            if ( !etConfirmarContraseniaRecuperar.getText().toString().trim().equals( etNuevaContraseniaRecuperar.getText().toString() ) ) {
                etConfirmarContraseniaRecuperar.requestFocus();
                inputLayoutConfirmarContraseniaRecuperar.setError("Contraseña confirmada no coincide.");
                return;
            }

            // obtenemos el token de autorizacion
            UsuarioDTO usuarioTibox = new UsuarioDTO();
            usuarioTibox.setUsername( "tibox@tibox.com.pe" );
            usuarioTibox.setPassword( "TiboxWebApi" );

            ObtenerTokenAsync obtenerTokenAsync = new ObtenerTokenAsync( ActivityRecuperarContrasenia.this, usuarioTibox  );
            obtenerTokenAsync.execute();
            //



        }
    };

    View.OnClickListener btnAtrassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setResult( RESULT_CANCELED );
            finish();
        }
    };

    protected void ObtenerVerificacion(){

        m_nCodVerificacion = GenerarCodigoVerificador();

        Alerta alerta = new Alerta();
        //alerta.setcMovil( m_Numero_Celular );
        alerta.setcTexto( "Hola SoyLucas, tu código para el cambio de contraseña es " + m_nCodVerificacion );
        alerta.setcEmail( "" );
        alerta.setcTitulo( "Lucas" );

        EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( ActivityRecuperarContrasenia.this, alerta );
        envioSMSAsyns.execute();

    }

    protected int GenerarCodigoVerificador(){
        int codigo = 0;
        int randomPIN = (int)( Math.random()  * 9000 ) + 1000;
        codigo = randomPIN;
        String val = ""+randomPIN;
        return codigo;
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
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Recuperando datos...");
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


                // Verificando el email ingresado.
                User verificar = new User();
                verificar.setEmail( etCorreoRecuperar.getText().toString() );

                VerificarEmailAsyns verificarEmailAsyns = new VerificarEmailAsyns( m_context, verificar );
                verificarEmailAsyns.execute();
                //


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

                m_UserResponse = m_webApi.VerificarEmail( m_User, m_Access_token );
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

                //enviamos el codigo por sms
                m_nCodVerificacion = GenerarCodigoVerificador();
                m_NumeroInternoTitular = m_UserForm.getcMovil();

                Alerta smsCodigo = new Alerta();
                smsCodigo.setcMovil( m_NumeroInternoTitular );
                smsCodigo.setcTexto( "Hola SoyLucas, tu código para el cambio de contraseña es " + m_nCodVerificacion );
                smsCodigo.setcTitulo( "Lucas" );

                EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( ActivityRecuperarContrasenia.this, smsCodigo );
                envioSMSAsyns.execute();
                //


            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                pd.dismiss();

                if ( m_UserResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Usted no se encuentra registrado.", Toast.LENGTH_SHORT ).show();
                    //Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
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

                m_RedResponse = m_webApi.EnvioSms( m_Alerta, m_Access_token );
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
                m_UserForm = new User();
                m_UserForm.setEmail( etCorreoRecuperar.getText().toString() );
                m_UserForm.setPassword( etNuevaContraseniaRecuperar.getText().toString() );
                m_UserForm.setLastName( m_Access_token );
                m_UserForm.setnCodPers( m_nCodVerificacion );
                m_UserForm.setcMovil( m_NumeroInternoTitular );

                Intent intent = new Intent( m_Context, ActivityCodigoConfirmacion.class );
                intent.putExtra( "Datos", (Serializable) m_UserForm );
                startActivityForResult(intent, CONFIRMACION_CODIGO_REQUEST_CODE );
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CONFIRMACION_CODIGO_REQUEST_CODE ) {
            if ( resultCode == RESULT_OK ) {
                //Toast.makeText( this, "SoyLucas!", Toast.LENGTH_SHORT).show();
            }
            else{
                //Toast.makeText( this, "Operación Cancelada.", Toast.LENGTH_SHORT).show();
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
                setResult( RESULT_CANCELED );
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
