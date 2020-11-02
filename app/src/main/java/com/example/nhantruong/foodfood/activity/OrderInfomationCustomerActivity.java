package com.example.nhantruong.foodfood.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.pager_adapter.PagerAdapter_OrderInfomation_Customer;
import com.example.nhantruong.foodfood.fragment.OrderHistory_Fragment;
import com.example.nhantruong.foodfood.fragment.OrderInfomation_Customer_Fragment;

public class OrderInfomationCustomerActivity extends AppCompatActivity {

    Toolbar toolbarMain;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_infomation_customer);

        Mapping();
        ActionBar();
        Init();
    }



    private void Mapping() {
        toolbarMain = findViewById(R.id.toolBar_OrderInfo_CustActivity);
        tabLayout = findViewById(R.id.tabLayout_OrderInfomation_Customer);
        viewPager = findViewById(R.id.viewPager_OrderInfomation_Customer);
    }

    private void ActionBar()
    {
        setSupportActionBar(toolbarMain);
        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void Init() {
        OrderInfomation_Customer_Fragment fmOrderInfo = new OrderInfomation_Customer_Fragment();
        OrderHistory_Fragment fmHistory = new OrderHistory_Fragment();



        PagerAdapter_OrderInfomation_Customer adapter = new PagerAdapter_OrderInfomation_Customer(getSupportFragmentManager(),fmOrderInfo,fmHistory);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
