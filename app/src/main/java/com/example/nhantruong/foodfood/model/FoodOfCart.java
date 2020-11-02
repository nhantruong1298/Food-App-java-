package com.example.nhantruong.foodfood.model;

import java.io.Serializable;

public class FoodOfCart implements Serializable {
    String NameStore;
    String NameFood;
    String Price;
    String Image;
    int Count;

    public FoodOfCart() {
    }

    public FoodOfCart(String nameStore, String nameFood, String price, String image, int count ) {
        NameStore = nameStore;
        NameFood = nameFood;
        Price = price;
        Image = image;
        Count = count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    public String getNameStore() {
        return NameStore;
    }

    public String getNameFood() {
        return NameFood;
    }

    public String getPrice() {
        return Price;
    }

    public String getImage() {
        return Image;
    }

    public void setNameStore(String nameStore) {
        NameStore = nameStore;
    }

    public void setNameFood(String nameFood) {
        NameFood = nameFood;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setImage(String image) {
        Image = image;
    }
}
