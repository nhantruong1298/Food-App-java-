package com.example.nhantruong.foodfood.model;

import java.io.Serializable;

public class FoodOfOrder implements Serializable {
    String StoreName;
    String AddressStore;
    String FoodName;
    int Count;
    String Price;

    public FoodOfOrder() {
    }

    public FoodOfOrder(String StoreName, String addressStore, String foodName, int  count, String price) {
        this.StoreName = StoreName;
        AddressStore = addressStore;
        FoodName = foodName;
        Count = count;
        Price = price;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public String getAddressStore() {
        return AddressStore;
    }

    public String getFoodName() {
        return FoodName;
    }



    public String getPrice() {
        return Price;
    }



    public void setAddressStore(String addressStore) {
        AddressStore = addressStore;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }



    public void setPrice(String price) {
        Price = price;
    }
}
