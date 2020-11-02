package com.example.nhantruong.foodfood.adapter.pager_adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nhantruong.foodfood.fragment.OrderHistory_Fragment;
import com.example.nhantruong.foodfood.fragment.OrderInfomation_Driver_Fragment;

public class PagerAdapter_OrderInfomation_Driver extends FragmentPagerAdapter {
    OrderInfomation_Driver_Fragment orderInfomation_driver_fragment;
    OrderHistory_Fragment orderHistory__fragment;


    public PagerAdapter_OrderInfomation_Driver(FragmentManager fm,OrderInfomation_Driver_Fragment orderInfomation_driver_fragment,OrderHistory_Fragment orderHistory__fragment) {
        super(fm);
        this.orderInfomation_driver_fragment = orderInfomation_driver_fragment;
        this.orderHistory__fragment = orderHistory__fragment;
    }

    @Override
    public Fragment getItem(int i) {

        if(i == 0)
        {
            return orderInfomation_driver_fragment;
        }
        else if(i ==1)
        {
            return orderHistory__fragment;
        }
        else
        {

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {
        if(i == 0)
        {
            return "Đơn hàng đã nhận ";
        }
        else if(i ==1)
        {
            return "Lịch sử đơn hàng";
        }
        else
        {

        }
        return null;
    }
}
