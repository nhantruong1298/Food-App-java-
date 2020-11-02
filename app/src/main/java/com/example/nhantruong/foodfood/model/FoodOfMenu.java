package com.example.nhantruong.foodfood.model;

public class FoodOfMenu {
    private String Name;
    private String Price;
    private String Image;

    public FoodOfMenu() {
    }

    public FoodOfMenu(String name, String price, String image) {
        Name = name;
        Price = price;
        Image = image;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getImage() {
        return Image;
    }
}
