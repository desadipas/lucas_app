/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tibox.lucas.network.access.consumirwebservices;

import com.tibox.lucas.network.dto.DatosEntrada.ELDocumento;
import com.tibox.lucas.network.dto.DatosEntrada.ELScoringDemograficoDatos;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;


/**
 *
 * @author Raúl Alva de inversiones ralva
 */
public class ConsumirWebServices {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        Datos loDatos = new Datos();
        
        
        //Score Buro: 1; Score Demografico: 2; Mora Comercial: 21; Score Lenddo: 3; Score de Comportamiento: 4;
        //Ingreso Predecido Demografico: 1; Ingreso Predecido RCC: 2; Ingreso Predecido Final: 3;
        //Scoring Buro: 1; Scoring Buro Cuotas Utilizada: 11; Scoring Demografico: 2;
        loDatos.nTipoServicio = 2; 
        
        //======================================================================
        //Esto para el Score de Buro:
        //----------------------------------------------------------------------
        //ELScoreBuroDatos loDatosServicio = new ELScoreBuroDatos();
        //loDatosServicio.sNroDoc = "41484910";
        //======================================================================
        
        //======================================================================
        //Esto para el Score Demografico:
        //----------------------------------------------------------------------
        //ELScoreDemograficoDatos loDatosServicio = new ELScoreDemograficoDatos();
        //ELDocumento loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 1;
        //loDocumento.sNroDoc = "41484910";
        //loDatosServicio.oDocumento = loDocumento;
        //loDatosServicio.nCondicionLaboral = 1;
        //loDatosServicio.nDepartamento = "130101";
        //loDatosServicio.nEdad = 34;
        //loDatosServicio.nEstadoCivil = 1;
        //loDatosServicio.nGenero = 1;
        //loDatosServicio.nIngresoSalarial = 4500;
        //loDatosServicio.nMoraComercial = 0;
        //loDatosServicio.nRankingTop10000 = 200;
        //loDatosServicio.nVivienda = 1;
        //======================================================================
        
        //======================================================================
        //Esto para la mora comercial:
        //----------------------------------------------------------------------
        //ELDocumento loDatosServicio = new ELDocumento();
        //loDatosServicio.nTipoDoc = 1;
        //loDatosServicio.sNroDoc = "41484910";
        //======================================================================
        
        //======================================================================
        //Esto para el Score Lenddo:
        //----------------------------------------------------------------------
        //ELScoreLenddoDatos loDatosServicio = new ELScoreLenddoDatos();
        //ELDocumento loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 1;
        //loDocumento.sNroDoc = "41484910";
        //loDatosServicio.oDocumento=loDocumento;
        //======================================================================
        
        //======================================================================
        //Esto para el Score de Comportamiento:
        //----------------------------------------------------------------------
        //ELScoreComportamientoDatos loDatosServicio = new ELScoreComportamientoDatos();
        //loDatosServicio.nProducto = 1;
        //loDatosServicio.nCodPers = 26433;
        //======================================================================
        
        //======================================================================
        //Esto para el Ingreso Predecido Demografico:
        //----------------------------------------------------------------------
        //ELIngresoPredecidoDemograficoDatos loDatosServicio = new ELIngresoPredecidoDemograficoDatos();
        //loDatosServicio.nConstante = 50;
        //loDatosServicio.nEdad = 34;
        //loDatosServicio.nEstadoCivil = 1;
        //loDatosServicio.nGenero = 1;
        //loDatosServicio.sCIIU = 15;
        //loDatosServicio.sUbiGeo = "130201";
        //======================================================================
        
        //======================================================================
        //Esto para el Ingreso Predecido RCC:
        //----------------------------------------------------------------------
        //ELIngresoPredecidoRCCDatos loDatosServicio = new ELIngresoPredecidoRCCDatos();
        //loDatosServicio.nEstadoCivil = 1;
        //ELDocumento loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 1;
        //loDocumento.sNroDoc = "41484910";
        //loDatosServicio.oDocumentoTitular = loDocumento;
        //loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 0;
        //loDocumento.sNroDoc = "";
        //loDatosServicio.oDocumentoConyuge = loDocumento;
        //======================================================================
        
        //======================================================================
        //Esto para el Ingreso Predecido Final:
        //----------------------------------------------------------------------
        //ELIngresoPredecidoDatos loDatosServicio = new ELIngresoPredecidoDatos();
        //ELDocumento loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 1;
        //loDocumento.sNroDoc = "41484910";
        //loDatosServicio.oDocumentoTitular = loDocumento;
        //loDatosServicio.nIngresoPredecidoDemografico = 3500.0;
        //loDatosServicio.nIngresoPredecidoRCC = 3000.0;
        //loDatosServicio.oDatosDemografico = new ELIngresoPredecidoDemograficoDatos();
        //loDatosServicio.oDatosRCC = new ELIngresoPredecidoRCCDatos();
        //======================================================================
        
