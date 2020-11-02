package com.example.nhantruong.foodfood.model;

import java.util.ArrayList;

public class HistoryOrder {
    String CodeOrder;
    String Date;
    String Status;
    String Total;
    ArrayList<FoodOfOrder> foodOfOrders;

    public HistoryOrder() {
    }

    public HistoryOrder(String codeOrder, String date, String total, String status,ArrayList<FoodOfOrder> foodOfOrders) {
        CodeOrder = codeOrder;
        Date = date;
        this.foodOfOrders = foodOfOrders;
        Total = total;
        Status = status;
    }

    public void setCodeOrder(String codeOrder) {
        CodeOrder = codeOrder;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setFoodOfOrders(ArrayList<FoodOfOrder> foodOfOrders) {
        this.foodOfOrders = foodOfOrders;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCodeOrder() {
        return CodeOrder;
    }

    public String getDate() {
        return Date;
    }

    public ArrayList<FoodOfOrder> getFoodOfOrders() {
        return foodOfOrders;
    }

    public String getTotal() {
        return Total;
    }

    public String getStatus() {
        return Status;
    }
}
