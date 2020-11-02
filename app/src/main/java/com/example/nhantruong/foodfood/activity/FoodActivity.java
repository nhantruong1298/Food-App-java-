package com.example.nhantruong.foodfood.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.FoodAdapter;
import com.example.nhantruong.foodfood.intface.StartActivityFromAdapter;
import com.example.nhantruong.foodfood.model.Store;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class FoodActivity extends AppCompatActivity implements StartActivityFromAdapter {

    String type ;
    //
    ListView listViewMain;
    Toolbar toolbarMain;
    View footerView;
    //Load listview
     ArrayList<Store> arrayList ;
     ArrayList<String> arrKeyStore;
    FoodAdapter adapter;
    //Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Mapping();
        ActionBar();
        Init();
        LoadListViewMain();


    }

    private void Init() {
        Intent intent = getIntent();
        type = intent.getStringExtra("Type");

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private  void LoadListViewMain(){

        arrayList = new ArrayList<>();
        arrKeyStore = new ArrayList<>();
        adapter = new FoodAdapter(getApplicationContext(),arrayList,arrKeyStore,this);


        LoadDataFromFireBase();


        listViewMain.setAdapter(adapter);
    }

    private void LoadDataFromFireBase() {
        mDatabase.child("Store").child(type).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot != null)
                {
                    Store store = dataSnapshot.getValue(Store.class);

                    arrayList.add(store);
                    arrKeyStore.add(dataSnapshot.getKey());
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void ActionBar()
    {
        setSupportActionBar(toolbarMain);
        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart:
                Intent intent = new Intent(FoodActivity.this,CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Mapping(){
        toolbarMain = (Toolbar)findViewById(R.id.toolBar_FoodActivity);
        listViewMain = (ListView)findViewById(R.id.listView_FoodActivity);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar,null);




    }

    @Override
    public void StartActivity(String keyStore,String nameStore,String addressStore) {
        Intent intent = new Intent(FoodActivity.this,OrderActivity.class);
        intent.putExtra("KeyStore",keyStore);
        intent.putExtra("NameStore",nameStore);
        intent.putExtra("AddressStore",addressStore);
        intent.putExtra("Type",type);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return true;
    }
}
