package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.ItemMenu;

import java.util.ArrayList;

public class MenuAdapter  extends BaseAdapter {
    ArrayList<ItemMenu> arrayList;
    LayoutInflater inflater;

    public MenuAdapter(ArrayList<ItemMenu> arrayList, Context context) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView tvNameMenuItem;
        ImageView imgMenuItem;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null)
        {
            convertView =inflater.inflate(R.layout.row_listview_menu,null);
            holder = new ViewHolder();
            holder.tvNameMenuItem = (TextView)convertView.findViewById(R.id.textView_MenuItem);
            holder.imgMenuItem = (ImageView)convertView.findViewById(R.id.img_MenuItem);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tvNameMenuItem.setText(arrayList.get(position).getName());
        holder.imgMenuItem.setImageResource(arrayList.get(position).getImage());




        return convertView;
    }
}
