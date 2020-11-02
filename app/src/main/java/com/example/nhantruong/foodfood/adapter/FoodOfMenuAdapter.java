package com.example.nhantruong.foodfood.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.CartActivity;
import com.example.nhantruong.foodfood.activity.OrderActivity;
import com.example.nhantruong.foodfood.model.FoodOfCart;
import com.example.nhantruong.foodfood.model.FoodOfMenu;
import com.example.nhantruong.foodfood.model.FoodOfOrder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FoodOfMenuAdapter extends BaseAdapter {
    Context context;
    ArrayList<FoodOfMenu> arrayList;
    String storeName;
    String storeAddress;

    //


    public FoodOfMenuAdapter(Context context,ArrayList<FoodOfMenu> arrayList,String storeName,String storeAddress){
        this.context = context;
        this.arrayList = arrayList;
        this.storeName = storeName;
        this.storeAddress = storeAddress;

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
        TextView tvName,tvPrice;
        Button btAddFood;
        ImageView imgFood;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_food_of_menu,parent,false);

            holder = new ViewHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.textView_NameFoodOfMenu);
            holder.tvPrice = (TextView)convertView.findViewById(R.id.textView_PriceFoodOfMenu);
            holder.btAddFood  = (Button)convertView.findViewById(R.id.button_AddFoodOfMenu);
            holder.imgFood = (ImageView)convertView.findViewById(R.id.imageView_FoodOfMenu);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final FoodOfMenu foodOfMenu = arrayList.get(position);

        Picasso.get().load(foodOfMenu.getImage())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading)
                .into(holder.imgFood);


        holder.tvName.setText(foodOfMenu.getName());

        //Format đơn vị tiền tệ
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(Integer.parseInt(foodOfMenu.getPrice()))+" đ");




        holder.btAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodOfCart foodOfCart = new FoodOfCart(storeName
                        ,foodOfMenu.getName()
                        ,foodOfMenu.getPrice()
                        ,foodOfMenu.getImage()
                        ,1);
                FoodOfOrder foodOfOrder = new FoodOfOrder(storeName
                        ,storeAddress,foodOfMenu.getName(),1,foodOfMenu.getPrice());

                if(CartActivity.cartArrayList == null)
                {
                    //DỮ liệu giỏ hàng
                    CartActivity.cartArrayList = new ArrayList<FoodOfCart>();
                    CartActivity.cartArrayList.add(foodOfCart);

                    //Dữ liệu dùng để thanh toán
                    CartActivity.foodOfOrders = new ArrayList<FoodOfOrder>();
                    CartActivity.foodOfOrders.add(foodOfOrder);

                }
                else
                {
                    int index = 99;
                    for(int i=0;i<CartActivity.cartArrayList.size();i++)
                    {
                        if(CartActivity.cartArrayList.get(i).getNameFood().equals(foodOfCart.getNameFood())
                                &&CartActivity.cartArrayList.get(i).getNameStore().equals(storeName))
                        {
                            index = i;
                        }
                    }
                    if(index != 99)
                    {
                        CartActivity.cartArrayList.get(index).setCount(CartActivity.cartArrayList.get(index).getCount()+1);

                        //
                        CartActivity.foodOfOrders.get(index).setCount(CartActivity.cartArrayList.get(index).getCount());
                    }
                    else
                    {
                        CartActivity.cartArrayList.add(foodOfCart);

                        CartActivity.foodOfOrders.add(foodOfOrder);

                    }
                }
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

            }
        });




        return convertView;
    }
}
