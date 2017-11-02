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
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.ELDocumento;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoRCCDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreLenddoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringDemograficoDatos;
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
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.PersonaRespDTO;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.IOException;
import java.util.Calendar;

public class ActivityAvisoLenddo extends AppCompatActivity {

    Button btnCerrar;

    protected Usuario m_Sesion;
    private PersonaCreditoDTO m_PersonaDatos;
    private int m_Year_Persona;
    private int m_nCodPers = 0;
    private int m_nIdFlujoMaestro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_lenddo);

        btnCerrar = (Button)findViewById( R.id.btnCerrar );
        btnCerrar.setOnClickListener( btnCerrarsetOnClickListener );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;
        m_PersonaDatos = new PersonaCreditoDTO();

        Intent intent = getIntent();
        if (intent != null) {
            if ( intent.getExtras().getParcelable("DatosPersona") != null ) {
                m_PersonaDatos = intent.getExtras().getParcelable("DatosPersona");
                m_Year_Persona = intent.getExtras().getInt("AnioPersona");

            }
        }

    }

    View.OnClickListener btnCerrarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityAvisoLenddo.this );
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Esta seguro de cerrar esta ventana?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    ScoreBuroAsyns scoreBuroAsyns = new ScoreBuroAsyns( ActivityAvisoLenddo.this, m_PersonaDatos );
                    scoreBuroAsyns.execute();

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

    public class ScoreBuroAsyns extends AsyncTask<Void,Void,String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ScoreBuroAsyns(Context context, PersonaCreditoDTO oPersona ){

            m_PersonaCredito = oPersona;
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
            try{

                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                m_elScoreBuroResultado = new ELScoreBuroResultado();
                m_elMoraComercialResultado = new ELMoraComercialResultado();
                m_elIngresoPredecidoDemograficoResultado = new ELIngresoPredecidoDemograficoResultado();
                m_elIngresoPredecidoRCCResultado = new ELIngresoPredecidoRCCResultado();
                m_elIngresoPredecidoResultado = new ELIngresoPredecidoResultado();
                m_elScoringBuroResultado = new ELScoringBuroResultado();
                m_elScoringBuroCuotaUtilizadaResultado = new ELScoringBuroCuotaUtilizadaResultado();
                m_elScoringDemograficoResultado = new ELScoringDemograficoResultado();
                m_elScoreDemograficoResultado = new ELScoreDemograficoResultado();
                m_elScoreLenddoResultado = new ELScoreLenddoResultado();

                ELScoreLenddoDatos elScoreLenddoDatos = new ELScoreLenddoDatos();
                ELIngresoPredecidoRCCDatos elIngresoPredecidoRCCDatos = new ELIngresoPredecidoRCCDatos();
                ELIngresoPredecidoDemograficoDatos elIngresoPredecidoDemograficoDatos = new ELIngresoPredecidoDemograficoDatos();
                ELDocumento documento = new ELDocumento();
                ELDocumento documentoCY = new ELDocumento();
                ELScoreBuroDatos elScoreBuroDatos = new ELScoreBuroDatos();
                Calendar calendar = Calendar.getInstance();
                ELScoringBuroDatos elScoringBuroDatos = new ELScoringBuroDatos();
                ELScoreDemograficoDatos elScoreDemograficoDatos = new ELScoreDemograficoDatos();
                ELScoringDemograficoDatos elScoringDemograficoDatos = new ELScoringDemograficoDatos();

                m_ElScoringFinalResultado = new ELScoringFinalResultado();

                //Datos
                elScoreBuroDatos.sNroDoc = m_PersonaCredito.getnNroDoc();
                documento.sNroDoc = m_PersonaCredito.getnNroDoc();
                int Anio = calendar.get( Calendar.YEAR );
                elIngresoPredecidoDemograficoDatos.nEdad = Anio - m_Year_Persona;
                elIngresoPredecidoDemograficoDatos.nEstadoCivil = m_PersonaCredito.getnEstadoCivil();
                elIngresoPredecidoDemograficoDatos.nGenero = m_PersonaCredito.getnSexo();
                elIngresoPredecidoDemograficoDatos.nConstante = 0;
                elIngresoPredecidoRCCDatos.nEstadoCivil =  m_PersonaCredito.getnEstadoCivil();

                elScoreDemograficoDatos.nEdad = Anio - m_Year_Persona;
                elScoreDemograficoDatos.nCondicionLaboral = m_PersonaCredito.getnSitLab();
                elScoreDemograficoDatos.nDepartamento = m_PersonaCredito.getcCodZona();
                elScoreDemograficoDatos.nEstadoCivil = m_PersonaCredito.getnEstadoCivil();
                elScoreDemograficoDatos.nVivienda = m_PersonaCredito.getnTipoResidencia();
                elScoreDemograficoDatos.nGenero = m_PersonaCredito.getnSexo();
                //

                m_elMoraComercialResultado = m_webApi.ScoreMoraComercial( documento );

                m_elIngresoPredecidoDemograficoResultado = m_webApi.IngresoPredecidoDemografico( elIngresoPredecidoDemograficoDatos  );
                if ( m_elIngresoPredecidoDemograficoResultado != null ){
                    m_elIngresoPredecidoRCCResultado = m_webApi.IngresoPredecidoRCC( elIngresoPredecidoRCCDatos, documento, documentoCY );
                    if ( m_elIngresoPredecidoRCCResultado != null ){
                        m_elIngresoPredecidoResultado = m_webApi.IngresoPredecidoFinal( elIngresoPredecidoDemograficoDatos, elIngresoPredecidoRCCDatos,
                                documento, m_elIngresoPredecidoDemograficoResultado.nIngresoPredecidoDemografico3,
                                m_elIngresoPredecidoRCCResultado.nIngresoPredecidoRCC3   );
                    }
                }

                m_elScoreBuroResultado = m_webApi.ScoreResultado( elScoreBuroDatos );
                if ( m_elScoreBuroResultado != null  ){
                    if ( m_elScoreBuroResultado.nScoreBuro > 0 ){  // BANCARIZADO
                        m_elScoringBuroCuotaUtilizadaResultado = m_webApi.ScoringBuroCuotaUtil( documento, documentoCY );
                        elScoringBuroDatos.nIngresoPredecido = m_elIngresoPredecidoResultado.nIngresoPredecidoFinal1;
                        elScoringBuroDatos.nCuotaUtilizada = m_elScoringBuroCuotaUtilizadaResultado.nCuotaUtilizada;
                        elScoringBuroDatos.nMoraComercial = m_elMoraComercialResultado.nMoraComercial;
                        elScoringBuroDatos.nScore = m_elScoreBuroResultado.nScoreBuro;

                        m_elScoringBuroResultado = m_webApi.ScoringBuroResultado( elScoringBuroDatos, documento );

                        m_ElScoringFinalResultado.setnDecicion( m_elScoringBuroResultado.nDecicion );
                        m_ElScoringFinalResultado.setnCuotaMaxima( m_elScoringBuroResultado.nCuotaMaxima );
                        m_ElScoringFinalResultado.setnCuotaUtilizada( m_elScoringBuroResultado.nCuotaUtilizada );
                        m_ElScoringFinalResultado.setnCuotaDisponible( m_elScoringBuroResultado.nCuotaDisponible );
                        m_ElScoringFinalResultado.setnPrestamo1( m_elScoringBuroResultado.nPrestamo1 );
                        m_ElScoringFinalResultado.setnPrestamo2( m_elScoringBuroResultado.nPrestamo2 );
                        m_ElScoringFinalResultado.setnPrestamo3( m_elScoringBuroResultado.nPrestamo3 );
                        m_ElScoringFinalResultado.setnPrestamo4( m_elScoringBuroResultado.nPrestamo4 );
                        m_ElScoringFinalResultado.setnTasa( m_elScoringBuroResultado.nTasa );
                        m_ElScoringFinalResultado.setnPlazo( m_elScoringBuroResultado.nPlazo );
                        m_ElScoringFinalResultado.setnPrestamoMinimo( m_elScoringBuroResultado.nPrestamoMinimo );
                        m_ElScoringFinalResultado.setnPrestamoMaximo( m_elScoringBuroResultado.nPrestamoMaximo );
                        m_ElScoringFinalResultado.setnPorcGarantiaAvaluo( m_elScoringBuroResultado.nPorcGarantiaAvaluo );
                        m_ElScoringFinalResultado.setnRCI( m_elScoringBuroResultado.nRCI );
                        m_ElScoringFinalResultado.setnRMA( m_elScoringBuroResultado.nRMA );
                        m_ElScoringFinalResultado.setnCategoria( m_elScoringBuroResultado.nCategoria );
                        m_ElScoringFinalResultado.setsDescripcionRechazo( m_elScoringBuroResultado.sDescripcionRechazo );
                        m_ElScoringFinalResultado.setnPorcentajeInicial( m_elScoringBuroResultado.nPorcentajeInicial );
                        m_ElScoringFinalResultado.setbError( m_elScoringBuroResultado.bError );
                        m_ElScoringFinalResultado.setsMensajeError( m_elScoringBuroResultado.sMensajeError );


                    }
                    else{   //NO BANCARIZADO
                        m_elScoreLenddoResultado = m_webApi.ScoreLendo( documento );
                        elScoreDemograficoDatos.nMoraComercial = m_elMoraComercialResultado.nMoraComercial;
                        elScoreDemograficoDatos.nIngresoSalarial = m_elIngresoPredecidoResultado.nIngresoPredecidoFinal1;
                        m_elScoreDemograficoResultado = m_webApi.ScoreDemografico( elScoreDemograficoDatos, documento );
                        elScoringDemograficoDatos.nIngresoPredecido = m_elIngresoPredecidoResultado.nIngresoPredecidoFinal1;
                        elScoringDemograficoDatos.nMoraComercial = m_elMoraComercialResultado.nMoraComercial;
                        elScoringDemograficoDatos.nScore = m_elScoreDemograficoResultado.nScoreDemografico;
                        elScoringDemograficoDatos.nScoreOtros = m_elScoreLenddoResultado.nScore;
                        m_elScoringDemograficoResultado = m_webApi.ScoringDemografico( elScoringDemograficoDatos, documento );

                        m_ElScoringFinalResultado.setnDecicion( m_elScoringDemograficoResultado.nDecicion );
                        m_ElScoringFinalResultado.setnCuotaMaxima( m_elScoringDemograficoResultado.nCuotaMaxima );
                        m_ElScoringFinalResultado.setnCuotaUtilizada( m_elScoringDemograficoResultado.nCuotaUtilizada );
                        m_ElScoringFinalResultado.setnCuotaDisponible( m_elScoringDemograficoResultado.nCuotaDisponible );
                        m_ElScoringFinalResultado.setnPrestamo1( m_elScoringDemograficoResultado.nPrestamo1 );
                        m_ElScoringFinalResultado.setnPrestamo2( m_elScoringDemograficoResultado.nPrestamo2 );
                        m_ElScoringFinalResultado.setnPrestamo3( m_elScoringDemograficoResultado.nPrestamo3 );
                        m_ElScoringFinalResultado.setnPrestamo4( m_elScoringDemograficoResultado.nPrestamo4 );
                        m_ElScoringFinalResultado.setnTasa( m_elScoringDemograficoResultado.nTasa );
                        m_ElScoringFinalResultado.setnPlazo( m_elScoringDemograficoResultado.nPlazo );
                        m_ElScoringFinalResultado.setnPrestamoMinimo( m_elScoringDemograficoResultado.nPrestamoMinimo );
                        m_ElScoringFinalResultado.setnPrestamoMaximo( m_elScoringDemograficoResultado.nPrestamoMaximo );
                        m_ElScoringFinalResultado.setnPorcGarantiaAvaluo( m_elScoringDemograficoResultado.nPorcGarantiaAvaluo );
                        m_ElScoringFinalResultado.setnRCI( m_elScoringDemograficoResultado.nRCI );
                        m_ElScoringFinalResultado.setnRMA( m_elScoringDemograficoResultado.nRMA );
                        m_ElScoringFinalResultado.setnCategoria( m_elScoringDemograficoResultado.nCategoria );
                        m_ElScoringFinalResultado.setsDescripcionRechazo( m_elScoringDemograficoResultado.sDescripcionRechazo );
                        m_ElScoringFinalResultado.setnPorcentajeInicial( m_elScoringDemograficoResultado.nPorcentajeInicial );
                        m_ElScoringFinalResultado.setbError( m_elScoringDemograficoResultado.bError );
                        m_ElScoringFinalResultado.setsMensajeError( m_elScoringDemograficoResultado.sMensajeError );

                    }
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

                m_PersonaCredito.setLenddoResultado( m_elScoreLenddoResultado );
                m_PersonaCredito.setIngresoPredecidoDemograficoResultado( m_elIngresoPredecidoDemograficoResultado );
                m_PersonaCredito.setIngresoPredecidoRCCResultado( m_elIngresoPredecidoRCCResultado );
                m_PersonaCredito.setIngresoPredecidoResultado( m_elIngresoPredecidoResultado );
                m_PersonaCredito.setScoringBuroCuotaUtilizadaResultado( m_elScoringBuroCuotaUtilizadaResultado );
                m_PersonaCredito.setScoringBuroResultado( m_elScoringBuroResultado );
                m_PersonaCredito.setScoreDemograficoResultado( m_elScoreDemograficoResultado );
                m_PersonaCredito.setScoringDemograficoResultado( m_elScoringDemograficoResultado );
                m_PersonaCredito.setScoreBuroResultado( m_elScoreBuroResultado );
                m_PersonaCredito.setMoraComercialResultado( m_elMoraComercialResultado );

                pd.dismiss();

                //RegistrarPersonaAsyns registrarPersonaAsyns = new RegistrarPersonaAsyns( ActivityAvisoLenddo.this, m_PersonaCredito, m_ElScoringFinalResultado );
                //registrarPersonaAsyns.execute();

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                Toast.makeText(m_context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private ELScoringFinalResultado m_ElScoringFinalResultado;
        private ELScoreLenddoResultado m_elScoreLenddoResultado;
        private ELIngresoPredecidoDemograficoResultado m_elIngresoPredecidoDemograficoResultado;
        private ELIngresoPredecidoRCCResultado m_elIngresoPredecidoRCCResultado;
        private ELIngresoPredecidoResultado m_elIngresoPredecidoResultado;
        private ELScoringBuroCuotaUtilizadaResultado m_elScoringBuroCuotaUtilizadaResultado;
        private ELScoringBuroResultado m_elScoringBuroResultado;
        private ELScoreDemograficoResultado m_elScoreDemograficoResultado;
        private ELScoringDemograficoResultado m_elScoringDemograficoResultado;
        private ELScoreBuroResultado m_elScoreBuroResultado;
        private ELMoraComercialResultado m_elMoraComercialResultado;

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private PersonaCreditoDTO m_PersonaCredito;
    }

    /*
    public class RegistrarPersonaAsyns extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarPersonaAsyns(Context context, PersonaCreditoDTO personaCreditoDTO, ELScoringFinalResultado scoringFinalResultado ){

            m_ElScoringFinalResultado = scoringFinalResultado;
            m_PersonaCreditoDTO = personaCreditoDTO;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Evaluando persona ...");
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
                Persona persona = new Persona(); //******************************************
                dto = m_webApi.registrarPersonaCredito( persona, m_Sesion.getToken() );

                if ( dto != null ){
                    if ( dto.getnIdFlujoMaestro() > Constantes.CERO ){ //Solo registra persona Y  REGISTRA EL SCORING.
                        m_nCodPers = dto.getnCodPers();
                        m_nIdFlujoMaestro = dto.getnIdFlujoMaestro();
                        resp = RESULT_OK;
                    }
                    else {
                        m_mensajeAlerta = "¡Error en el proceso!";
                        resp = RESULT_FALSE;
                    }
                }
                else
                {m_mensajeAlerta = "¡Error en el proceso!";
                    resp = RESULT_FALSE;}

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

                if (  m_ElScoringFinalResultado.getnDecicion() == Constantes.DOS ){
                    //BLOQUEADO
                    AlertDialog.Builder builder = new AlertDialog.Builder( ActivityAvisoLenddo.this );
                    builder.setMessage("" + m_ElScoringFinalResultado.getsDescripcionRechazo() )
                            .setTitle("Cliente Rechazado!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();

                                            Intent i = new Intent( ActivityAvisoLenddo.this, ActivitySimulador.class);
                                            startActivity( i );
                                            finish();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {

                    PersonaCreditoDTO datosPersona = new PersonaCreditoDTO();

                    datosPersona.setcNombres( m_PersonaDatos.getcNombres() );
                    datosPersona.setnNroDoc( m_PersonaDatos.getnNroDoc() );
                    datosPersona.setcApePat( m_PersonaDatos.getcApePat() );
                    datosPersona.setcApeMat( m_PersonaDatos.getcApeMat() );
                    datosPersona.setcEmail( m_PersonaDatos.getcEmail() );
                    datosPersona.setdFechaNacimiento( m_PersonaDatos.getdFechaNacimiento() );
                    datosPersona.setcDirValor1( m_PersonaDatos.getcDirValor1() );
                    datosPersona.setcTelefono( m_PersonaDatos.getcTelefono() );

                    Intent enviarLendo = new Intent( ActivityAvisoLenddo.this, ActivityRegistroGarantias.class );
                    enviarLendo.putExtra( "DatosScoringFinal", m_ElScoringFinalResultado );
                    enviarLendo.putExtra( "IdFlujoMaestro",m_nIdFlujoMaestro );
                    enviarLendo.putExtra( "DatosPersona", datosPersona );
                    startActivity(enviarLendo);
                    finish();
                }
            }
            else if (mensaje.equals(RESULT_FALSE)) {
                Toast.makeText(m_context, m_mensajeAlerta, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private ELScoringFinalResultado m_ElScoringFinalResultado;
        private String m_mensajeAlerta;
        private PersonaCreditoDTO m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }
    */

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent solicitudFinal = new Intent( ActivityAvisoLenddo.this, ActivityBandejaCreditos.class );
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
