package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.CalendarioDTO;

import java.util.List;

/**
 * Created by desa02 on 05/09/2017.
 */

public class CalendarioResponse {
    public CalendarioResponse(List<CalendarioDTO> listdata, String response, boolean success){
        m_listdata = listdata;
        m_response = response;
        m_success = success;
    }
    private List<CalendarioDTO> m_listdata;
    private String m_response;
    private boolean m_success;

    public List<CalendarioDTO> getM_listdata() {
        return m_listdata;
    }

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }
}
