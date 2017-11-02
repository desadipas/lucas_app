package com.tibox.lucas.network.access;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.util.Base64;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tibox.lucas.network.access.consumirwebservices.Datos;
import com.tibox.lucas.network.dto.BandParamDTO;
import com.tibox.lucas.network.dto.CO_DocumentosDTO;
import com.tibox.lucas.network.dto.CalendarioDTO;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.ClienteDTO;
import com.tibox.lucas.network.dto.CreditoFlujoDTO;
import com.tibox.lucas.network.dto.CreditoGarantiaDTO;
import com.tibox.lucas.network.dto.DatoEncriptadoDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.Documento;
import com.tibox.lucas.network.dto.DatosEntrada.ELDocumento;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoCliente;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELIngresoPredecidoRCCDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreBuroDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreDemograficoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoreLenddoDatos;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringBuroCuotaUtilizadaDatos;
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
import com.tibox.lucas.network.dto.DatosSalida.PersonaCredito;
import com.tibox.lucas.network.dto.DatosSalida.PreEvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.Red;
import com.tibox.lucas.network.dto.DatosSalida.Respt1;
import com.tibox.lucas.network.dto.DatosSalida.Respt2;
import com.tibox.lucas.network.dto.DatosSalida.Respt3;
import com.tibox.lucas.network.dto.DatosSalida.Resultado;
import com.tibox.lucas.network.dto.DatosSalida.ResultadoEnvio;
import com.tibox.lucas.network.dto.DatosSalida.Texto;
import com.tibox.lucas.network.dto.DatosSalida.TokenItems;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;
import com.tibox.lucas.network.dto.ElectrodomesticoDTO;
import com.tibox.lucas.network.dto.FlujoLucasDTO;
import com.tibox.lucas.network.dto.OperacionesDTO;
import com.tibox.lucas.network.dto.PersonaRespDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.dto.RespuestaDTO;
import com.tibox.lucas.network.dto.SeguimientoDTO;
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
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.utilidades.Constantes;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by desa02 on 01/03/2017.
 */

public class AppCreditoswebApi implements IAppCreditosWebApi {

    public AppCreditoswebApi(Context context) {
        m_Context = context;
    }
    private Context m_Context;

