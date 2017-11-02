package com.tibox.lucas.network.connections;

import com.tibox.lucas.utilidades.Constantes;

/**
 * Created by desa02 on 01/03/2017.
 */

public class ConexionLocal implements ConnectionInfo{
    @Override
    public String getPrivatePedidoUrl() {
        return Constantes.IP_CONEXIONYPUERTO;
    }

    @Override
    public String getPublicPedidUrl() {
        return Constantes.IP_CONEXIONYPUERTO;
    }

    @Override
    public String getServerUrl(boolean hasInternet) {
        return null;
    }
}
