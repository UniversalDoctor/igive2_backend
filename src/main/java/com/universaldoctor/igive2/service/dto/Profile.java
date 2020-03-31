package com.universaldoctor.igive2.service.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public class Profile {

    private byte[] icon;
    private String username;
    private String email;
    private String status;

    public Profile(byte[] icon, String username, String email, String status) {
        this.icon = icon;
        this.username = username;
        this.email = email;
        this.status = status;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
