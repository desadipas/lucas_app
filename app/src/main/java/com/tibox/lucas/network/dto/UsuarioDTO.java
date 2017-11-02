package com.tibox.lucas.network.dto;

import java.io.Serializable;

/**
 * Created by desa02 on 28/02/2017.
 */

public class UsuarioDTO implements Serializable{
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
