package com.example.nhantruong.foodfood.model;

public class AccountInfo {
    String SurName;
    String FirstName;
    String Address;
    String Phone;
    String Email;

    public AccountInfo() {
        //Dùng để đọc và ghi dữ liệu lên database
    }

    public AccountInfo(String surName, String firstName, String address, String phone, String email) {
        SurName = surName;
        FirstName = firstName;
        Address = address;
        Phone = phone;
        Email = email;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
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

    public String getSurName() {
        return SurName;
    }

    public String getFirstName() {
        return FirstName;
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
