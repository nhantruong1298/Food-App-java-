package com.example.nhantruong.foodfood.adapter.pager_adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nhantruong.foodfood.fragment.OrderHistory_Fragment;
import com.example.nhantruong.foodfood.fragment.OrderInfomation_Customer_Fragment;

public class PagerAdapter_OrderInfomation_Customer extends FragmentPagerAdapter {
    OrderInfomation_Customer_Fragment fmOrderInfo;
    OrderHistory_Fragment fmHistory;


    public PagerAdapter_OrderInfomation_Customer(FragmentManager fm,OrderInfomation_Customer_Fragment fmOrderInfo,OrderHistory_Fragment fmHistory) {
        super(fm);
        this.fmOrderInfo = fmOrderInfo;
        this.fmHistory = fmHistory;
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0)
        {
            return fmOrderInfo;
        }
        else if(i ==1)
        {
            return fmHistory;
        }
        else
        {

        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {
        if(i == 0)
        {
            return "Đơn hàng đang chờ ";
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

    @Override
    public int getCount() {
        return 2;
    }
}
