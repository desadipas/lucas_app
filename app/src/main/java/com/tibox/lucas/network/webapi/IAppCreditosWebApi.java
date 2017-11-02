package com.tibox.lucas.network.webapi;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.tibox.lucas.network.dto.AnularDTO;
import com.tibox.lucas.network.dto.Autenticacion;
import com.tibox.lucas.network.dto.BandCreditoDTO;
import com.tibox.lucas.network.dto.BandParamDTO;
import com.tibox.lucas.network.dto.CO_DocumentosDTO;
import com.tibox.lucas.network.dto.CalendarioDTO;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.ClienteDTO;
import com.tibox.lucas.network.dto.CreditoFlujoDTO;
import com.tibox.lucas.network.dto.CreditoGarantiaDTO;
import com.tibox.lucas.network.dto.DatoFirmaCuentaDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.Documento;
import com.tibox.lucas.network.dto.DatosEntrada.ELDocumento;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoCliente;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoRCCDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.MontoCuota;
import com.tibox.lucas.network.dto.DatosEntrada.Reporte;
import com.tibox.lucas.network.dto.DatosEntrada.User;
import com.tibox.lucas.network.dto.DatosObligatorioDTO;
import com.tibox.lucas.network.dto.DatosSalida.Calendario;
import com.tibox.lucas.network.dto.DatosSalida.CodPersona;
import com.tibox.lucas.network.dto.DatosSalida.Credito;
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
import com.tibox.lucas.network.dto.DatosSalida.EvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.DatosSalida.PreEvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.ResultadoEnvio;
import com.tibox.lucas.network.dto.DatosSalida.Texto;
import com.tibox.lucas.network.dto.DatosSalida.TokenItems;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;
import com.tibox.lucas.network.dto.ElectrodomesticoDTO;
import com.tibox.lucas.network.dto.FlujoDTO;
import com.tibox.lucas.network.dto.FlujoLucasDTO;
import com.tibox.lucas.network.dto.OperacionesDTO;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.PersonaRespDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.dto.RespuestaDTO;
import com.tibox.lucas.network.dto.SeguimientoDTO;
import com.tibox.lucas.network.dto.SolicitudDTO;
import com.tibox.lucas.network.dto.UsuarioDTO;
import com.tibox.lucas.network.dto.UsuarioLucasDTO;
import com.tibox.lucas.network.dto.Varnegocio;
import com.tibox.lucas.network.dto.ZonasDTO;
import com.tibox.lucas.network.response.CalendarioCreditoResponse;
import com.tibox.lucas.network.response.CalendarioListCreditoResponse;
import com.tibox.lucas.network.response.CalendarioResponse;
import com.tibox.lucas.network.response.CodPersonaResponse;
import com.tibox.lucas.network.response.CreditoResponse;
import com.tibox.lucas.network.response.FileResponse;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.PersonaListResponse;
import com.tibox.lucas.network.response.PersonaRespResponse;
import com.tibox.lucas.network.response.PersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.SolicitudResponse;
import com.tibox.lucas.network.response.UserListResponse;
import com.tibox.lucas.network.response.UserResponse;
import com.tibox.lucas.network.response.VarnegocioResponse;
import com.tibox.lucas.network.response.ZonasResponse;

/**
 * Created by desa02 on 01/03/2017.
 */

public interface IAppCreditosWebApi {
    Persona obtenerAutenticacion ( UsuarioDTO usuario ) throws IOException;


    double obtenerMontoCuota ( double prestamo, int cuota, double tasainterese ) throws IOException;


    List<ElectrodomesticoDTO> obtenerFamilia ( ElectrodomesticoDTO parametro ) throws  IOException;
    List<ElectrodomesticoDTO> obtenerFamiliaPost (ElectrodomesticoDTO parametro, int nCodNivel ) throws IOException;



    ELScoreBuroResultado ScoreResultado ( ELScoreBuroDatos datos ) throws IOException;
    ELMoraComercialResultado ScoreMoraComercial ( ELDocumento datos ) throws IOException;
    ELIngresoPredecidoDemograficoResultado IngresoPredecidoDemografico ( ELIngresoPredecidoDemograficoDatos datos ) throws IOException;
    ELIngresoPredecidoRCCResultado IngresoPredecidoRCC ( ELIngresoPredecidoRCCDatos datos, ELDocumento datosTitular,  ELDocumento datosConyugue ) throws IOException;
    ELIngresoPredecidoResultado IngresoPredecidoFinal ( ELIngresoPredecidoDemograficoDatos  datosD , ELIngresoPredecidoRCCDatos datosR, ELDocumento datosTitular, double nIngresoPredecidoDemografico3, double nIngresoPredecidoRCC3 ) throws IOException;
    ELScoringBuroCuotaUtilizadaResultado ScoringBuroCuotaUtil ( ELDocumento datosTitular,  ELDocumento datosConyugue ) throws IOException;
    ELScoreLenddoResultado ScoreLendo ( ELDocumento datosTitular ) throws IOException;
    ELScoreDemograficoResultado ScoreDemografico ( ELScoreDemograficoDatos datos, ELDocumento datosTitular ) throws IOException;
    ELScoringDemograficoResultado ScoringDemografico (ELScoringDemograficoDatos datos, ELDocumento datosTitular ) throws IOException;
    ELScoringBuroResultado ScoringBuroResultado ( ELScoringBuroDatos datos, ELDocumento datosTitular ) throws IOException;

    ELIngresoCliente IngresoPredecido ( ELIngresoCliente datos ) throws IOException;

