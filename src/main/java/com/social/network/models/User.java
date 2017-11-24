package com.social.network.models;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    private String username;

    private String password;
    private String role;
    private int enabled;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = "{noop}" + password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        return username.equals(((User)obj).username) && password.equals(((User)obj).password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
