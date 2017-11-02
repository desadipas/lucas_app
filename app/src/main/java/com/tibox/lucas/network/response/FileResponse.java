package com.tibox.lucas.network.response;

import java.io.File;

/**
 * Created by desa02 on 05/09/2017.
 */

public class FileResponse {
    public FileResponse(File data, String response, boolean success){
        m_data = data;
        m_response = response;
        m_success = success;
    }
    private String m_response;
    private boolean m_success;
    private File m_data;

    public String getM_response() {
        return m_response;
    }

    public boolean isM_success() {
        return m_success;
    }

    public File getM_data() {
        return m_data;
    }
}
