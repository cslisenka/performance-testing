package com.example.demo.dto;

import java.io.Serializable;

public class User implements Serializable {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
