package com.example.demo.dto;

import java.io.Serializable;

public class Room implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
