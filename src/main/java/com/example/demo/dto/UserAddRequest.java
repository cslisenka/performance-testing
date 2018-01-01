package com.example.demo.dto;

public class UserAddRequest extends Request {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "{" +
                "login='" + login + '\'' +
                '}';
    }
}
