package com.example.demo.dto;

import java.io.Serializable;

public class Request implements Serializable {

    private int delay;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
