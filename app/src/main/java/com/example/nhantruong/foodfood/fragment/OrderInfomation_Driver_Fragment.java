package com.example.nhantruong.foodfood.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.DriverActivity;
import com.example.nhantruong.foodfood.activity.DriverMapsActivity;
import com.example.nhantruong.foodfood.activity.OrderInfomationDriverActivity;
import com.example.nhantruong.foodfood.adapter.FragmentOrderInfoDriverAdapter;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderInfomation_Driver_Fragment extends Fragment implements Constant {
    public static OrderInfo orderInfoMain;
    //Firebase
    private DatabaseReference mDatabase;

    //
    TextView tvName,tvAddress,tvPhone,tvDate,tvTotal;
    Button btMap,btTaken,btDone;
    ListView listView;
    View mRoot;
    //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_order_infomation_driver,container,false);
        Mapping();
        loadListView();
        Init();


        return mRoot;
    }

    private void Init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private  void Mapping(){
        listView = mRoot.findViewById(R.id.listView_FragmentOrderInfo_Customer);
        tvName = mRoot.findViewById(R.id.tvName_FragmentOrderInfo_Customer);
        tvAddress = mRoot.findViewById(R.id.tvAddress_FragmentOrderInfo_Driver);
        tvPhone = mRoot.findViewById(R.id.tvPhone_FragmentOrderInfo_Customer);
        tvDate = mRoot.findViewById(R.id.tvDate_FragmentOrderInfo_Customer);
        tvTotal = mRoot.findViewById(R.id.tvTotal_FragmentOrderInfo_Driver);
        btMap = mRoot.findViewById(R.id.btMap_FragmentOrderInfo_Driver);
        btDone = mRoot.findViewById(R.id.btDone_FragmentOrderInfo_Driver);
        btTaken = mRoot.findViewById(R.id.btTaken_FragmentOrderInfo_Driver);



    }
    private void loadListView(){
        if(orderInfoMain != null)
        {
            FragmentOrderInfoDriverAdapter adapter = new FragmentOrderInfoDriverAdapter(getContext(),orderInfoMain.getFoodOfOrders());
            listView.setAdapter(adapter);

            tvName.setText("Tên KH "+orderInfoMain.getCustomerInfo().getName());
            tvAddress.setText("Địa chỉ : "+orderInfoMain.getCustomerInfo().getAddress());
            tvPhone.setText("SĐT : "+orderInfoMain.getCustomerInfo().getPhone());
            tvDate.setText("Ngày : "+orderInfoMain.getDate());
            tvTotal.setText("Tổng đơn : "+orderInfoMain.getTotal());



            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(orderInfoMain != null)
                    {
                        Intent intent = new Intent(getActivity(),DriverMapsActivity.class);
                        intent.putExtra(LATITUDE,orderInfoMain.getCustomerInfo().getLatitude());
                        intent.putExtra(LONGITUDE,orderInfoMain.getCustomerInfo().getLongitude());
                        startActivity(intent);
                    }
                }
            });

            btTaken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   setTakenFood();
                }
            });

            btDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDoneOrder();

                }
            });

        }
    }


    private void setTakenFood() {
        mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    if(orderInfo.getCustomerInfo().getEmail().equals(orderInfoMain.getCustomerInfo().getEmail()
                    )&& orderInfo.getTime().equals(orderInfoMain.getTime()))
                    {
                        if(orderInfo.getStatus().equals(RECEIVED_ORDER))
                            mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).child(dataSnapshot.getKey()).child(STATUS).setValue(HAS_TAKEN_FOOD);
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
    private void setDoneOrder(){
        //Set status
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    final OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    try
                    {
                        if(orderInfo.getCustomerInfo().getEmail().equals(orderInfoMain.getCustomerInfo().getEmail()
                        )&& orderInfo.getTime().equals(orderInfoMain.getTime()))
                        {
                            if(orderInfo.getStatus().equals(HAS_TAKEN_FOOD))
                            {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).child(dataSnapshot.getKey()).child(STATUS).setValue(DONE);
                                                orderInfo.setStatus(DONE);
                                                //chuyển sang history
                                                mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(HISTORY).push().setValue(orderInfo);
                                                mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).child(dataSnapshot.getKey()).removeValue();
                                                ResetData();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Đã giao hàng cho khách ?").setPositiveButton("Đã giao", dialogClickListener)
                                        .setNegativeButton("Chưa", dialogClickListener).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Bạn chưa lấy đồ ăn ?", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    catch (Exception e)
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

    private void ResetData()
    {
        tvDate.setText("");
        tvTotal.setText("");
        tvPhone.setText("");
        tvName.setText("");
        tvAddress.setText("");
        listView.setAdapter(null);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        orderInfoMain = null;
        OrderInfomationDriverActivity.orderInfoMain = null;
        DriverActivity.listViewMain.setVisibility(View.VISIBLE);



    }

}
