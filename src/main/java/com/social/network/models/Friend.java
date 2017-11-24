package com.social.network.models;


import javafx.beans.DefaultProperty;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedList;

@Entity
public class Friend {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;

    @Id
    private String email;
    private String friendEmail;
    private Boolean pending;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public boolean equals(Object obj) {
        return this.email.equals(((Friend) obj).email) && this.friendEmail.equals(((Friend) obj).getFriendEmail());
}

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }
}
