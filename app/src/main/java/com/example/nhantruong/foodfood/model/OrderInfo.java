package com.example.nhantruong.foodfood.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderInfo  implements Serializable {
    CustomerInfo customerInfo;
    DriverInfo driverInfo;
    String Status;
    String Time;
    String Date;
    String Total;
    ArrayList<FoodOfOrder> foodOfOrders ;

    public OrderInfo() {}

    public OrderInfo(CustomerInfo customerInfo, DriverInfo driverInfo, String status, String time, String date, String total, ArrayList<FoodOfOrder> foodOfOrders) {
        this.customerInfo = customerInfo;
        this.driverInfo = driverInfo;
        Status = status;
        Time = time;
        Date = date;
        Total = total;
        this.foodOfOrders = foodOfOrders;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public void setFoodOfOrders(ArrayList<FoodOfOrder> foodOfOrders) {
        this.foodOfOrders = foodOfOrders;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public String getStatus() {
        return Status;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public String getTotal() {
        return Total;
    }

    public ArrayList<FoodOfOrder> getFoodOfOrders() {
        return foodOfOrders;
    }
}

