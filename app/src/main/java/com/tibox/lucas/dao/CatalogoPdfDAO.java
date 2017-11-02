package com.tibox.lucas.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.URI;

/**
 * Created by desa02 on 29/05/2017.
 */

public class CatalogoPdfDAO {

    public Bitmap getDescargarPDF( DocumentosPdfDTO param ) {
        Bitmap bmp = null;
        try {
            /*
            String serverName = AppConfig.connection.getPrivatePedidoUrl();
            HttpGet httpRequest = new HttpGet(URI.create( serverName + "/api/GetImageArticulo/GetImage" + "?cSKU=" + cSKU
                    + "&nIdFotoArticulo=" + oFotoArticuloDTO.getnIdFotoArticulo() ) );
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) httpclient.execute( httpRequest );
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity( entity );
            bmp = BitmapFactory.decodeStream( bufHttpEntity.getContent() );
            httpRequest.abort();
            */

            String serverName = AppConfig.connection.getPrivatePedidoUrl();
            HttpPost httpRequest = new HttpPost(serverName + "");
            HttpParams httpParameters = new BasicHttpParams();
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpRequest.setHeader("content-type", "application/json");
            String dataJson = new Gson().toJson(param);
            httpRequest.setEntity(new StringEntity(dataJson, HTTP.UTF_8));
            HttpResponse response = httpClient.execute(httpRequest);

            if (response.getStatusLine().getStatusCode() >= 400) {
                throw new IOException(response.getStatusLine().getReasonPhrase());
            }

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity( entity );
            bmp = BitmapFactory.decodeStream( bufHttpEntity.getContent() );
            httpRequest.abort();


        }
        catch ( IOException error )
        {
            bmp = null;
        }
        catch ( Exception e ) {
            bmp = null;
        }
        finally {
            if ( bmp == null ) {
                bmp = null;
                return bmp;
            }
        }
        return bmp;
    }
}
