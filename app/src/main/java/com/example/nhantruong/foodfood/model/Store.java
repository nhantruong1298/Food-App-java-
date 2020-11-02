package com.example.nhantruong.foodfood.model;

public class Store {
    private String Name;
    private String Address;
    private String Image;

    public Store() {
        //Phương thức phải có để đọc và ghi dữ liệu lên firebase
    }

    public Store(String name, String address, String image) {
        Name = name;
        Address = address;
        Image = image;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getImage() {
        return Image;
    }
}
