package com.example.nhantruong.foodfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.AccountActivity;
import com.example.nhantruong.foodfood.activity.CheckOrderCustomer;
import com.example.nhantruong.foodfood.adapter.OrderInfomation_Customer_Fragment_Adapter;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderInfomation_Customer_Fragment extends Fragment {
    public static ArrayList<OrderInfo> orderInfos;
    public static OrderInfomation_Customer_Fragment_Adapter adapter;
    //
    ListView lvMain;
    //Firebase
    private DatabaseReference mDatabase;

    View mRoot;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_order_infomation_customer,container,false);

        Mapping();
        LoadListView();


        return mRoot;
    }

    private void LoadListView() {

        orderInfos = new ArrayList<>();
        adapter = new OrderInfomation_Customer_Fragment_Adapter(orderInfos,getContext());

        LoadDataFromFireBase();

        lvMain.setAdapter(adapter);

    }

    private void LoadDataFromFireBase() {
        mDatabase.child("User").child("OrdersInfo").child("Transactions").child("Waiting").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    //Lây đơn hàng của KH
                    try
                    {
                        if(orderInfo.getCustomerInfo().getEmail().equals(AccountActivity.accountInfoMain.getEmail()))
                        {

                            orderInfos.add(orderInfo);
                            adapter.notifyDataSetChanged();

                        }
                    }catch (Exception e)
                    {

                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Mapping() {
        lvMain = mRoot.findViewById(R.id.listView_FragmentOrderInfo_Customer);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
