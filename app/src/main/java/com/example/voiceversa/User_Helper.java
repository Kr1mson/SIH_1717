package com.example.voiceversa;

public class User_Helper {
    String name, email, pswd,username;

    public User_Helper() {
    }

    public User_Helper(String name, String email, String pswd, String username) {
        this.name = name;
        this.email = email;
        this.pswd = pswd;
        this.username = username;
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

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
