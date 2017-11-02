package com.tibox.lucas.network.response;

import com.tibox.lucas.network.dto.DatosEntrada.User;

import java.util.List;

/**
 * Created by desa02 on 17/10/2017.
 */

public class UserListResponse {
    public UserListResponse( List<User> listData, String response, boolean success ){
        m_listData = listData;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private List<User> m_listData;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public List<User> getM_listData() {
        return m_listData;
    }
}
