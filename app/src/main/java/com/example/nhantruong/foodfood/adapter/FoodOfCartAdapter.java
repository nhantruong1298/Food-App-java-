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

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.activity.CartActivity;
import com.example.nhantruong.foodfood.intface.AdapterAndActivity;
import com.example.nhantruong.foodfood.model.FoodOfCart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FoodOfCartAdapter extends BaseAdapter {
    ArrayList<FoodOfCart> cartArrayList;
    Context context;
    AdapterAndActivity adapterAndActivity;
    //
    public FoodOfCartAdapter(ArrayList<FoodOfCart> arrayList, Context context,AdapterAndActivity adapterAndActivity) {
        this.cartArrayList = arrayList;
        this.context = context;
        this.adapterAndActivity = adapterAndActivity;

    }

    @Override
    public int getCount() {
        return cartArrayList.size();
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
        TextView tvNameStore,tvNameFood,tvPrice,tvCount;
        ImageView imgFood;
        Button btCancle,btIncrease,btReduce;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_food_of_cart,parent,false);

            holder = new ViewHolder();
            holder.tvNameStore = (TextView)convertView.findViewById(R.id.textView_NameStoreOfCart);
            holder.tvNameFood = (TextView)convertView.findViewById(R.id.textView_NameFoodOfCart);
            holder.tvPrice = (TextView)convertView.findViewById(R.id.textView_PriceFoodOfCart);
            holder.tvCount = (TextView)convertView.findViewById(R.id.textView_CountFoodOfCart);
            holder.btCancle  = (Button)convertView.findViewById(R.id.button_CancelFoodOfCart);
            holder.btIncrease = (Button)convertView.findViewById(R.id.button_IncreaseCountFoodOfCart);
            holder.btReduce = (Button)convertView.findViewById(R.id.button_ReduceCountFoodOfCart);
            holder.imgFood = (ImageView)convertView.findViewById(R.id.imageView_FoodOfCart);

            convertView.setTag(holder);
            adapterAndActivity.ResetToTalCart();
            adapterAndActivity.TotalOfCart(cartArrayList.size());
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final FoodOfCart foodOfCart = cartArrayList.get(position);
        holder.tvNameStore.setText(foodOfCart.getNameStore().toString());
        holder.tvNameFood.setText(foodOfCart.getNameFood());

        //
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(Integer.parseInt(foodOfCart.getPrice()))+" đ");
        holder.tvCount.setText(foodOfCart.getCount()+"");

        Picasso.get().load(cartArrayList.get(position).getImage()).into(holder.imgFood);


        holder.tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_get_count);

                final EditText editText = dialog.findViewById(R.id.editTex_DialogSetCount);
                editText.setText(cartArrayList.get(position).getCount()+"");

                Button button = dialog.findViewById(R.id.button_DialogSetCount);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.tvCount.setText(editText.getText());
                        //Chỉnh lại số lượng
                        cartArrayList.get(position).setCount(Integer.parseInt(editText.getText().toString()));
                        CartActivity.foodOfOrders.get(position).setCount(Integer.parseInt(editText.getText().toString()));
                        adapterAndActivity.ResetToTalCart();
                        adapterAndActivity.TotalOfCart(cartArrayList.size());
                        dialog.dismiss();


                    }
                });

                dialog.show();
            }
        });

        holder.btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = foodOfCart.getCount();
                if(value >= 0)
                {
                    //Tăng lên gán lại vào editext
                    value++;
                    holder.tvCount.setText(String.valueOf(value));
                    //Chỉnh lại value trog mảng
                    cartArrayList.get(position).setCount(value);
                    CartActivity.foodOfOrders.get(position).setCount(value);
                    adapterAndActivity.ResetToTalCart();
                    adapterAndActivity.TotalOfCart(cartArrayList.size());

                }

            }
        });
        holder.btReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = foodOfCart.getCount();
                if(value == 1)
                {
                    adapterAndActivity.RemoveItemInListView(position);
                    adapterAndActivity.ResetToTalCart();
                    adapterAndActivity.TotalOfCart(cartArrayList.size());
                }
                else if(value > 1)
                {
                    value--;
                    holder.tvCount.setText(String.valueOf(value));
                    //Lưu lại vào mảng
                    cartArrayList.get(position).setCount(value);
                    CartActivity.foodOfOrders.get(position).setCount(value);
                    adapterAndActivity.ResetToTalCart();
                    adapterAndActivity.TotalOfCart(cartArrayList.size());
                }
//                else if(value == 0)
//                {
//                    adapterAndActivity.RemoveItemInListView(position);
//                    adapterAndActivity.ResetToTalCart();
//                    adapterAndActivity.TotalOfCart(cartArrayList.size());
//                }
            }
        });

        holder.btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    adapterAndActivity.RemoveItemInListView(position);
                    adapterAndActivity.ResetToTalCart();
                    adapterAndActivity.TotalOfCart(cartArrayList.size());


            }
        });





        return convertView;
    }
}
