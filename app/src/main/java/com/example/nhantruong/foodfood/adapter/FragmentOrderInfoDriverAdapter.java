package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.FoodOfOrder;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentOrderInfoDriverAdapter extends BaseAdapter {
    Context context;
    ArrayList<FoodOfOrder> foodOfOrders;

    public FragmentOrderInfoDriverAdapter(Context context, ArrayList<FoodOfOrder> foodOfOrders) {
        this.context = context;
        this.foodOfOrders = foodOfOrders;
    }

    @Override
    public int getCount() {
        return foodOfOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class MyViewHolder{
        TextView tvNameStore,tvAddressStore,tvFoodName,tvFoodPrice,tvCount;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder ;
        if(convertView == null)
        {
            holder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_fragment_orderinfo_driver,parent,false);
            holder.tvNameStore = convertView.findViewById(R.id.tvNameStore_listview_fragment_orderinfo);
            holder.tvAddressStore = convertView.findViewById(R.id.tvAddressStore_listview_fragment_orderinfo);
            holder.tvFoodName = convertView.findViewById(R.id.tvFoodName_listview_fragment_orderinfo);
            holder.tvFoodPrice = convertView.findViewById(R.id.tvFoodPrice_listview_fragment_orderinfo);
            holder.tvCount = convertView.findViewById(R.id.tvCount_listview_fragment_orderinfo);

            convertView.setTag(holder);
        }
        else
        {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.tvNameStore.setText("CH : "+foodOfOrders.get(position).getStoreName());
        holder.tvAddressStore.setText("Đ/c : "+foodOfOrders.get(position).getAddressStore());
        holder.tvFoodName.setText("Tên món : "+foodOfOrders.get(position).getFoodName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvFoodPrice.setText("Giá : "+decimalFormat.format(Integer.parseInt(foodOfOrders.get(position).getPrice()))+"đ");
        holder.tvCount.setText("Số lượng : "+String.valueOf(foodOfOrders.get(position).getCount()));




        return convertView;
    }
}
