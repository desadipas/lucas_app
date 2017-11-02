package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.Calendario;
import java.util.List;

/**
 * Created by desa02 on 11/10/2017.
 */

public class CalendarioListCreditoResponse {
    public CalendarioListCreditoResponse( List<Calendario> listdata, String response, boolean success ){
        m_listdata = listdata;
        m_response = response;
        m_success = success;
    }

    private List<Calendario> m_listdata;
    private String m_response;
    private boolean m_success;

    public List<Calendario> getM_listdata() {
        return m_listdata;
    }

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }
}
