package com.example.nhantruong.foodfood.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.HotItemAdapter;
import com.example.nhantruong.foodfood.adapter.MenuAdapter;
import com.example.nhantruong.foodfood.intface.StartActivityFromAdapter;
import com.example.nhantruong.foodfood.model.ItemMenu;
import com.example.nhantruong.foodfood.model.Store;
import com.example.nhantruong.foodfood.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity implements StartActivityFromAdapter,Constant {
    Toolbar toolbarMain;
    RecyclerView recyclerViewMain;
    NavigationView navigationViewMain;
    ListView listViewMain;
    DrawerLayout drawerLayoutMain;
    ViewFlipper viewFlipperMain;

    //Firebase
    private DatabaseReference mDatabase;
    public static  FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        ActionBar();
        ActionViewFlipper();
        ListViewMenu();
        LoadRecycleView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_cart:
                Intent intent = new Intent(CustomerActivity.this,CartActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadRecycleView()
    {
        final ArrayList<Store> arrayList = new ArrayList<>();
        final ArrayList<String> arrKeyStore = new ArrayList<>();
        final HotItemAdapter adapter = new HotItemAdapter(getApplicationContext(),arrayList,arrKeyStore,this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(HOTSTORE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Store store = dataSnapshot.getValue(Store.class);
                arrayList.add(store);
                arrKeyStore.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
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


        recyclerViewMain.setHasFixedSize(true);
        recyclerViewMain.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerViewMain.setAdapter(adapter);


    }

    private void ListViewMenu()
    {
        ArrayList<ItemMenu> arrayList = new ArrayList<>();
        arrayList.add(new ItemMenu(HOMEPAGE,R.drawable.homepage));
        arrayList.add(new ItemMenu(FOOD,R.drawable.food));
        arrayList.add(new ItemMenu(DRINK,R.drawable.drink));
        arrayList.add(new ItemMenu(ORDER,R.drawable.order));
        arrayList.add(new ItemMenu(ACCOUNT,R.drawable.account));

        MenuAdapter menuAdapter = new MenuAdapter(arrayList,getApplicationContext());

        listViewMain.setAdapter(menuAdapter);
        SetClickITemListViewMenu();
    }

    private void ActionViewFlipper() {
        //Chạy quảng cáo
        ArrayList<String> advArray = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(FLIPPER).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot != null)
                {
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.get().load(dataSnapshot.getValue().toString())
                            .error(R.drawable.error)
                            .into(imageView);


                    viewFlipperMain.addView(imageView);
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

        viewFlipperMain.setFlipInterval(5000);
        viewFlipperMain.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipperMain.setInAnimation(animation_slide_in);
        viewFlipperMain.setOutAnimation(animation_slide_out);

    }

    private void ActionBar(){
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMain.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutMain.openDrawer(GravityCompat.START);
            }
        });

    }
    private void Mapping()
    {
        toolbarMain = (Toolbar)findViewById(R.id.toolBar_Main);
        viewFlipperMain = (ViewFlipper)findViewById(R.id.viewFlipper_Main);
        recyclerViewMain = (RecyclerView)findViewById(R.id.recycleView_Main);
        navigationViewMain = (NavigationView)findViewById(R.id.navigationMain);
        listViewMain = (ListView)findViewById(R.id.listView_Menu);
        drawerLayoutMain = (DrawerLayout)findViewById(R.id.drawerLayout_Main);
    }

    private void SetClickITemListViewMenu()
    {
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position)
                {
                    case 0:
                        drawerLayoutMain.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        drawerLayoutMain.closeDrawer(GravityCompat.START);
                        intent = new Intent(CustomerActivity.this,FoodActivity.class);
                        intent.putExtra("Type","Food");
                        startActivity(intent);
                        break;
                    case 2:
                        drawerLayoutMain.closeDrawer(GravityCompat.START);
                        intent = new Intent(CustomerActivity.this,FoodActivity.class);
                        intent.putExtra("Type","Drink");
                        startActivity(intent);
                        break;
                    case 3:
                        drawerLayoutMain.closeDrawer(GravityCompat.START);
                        intent = new Intent(CustomerActivity.this,OrderInfomationCustomerActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(CustomerActivity.this,AccountActivity.class);
                        startActivity(intent);
                        drawerLayoutMain.closeDrawer(GravityCompat.START);
                        break;

                }
            }
        });
    }


    @Override
    public void StartActivity(String keyStore, String nameStore,String storeAddress) {
        Intent intent = new Intent(CustomerActivity.this,OrderActivity.class);
        intent.putExtra("KeyStore",keyStore);
        intent.putExtra("NameStore",nameStore);
        intent.putExtra("AddressStore",storeAddress);
        if(keyStore.contains("SF"))
        {
            intent.putExtra("Type","Food");
        }
        else
        {
            intent.putExtra("Type","Drink");
        }


        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        LogOut();
    }
    private void LogOut(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        mAuth.signOut();
                        Intent intent = new Intent(CustomerActivity.this,LoginActivity.class);
                        finishAffinity();
                        startActivity(intent);
                      //  System.exit(0);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn thoát ?").setPositiveButton("Có", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();
    }
}
