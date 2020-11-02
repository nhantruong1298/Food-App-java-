package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.HistoryOrder;

import java.util.ArrayList;

public class FragmentHistoryDriverAdapter extends BaseAdapter {
    Context context;
    ArrayList<HistoryOrder> historyOrders;

    public FragmentHistoryDriverAdapter(Context context, ArrayList<HistoryOrder> historyOrders) {
        this.context = context;
        this.historyOrders = historyOrders;
    }

    @Override
    public int getCount() {
        return historyOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class HistoryViewHoler{
        TextView tvCode,tvDate,tvProduct,tvTotal,tvStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryViewHoler holder ;
        if(convertView == null)
        {
            holder = new HistoryViewHoler();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_history,parent,false);
            holder.tvCode = convertView.findViewById(R.id.textView_CodeOrder);
            holder.tvDate = convertView.findViewById(R.id.textView_DatePurchase);
            holder.tvProduct = convertView.findViewById(R.id.textView_AllProducts);
            holder.tvTotal = convertView.findViewById(R.id.textView_Total_HistoryDriver);
            holder.tvStatus = convertView.findViewById(R.id.textView_StatusHistoryDriver);

            convertView.setTag(holder);
        }
        else
        {
            holder = (HistoryViewHoler) convertView.getTag();
        }

        HistoryOrder historyOrder = historyOrders.get(position);

        holder.tvCode.setText(historyOrders.get(position).getCodeOrder());
        holder.tvStatus.setText(historyOrders.get(position).getStatus());
        holder.tvTotal.setText(historyOrders.get(position).getTotal());
        holder.tvDate.setText(historyOrders.get(position).getDate());

        String Allproduct = "";
        for(int i=0;i<historyOrder.getFoodOfOrders().size();i++)
        {
            Allproduct += historyOrder.getFoodOfOrders().get(i).getFoodName()+" x"+historyOrder.getFoodOfOrders().get(i).getCount()+" ,";
        }

        holder.tvProduct.setText(Allproduct);




        return convertView;
    }
}
