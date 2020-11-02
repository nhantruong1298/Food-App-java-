package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.intface.StartActivityFromAdapter;
import com.example.nhantruong.foodfood.model.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {
    StartActivityFromAdapter startActivityFromAdapter;
    ArrayList<Store> arrayList;
    public ArrayList<String> arrayKeyStore;
    Context context;

    public FoodAdapter(Context context,ArrayList<Store> arrayList,ArrayList<String> arrKeyStore,StartActivityFromAdapter startActivityFromAdapter) {
        this.arrayList = arrayList;
        this.context = context;
        this.startActivityFromAdapter = startActivityFromAdapter;
        this.arrayKeyStore = arrKeyStore;
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
        TextView tvName,tvAddress;
        ImageView imageView;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
           final ViewHolder viewHolder;

            if(convertView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_listview_food,null);
                viewHolder.tvName = (TextView)convertView.findViewById(R.id.textViewName_ListViewFoodActi);
                viewHolder.tvAddress = (TextView)convertView.findViewById(R.id.textViewAddress_ListViewFoodActi);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView_ListViewFoodActi);

                convertView.setTag(viewHolder);

            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Store store = arrayList.get(position);

            //Cài đặt tên store tối đa 1 dòng & rút gọn bằng ...
            viewHolder.tvName.setMaxLines(1);
            viewHolder.tvName.setEllipsize(TextUtils.TruncateAt.END);
            viewHolder.tvName.setText(store.getName());

            viewHolder.tvAddress.setText(store.getAddress());

            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            //Dowload hình từ Storage

        String link = store.getImage();

        Picasso.get().load(link)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(viewHolder.imageView);


       convertView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivityFromAdapter.StartActivity(arrayKeyStore.get(position),store.getName(),store.getAddress());
           }
       });


        return convertView;
    }
}
