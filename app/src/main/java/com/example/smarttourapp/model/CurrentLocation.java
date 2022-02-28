package com.example.smarttourapp.model;

public class CurrentLocation {

    private String city ;
    private String district ;
    private String state ;
    private String pinCode ;
    private String country ;
    private String latitude ;
    private String longitude;
    private String addressLine ;

    public CurrentLocation(String city, String district, String state, String pinCode, String country, String latitude, String longitude, String addressLine) {
        this.city = city;
        this.district = district;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressLine = addressLine;
    }

    public CurrentLocation(CurrentLocation currentLocation) {
        this.city = currentLocation.getCity();
        this.district = currentLocation.getDistrict();
        this.state = currentLocation.getState();
        this.pinCode = currentLocation.getPinCode();
        this.country = currentLocation.getCountry();
        this.latitude = currentLocation.getLatitude();
        this.longitude = currentLocation.getLongitude();
        this.addressLine = currentLocation.getAddressLine();
    }




    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
}
