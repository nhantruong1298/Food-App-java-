package com.example.nhantruong.foodfood.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.adapter.DriverAdapter;
import com.example.nhantruong.foodfood.adapter.MenuAdapter;
import com.example.nhantruong.foodfood.intface.InVisibleListViewFromAdapter;
import com.example.nhantruong.foodfood.intface.UpdateDriverLocation;
import com.example.nhantruong.foodfood.model.DriverInfo;
import com.example.nhantruong.foodfood.model.ItemMenu;
import com.example.nhantruong.foodfood.model.OrderInfo;
import com.example.nhantruong.foodfood.utils.Constant;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DriverActivity extends AppCompatActivity implements InVisibleListViewFromAdapter,UpdateDriverLocation,Constant {

    Toolbar toolbarMain;
    NavigationView navigationViewMain;
    ListView listView_Menu;
    public static ListView listViewMain;

    DrawerLayout drawerLayoutMain;
    Button btFindOrder;

    //Firebase
    private DatabaseReference mDatabase;
    //Authentication
    public static FirebaseAuth mAuth;
    //listview & adapter
    public static ArrayList<OrderInfo> orderInfos;
    public static DriverAdapter adapter;

    //Location
    private static final int LOCATION_REQUEST_CODE = 1;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);


        Mapping();
        ActionBar();
        Init();
        LoadListViewMain();
        LoadListViewMenu();



    }

    private void Init() {
        setUpLocation();
        checkLocationPermission();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btFindOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadListViewMain();
            }
        });
    }

    private void Mapping() {
        navigationViewMain = findViewById(R.id.navigationMain_DriverActivity);
        listViewMain = findViewById(R.id.listviewMain_DriverActivity);
        listView_Menu = findViewById(R.id.listView_Menu_DriverActivity);
        toolbarMain = findViewById(R.id.toolBar_DriverActivity);
        drawerLayoutMain = findViewById(R.id.drawerLayout_DriverActivity);
        btFindOrder = findViewById(R.id.button_FindOrder_DriverActivity);


    }

    private void LoadListViewMain(){
       orderInfos = new ArrayList<>();

        adapter = new DriverAdapter(getApplicationContext(),orderInfos,this,this);

        LoadDataFromFireBase();
        listViewMain.setAdapter(adapter);


    }
    private void LoadListViewMenu(){
        ArrayList<ItemMenu> arrayList = new ArrayList<>();
        arrayList.add(new ItemMenu("Trang chủ",R.drawable.homepage));
        arrayList.add(new ItemMenu("Đơn Hàng",R.drawable.order));
        arrayList.add(new ItemMenu("Tài Khoản",R.drawable.account));

        MenuAdapter menuAdapter = new MenuAdapter(arrayList,getApplicationContext());

        listView_Menu.setAdapter(menuAdapter);
        SetClickITemListViewMenu();
    }

    private void SetClickITemListViewMenu() {
        listView_Menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        intent = new Intent(DriverActivity.this,OrderInfomationDriverActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        drawerLayoutMain.closeDrawer(GravityCompat.START);
                        intent = new Intent(DriverActivity.this,AccountActivity.class);
                        startActivity(intent);
                        break;


                }
            }
        });
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

    @Override
    public void onBackPressed() {
        LogOut();
    }

    private void LogOut(){
        final Dialog dialog = new Dialog(DriverActivity.this);
        dialog.setContentView(R.layout.dialog_logout);

        Button btYes = dialog.findViewById(R.id.button_YesDialogLogOut);
        Button btNo = dialog.findViewById(R.id.button_NoDiaLogLogOut);

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(DriverActivity.this,LoginActivity.class);
                finishAffinity();
                startActivity(intent);

            }
        });

        dialog.show();
    }

    private void LoadDataFromFireBase(){
        mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    OrderInfo orderInfo = dataSnapshot.getValue(OrderInfo.class);
                    //Lây đơn hàng chưa có tài xế
                    try
                    {
                        if(orderInfo.getStatus().equals(WAITING_DRIVER))
                        {

                            orderInfos.add(orderInfo);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            if(orderInfo.getDriverInfo().getEmail().equals(AccountActivity.accountInfoMain.getEmail()))
                            {
                                OrderInfomationDriverActivity.orderInfoMain = orderInfo;
                                InVisible();
                            }
                        }
                    }catch (Exception e)
                    {

                    }


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
    }


    @Override
    public void InVisible() {
        listViewMain.setVisibility(View.INVISIBLE);
    }

    @Override
    public void LoadNewData() {
        LoadListViewMain();
    }


    //Location

    private void setUpLocation() {
        mFusedLocationClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //        callback khi gọi requestLocationUpdate
        mLocationCallback = new LocationCallback(){
            @Override
            //gọi event cập nhật vị trí
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    //lấy tọa độ nhận được, để cập nhật lên firebase
                    lastLocation = locationResult.getLastLocation();
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {

            }
        };
    }

    private void checkLocationPermission() {
        if (mFusedLocationClient == null){
            mFusedLocationClient = new FusedLocationProviderClient(this);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Grant permission")
                    .setMessage("Please provide permission")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(DriverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        day la ham cap nhat location
                        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please grant permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    @Override
    public void UpdateDriverLocation(String key, OrderInfo orderInfo) {
        orderInfo.getDriverInfo().setLatitude(String.valueOf(lastLocation.getLatitude()));
        orderInfo.getDriverInfo().setLongitude(String.valueOf(lastLocation.getLongitude()));
        mDatabase.child(USER).child(ORDERINFOS).child(TRANSACTIONS).child(WAITING).child(key).child(DRIVER_INFO).setValue(orderInfo.getDriverInfo());
        OrderInfomationDriverActivity.orderInfoMain = orderInfo;

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadListViewMain();
    }
}