    ClienteDTO ObtenerConsultaCliente ( String Doc ) throws IOException;
    double obtenerMontoMaximoPrestamo (ElectrodomesticoDTO parametro ) throws IOException;
    ResponseSolicitudDTO RegistrarCreditoGarantiaFlujo (CreditoGarantiaDTO parametro ) throws IOException;
    int RegistrarImagenesGarantia( CO_DocumentosDTO parametro ) throws IOException;
    ResponseSolicitudDTO RegistrarEstadoCuentaFlujo ( CreditoGarantiaDTO credito ) throws IOException;

    double ObtenerTEA ( double tasa ) throws IOException;

    int RegistrarSeguimientoCoordenadas (SeguimientoDTO seguimiento) throws IOException;

    CreditoFlujoDTO ObtenerDatosCreditoFlujo ( int nIdFlujoMa ) throws IOException;
    int ObtenerDetalleImagePDF ( DocumentosPdfDTO param ) throws IOException;
    FlujoLucasDTO ObtenerFlujoLucas ( int nIdFlujoMaestro ) throws IOException;




    String ClaveEncriptada ( String dato ) throws IOException;
    RespuestaDTO ActualizarContrasenia(UsuarioLucasDTO datos) throws IOException;


    TokenItems ObtenerToken ( UsuarioDTO usuario ) throws IOException;

    List<CatalagoCodigosDTO> obtenerCatalago (int nCodvar, String Access_token ) throws IOException;

    PreEvaluacionResp obtenerPreEvaluacion(Persona persona, String Access_token ) throws IOException;
    EvaluacionResp obtenerEvaluacion(Persona persona, String Access_token ) throws IOException;



    FlujoMaestro obtenerFlujoSolicitud(int iD, String Access_token) throws IOException;
    ResponseSolicitudDTO RegistrarSolicitudCreditoGarantiaFlujo ( Credito solicitud, String Access_token) throws IOException;

    ResultadoEnvio ReporteEnvio (Reporte datos, String Access_token) throws IOException;
    Credito obtenerDatosPrestamo(int Agencia, int Credito, String Access_token) throws IOException;



    List<DatosObligatorioDTO> obtenerDocObligatorios ( String Access_token ) throws IOException;



    Texto Desencriptar ( User user, String Access_token ) throws IOException;

    int IngresarFlujoLucas ( FlujoMaestro flujo, String Access_token ) throws IOException;
    int AnularCreditoLucas ( FlujoMaestro credito, String Access_token ) throws IOException;
    boolean EnvioEmail(Alerta alerta, String Access_token) throws IOException;
    int obtenerRechazoxDia( String documento, String Access_token ) throws IOException;
    int anulaxActualizacion( String documento, String Access_token ) throws IOException;
    int obtenerCreditoxFlujo( String documento, String Access_token ) throws IOException;
    //List<Calendario> ObtenerCalendarioMontoCuota( MontoCuota datos, String Access_token ) throws IOException;

    ResultadoEnvio GenerandoReporte( FlujoMaestro datos, String Access_token ) throws IOException;
    List<OperacionesDTO> ObtenerOperacionesCreditoLucas ( int nCodCred, int nCodAge, String Access_token ) throws IOException;

    //RESPONSE
    CreditoResponse ConsultaCreditos (BandParamDTO bandParamDTO, String Access_token ) throws IOException;
    PersonaResponse obtenerPersonaDatos (Persona persona, String Access_token) throws IOException;
    FlujoMaestroResponse obtenerFlujo(int iD, String Access_token) throws IOException;
    CalendarioResponse ObtenerCalendarioCreditoLucas (int nCodCred, int nCodAge, String Access_token ) throws IOException;
    FileResponse ObtenerImagePDF (DocumentosPdfDTO param, String namePdf, String Access_token ) throws IOException;
    CalendarioCreditoResponse ObtenerCalendarioMontoCuota(MontoCuota datos, String Access_token ) throws IOException;
    VarnegocioResponse obtenerValorNegocio (int nCodvar, String Access_token ) throws IOException;
    SolicitudResponse RegistrarNumeroCuenta (Credito datos, String Access_token) throws IOException;
    RedResponse EnvioSms(Alerta alerta, String Access_token) throws IOException;
    SolicitudResponse RegistrarFirmaDigital (Credito datos, String Access_token ) throws IOException;
    UserResponse VerificarEmail( User user, String Access_token ) throws IOException;
    CodPersonaResponse CambioPassword(User user, String Access_token ) throws IOException;
    ZonasResponse obtenerZonas (String cCodigo, String cCodigoSuperior, int nNivel, String Access_token ) throws IOException;
    PersonaRespResponse actualizarPersonaCredito (Persona persona, String Access_token ) throws IOException;
    PersonaRespResponse registrarPersonaCredito (Persona persona, String Access_token ) throws IOException;
    SolicitudResponse RegistrarDocumentosFlujo ( Documento documento, String Access_token ) throws IOException;

    FlujoMaestroResponse obtenerPasosFlujo(int nidFlujoMaestro, String Access_token) throws IOException;
    CalendarioListCreditoResponse ObtenerCalendarioList( MontoCuota datos, String Access_token ) throws IOException;

    UserListResponse verificarDocumento( String documento, String Access_token ) throws IOException;
    int verificarCelular( Persona data, String Access_token ) throws IOException;
    UserListResponse verificarEmailList( User user, String Access_token ) throws IOException;
    double ObtenerTCEA( MontoCuota datos, String Access_token ) throws IOException;
}
