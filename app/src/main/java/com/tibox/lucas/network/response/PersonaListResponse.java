package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.Persona;

import java.util.List;

/**
 * Created by desa02 on 17/10/2017.
 */

public class PersonaListResponse {
    public PersonaListResponse( List<Persona> listData, String response, boolean success ){
        m_listData = listData;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private List<Persona> m_listData;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public List<Persona> getM_listData() {
        return m_listData;
    }
}
