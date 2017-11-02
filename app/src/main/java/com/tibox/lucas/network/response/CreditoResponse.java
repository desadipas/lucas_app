package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosSalida.Credito;

import java.util.List;

/**
 * Created by desa02 on 04/09/2017.
 */

public class CreditoResponse {

    public CreditoResponse( List<Credito> listdata, String response, boolean success ){
        m_listdata = listdata;
        m_response = response;
        m_success = success;
    }

    private List<Credito> m_listdata;
    private String m_response;
    private boolean m_success;

    public boolean isM_success() {
        return m_success;
    }

    public String getM_response() {
        return m_response;
    }

    public List<Credito> getM_listdata() {
        return m_listdata;
    }
}
