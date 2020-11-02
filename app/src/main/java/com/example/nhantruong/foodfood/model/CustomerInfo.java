package com.example.nhantruong.foodfood.model;

import java.io.Serializable;

public class CustomerInfo implements Serializable {
    String Name;
    String Address;
    String Phone;
    String Email;
    String Longitude;
    String Latitude;

    public CustomerInfo() {
    }

    public CustomerInfo(String name, String address, String phone, String email,String longitude,String latitude) {
        Name = name;
        Address = address;
        Phone = phone;
        Email = email;
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

    public void setAddress(String address) {
        Address = address;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }
}
