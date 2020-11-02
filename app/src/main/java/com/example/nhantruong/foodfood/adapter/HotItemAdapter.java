package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.FoodActivity;
import com.example.nhantruong.foodfood.intface.StartActivityFromAdapter;
import com.example.nhantruong.foodfood.model.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotItemAdapter extends RecyclerView.Adapter<HotItemAdapter.ItemHolder> {
    ArrayList<Store> arrayList;
    ArrayList<String> arrayKeyStore;
    Context context;
    StartActivityFromAdapter startActivityFromAdapter;

    public HotItemAdapter(Context context,ArrayList<Store> arrayList,ArrayList<String> arrayKeyStore,StartActivityFromAdapter startActivityFromAdapter) {
        this.arrayList = arrayList;
        this.context = context;
        this.arrayKeyStore = arrayKeyStore;
        this.startActivityFromAdapter = startActivityFromAdapter;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycleview_hotfood,null);
        ItemHolder itemHolder = new ItemHolder(v);


        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        Store store = arrayList.get(i);
        itemHolder.tvNameHotItem.setText(store.getName());
        itemHolder.tvAddressHotItem.setText(store.getAddress());

        //Placeholder : load ảnh chờ , error : ảnh bị lỗi
        Picasso.get().load(store.getImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(itemHolder.imgHotItem);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imgHotItem;
        public TextView tvNameHotItem,tvAddressHotItem;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgHotItem = (ImageView)itemView.findViewById(R.id.imageView_HotItem);
            tvNameHotItem = (TextView)itemView.findViewById(R.id.textView_NameHotItem);
            tvAddressHotItem = (TextView)itemView.findViewById(R.id.textView_AddressHotItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityFromAdapter.StartActivity(arrayKeyStore.get(getAdapterPosition()),arrayList.get(getAdapterPosition()).getName(),arrayList.get(getAdapterPosition()).getAddress());
                }
            });
        }
    }

}
