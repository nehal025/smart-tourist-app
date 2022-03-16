package com.example.smarttourapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class RegisterBody {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;


    public RegisterBody(String username, String password) {
        super();
        this.username = username;
        this.password = password;

    }


    public RegisterBody(String username, String name, String password) {
        super();
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

