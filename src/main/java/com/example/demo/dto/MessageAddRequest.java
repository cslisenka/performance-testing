package com.example.demo.dto;

public class MessageAddRequest extends Request {

    private String user;
    private String message;
    private String room;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "{" +
                "user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}