package com.example.nhantruong.foodfood.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.FragmentOrderInfoDriverAdapter;
import com.example.nhantruong.foodfood.model.FoodOfOrder;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckOrderCustomer extends AppCompatActivity {

    ListView listviewMain;
    TextView tvName,tvPhone,tvDate,tvTotal;
    Button btMap;
    Toolbar toolbarMain;
    //
    DatabaseReference mDatabase;
    //
    OrderInfo orderInfo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order_customer);

        Mapping();
        Init();
        LoadListView();
        ActionBar();
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

    private void ActionBar()
    {
        setSupportActionBar(toolbarMain);
        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void LoadListView() {
        if(orderInfo.getFoodOfOrders() != null)
        {
            FragmentOrderInfoDriverAdapter adapter = new FragmentOrderInfoDriverAdapter(this,orderInfo.getFoodOfOrders());

            listviewMain.setAdapter(adapter);

            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!orderInfo.getDriverInfo().getEmail().equals("null"))
                    {
                        Intent intent = new Intent(CheckOrderCustomer.this,CustomerMapsActivity.class);
                        intent.putExtra("DriverEmail",orderInfo.getDriverInfo().getEmail());

                        startActivity(intent);
                    }
                }
            });
        }

    }

    private void Init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        if(intent != null) {
            orderInfo = (OrderInfo) intent.getSerializableExtra("orderInfo");
            tvName.setText("Tên tài xế : "+orderInfo.getDriverInfo().getName());
            tvPhone.setText("SĐT : "+orderInfo.getDriverInfo().getPhone());
            tvDate.setText("Ngày : "+orderInfo.getDate());

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvTotal.setText("Tổng đơn : "+decimalFormat.format(Integer.parseInt(orderInfo.getTotal()))+"đ");

        }

    }

    private void Mapping() {
        listviewMain = findViewById(R.id.listView_CheckOrder);
        tvName = findViewById(R.id.tvNameDriver_CheckOrder);
        tvPhone = findViewById(R.id.tvPhone_CheckOrder);
        tvDate = findViewById(R.id.tvDate_CheckOrder);
        tvTotal = findViewById(R.id.tvTotal_CheckOrder);
        btMap = findViewById(R.id.btMap_Checkorder);
        toolbarMain = findViewById(R.id.toolBar_CheckOrderCustomer);
    }
}
