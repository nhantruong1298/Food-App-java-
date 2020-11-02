package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.AccountActivity;
import com.example.nhantruong.foodfood.activity.OrderInfomationDriverActivity;
import com.example.nhantruong.foodfood.intface.InVisibleListViewFromAdapter;
import com.example.nhantruong.foodfood.intface.UpdateDriverLocation;
import com.example.nhantruong.foodfood.model.AccountInfo;
import com.example.nhantruong.foodfood.model.DriverInfo;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DriverAdapter extends BaseAdapter implements Constant {
    Context context;
    ArrayList<OrderInfo> orderInfos;
    InVisibleListViewFromAdapter inVisibleListViewFromAdapter;
    //
    private DatabaseReference mDatabase;
    //
    UpdateDriverLocation updateDriverLocation;



    public DriverAdapter(Context context, ArrayList<OrderInfo> orderInfos,InVisibleListViewFromAdapter inVisibleListViewFromAdapter,UpdateDriverLocation updateDriverLocation) {
        this.context = context;
        this.orderInfos = orderInfos;
        this.inVisibleListViewFromAdapter = inVisibleListViewFromAdapter;
        this.updateDriverLocation = updateDriverLocation;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return orderInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class MyViewHoler{
        TextView tvDate,tvName,tvAddress,tvToTal;
        Button btReceiveOrder;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHoler holder ;
        if(convertView == null)
        {
            holder = new MyViewHoler();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_driver,parent,false);

            holder.tvDate = convertView.findViewById(R.id.textViewDate_ListView_DriverActivity);
            holder.tvName = convertView.findViewById(R.id.textView_NameCustomer_ListView_DriverActivity);
            holder.tvAddress = convertView.findViewById(R.id.textView_AddresCustomer_ListView_DriverActivity);
            holder.tvToTal = convertView.findViewById(R.id.textView_Total_ListView_DriverActivity);
            holder.btReceiveOrder = convertView.findViewById(R.id.button_ReceiveOrder);

            convertView.setTag(holder);
        }
        else
        {
            holder = (MyViewHoler) convertView.getTag();
        }

        holder.tvDate.setText("Ngày : "+orderInfos.get(position).getDate());
        holder.tvName.setText("Tên KH : "+orderInfos.get(position).getCustomerInfo().getName());
        holder.tvAddress.setText("Địa chỉ : "+orderInfos.get(position).getCustomerInfo().getAddress());
        holder.tvToTal.setText("Tổng đơn : "+orderInfos.get(position).getTotal());


        holder.btReceiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists())
                        {
                            OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);

                            if(orderInfo.getTime().equals(orderInfos.get(position).getTime())&&orderInfo.getDate().equals(orderInfos.get(position).getDate()))
                            {
                                if(!orderInfo.getDriverInfo().getName().equals("null"))
                                {
                                    //TH trùng đơn hàng
                                    Toast.makeText(context, "Đã có tài xế nhận đơn hàng", Toast.LENGTH_SHORT).show();
                                    //Load new data listview nhận đơn
                                    inVisibleListViewFromAdapter.LoadNewData();
                                }
                                else
                                {
                                    //TH chưa có ai nhận đơn

                                    mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).child(dataSnapshot.getKey()).child(STATUS).setValue(RECEIVED_ORDER);
                                    //Update info driver
                                    AccountInfo accountInfo = AccountActivity.accountInfoMain;
                                    DriverInfo driverInfo =new DriverInfo(accountInfo.getFirstName(),accountInfo.getEmail(),accountInfo.getPhone(),"null","null");
                                    orderInfo.setDriverInfo(driverInfo);
                                    updateDriverLocation.UpdateDriverLocation(dataSnapshot.getKey(),orderInfo);
                                    //Ẩn listview nhận đơn
                                    inVisibleListViewFromAdapter.InVisible();
                                    Toast.makeText(context, "Nhận đơn thành công", Toast.LENGTH_SHORT).show();
                                }

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
        });

        return convertView;
    }


}
