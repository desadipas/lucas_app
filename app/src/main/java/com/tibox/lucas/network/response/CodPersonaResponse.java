package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.CodPersona;

/**
 * Created by desa02 on 05/09/2017.
 */

public class CodPersonaResponse {
    public CodPersonaResponse(CodPersona data, String response, boolean success ){
        m_data = data;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private CodPersona m_data;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public CodPersona getM_data() {
        return m_data;
    }
}