        //======================================================================
        //Esto para el Scoring Buro:
        //----------------------------------------------------------------------
        //ELScoringBuroDatos loDatosServicio = new ELScoringBuroDatos();
        //loDatosServicio.nProducto = 7;
        //loDatosServicio.nModalidad = 1;
        //ELDocumento loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 1;
        //loDocumento.sNroDoc = "41484910";
        //loDatosServicio.oDocumentoTitual = loDocumento;
        //loDatosServicio.nIngresoPredecido = 4500;
        //loDatosServicio.nCuotaUtilizada = -300.0;
        //loDatosServicio.nMoraComercial = 0;
        //loDatosServicio.nScore = 747;
        //loDatosServicio.nScoreOtros = 0;
        //loDatosServicio.nGarantia = 0.0;
        //======================================================================
        
        //======================================================================
        //Esto para el Scoring Buro Cuotas Utilizada:
        //----------------------------------------------------------------------
        //ELScoringBuroCuotaUtilizadaDatos loDatosServicio = new ELScoringBuroCuotaUtilizadaDatos();
        //loDatosServicio.nEstadoCivil = 1;
        //ELDocumento loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 1;
        //loDocumento.sNroDoc = "41484910";
        //loDatosServicio.oDocumentoTitular = loDocumento;
        //loDocumento = new ELDocumento();
        //loDocumento.nTipoDoc = 0;
        //loDocumento.sNroDoc = "";
        //loDatosServicio.oDocumentoConyuge = loDocumento;
        //======================================================================
        
        //======================================================================
        //Esto para el Scoring Demografico:
        //----------------------------------------------------------------------
        ELScoringDemograficoDatos loDatosServicio = new ELScoringDemograficoDatos();
        loDatosServicio.nProducto = 7;
        loDatosServicio.nModalidad = 1;
        ELDocumento loDocumento = new ELDocumento();
        loDocumento.nTipoDoc = 1;
        loDocumento.sNroDoc = "41484910";
        loDatosServicio.oDocumentoTitual = loDocumento;
        loDatosServicio.nIngresoPredecido = 4500.0;
        loDatosServicio.nMoraComercial = 0;
        loDatosServicio.nScore = 750;
        loDatosServicio.nScoreOtros = 850.0;
        loDatosServicio.nGarantia = 0.0;
        //======================================================================
        
        loDatos.oDatosServicio = loDatosServicio;
        
        
//        // Para un Get
//        HttpClient client;
//        client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet("http://10.19.150.18:1811/SoftBoxApi/Test/41484910");
//        // Ejecuta
//        HttpResponse response = client.execute(request);
//                
//        BufferedReader dr = new BufferedReader( new InputStreamReader(response.getEntity().getContent()));
//        String line = "";
//        while((line = dr.readLine()) != null){
//            System.out.println("resultado: " + line);
//        }
        
        // Para un Post
//        String stringJson = "";
//        JSONObject json= new JSONObject();
//        json.getClass();
//        System.out.println(stringJson);

        Gson gson = new Gson();
        
        String jSon = "";        
        jSon = gson.toJson(loDatos);        
        System.out.print("Entrada: " + jSon);
        
        
        HttpClient httpClient = new DefaultHttpClient();
        //url y tipo de contenido
        // Para Score:
        //HttpPost httppost = new HttpPost("http://10.19.150.18:1811/SoftBoxApi/Score");
        // Para Ingreso Predecido:
        //HttpPost httppost = new HttpPost("http://10.19.150.18:1811/SoftBoxApi/IngresoPredecido");
        // Para Scoring
        HttpPost httppost = new HttpPost("http://10.19.150.18:1811/SoftBoxApi/Scoring");
        httppost.addHeader("Content-Type", "application/json");
        
        StringEntity stringEntity = new StringEntity(jSon);
        
        stringEntity.setContentType( (Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
        httppost.setEntity(stringEntity); 
        
        // Invocamos el Webservice
        HttpResponse postresponse = httpClient.execute(httppost);
        
        // Para hacer un salto de linea
        System.out.println(" ");
        
        //Obtenemos el JSON
        BufferedReader ldr = new BufferedReader( new InputStreamReader(postresponse.getEntity().getContent()));
        String postline = "";
        while((postline = ldr.readLine()) != null){
            jSon = postline;
            System.out.println("Salida: " + jSon);
        }
        
        // Para hacer un salto de linea
        System.out.println(" ");
        
        //======================================================================
        //Esto para el Score Demografico:
        //----------------------------------------------------------------------
        //ELScoreDemograficoResultado loScoreDemografico = new ELScoreDemograficoResultado();
        //loScoreDemografico = gson.fromJson(jSon, ELScoreDemograficoResultado.class);
        //
        //Para ver los items
        //System.out.println("Número de registros: " + loScoreDemografico.oScoreItems.size());
        //for (int i = 0; i < loScoreDemografico.oScoreItems.size() -1; i++) {
        //    System.out.println(loScoreDemografico.oScoreItems.get(i).sScoreDescripcion);
        //}
        //======================================================================
        
    }
   
}
