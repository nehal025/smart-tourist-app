package com.example.smarttourapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class LoginResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private String data;



    public LoginResponse(String status, String data) {
        super();
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}





