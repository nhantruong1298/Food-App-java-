package com.example.nhantruong.foodfood.model;

public class ItemMenu {
    private String Name;
    private int Image;

    public ItemMenu( String name, int image) {
        Name = name;
        Image = image;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setImage(int image) {
        Image = image;
    }


    public String getName() {
        return Name;
    }

    public int getImage() {
        return Image;
    }
}
