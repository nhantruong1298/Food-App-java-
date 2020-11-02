package com.example.nhantruong.foodfood.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.AccountActivity;
import com.example.nhantruong.foodfood.adapter.FragmentHistoryDriverAdapter;
import com.example.nhantruong.foodfood.model.HistoryOrder;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderHistory_Fragment extends Fragment implements Constant {
    View mRoot;
    ListView listviewMain;
    //
    public static ArrayList<HistoryOrder> historyOrders;
    public static FragmentHistoryDriverAdapter adapter;
    //
    private DatabaseReference mDatabase;
    //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_order_history_driver, container, false);

        Mapping();
        LoadListView();

        return mRoot;
    }

    private void LoadListView() {
        historyOrders = new ArrayList<>();

        adapter = new FragmentHistoryDriverAdapter(getContext(), historyOrders);
        LoadDataFromFireBase();
        listviewMain.setAdapter(adapter);
    }

    private void Mapping() {

        listviewMain = mRoot.findViewById(R.id.listView_HistoryDriver);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void LoadDataFromFireBase() {

        mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(HISTORY).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    if (orderInfo.getDriverInfo().getEmail().equals(AccountActivity.accountInfoMain.getEmail())) {
                        HistoryOrder historyOrder = new HistoryOrder(dataSnapshot.getKey(), orderInfo.getDate(), orderInfo.getTotal(), orderInfo.getStatus(), orderInfo.getFoodOfOrders());
                        historyOrders.add(historyOrder);
                        adapter.notifyDataSetChanged();
                    } else if (orderInfo.getCustomerInfo().getEmail().equals(AccountActivity.accountInfoMain.getEmail())) {
                        HistoryOrder historyOrder = new HistoryOrder(dataSnapshot.getKey(), orderInfo.getDate(), orderInfo.getTotal(), orderInfo.getStatus(), orderInfo.getFoodOfOrders());
                        historyOrders.add(historyOrder);
                        adapter.notifyDataSetChanged();
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

}
