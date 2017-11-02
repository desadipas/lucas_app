package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.CreditoGarantiaDTO;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringFinalResultado;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityRegistroEstadoCuenta extends AppCompatActivity {
    Spinner spEnvio;
    Button btnAceptarEnvio;
    protected Usuario m_Sesion;

    private ELScoringFinalResultado m_ElScoringFinalResultado;
    private int m_nIdFlujoMaestro = 0;
    private int m_nCodCred = 0;
    private PersonaCreditoDTO m_PersonaDatos;
    private double m_PrestamoObtenido = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_estado_cuenta);

        btnAceptarEnvio = (Button) findViewById( R.id.btnAceptarEnvio );
        spEnvio = (Spinner) findViewById( R.id.spEnvio );

        btnAceptarEnvio.setOnClickListener( btnAceptarEnviosetOnClickListener );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;
        m_ElScoringFinalResultado = new ELScoringFinalResultado();
        m_PersonaDatos = new PersonaCreditoDTO();

        Intent intent = getIntent();
        if (intent != null) {
            if ( intent.getExtras().getParcelable("DatosScoringFinal") != null ) {
                m_ElScoringFinalResultado = intent.getExtras().getParcelable("DatosScoringFinal");
                m_nIdFlujoMaestro = intent.getExtras().getInt( "IdFlujoMaestro" );
                m_nCodCred = intent.getExtras().getInt( "nCodCred" );
            }
            if ( intent.getExtras().getParcelable("DatosPersona") != null ) {
                m_PersonaDatos = intent.getExtras().getParcelable("DatosPersona");
                m_PrestamoObtenido = intent.getExtras().getDouble( "PrestamoObtenido" );
            }
        }

        ConsultaCatalogoAsync consultaCatalogoAsync = new ConsultaCatalogoAsync( ActivityRegistroEstadoCuenta.this, Constantes.TIPO_ENVIO );
        consultaCatalogoAsync.execute();

    }

    View.OnClickListener btnAceptarEnviosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //REGISTRAR ESTADO DE CUENTA

            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityRegistroEstadoCuenta.this );
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Esta seguro que desea realizar el proceso?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    int IdEnvio = 0;
                    IdEnvio = (int) spEnvio.getSelectedItemId();

                    RegistrarEstadoCuentaAsync registrarEstadoCuentaAsync =
                            new RegistrarEstadoCuentaAsync( ActivityRegistroEstadoCuenta.this, IdEnvio );
                    registrarEstadoCuentaAsync.execute();
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

    public class RegistrarEstadoCuentaAsync extends AsyncTask<Void, Void, String >{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarEstadoCuentaAsync( Context context, int IdSpinner ){
            m_IdSpinner = IdSpinner;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando estado de cuenta...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                CreditoGarantiaDTO envioCredito = new CreditoGarantiaDTO();

                envioCredito.setnCodCred( m_nCodCred );
                envioCredito.setnIdFlujoMaestro( m_nIdFlujoMaestro );
                envioCredito.setnIdFlujo( 25 );
                envioCredito.setcNomForm( "StateEstadoCuenta" );
                envioCredito.setnCodAge( m_Sesion.getAgencia() );
                envioCredito.setcUsuReg( m_Sesion.getUsuario() );
                envioCredito.setnCodPersReg( m_Sesion.getCodPers() );
                envioCredito.setnTipoEnvio( m_IdSpinner );
                envioCredito.setnProd( Constantes.TRES );
                envioCredito.setnSubProd( Constantes.SIETE );
                envioCredito.setnMoneda( Constantes.UNO );
                envioCredito.setnOrdenFlujo( Constantes.TRES );

                ResponseSolicitudDTO retorno = new ResponseSolicitudDTO();
                retorno = m_webApi.RegistrarEstadoCuentaFlujo( envioCredito );

                if ( retorno != null ){
                    if ( retorno.getnRespuesta() > 0 ){
                        m_nRespuesta = retorno.getnRespuesta();
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
            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();

                Intent pase = new Intent( ActivityRegistroEstadoCuenta.this, ActivityRegistroDocumento.class );
                pase.putExtra( "DatosScoringFinal", m_ElScoringFinalResultado );
                pase.putExtra( "IdFlujoMaestro",m_nIdFlujoMaestro );
                pase.putExtra( "nCodCred", m_nCodCred );
                pase.putExtra( "DatosPersona", m_PersonaDatos );
                pase.putExtra( "PrestamoObtenido", m_PrestamoObtenido );
                startActivity( pase );
                finish();

            }
            else if (mensaje.equals( RESULT_FALSE )) {
                pd.dismiss();
                Toast.makeText( m_context, "Error en registrar estado cuenta", Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }

        private int m_IdSpinner;
        private int m_nRespuesta;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    protected int ObteneridSP(){
        int id = 0;
        try{
            id = (int) spEnvio.getSelectedItemId();
        }
        catch ( Exception e ){
            id = 0;
        }
        return id;
    }

    public class ConsultaCatalogoAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ConsultaCatalogoAsync(Context context, int Codigo ){
            m_codigo = Codigo;
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

                    m_listcatalogo = m_webApi.obtenerCatalago( m_codigo, m_Sesion.getToken() );
                    if ( m_listcatalogo != null ) {
                        if ( m_listcatalogo.size() > 0 ) {
                            new CatalagoCodigosDAO().LimpiarCatalogoCodigosxID( m_codigo );
                            for ( CatalagoCodigosDTO dto : m_listcatalogo ) {
                                CatalagoCodigos catalagoCodigos = new CatalagoCodigos();
                                catalagoCodigos.setcNomCod( dto.getcNomCod() );
                                catalagoCodigos.setnCodigo( m_codigo );
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
                new CatalagoCodigosDAO().rellenaSpinner( spEnvio, m_codigo, ActivityRegistroEstadoCuenta.this );
                pd.dismiss();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
            }
            else{
                pd.dismiss();
            }
        }
        private int m_codigo;
        private List<CatalagoCodigosDTO> m_listcatalogo;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent solicitudFinal = new Intent( ActivityRegistroEstadoCuenta.this, ActivityBandejaCreditos.class );
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
