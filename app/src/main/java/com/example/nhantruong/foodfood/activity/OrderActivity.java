package com.example.nhantruong.foodfood.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.pager_adapter.PagerAdapter_OrderActivity;
import com.example.nhantruong.foodfood.fragment.InfoStoreFragment;
import com.example.nhantruong.foodfood.fragment.MenuOrderFragment;
import com.example.nhantruong.foodfood.model.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OrderActivity extends AppCompatActivity {

    //Dữ liệu từ Activity trước
    String type;
    String storeName;
    String storeAddress;
    //Key load UI
    String keyStore;
    //
    Toolbar toolBarMain;
    ViewPager viewPagerMain;
    TabLayout tabLayoutMain;
    TextView textViewName;
    TextView textViewAddress;
    ImageView imageViewMain;

    // 2 fragment con trong activity
    InfoStoreFragment infoStoreFragment ;
    MenuOrderFragment menuOrderFragment;


    //FireBase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Mapping();
        LoadUI();
        ActionBar();
        LoadViewPager_TabLayout();


    }

    private void LoadUI() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Store").child(type).child(keyStore).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                textViewName.setText(store.getName());
                textViewAddress.setText(store.getAddress());
                Picasso.get().load(store.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(imageViewMain);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return true;
    }

    private void LoadViewPager_TabLayout(){
        infoStoreFragment = new InfoStoreFragment();
        menuOrderFragment = new MenuOrderFragment();
        menuOrderFragment.keyStore = keyStore;
        menuOrderFragment.storeName = storeName;
        menuOrderFragment.storeAddres = storeAddress;
        menuOrderFragment.type = type;


        PagerAdapter_OrderActivity adapter = new PagerAdapter_OrderActivity(getSupportFragmentManager(), infoStoreFragment, menuOrderFragment);

        viewPagerMain.setAdapter(adapter);
        tabLayoutMain.setupWithViewPager(viewPagerMain);
    }
    private void ActionBar() {
        setSupportActionBar(toolBarMain);
        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart:
                Intent intent = new Intent(OrderActivity.this,CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Mapping() {
        toolBarMain = findViewById(R.id.toolBar_OrderActivity);
        viewPagerMain = findViewById(R.id.viewPager_OrderActivity);
        tabLayoutMain = findViewById(R.id.tabLayout_OrderActivity);
        textViewName = findViewById(R.id.textViewName_OrderActi);
        textViewAddress = findViewById(R.id.textViewAddress_OrderActi);
        imageViewMain = findViewById(R.id.ImageMain_OrderActivity);
        imageViewMain.setScaleType(ImageView.ScaleType.FIT_XY);

        //Nhận dữ liệu từ activity trước
        Intent intent = getIntent();
        keyStore = intent.getStringExtra("KeyStore");
        storeName = intent.getStringExtra("NameStore");
        storeAddress = intent.getStringExtra("AddressStore");
        type = intent.getStringExtra("Type");


    }

}
