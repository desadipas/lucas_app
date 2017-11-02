package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.PersonaRespDTO;

/**
 * Created by desa02 on 05/09/2017.
 */

public class PersonaRespResponse {
    public PersonaRespResponse( PersonaRespDTO data, String response, boolean success ){
        m_data = data;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private PersonaRespDTO m_data;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public PersonaRespDTO getM_data() {
        return m_data;
    }
}
