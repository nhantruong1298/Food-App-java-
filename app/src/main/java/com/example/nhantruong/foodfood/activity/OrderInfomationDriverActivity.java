package com.example.nhantruong.foodfood.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.pager_adapter.PagerAdapter_OrderInfomation_Driver;
import com.example.nhantruong.foodfood.fragment.OrderHistory_Fragment;
import com.example.nhantruong.foodfood.fragment.OrderInfomation_Driver_Fragment;
import com.example.nhantruong.foodfood.model.OrderInfo;

public class OrderInfomationDriverActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbarMain;
    public static OrderInfo orderInfoMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_infomation_driver);
        Mapping();
        ActionBar();
        Init();
    }

    private void Init() {
        OrderInfomation_Driver_Fragment fmOrder = new OrderInfomation_Driver_Fragment();
        OrderHistory_Fragment fmHistory = new OrderHistory_Fragment();




        if(orderInfoMain != null)
        {
            OrderInfomation_Driver_Fragment.orderInfoMain = orderInfoMain;
        }



        PagerAdapter_OrderInfomation_Driver adapter = new PagerAdapter_OrderInfomation_Driver(getSupportFragmentManager(),fmOrder,fmHistory);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void Mapping(){
        tabLayout = findViewById(R.id.tabLayout_OrderInfomation_Driver);
        viewPager = findViewById(R.id.viewPager_OrderInfomation_Driver);
        toolbarMain = findViewById(R.id.toolBar_OrderInfo_DriverActivity);
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
}
