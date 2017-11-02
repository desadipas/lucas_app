package com.tibox.lucas.network.connections;

/**
 * Created by desa02 on 01/03/2017.
 */

public interface ConnectionInfo {
    public String getPrivatePedidoUrl();
    public String getPublicPedidUrl();
    public abstract String getServerUrl( boolean hasInternet );
}
