package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.Red;

/**
 * Created by desa02 on 05/09/2017.
 */

public class RedResponse {
    public RedResponse( Red data, String response, boolean success ){
        m_data = data;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private Red m_data;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public Red getM_data() {
        return m_data;
    }
}
