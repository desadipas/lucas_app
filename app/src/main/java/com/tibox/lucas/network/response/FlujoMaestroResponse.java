package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;

/**
 * Created by desa02 on 05/09/2017.
 */

public class FlujoMaestroResponse {

    public FlujoMaestroResponse(FlujoMaestro data, String response, boolean success){
        m_data = data;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private FlujoMaestro m_data;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public FlujoMaestro getM_data() {
        return m_data;
    }
}
