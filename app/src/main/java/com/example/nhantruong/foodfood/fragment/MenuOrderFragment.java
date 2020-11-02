package com.example.nhantruong.foodfood.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.FoodOfMenuAdapter;
import com.example.nhantruong.foodfood.model.FoodOfMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuOrderFragment extends Fragment {

    //
    ListView listViewMain;
    private View mRootView;

    //Dữ liệu từ order activity
     public String keyStore;
     public String storeName;
     public String storeAddres;
     public String type;


    private DatabaseReference mDatabase;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_menu_oder,container,false);


        Mapping();
        LoadMenu();


        

        return mRootView;
    }
    private void Mapping(){
        listViewMain = (ListView)mRootView.findViewById(R.id.listView_MenuOrder);
    }
    private void LoadMenu(){
        final ArrayList<FoodOfMenu> arrayList = new ArrayList<>();
        final FoodOfMenuAdapter adapter = new FoodOfMenuAdapter(getContext(),arrayList,storeName,storeAddres);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Menu").child(type).child(keyStore).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    FoodOfMenu foodOfMenu = dataSnapshot.getValue(FoodOfMenu.class);
                    arrayList.add(foodOfMenu);
                    adapter.notifyDataSetChanged();
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
        listViewMain.setAdapter(adapter);

    }
}
