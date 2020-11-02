package com.example.nhantruong.foodfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.AccountInfo;

public class InfoAccountAdapter extends BaseAdapter {
    AccountInfo accountInfoMain;
    Context context;


    public InfoAccountAdapter(AccountInfo accountInfoMain, Context context) {
        this.accountInfoMain = accountInfoMain;
        this.context = context;

    }

    @Override
    public int getCount() {
        return 5;
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
        TextView tvAccount,tvValueAccount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder ;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview_account,parent,false);
            holder = new ViewHolder();
            holder.tvAccount = convertView.findViewById(R.id.textView_AccountActivity);
            holder.tvValueAccount = convertView.findViewById(R.id.textView_ValueAccountActivity);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if(accountInfoMain != null)
        {
            switch (position)
            {
                case 0: holder.tvAccount.setText("Họ : ");
                    holder.tvValueAccount.setText(accountInfoMain.getSurName());
                    break;
                case 1: holder.tvAccount.setText("Tên : ");
                    holder.tvValueAccount.setText(accountInfoMain.getFirstName());
                    break;
                case 2: holder.tvAccount.setText("SĐT : ");
                    holder.tvValueAccount.setText(accountInfoMain.getPhone());
                    break;
                case 3: holder.tvAccount.setText("Địa chỉ : ");
                    holder.tvValueAccount.setText(accountInfoMain.getAddress());
                    break;
                case 4: holder.tvAccount.setText("Email : ");
                    holder.tvValueAccount.setText(accountInfoMain.getEmail());
                    break;

            }
        }



        return convertView;
    }
}
