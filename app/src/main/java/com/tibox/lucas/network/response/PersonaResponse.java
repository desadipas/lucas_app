package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.Persona;

/**
 * Created by desa02 on 04/09/2017.
 */

public class PersonaResponse {
    public PersonaResponse( Persona data, String response, boolean success ){
        m_data = data;
        m_response = response;
        m_success = success;

    }
    private String m_response;
    private boolean m_success;
    private Persona m_data;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public Persona getM_data() {
        return m_data;
    }
}
