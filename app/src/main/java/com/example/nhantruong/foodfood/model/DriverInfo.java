package com.example.nhantruong.foodfood.model;

import java.io.Serializable;

public class DriverInfo implements Serializable {
    String Name;
    String Email;
    String Phone;
    String Longitude;
    String Latitude;

    public DriverInfo() {
    }

    public DriverInfo(String name, String email, String phone,String longitude,String latitude) {
        Name = name;
        Email = email;
        Phone = phone;
        Longitude = longitude;
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }
}
