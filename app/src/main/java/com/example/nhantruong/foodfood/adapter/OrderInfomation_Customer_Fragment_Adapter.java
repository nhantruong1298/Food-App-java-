package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.CheckOrderCustomer;
import com.example.nhantruong.foodfood.fragment.OrderInfomation_Customer_Fragment;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderInfomation_Customer_Fragment_Adapter extends BaseAdapter implements Constant {
    ArrayList<OrderInfo> orderInfos;
    Context context;

    //
    DatabaseReference mDataBase;

    public OrderInfomation_Customer_Fragment_Adapter(ArrayList<OrderInfo> orderInfos, Context context) {
        this.orderInfos = orderInfos;
        this.context = context;
        mDataBase = FirebaseDatabase.getInstance().getReference();
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
        TextView tvDate,tvAddress,tvTotal,tvStatus;
        Button btCancel;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHoler holer ;
        if(convertView == null)
        {
            holer = new MyViewHoler();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_fragment_orderinfo_customer,parent,false);

            holer.tvDate = convertView.findViewById(R.id.tvDate_fm_oo_customer);
            holer.tvAddress = convertView.findViewById(R.id.tvAdress_fm_oo_customer);
            holer.tvTotal = convertView.findViewById(R.id.tvTotal_fm_oo_customer);
            holer.tvStatus = convertView.findViewById(R.id.tvStatus_fm_oo_customer);
            holer.btCancel = convertView.findViewById(R.id.bt_Cancel_fm_oo_customer);

            convertView.setTag(holer);
        }
        else
        {
            holer = (MyViewHoler) convertView.getTag();
        }
        final OrderInfo orderInfo = orderInfos.get(position);

        holer.tvDate.setText("Ngày : "+orderInfo.getDate());
        holer.tvAddress.setText("Địa chỉ : "+orderInfo.getCustomerInfo().getAddress());
        holer.tvTotal.setText("Tổng đơn "+orderInfo.getTotal());
        holer.tvStatus.setText("Trạng thái : "+orderInfo.getStatus());

        holer.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn xóa đơn hàng?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        mDataBase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING)
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        OrderInfo orderInfo1 = dataSnapshot.getValue(OrderInfo.class);
                                        if(orderInfo1.getTime().equals(orderInfo.getTime()))
                                        {
                                            if(!orderInfo1.getStatus().equals(HAS_TAKEN_FOOD))
                                            {
                                                mDataBase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(CANCEL).push().setValue(orderInfo);
                                                mDataBase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).child(dataSnapshot.getKey()).removeValue();

                                                Toast.makeText(context, "Hủy thành công", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
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
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CheckOrderCustomer.class);
                intent.putExtra("orderInfo",orderInfo);

                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
