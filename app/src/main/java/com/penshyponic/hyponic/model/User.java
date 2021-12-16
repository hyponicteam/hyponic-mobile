package com.penshyponic.hyponic.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Time time;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
