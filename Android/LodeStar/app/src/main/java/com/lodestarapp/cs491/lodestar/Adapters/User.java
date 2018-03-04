package com.lodestarapp.cs491.lodestar.Adapters;

/**
 * Created by efeulasakayseyitoglu on 21/02/2018.
 */

public class User {

    private String email;
    private String name;
    private String surname;
    public String uid;

    public User(String name, String email) {

        this.name = name;
        this.email = email;

    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return surname;
    }

    public void setName(String str) {
        name = str;
    }

    public void setLastName(String str) {
        surname = str;
    }

    public void email(String str) {
        email = str;
    }

}
