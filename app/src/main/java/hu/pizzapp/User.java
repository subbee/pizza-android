package hu.pizzapp;

import com.google.gson.annotations.SerializedName;

public class User {

    private int id;
    private String name;
    private String email;
    private String role;


    // Getterek
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