    @Override
    public Persona obtenerAutenticacion(UsuarioDTO usuario) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Usuario_Login;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + usuario.getPassword() );
        post.setHeader("content-type", "application/json");

        User jsonData = new User();
        jsonData.setEmail( usuario.getUsername() );

        String dataJson = new Gson().toJson( jsonData );
        post.setEntity(new StringEntity( dataJson, HTTP.UTF_8 ) );

        Persona respAutenticacion = new Persona();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( httpResponse.getEntity() );
            Persona[] testCase = new Gson().fromJson( responseString, Persona[].class );
            //respAutenticacion = new Gson().fromJson( responseString, Persona.class);
            return testCase[0];
            //return respAutenticacion;
        }
        catch (Exception ex) {
            respAutenticacion = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return respAutenticacion;
    }

    @Override
    public VarnegocioResponse obtenerValorNegocio(int nCodvar, String Access_token) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_VarNegocio;

        HttpGet get = new HttpGet(serverName + path + nCodvar);

        get.setHeader("Authorization", "Bearer " + Access_token );
        get.setHeader("content-type", "application/json");

        Varnegocio dtoVarnegocio = new Varnegocio();

        try {
            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(resp.getEntity());
            dtoVarnegocio = new Gson().fromJson( responseString, Varnegocio.class );
            //return dtoVarnegocio;

            return new VarnegocioResponse( dtoVarnegocio , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new VarnegocioResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new VarnegocioResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new VarnegocioResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new VarnegocioResponse( null, ex.getMessage(), false );
        }
        //return dtoVarnegocio;
    }

    @Override
    public double obtenerMontoCuota(double prestamo, int cuota, double tasainterese) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                throw new NetworkOnMainThreadException();
            }
            else {
                return 0;
            }
        }
        //HttpParams httpParameters = new BasicHttpParams();
        //HttpClient httpClient = new DefaultHttpClient(httpParameters);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = AppConfig.connection.getPrivatePedidoUrl();
        String path = Constantes.URL_CONSULTA_MONTOCUOTA;
        String path2 = Constantes.URL_CONSULTA_MONTOCUOTA2;
        String path3 = Constantes.URL_CONSULTA_MONTOCUOTA3;

        HttpGet get = new HttpGet( serverName + path + prestamo + path2 + cuota + path3 + tasainterese );
        get.setHeader("content-type", "application/json");

        double retorno = 0;
        try {
            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(resp.getEntity());
            retorno = Double.parseDouble( responseString );
            return retorno;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return retorno;
    }

    @Override
    public List<CatalagoCodigosDTO> obtenerCatalago( int nCodvar, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_CatalogoCodigo;

        HttpGet get = new HttpGet( serverName + path + nCodvar );
        get.setHeader("Authorization", "Bearer " +  Access_token );
        //get.setHeader("content-type", "application/json");

        List<CatalagoCodigosDTO> Listconsulta = new ArrayList<CatalagoCodigosDTO>();
        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);

            for (int i = Constantes.CERO; i < respJSON.length(); i++) {
                JSONObject obj = respJSON.getJSONObject(i);
                CatalagoCodigosDTO dto = new CatalagoCodigosDTO();
                dto.setnCodigo( nCodvar );
                dto.setcNomCod(obj.getString("cNomCod"));
                dto.setnValor(obj.getString("nValor"));
                Listconsulta.add(dto);
            }
            return Listconsulta;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return Listconsulta;
    }

    @Override
    public ZonasResponse obtenerZonas( String Codigo, String CodigoSuperior, int nNivel, String Access_token ) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;

        String path = "";
        String path2 = "";
        HttpGet get = new HttpGet();

        if ( nNivel == Constantes.UNO ){
            path = Constantes.Lucas_WebApi_Zona_Departamento; //Constantes.URL_DEPARTAMENTOS;
            get = new HttpGet( serverName + path );
        }
        else if ( nNivel == Constantes.DOS ) {
            path = Constantes.Lucas_WebApi_Zona_Provincia; //Constantes.URL_PROVICIASPORID;
            get = new HttpGet( serverName + path + CodigoSuperior );
        }
        else{
            path = Constantes.Lucas_WebApi_Zona_Distrito; //Constantes.URL_DISTROPORIDS;
            get = new HttpGet( serverName + path + CodigoSuperior + "/" + Codigo );
        }

        get.setHeader("Authorization", "Bearer " +  Access_token );

        List<ZonasDTO> Listconsulta = new ArrayList<ZonasDTO>();
        try {

            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray( respStr );

            for ( int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);

                ZonasDTO dto = new ZonasDTO();
                dto.setCODIGO( obj.getString( "cCodZona" ) );
                if ( nNivel == Constantes.UNO ){
                    dto.setDESCRIPCION( obj.getString("cNomZona") );
                }
                else if ( nNivel == Constantes.DOS ) {
                    dto.setDESCRIPCION( obj.getString("cNomZona") );
                }
                else{
                    dto.setDESCRIPCION( obj.getString("cNomZona") );
                }
                Listconsulta.add(dto);
            }
            //return Listconsulta;
            return new ZonasResponse( Listconsulta , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new ZonasResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new ZonasResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new ZonasResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ZonasResponse( null, ex.getMessage(), false );
        }
        //return Listconsulta;
    }

    @Override
    public List<ElectrodomesticoDTO> obtenerFamilia(ElectrodomesticoDTO parametro) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String serverName = AppConfig.connection.getPrivatePedidoUrl();
        String path = Constantes.URL_LLENA_COMBO_FAMILIA;
        HttpGet get = new HttpGet( serverName + path );
        get.setHeader("content-type", "application/json");
        List<ElectrodomesticoDTO> Listconsulta = new ArrayList<ElectrodomesticoDTO>();
        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);
            for (int i = 0; i < respJSON.length(); i++) {
                JSONObject obj = respJSON.getJSONObject(i);
                ElectrodomesticoDTO dto = new ElectrodomesticoDTO();
                dto.setcNomFamilia( obj.getString("cNomFamilia") );
                dto.setnCodFamilia( obj.getInt("nCodFamilia") );
                dto.setnMoneda( obj.getInt("nMoneda") );
                Listconsulta.add(dto);
            }
            return Listconsulta;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return Listconsulta;
    }

    @Override
    public List<ElectrodomesticoDTO> obtenerFamiliaPost(ElectrodomesticoDTO parametro, int Nivel) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = AppConfig.connection.getPrivatePedidoUrl();
        String path = "";

        if ( Nivel == Constantes.DOS )
            path = Constantes.URL_LLENA_COMBO_ARTICULO;
        else if ( Nivel == Constantes.TRES )
            path = Constantes.URL_LLENA_COMBO_MARCA;
        else if ( Nivel == Constantes.CUATRO )
            path = Constantes.URL_LLENA_COMBO_LINEA;
        else
            path = Constantes.URL_LLENA_COMBO_MODELO;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        String json = "";
        json = "";

        //String dataJson = new Gson().toJson( parametro );

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(parametro);

        /*Gson gson = new Gson();
        String dataJson = String.valueOf( parametro );
        ElectrodomesticoDTO parsedData = gson.fromJson( dataJson, ElectrodomesticoDTO.class);*/

        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        List<ElectrodomesticoDTO> Lista = new ArrayList<>();
        //Autenticacion respAutenticacion = new Autenticacion();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());

            JSONArray respJSON = new JSONArray( responseString );
            for (int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);
                ElectrodomesticoDTO dto = new ElectrodomesticoDTO();

                dto.setcNomArticulo( obj.getString("cNomArticulo") );
                dto.setnCodArticulo( obj.getInt("nCodArticulo") );
                dto.setcNomLinea( obj.getString("cNomLinea") );
                dto.setnCodLinea( obj.getInt("nCodLinea") );
                dto.setcNomMarca( obj.getString("cNomMarca") );
                dto.setnCodMarca( obj.getInt("nCodMarca") );
                dto.setcNomModelo( obj.getString("cNomModelo") );
                dto.setnCodModelo( obj.getInt("nCodModelo") );
                dto.setnPorValorTasado( obj.getDouble("nPorValorTasado") );
                dto.setnValorTasado( obj.getDouble("nPorValorTasado") );
                dto.setnPrestamoTotal( obj.getDouble("nPrestamoTotal") );
                dto.setnMoneda( obj.getInt("nMoneda") );
                Lista.add(dto);
            }
            //respAutenticacion = new Gson().fromJson(responseString, Autenticacion.class);
            return Lista;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return Lista;
    }

    @Override
    public List<DatosObligatorioDTO> obtenerDocObligatorios( String Access_token ) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName =  Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Documento_Tipo;

        HttpGet get = new HttpGet( serverName + path );

        get.setHeader("Authorization", "Bearer " +  Access_token );
        get.setHeader("content-type", "application/json");

        List<DatosObligatorioDTO> Listconsulta = new ArrayList<DatosObligatorioDTO>();
        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);

            for (int i = 0; i < respJSON.length(); i++) {
                JSONObject obj = respJSON.getJSONObject(i);
                DatosObligatorioDTO dto = new DatosObligatorioDTO();
                dto.setcNombre( obj.getString("cNombreDoc") );
                dto.setnCodigo( obj.getInt("nCodigo") );
                Listconsulta.add(dto);
            }
            return Listconsulta;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return Listconsulta;
    }

    @Override
    public PersonaRespResponse registrarPersonaCredito( Persona persona, String Access_token ) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Persona;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(persona);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        PersonaCredito personaCredito = new PersonaCredito();
        PersonaRespDTO resp = new PersonaRespDTO();
        int nCodPers = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            personaCredito = new Gson().fromJson(responseString, PersonaCredito.class);
            nCodPers = personaCredito.getnCodPers();
            resp.setnCodPers( nCodPers );
            return new PersonaRespResponse( resp, "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new PersonaRespResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new PersonaRespResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new PersonaRespResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new PersonaRespResponse( null, ex.getMessage(), false );
        }
        //return resp;
    }

    @Override
    public ELScoreBuroResultado ScoreResultado( ELScoreBuroDatos datos ) throws IOException {
        Datos loDatos = new Datos();

        //Score Buro: 1; Score Demografico: 2; Mora Comercial: 21; Score Lenddo: 3; Score de Comportamiento: 4;
        //Ingreso Predecido Demografico: 1; Ingreso Predecido RCC: 2;
        //Scoring Buro: 1; Scoring Buro Cuotas Utilizada: 11
        loDatos.nTipoServicio = 1 ;
        //======================================================================
        //Esto para el Score de Buro:
        //----------------------------------------------------------------------
        ELScoreBuroDatos loDatosServicio = new ELScoreBuroDatos();
        loDatosServicio.sNroDoc = datos.sNroDoc;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;
        ELScoreBuroResultado salida = new ELScoreBuroResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORE;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELScoreBuroResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELMoraComercialResultado ScoreMoraComercial(ELDocumento datos) throws IOException {

        Datos loDatos = new Datos();
        //Score Buro: 1; Score Demografico: 2; Mora Comercial: 21; Score Lenddo: 3; Score de Comportamiento: 4;
        loDatos.nTipoServicio = 21 ;
        //======================================================================

        //======================================================================
        //Esto para la mora comercial:
        //----------------------------------------------------------------------
        ELDocumento loDatosServicio = new ELDocumento();
        loDatosServicio.nTipoDoc = 1;
        loDatosServicio.sNroDoc = datos.sNroDoc;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELMoraComercialResultado salida = new ELMoraComercialResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORE;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELMoraComercialResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELIngresoPredecidoDemograficoResultado IngresoPredecidoDemografico(ELIngresoPredecidoDemograficoDatos datos) throws IOException {

        Datos loDatos = new Datos();
        //Ingreso Predecido Demografico: 1; Ingreso Predecido RCC: 2; Ingreso Predecido Final: 3;
        loDatos.nTipoServicio = 1 ;
        //======================================================================

        //======================================================================
        //Esto para el Ingreso Predecido Demografico:
        //----------------------------------------------------------------------
        ELIngresoPredecidoDemograficoDatos loDatosServicio = new ELIngresoPredecidoDemograficoDatos();
        loDatosServicio.nConstante = datos.nConstante;
        loDatosServicio.nEdad = datos.nEdad;
        loDatosServicio.nEstadoCivil = datos.nEstadoCivil;
        loDatosServicio.nGenero = datos.nGenero;
        loDatosServicio.sCIIU = datos.sCIIU;
        loDatosServicio.sUbiGeo = datos.sUbiGeo;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELIngresoPredecidoDemograficoResultado salida = new ELIngresoPredecidoDemograficoResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_INGRESOPREDECIDO;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELIngresoPredecidoDemograficoResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELIngresoPredecidoRCCResultado IngresoPredecidoRCC( ELIngresoPredecidoRCCDatos datos, ELDocumento datosTitular,  ELDocumento datosConyugue ) throws IOException {

        Datos loDatos = new Datos();
        //Ingreso Predecido Demografico: 1; Ingreso Predecido RCC: 2; Ingreso Predecido Final: 3;
        loDatos.nTipoServicio = 2;
        //======================================================================

        //======================================================================
        //Esto para el Ingreso Predecido RCC:
        //----------------------------------------------------------------------
        ELIngresoPredecidoRCCDatos loDatosServicio = new ELIngresoPredecidoRCCDatos();
        loDatosServicio.nEstadoCivil = datos.nEstadoCivil;
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumentoTitular = loDocumento;
        loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 0;
        loDocumento.sNroDoc = "";
        loDatosServicio.oDocumentoConyuge = loDocumento;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELIngresoPredecidoRCCResultado salida = new ELIngresoPredecidoRCCResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_INGRESOPREDECIDO;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELIngresoPredecidoRCCResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELIngresoPredecidoResultado IngresoPredecidoFinal( ELIngresoPredecidoDemograficoDatos  datosD , ELIngresoPredecidoRCCDatos datosR, ELDocumento datosTitular, double nIngresoPredecidoDemografico3, double nIngresoPredecidoRCC3 ) throws IOException {

        Datos loDatos = new Datos();
        //Ingreso Predecido Demografico: 1; Ingreso Predecido RCC: 2; Ingreso Predecido Final: 3;
        loDatos.nTipoServicio = 3;
        //======================================================================

        //======================================================================
        //Esto para el Ingreso Predecido Final:
        //----------------------------------------------------------------------
        ELIngresoPredecidoDatos loDatosServicio = new ELIngresoPredecidoDatos();
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumentoTitular = loDocumento;
        loDatosServicio.nIngresoPredecidoDemografico = nIngresoPredecidoDemografico3;
        loDatosServicio.nIngresoPredecidoRCC = nIngresoPredecidoRCC3;
        loDatosServicio.oDatosDemografico = datosD; //new ELIngresoPredecidoDemograficoDatos();
        loDatosServicio.oDatosRCC = datosR;//new ELIngresoPredecidoRCCDatos();
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELIngresoPredecidoResultado salida = new ELIngresoPredecidoResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_INGRESOPREDECIDO;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELIngresoPredecidoResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELScoringBuroCuotaUtilizadaResultado ScoringBuroCuotaUtil( ELDocumento datosTitular,  ELDocumento datosConyugue ) throws IOException {

        Datos loDatos = new Datos();
        ///Scoring Buro: 1; Scoring Buro Cuotas Utilizada: 11; Scoring Demografico: 2;
        loDatos.nTipoServicio = 11;
        //======================================================================

        //======================================================================
        //Esto para el Scoring Buro Cuotas Utilizada:
        //----------------------------------------------------------------------
        ELScoringBuroCuotaUtilizadaDatos loDatosServicio = new ELScoringBuroCuotaUtilizadaDatos();
        loDatosServicio.nEstadoCivil = 1;
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumentoTitular = loDocumento;
        loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 0;
        loDocumento.sNroDoc = "";
        loDatosServicio.oDocumentoConyuge = loDocumento;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELScoringBuroCuotaUtilizadaResultado salida = new ELScoringBuroCuotaUtilizadaResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORING;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELScoringBuroCuotaUtilizadaResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELScoreLenddoResultado ScoreLendo( ELDocumento datosTitular ) throws IOException {
        Datos loDatos = new Datos();
        //Score Buro: 1; Score Demografico: 2; Mora Comercial: 21; Score Lenddo: 3; Score de Comportamiento: 4;
        loDatos.nTipoServicio = 3;
        //======================================================================

        //======================================================================
        //Esto para el Score Lenddo:
        //----------------------------------------------------------------------
        ELScoreLenddoDatos loDatosServicio = new ELScoreLenddoDatos();
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumento=loDocumento;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELScoreLenddoResultado salida = new ELScoreLenddoResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORE;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELScoreLenddoResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELScoreDemograficoResultado ScoreDemografico(ELScoreDemograficoDatos datos, ELDocumento datosTitular) throws IOException {

        Datos loDatos = new Datos();
        //Score Buro: 1; Score Demografico: 2; Mora Comercial: 21; Score Lenddo: 3; Score de Comportamiento: 4;
        loDatos.nTipoServicio = 2;
        //======================================================================

        //======================================================================
        //Esto para el Score Demografico:
        //----------------------------------------------------------------------
        ELScoreDemograficoDatos loDatosServicio = new ELScoreDemograficoDatos();
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumento = loDocumento;
        loDatosServicio.nCondicionLaboral = datos.nCondicionLaboral;
        loDatosServicio.nDepartamento = datos.nDepartamento;
        loDatosServicio.nEdad = datos.nEdad;
        loDatosServicio.nEstadoCivil = datos.nEstadoCivil;
        loDatosServicio.nGenero = datos.nGenero;
        loDatosServicio.nIngresoSalarial = datos.nIngresoSalarial;
        loDatosServicio.nMoraComercial = datos.nMoraComercial;
        loDatosServicio.nRankingTop10000 = 0;
        loDatosServicio.nVivienda = datos.nVivienda;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELScoreDemograficoResultado salida = new ELScoreDemograficoResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORE;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELScoreDemograficoResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELScoringDemograficoResultado ScoringDemografico(ELScoringDemograficoDatos datos, ELDocumento datosTitular) throws IOException {

        Datos loDatos = new Datos();
        //Scoring Buro: 1; Scoring Buro Cuotas Utilizada: 11; Scoring Demografico: 2;
        loDatos.nTipoServicio = 2;
        //======================================================================

        //======================================================================
        //Esto para el Scoring Demografico:
        //----------------------------------------------------------------------
        ELScoringDemograficoDatos loDatosServicio = new ELScoringDemograficoDatos();
        loDatosServicio.nProducto = 7;
        loDatosServicio.nModalidad = 1;
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumentoTitual = loDocumento;
        loDatosServicio.nIngresoPredecido = datos.nIngresoPredecido;
        loDatosServicio.nMoraComercial = datos.nMoraComercial;
        loDatosServicio.nScore = datos.nScore;
        loDatosServicio.nScoreOtros = datos.nScoreOtros;
        loDatosServicio.nGarantia = 0.0;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELScoringDemograficoResultado salida = new ELScoringDemograficoResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORING;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELScoringDemograficoResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;
    }

    @Override
    public ELScoringBuroResultado ScoringBuroResultado( ELScoringBuroDatos datos, ELDocumento datosTitular ) throws IOException {

        Datos loDatos = new Datos();
        ///Scoring Buro: 1; Scoring Buro Cuotas Utilizada: 11; Scoring Demografico: 2;
        loDatos.nTipoServicio = 1;
        //======================================================================

        //======================================================================
        //Esto para el Scoring Buro:
        //----------------------------------------------------------------------
        ELScoringBuroDatos loDatosServicio = new ELScoringBuroDatos();
        loDatosServicio.nProducto = 7;
        loDatosServicio.nModalidad = 1;
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = datosTitular.sNroDoc;
        loDatosServicio.oDocumentoTitual = loDocumento;
        loDatosServicio.nIngresoPredecido = datos.nIngresoPredecido;
        loDatosServicio.nCuotaUtilizada = datos.nCuotaUtilizada;
        loDatosServicio.nMoraComercial = datos.nMoraComercial;
        loDatosServicio.nScore = datos.nScore;
        loDatosServicio.nScoreOtros = 0;
        loDatosServicio.nGarantia = 0.0;
        //======================================================================

        loDatos.oDatosServicio = loDatosServicio;

        ELScoringBuroResultado salida = new ELScoringBuroResultado();

        Gson gson = new Gson();
        String jSon = "";
        jSon = gson.toJson(loDatos);

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_SCORING;
        String path = Constantes.URL_SCORING;
        HttpPost httppost = new HttpPost( serverName + path );
        httppost.addHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jSon);
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        try{

            HttpResponse httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            salida = new Gson().fromJson(responseString, ELScoringBuroResultado.class);
            return salida;

        }
        catch (Exception ex){
            Log.e("ServicioRest", "Error!", ex);
        }
        return salida;

    }

    @Override
    public ELIngresoCliente IngresoPredecido(ELIngresoCliente datos) throws IOException {
        ELIngresoCliente nuevo = new ELIngresoCliente();
        nuevo.nIngresoFinal = Constantes.CERO;
        return nuevo;
    }

    @Override
    public ClienteDTO ObtenerConsultaCliente(String Doc) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_BUSQUEDA_PERSONA;
        HttpGet get = new HttpGet(serverName + path + Doc);
        get.setHeader("content-type", "application/json");
        ClienteDTO clienteDTO = new ClienteDTO();
        try {
            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(resp.getEntity());
            clienteDTO = new Gson().fromJson(responseString, ClienteDTO.class);
            return clienteDTO;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return clienteDTO;
    }

    @Override
    public double obtenerMontoMaximoPrestamo(ElectrodomesticoDTO parametro) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = AppConfig.connection.getPrivatePedidoUrl();
        String path = Constantes.URL_MONTO_MAX_PRESTAMO;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(parametro);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ElectrodomesticoDTO dto = new ElectrodomesticoDTO();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());

            JSONArray respJSON = new JSONArray( responseString );

            for (int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);

                dto.setnValorTasado( obj.getDouble("nPorValorTasado") );
            }
            return dto.getnValorTasado();
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return 0.0;
    }

    @Override
    public ResponseSolicitudDTO RegistrarCreditoGarantiaFlujo(CreditoGarantiaDTO parametro) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_INS_CREDITO_GARANTIA;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(parametro);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ResponseSolicitudDTO responseSolicitudDTO = new ResponseSolicitudDTO();

        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            responseSolicitudDTO = new Gson().fromJson(responseString, ResponseSolicitudDTO.class);
            return responseSolicitudDTO;

        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return responseSolicitudDTO;
    }

    @Override
    public int RegistrarImagenesGarantia(CO_DocumentosDTO parametro) throws IOException {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                throw new NetworkOnMainThreadException();
            } else {
                return 0;
            }
        }
        int nResp = 0;
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_INS_IMAGEN_GARANTIA;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(parametro);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            int responseString = Integer.parseInt(EntityUtils.toString(httpResponse.getEntity()));
            if (responseString != 0) {
                nResp = responseString;
            } else {
                nResp = 0;
            }
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return nResp;
    }

    @Override
    public ResponseSolicitudDTO RegistrarEstadoCuentaFlujo( CreditoGarantiaDTO credito ) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_INS_ESTADO_CUENTA_FLUJO;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(credito);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ResponseSolicitudDTO responseSolicitudDTO = new ResponseSolicitudDTO();

        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            responseSolicitudDTO = new Gson().fromJson(responseString, ResponseSolicitudDTO.class);
            return responseSolicitudDTO;

        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return responseSolicitudDTO;
    }

    @Override
    public SolicitudResponse RegistrarDocumentosFlujo( Documento documento, String Access_token ) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Documento_Subir;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " +  Access_token );
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(documento);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ResponseSolicitudDTO responseSolicitudDTO = new ResponseSolicitudDTO();

        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            Respt3 respt = new Gson().fromJson(responseString, Respt3.class);
            responseSolicitudDTO.setnValorRetorno( respt.getbRespuesta() ); //new Gson().fromJson(responseString, ResponseSolicitudDTO.class);

            return new SolicitudResponse( responseSolicitudDTO , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new SolicitudResponse( null, ex.getMessage(), false );
        }
        //return responseSolicitudDTO;

    }

    @Override
    public double ObtenerTEA(double tasa) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_OBTENER_TEA;
        HttpGet get = new HttpGet(serverName + path + tasa);
        get.setHeader("content-type", "application/json");
        double nTea = 0.;
        try {
            HttpResponse resp = httpClient.execute(get);

            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            //String responseString = EntityUtils.toString(resp.getEntity());
            double responseString = Double.parseDouble(EntityUtils.toString( resp.getEntity()));

            if (responseString != 0.) {
                nTea = responseString;
            }
            else {
                nTea = 0.;
            }
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return nTea;
    }

    @Override
    public ResponseSolicitudDTO RegistrarSolicitudCreditoGarantiaFlujo( Credito solicitud, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(solicitud);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ResponseSolicitudDTO responseSolicitudDTO = new ResponseSolicitudDTO();
        int nCodCred = 0;

        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }

            String responseString = EntityUtils.toString(httpResponse.getEntity());
            Respt1 respt1 = new Gson().fromJson( responseString, Respt1.class);
            responseSolicitudDTO.setnCodCred( respt1.getnCodCred() ); //new Gson().fromJson( responseString, ResponseSolicitudDTO.class);
            responseSolicitudDTO.setnValorRetorno( Constantes.UNO );
            return responseSolicitudDTO;

        }
        catch (Exception ex) {
            responseSolicitudDTO = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return responseSolicitudDTO;
    }

    @Override
    public int RegistrarSeguimientoCoordenadas(SeguimientoDTO seguimiento) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_INS_SEGUIMIENTOCRED;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(seguimiento);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        int nRespuesta = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            nRespuesta = Integer.parseInt( EntityUtils.toString( httpResponse.getEntity()) );
            return nRespuesta;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return nRespuesta;
    }

    @Override
    public CreditoResponse ConsultaCreditos(BandParamDTO bandParamDTO, String Access_token ) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        //String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Bandeja;//Constantes.URL_OBTENER_CREDITOS;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        Credito Paramdto = new Credito();
        Paramdto.setnCodPers( bandParamDTO.getnCodPers() );
        Paramdto.setnCodAge( bandParamDTO.getnCodAge() );

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson( Paramdto );

        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        List<Credito> Lista = new ArrayList<>();

        try {
            HttpResponse httpResponse = httpClient.execute(post);

            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }

            String responseString = EntityUtils.toString(httpResponse.getEntity());
            JSONArray respJSON = new JSONArray( responseString );

            for (int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);

                Credito dto = new Credito();

                dto.setnIdFlujoMaestro( obj.getInt("nIdFlujoMaestro") );
                dto.setnIdFlujo( obj.getInt("nIdFlujo") );
                dto.setnCodCred( obj.getInt("nCodCred") );
                dto.setnCodAge( obj.getInt("nCodAge") );
                //dto.setnCodPersTitular( obj.getInt("nCodPersTitular") );
                dto.setnPrestamo( obj.getDouble("nPrestamo") );
                dto.setnEstado( obj.getInt("nEstado") );
                dto.setnProd( obj.getInt("nProd") );
                dto.setnSubProd( obj.getInt("nSubProd") );
                dto.setnOrdenFlujo( obj.getInt("nOrdenFlujo") );
                //dto.setnNecesario( obj.getInt("nNecesario") );
                dto.setbActivo( obj.getInt("bActivo") );
                //dto.setcNomAge( obj.getString("cNomAge") );
                //dto.setcCliente( obj.getString("cCliente") );
                dto.setcProducto( obj.getString("cProducto") );
                dto.setcSubProd( obj.getString("cSubProd") );
                dto.setcEstado( obj.getString("cEstado") );
                //dto.setcNombreProceso( obj.getString("cNombreProceso") );
                //dto.setcDocumento( obj.getString("cDocumento") );
                dto.setdFechaRegistro( obj.getString("dFechaRegistro") );
                dto.setcMoneda( obj.getString("cMoneda") );
                dto.setcNumeroContrato( obj.getString( "cNumeroContrato" ) );

                dto.setcFormulario( obj.getString( "cFormulario" ) );

                dto.setnMontoCuota( obj.getDouble( "nMontoCuota" ) );
                dto.setnNroCuotas( obj.getInt( "nNroCuotas" ) );
                dto.setnTasaComp( obj.getDouble( "nIntComp" ) );

                Lista.add(dto);
            }

            return new CreditoResponse( Lista , "Consulta correcta", true );
            //return Lista;
        } catch ( SocketTimeoutException ex ) {
            return new CreditoResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new CreditoResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new CreditoResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CreditoResponse( null, ex.getMessage(), false );
        }
        //return Lista;
    }

    @Override
    public CreditoFlujoDTO ObtenerDatosCreditoFlujo(int nIdFlujoMa) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_OBTENER_DATOS_CREDITO_FLUJO;
        HttpGet get = new HttpGet( serverName + path + nIdFlujoMa );
        get.setHeader("content-type", "application/json");
        CreditoFlujoDTO clienteDTO = new CreditoFlujoDTO();
        try {
            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(resp.getEntity());
            clienteDTO = new Gson().fromJson(responseString, CreditoFlujoDTO.class);
            return clienteDTO;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return clienteDTO;
    }

    @Override
    public int ObtenerDetalleImagePDF(DocumentosPdfDTO param) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_OBTENER_DETALLE_PDF_CREDITO;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(param);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        int nRespuesta = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            nRespuesta = Integer.parseInt( EntityUtils.toString( httpResponse.getEntity()) );
            return nRespuesta;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return nRespuesta;
    }

    @Override
    public FileResponse ObtenerImagePDF(DocumentosPdfDTO param, String namePdf, String Access_token ) throws IOException {

        /*
        File m_file = null;
        try {

            String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
            String path = Constantes.Lucas_WebApi_Reporte;

            HttpPost post = new HttpPost( serverName + path );
            HttpParams httpParameters = new BasicHttpParams();

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            post.setHeader("content-type", "application/json");

            String dataJson = new Gson().toJson(param);
            post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
            HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(response.getStatusLine().getReasonPhrase());
            }

            // Eliminamos las fotos de la carpeta almacenada en el movil.
            File direDelete = new File( AppConfig.directorioPdf );
            if ( direDelete.isDirectory() )
            {
                String[] children = direDelete.list();
                for ( int i = 0; i < children.length; i++ )
                {
                    new File( direDelete, ( namePdf + ".pdf" ) ).delete();
                }
            }

            File newdir = new File( AppConfig.directorioPdf );
            newdir.mkdirs();
            InputStream is = response.getEntity().getContent();
            File file = new File( AppConfig.directorioPdf, namePdf + ".pdf" );
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream fos = new FileOutputStream( file );
            int read = 0;
            byte[] buffer = new byte[ Constantes.MEGABYTE ];
            while((read = is.read(buffer)) > 0){
                fos.write(buffer, 0, read);
            }
            fos.close();
            is.close();
            m_file = file;
            post.abort();
        }
        catch ( IOException error )
        {
            m_file = null;
        }
        catch ( Exception e ) {
            m_file = null;
        }
        finally {
            if ( m_file == null ){
                m_file = null;
            }
        }
        return m_file;
        */



        // GET

        File m_file = null;
        HttpClient httpClient = new DefaultHttpClient();
        String serverName =  Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Reporte;
        HttpGet get = new HttpGet( serverName + path + param.getnAgencia() + "/" + param.getnCredito() + "/" + param.getnTipoDoc() );
        get.setHeader("Authorization", "Bearer " +  Access_token );
        get.setHeader("content-type", "application/json");

        try {
            HttpResponse resp = httpClient.execute(get);

            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }

            String respStr = EntityUtils.toString(resp.getEntity());
            Reporte[] documento = new Gson().fromJson( respStr, Reporte[].class );

            // Eliminamos las fotos de la carpeta almacenada en el movil.
            File direDelete = new File( AppConfig.directorioPdf );
            if ( direDelete.isDirectory() )
            {
                String[] children = direDelete.list();
                for ( int i = 0; i < children.length; i++ )
                {
                    new File( direDelete, ( namePdf + ".pdf" ) ).delete();
                }
            }
            //

            File newdir = new File( AppConfig.directorioPdf );
            newdir.mkdirs();
            //Creo el archivo
            File file = new File( AppConfig.directorioPdf, namePdf + ".pdf" );
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //


            /*
            InputStream stream = new ByteArrayInputStream( documento[0].getoDocumento().getBytes() );
            InputStream is = stream;
            FileOutputStream fos = new FileOutputStream( file );
            byte[] buffer = new byte[ Constantes.MEGABYTE ];
            int read = 0;
            while( ( read = is.read( buffer ) ) > Constantes.CERO ){
                fos.write( buffer, Constantes.CERO, read);
            }

            fos.close();
            is.close();
            m_file = file;
            get.abort();
            */




            final File dwldsPath = new File( AppConfig.directorioPdf, namePdf + ".pdf" );
            byte[] pdfAsBytes = Base64.decode( documento[0].getoDocumento().toString(), 0);
            FileOutputStream os;
            os = new FileOutputStream(dwldsPath, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();

            m_file = file;
            return new FileResponse( m_file , "Consulta correcta", true );


            /*
            FileWriter fstream = new FileWriter( file );
            BufferedWriter out = new BufferedWriter( fstream );
            for ( Byte b : documento[0].getoDocumento().toString().getBytes() ){
                out.write( b );
            }
            */

            /*
            byte[] bytes = documento[0].getoDocumento().toString().getBytes();
            File someFile = new File( AppConfig.directorioPdf, namePdf + ".pdf" );
            FileOutputStream fos = new FileOutputStream( someFile );
            fos.write(bytes);
            fos.flush();
            fos.close();
            */


            /*
            try {

                //OutputStream os = response.getOutputStream();
                OutputStream os = documento[0].getoDocumento().getBytes();

                byte[] buf = new byte[8192];
                InputStream is = new FileInputStream(file);
                int c = 0;
                while ((c = is.read(buf, 0, buf.length)) > 0) {
                    os.write(buf, 0, c);
                    os.flush();
                }
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */


        }
        catch ( IOException error )
        {
            //m_file = null;
            return new FileResponse( null, error.getMessage(), false );
        }
        catch ( Exception e ) {
            return new FileResponse( null, e.getMessage(), false );
        }
        finally {
            if ( m_file == null ){
                m_file = null;
            }
        }
        //return m_file;


    }

    @Override
    public SolicitudResponse RegistrarFirmaDigital( Credito datos, String Access_token ) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Firma;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " +  Access_token );
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ResponseSolicitudDTO responseSolicitudDTO = new ResponseSolicitudDTO();
        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            Respt2 respt = new Gson().fromJson(responseString, Respt2.class);
            responseSolicitudDTO.setnValorRetorno( respt.getbExito() ); //new Gson().fromJson(responseString, ResponseSolicitudDTO.class);
            //return responseSolicitudDTO;
            return new SolicitudResponse( responseSolicitudDTO , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new SolicitudResponse( null, ex.getMessage(), false );
        }
        //return responseSolicitudDTO;
    }

    @Override
    public SolicitudResponse RegistrarNumeroCuenta(Credito datos , String Access_token) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Modalidad;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        ResponseSolicitudDTO responseSolicitudDTO = new ResponseSolicitudDTO();

        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            Respt2 respt = new Gson().fromJson(responseString, Respt2.class);
            responseSolicitudDTO.setnValorRetorno( respt.getbExito() );
            //return responseSolicitudDTO;
            return new SolicitudResponse( responseSolicitudDTO , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new SolicitudResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new SolicitudResponse( null, ex.getMessage(), false );
        }
        //return responseSolicitudDTO;
    }

    @Override
    public FlujoLucasDTO ObtenerFlujoLucas(int nIdFlujoMaestro) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_OBTENER_FLUJO_LUCAS;

        HttpGet get = new HttpGet( serverName + path + nIdFlujoMaestro );
        get.setHeader("content-type", "application/json");
        FlujoLucasDTO clienteDTO = new FlujoLucasDTO();

        try {
            HttpResponse resp = httpClient.execute(get);
            if ( resp.getStatusLine().getStatusCode() >= 400 ) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( resp.getEntity() );
            clienteDTO = new Gson().fromJson(responseString, FlujoLucasDTO.class);
            return clienteDTO;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return clienteDTO;
    }

    @Override
    public int AnularCreditoLucas( FlujoMaestro credito , String Access_token ) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Flujo_Eliminar;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(credito);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        Integer response = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            response = Integer.parseInt( EntityUtils.toString( httpResponse.getEntity() ) );
            return response;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return response;

    }

    @Override
    public String ClaveEncriptada(String dato) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXION_ENCRIPTAR;
        String path = Constantes.URL_ENCRIPTAR_DATO;
        HttpGet get = new HttpGet(serverName + path + dato);
        get.setHeader("content-type", "application/json");
        String cDatoEncriptado = "";
        DatoEncriptadoDTO datoEncriptadoDTO = new DatoEncriptadoDTO();
        try {
            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( resp.getEntity() );
            datoEncriptadoDTO = new Gson().fromJson( responseString, DatoEncriptadoDTO.class );
            cDatoEncriptado = datoEncriptadoDTO.getClave();
            return cDatoEncriptado;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return cDatoEncriptado;
    }

    @Override
    public RespuestaDTO ActualizarContrasenia(UsuarioLucasDTO datos) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        String serverName = Constantes.IP_CONEXIONYPUERTO_API;
        String path = Constantes.URL_ACTUALIZAR_CONTRASENIA;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        RespuestaDTO responseSolicitudDTO = new RespuestaDTO();

        try {
            //
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            responseSolicitudDTO = new Gson().fromJson(responseString, RespuestaDTO.class);
            return responseSolicitudDTO;

        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return responseSolicitudDTO;
    }

    @Override
    public CalendarioResponse ObtenerCalendarioCreditoLucas(int nCodCred, int nCodAge, String Access_token) throws IOException {

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Calendario_Lista;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet();

        get = new HttpGet( serverName + path + nCodAge + "/" + nCodCred );

        get.setHeader("Authorization", "Bearer " +  Access_token );
        get.setHeader("content-type", "application/json");
        List<CalendarioDTO> Listconsulta = new ArrayList<CalendarioDTO>();
        List<Credito> listCalendario = new ArrayList<Credito>();

        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);

            for (int i = 0; i < respJSON.length(); i++) {

                JSONObject obj = respJSON.getJSONObject(i);
                CalendarioDTO dto = new CalendarioDTO();
                //01/11/2017|0.00|PENDIENTE|5.21
                dto.setdFecCob( String.valueOf( obj.getString( "dFecCob" ) + "|" + "0.00" + "|" + obj.getString( "cDescripcion" )  + "|" + obj.getDouble( "nGasto" ) ) );
                dto.setnIntComp( obj.getDouble( "nIntComp" ) );
                dto.setnCapital( obj.getDouble( "nCapital" ) );
                dto.setdFecVenc( obj.getString( "dFecVenc" ) );
                dto.setnNrosubCuota( obj.getInt( "nNroCuotas" ) );
                dto.setdFecPago( obj.getString( "dFecPago" ) );
                dto.setnSeguro( obj.getDouble( "nSeguro" ) );

                Listconsulta.add(dto);
            }
            //return Listconsulta;
            return new CalendarioResponse( Listconsulta , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new CalendarioResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new CalendarioResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new CalendarioResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CalendarioResponse( null, ex.getMessage(), false );
        }
        //return Listconsulta;

    }

    @Override
    public List<OperacionesDTO> ObtenerOperacionesCreditoLucas(int nCodCred, int nCodAge, String Access_token) throws IOException {

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Kardex_Lista;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet();

        get = new HttpGet( serverName + path + nCodAge + "/" + nCodCred );

        get.setHeader("Authorization", "Bearer " +  Access_token );
        get.setHeader("content-type", "application/json");
        List<OperacionesDTO> Listconsulta = new ArrayList<OperacionesDTO>();

        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);

            for (int i = 0; i < respJSON.length(); i++) {

                JSONObject obj = respJSON.getJSONObject(i);
                OperacionesDTO dto = new OperacionesDTO();

                dto.setnMontoNuevoSaldo( obj.getDouble( "nMontoNuevoSaldo" ) );
                dto.setnIntComp( obj.getDouble( "nIntComp" ) );
                dto.setnCapital( obj.getDouble( "nCapital" ) );
                //26/05/2017|PAGO CUOTA EFECTIVO NORMAL
                dto.setdFecha( obj.getString( "dFecPago" ) + "|" + obj.getString( "cDescripcion" ) );
                dto.setnCuotaPagada( obj.getInt( "nCuotaPag" ) );
                dto.setnIGV( obj.getDouble( "nIGV" ) );

                Listconsulta.add(dto);
            }
            return Listconsulta;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return Listconsulta;

    }

    @Override
    public int IngresarFlujoLucas( FlujoMaestro flujo, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Post_Flujo;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String dataJson = gson.toJson(flujo);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        Integer response = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( httpResponse.getEntity() );
            Resultado resultado = new Gson().fromJson( responseString, Resultado.class );

            response = resultado.getbResultado();//Integer.parseInt( EntityUtils.toString( httpResponse.getEntity() ) );
            return response;
        }
        catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return response;
    }

    @Override
    public TokenItems ObtenerToken( UsuarioDTO usuario ) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Token;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair( "grant_type", "password" ) );
        nameValuePairs.add(new BasicNameValuePair( "username", usuario.getUsername() ) );
        nameValuePairs.add(new BasicNameValuePair( "password", usuario.getPassword() ) );
        nameValuePairs.add(new BasicNameValuePair( "tipo", "lucas" ) );

        HttpPost post = new HttpPost( serverName + path );
        post.setHeader("content-type", "application/x-www-form-urlencoded");
        post.setEntity(new UrlEncodedFormEntity( nameValuePairs, HTTP.UTF_8));

        TokenItems tokenItems = new TokenItems();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if ( httpResponse.getStatusLine().getStatusCode() >= 400 ) {
                throw new IOException( httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            tokenItems = new Gson().fromJson(responseString, TokenItems.class);
            return tokenItems;

        }
        catch (Exception ex) {
            Log.e( "ServicioRest", "Error!", ex );
            tokenItems = null;
        }
        return tokenItems;

    }

    @Override
    public PreEvaluacionResp obtenerPreEvaluacion(Persona persona, String Access_token) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Evaluacion_PreEvaluacion;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(persona);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        PreEvaluacionResp respuesta = new PreEvaluacionResp();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            respuesta = new Gson().fromJson(responseString, PreEvaluacionResp.class);
            return respuesta;
        }
        catch (Exception ex) {
            respuesta = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return respuesta;
    }

    @Override
    public EvaluacionResp obtenerEvaluacion(Persona persona, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Evaluacion;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(persona);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        EvaluacionResp respuesta = new EvaluacionResp();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            respuesta = new Gson().fromJson(responseString, EvaluacionResp.class);
            return respuesta;
        }
        catch (Exception ex) {
            respuesta = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return respuesta;
    }

    @Override
    public PersonaRespResponse actualizarPersonaCredito(Persona persona, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Persona_Put;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(persona);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        PersonaCredito personaCredito = new PersonaCredito();
        PersonaRespDTO resp = new PersonaRespDTO();
        int nCodPers = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            personaCredito = new Gson().fromJson(responseString, PersonaCredito.class);
            nCodPers = personaCredito.getnCodPers();
            resp.setnCodPers( nCodPers );
            //return resp;
            return new PersonaRespResponse( resp, "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new PersonaRespResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new PersonaRespResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new PersonaRespResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new PersonaRespResponse( null, ex.getMessage(), false );
        }
        //return resp;
    }

    @Override
    public PersonaResponse obtenerPersonaDatos( Persona persona, String Access_token ) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Persona_Datos;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson( persona );
        post.setEntity(new StringEntity( dataJson, HTTP.UTF_8 ) );

        Persona respDatos = new Persona();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( httpResponse.getEntity() );
            Persona[] personalist = new Gson().fromJson( responseString, Persona[].class );
            //return personalist[0];
            return new PersonaResponse( personalist[0] , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new PersonaResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new PersonaResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new PersonaResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new PersonaResponse( null, ex.getMessage(), false );
        }
        //return respDatos;
    }

    @Override
    public FlujoMaestroResponse obtenerFlujo(int iD, String Access_token) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Flujo;

        HttpGet get = new HttpGet( serverName + path + iD );
        get.setHeader("Authorization", "Bearer " +  Access_token );

        //FlujoMaestro flujoMaestro = new FlujoMaestro();

        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());

            FlujoMaestro[] flujoMaestros = new Gson().fromJson( respStr, FlujoMaestro[].class );
            //return flujoMaestros[0];
            return new FlujoMaestroResponse( flujoMaestros[0] , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        }
        //return flujoMaestro;
    }

    @Override
    public FlujoMaestro obtenerFlujoSolicitud(int iD, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Flujo_Solicitud;

        HttpGet get = new HttpGet( serverName + path + iD );
        get.setHeader("Authorization", "Bearer " +  Access_token );

        FlujoMaestro flujoMaestro = new FlujoMaestro();

        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());

            //flujoMaestro = new Gson().fromJson( respStr, FlujoMaestro.class );
            //return flujoMaestro;

            FlujoMaestro[] flujoMaestros = new Gson().fromJson( respStr, FlujoMaestro[].class );
            return flujoMaestros[0];
        }
        catch (Exception ex) {
            flujoMaestro = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return flujoMaestro;
    }

    @Override
    public ResultadoEnvio ReporteEnvio(Reporte datos, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Reporte_Envio;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        ResultadoEnvio respuesta = new ResultadoEnvio();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            respuesta = new Gson().fromJson(responseString, ResultadoEnvio.class);
            return respuesta;
        }
        catch (Exception ex) {
            respuesta = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return respuesta;
    }

    @Override
    public Credito obtenerDatosPrestamo(int Agencia, int Credito, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_DatosPrestamo;

        HttpGet get = new HttpGet( serverName + path + Agencia + "/" + Credito );
        get.setHeader("Authorization", "Bearer " +  Access_token );

        Credito creditodatos = new Credito();

        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());

            Credito[] credito = new Gson().fromJson( respStr, Credito[].class );
            return credito[0];
        }
        catch (Exception ex) {
            creditodatos = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return creditodatos;
    }

    @Override
    public RedResponse EnvioSms(Alerta alerta, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Alerta_SMS;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(alerta);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        boolean respuesta = false;
        Red cRed = new Red();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            cRed = new Gson().fromJson(responseString, Red.class);
            //return respuesta = cRed.iscRed();
            return new RedResponse( cRed, "Consulta correcta", true );


        } catch ( SocketTimeoutException ex ) {
            return new RedResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new RedResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new RedResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RedResponse( null, ex.getMessage(), false );
        }
        //return respuesta;
    }

    @Override
    public UserResponse VerificarEmail(User user, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Usuario_Verificar;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(user);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            User[] respuesta = new Gson().fromJson( responseString, User[].class );
            //return respuesta[0];
            return new UserResponse( respuesta[0], "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new UserResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new UserResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new UserResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new UserResponse( null, ex.getMessage(), false );
        }
        //return userspt;
    }

    @Override
    public CodPersonaResponse CambioPassword(User user, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Usuario_CambioPass;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(user);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        CodPersona codPersona = new CodPersona();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            codPersona = new Gson().fromJson( responseString, CodPersona.class );
            //return codPersona;
            return new CodPersonaResponse( codPersona, "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new CodPersonaResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new CodPersonaResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new CodPersonaResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CodPersonaResponse( null, ex.getMessage(), false );
        }
        //return codPersona;
    }

    @Override
    public Texto Desencriptar(User user, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Usuario_Desencriptar;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(user);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        Texto texto = new Texto();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            texto = new Gson().fromJson( responseString, Texto.class );
            return texto;
        }
        catch (Exception ex) {
            texto = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return texto;
    }

    @Override
    public boolean EnvioEmail(Alerta alerta, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Alerta_Email;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(alerta);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
        boolean respuesta = false;
        Red cRed = new Red();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            cRed = new Gson().fromJson(responseString, Red.class);
            return respuesta = cRed.iscRed();
        }
        catch (Exception ex) {
            respuesta = false;
            Log.e("ServicioRest", "Error!", ex);
        }
        return respuesta;
    }

    @Override
    public int obtenerRechazoxDia(String documento, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_RechazadoPorDia;
        HttpGet get = new HttpGet( serverName + path + documento );

        get.setHeader("Authorization", "Bearer " + Access_token );
        get.setHeader("content-type", "application/json");

        int response = 0;

        try {
            HttpResponse resp = httpClient.execute(get);

            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            response = Integer.parseInt( EntityUtils.toString( resp.getEntity())) ;
        }
        catch (Exception ex) {
            response = 0;
            Log.e("ServicioRest", "Error!", ex);
        }
        return response;
    }

    @Override
    public int anulaxActualizacion(String documento, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_AnulaxActualizacion;

        HttpGet get = new HttpGet( serverName + path + documento );

        get.setHeader("Authorization", "Bearer " + Access_token );
        get.setHeader("content-type", "application/json");
        int response = 0;
        try {
            HttpResponse resp = httpClient.execute(get);

            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }

            response = Integer.parseInt( EntityUtils.toString( resp.getEntity())) ;
        }
        catch (Exception ex) {
            response = 0;
            Log.e("ServicioRest", "Error!", ex);
        }

        return response;
    }

    @Override
    public int obtenerCreditoxFlujo(String documento, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_CreditoxFlujo;

        HttpGet get = new HttpGet( serverName + path + documento );

        get.setHeader("Authorization", "Bearer " + Access_token );
        get.setHeader("content-type", "application/json");
        int response = 0;
        try {
            HttpResponse resp = httpClient.execute(get);

            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }

            response = Integer.parseInt( EntityUtils.toString( resp.getEntity())) ;
        }
        catch (Exception ex) {
            response = 0;
            Log.e("ServicioRest", "Error!", ex);
        }

        return response;
    }

    @Override
    public CalendarioCreditoResponse ObtenerCalendarioMontoCuota(MontoCuota datos, String Access_token) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Calendario;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        double nMontoCuota = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());

            Calendario[] calendario = new Gson().fromJson( responseString, Calendario[].class );
            nMontoCuota = Double.parseDouble( calendario[0].getMontoCuota() );
            //return nMontoCuota;
            return new CalendarioCreditoResponse( calendario[0], "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new CalendarioCreditoResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new CalendarioCreditoResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new CalendarioCreditoResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CalendarioCreditoResponse( null, ex.getMessage(), false );
        }
        //return nMontoCuota;
    }

    @Override
    public ResultadoEnvio GenerandoReporte(FlujoMaestro datos, String Access_token) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Reporte_Generar;

        HttpGet get = new HttpGet(serverName + path + datos.getnCodCred()+"/"+datos.getnCodAge()+"/"+datos.getnClientePEP());

        get.setHeader("Authorization", "Bearer " + Access_token );
        get.setHeader("content-type", "application/json");

        ResultadoEnvio respuesta = new ResultadoEnvio();
        try {

            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( resp.getEntity() );
            respuesta = new Gson().fromJson(responseString, ResultadoEnvio.class);
            return respuesta;
        }
        catch (Exception ex) {
            respuesta = null;
            Log.e("ServicioRest", "Error!", ex);
        }
        return respuesta;
    }

    @Override
    public FlujoMaestroResponse obtenerPasosFlujo(int nidFlujoMaestro, String Access_token) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Flujo_Wizard;

        HttpGet get = new HttpGet( serverName + path + nidFlujoMaestro );
        get.setHeader("Authorization", "Bearer " +  Access_token );


        try {
            HttpResponse resp = httpClient.execute(get);
            String respStr = EntityUtils.toString(resp.getEntity());

            FlujoMaestro[] flujoMaestros = new Gson().fromJson( respStr, FlujoMaestro[].class );
            FlujoMaestro procesoActual = new FlujoMaestro();

            if ( flujoMaestros != null ){
                if ( flujoMaestros.length > Constantes.CERO ){
                    for ( FlujoMaestro dto : flujoMaestros ){
                        if ( dto.getcClassEstilo().equals( "active" ) ){
                            procesoActual.setcNombreProceso( dto.getcNombreProceso() );
                            procesoActual.setnOrdenFlujo( dto.getnOrdenFlujo() );
                            procesoActual.setcComentario( dto.getcNombreProceso() + " / " + String.valueOf( dto.getnOrdenFlujo() ) + " de " +  String.valueOf( flujoMaestros.length ) );
                        }
                    }
                }
            }

            //return new FlujoMaestroResponse( flujoMaestros[0] , "Consulta correcta", true );
            return new FlujoMaestroResponse( procesoActual , "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new FlujoMaestroResponse( null, ex.getMessage(), false );
        }
    }

    @Override
    public CalendarioListCreditoResponse ObtenerCalendarioList(MontoCuota datos, String Access_token) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Calendario;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        List<Calendario> ListCalendario = new ArrayList<Calendario>();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            JSONArray respJSON = new JSONArray( responseString );

            for ( int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);
                Calendario dto = new Calendario();
                dto.setMontoCuota( obj.getString( "montoCuota" ) );
                dto.setcFechaPago( obj.getString( "cFechaPago" ) );
                ListCalendario.add( dto );
            }

            //Calendario[] calendario = new Gson().fromJson( responseString, Calendario[].class );


            return new CalendarioListCreditoResponse( ListCalendario, "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new CalendarioListCreditoResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new CalendarioListCreditoResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new CalendarioListCreditoResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CalendarioListCreditoResponse( null, ex.getMessage(), false );
        }
    }

    @Override
    public UserListResponse verificarDocumento(String documento, String Access_token) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Persona_Verifica;

        HttpGet get = new HttpGet( serverName + path + documento );
        get.setHeader( "Authorization", "Bearer " + Access_token );
        get.setHeader( "content-type", "application/json" );

        List<User> listData = new ArrayList<>();

        try {
            HttpResponse resp = httpClient.execute(get);
            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( resp.getEntity() );
            JSONArray respJSON = new JSONArray( responseString );

            for ( int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);
                User dto = new User();
                dto.setEmail( "email" );
                dto.setFirstName( "firstname" );
                dto.setLastName( "lastName" );
                dto.setPassword( "password" );
                listData.add( dto );
            }

            return new UserListResponse( listData, "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new UserListResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new UserListResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new UserListResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new UserListResponse( null, ex.getMessage(), false );
        }
    }

    @Override
    public int verificarCelular(Persona data, String Access_token) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Persona_Celular;

        HttpGet get = new HttpGet( serverName + path + data.getnNroDoc() + "/" + data.getcCelular() );
        get.setHeader("Authorization", "Bearer " + Access_token );
        get.setHeader("content-type", "application/json");
        int response = 0;
        try {
            HttpResponse resp = httpClient.execute(get);

            if (resp.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(resp.getStatusLine().getReasonPhrase());
            }
            response = Integer.parseInt( EntityUtils.toString( resp.getEntity())) ;
        }
        catch (Exception ex) {
            response = -1;
            Log.e("ServicioRest", "Error!", ex);
        }

        return response;
    }

    @Override
    public UserListResponse verificarEmailList(User user, String Access_token) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Usuario_Verificar;

        HttpPost post = new HttpPost(serverName + path);

        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(user);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        List<User> listData = new ArrayList<>();

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            JSONArray respJSON = new JSONArray( responseString );
            for ( int i = 0; i < respJSON.length(); i++ ) {
                JSONObject obj = respJSON.getJSONObject(i);
                User dto = new User();
                dto.setEmail( "email" );
                dto.setFirstName( "firstname" );
                dto.setLastName( "lastName" );
                dto.setPassword( "password" );
                listData.add( dto );
            }

            return new UserListResponse( listData, "Consulta correcta", true );

        } catch ( SocketTimeoutException ex ) {
            return new UserListResponse( null, ex.getMessage(), false );
        } catch ( FileNotFoundException ex ) {
            return new UserListResponse( null, ex.getMessage(), false );
        } catch ( IOException ex ){
            return new UserListResponse( null, ex.getMessage(), false );
        } catch (Exception ex) {
            ex.printStackTrace();
            return new UserListResponse( null, ex.getMessage(), false );
        }
    }

    @Override
    public double ObtenerTCEA(MontoCuota datos, String Access_token) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        String serverName = Constantes.IP_CONEXIONYPUERTO_LUCAS;
        String path = Constantes.Lucas_WebApi_Credito_Tcea;

        HttpPost post = new HttpPost(serverName + path);
        post.setHeader("Authorization", "Bearer " + Access_token );
        post.setHeader("content-type", "application/json");

        String dataJson = new Gson().toJson(datos);
        post.setEntity(new StringEntity(dataJson, HTTP.UTF_8));

        double response = 0;

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(httpResponse.getStatusLine().getReasonPhrase());
            }
            String responseString = EntityUtils.toString( httpResponse.getEntity() );
            response = Double.parseDouble( responseString ) ;
            return response;
        }
        catch (Exception ex) {
            response = -1;
            Log.e("ServicioRest", "Error!", ex);
        }
        return response;
    }

}
