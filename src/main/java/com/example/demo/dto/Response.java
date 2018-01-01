package com.example.demo.dto;

import java.io.Serializable;

public class Response implements Serializable {

    private String message;

    public Response() {
    }

    public Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
